/* Leo Gardiner
 * 
 * This is my GeneticAlgorithm class that contains the main method that uses a genetic algorithm to solve the
 * n-Queens problem.
 */ 

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm{
  
  private static final int BOARD_SIZE = 8; //Size of the chess board *BOARD_SIZE must match that of the Solution class.
  private static final int POP_SIZE = 1000; //Initial population size
  private static final double MUTATION_PROBABILITY = 0.009; //Probability of a mutation (each solution per generation)   
  private static final int NUMBER_OF_PARENTS = 50; //Number of parents selected each generation
  private static final int NUMBER_OF_CHILDREN = 50; //Number of children selected each generation
  private static final double MATING_PROBABILITY = 0.7; //Probability of two selected parents mating
  private static final int NO_OF_PRUNED_SOLUTIONS = 800; //Number of solutions that 'die' each generation (to keep memory low(ish) on long runs)
  private static final int MAX_GENERATIONS = 10000; //Max number of generations before giving up searching for a solution
  
  private static ArrayList<Solution> solutions = new ArrayList<Solution>(); //The array that holds all generated solutions
  
  
  /* The main method initializes a population of random potential solutions. After that it goes into a loop which
   * calculates the fitness of every solution, calls on a roulette selection method to find potential parents, mates
   * those parents randomly, possibly mutates solutions and then prepares the population for the next generation. The
   * loop ends after either the max number of generations is reached or (more preferably) when a solution is found. If
   * a solution is found then it prints the solution and how many generations it took to reach.
   */ 
  public static void main(String[] args){
    //Initializing a random population
    for (int j = 0; j < POP_SIZE; j++){ 
      Solution solution = new Solution();
      for (int i = 0; i < BOARD_SIZE; ++i)   
      { 
        int rnd = 0;
        rnd = uniformRandom(BOARD_SIZE);
        solution.setQueen(i,rnd); 
      }
      solutions.add(solution);
    }
    
    
    int maxGenerations = MAX_GENERATIONS;
    int currentGeneration = 1;
    
    //loop untill a solution is found or we reach the max number of generations
    while(checkSolutions() == false && currentGeneration < maxGenerations){
      
      if (getFitness() == 1){
        break;
      }
      rouletteSelection();
      mating();
      mutation();
      readyGeneration();
      currentGeneration++;
    }
    
    //If we have found a solution print it to console
    for (Solution solution : solutions){
      if (solution.getFitness() == 1.0){
        System.out.println("A perfect Solution: " + solution.printSolution() + " found in " + currentGeneration 
                             + " generations");
      }
    }
    return;
  }
  
  /* This method performs a mutation on a possible solution with the probability defined by MUTATION_PROBABILITY.
   * If a solution is selected for mutation this method selects a random queen in the solution and changes it to
   * a random position. It may potentially not change the solution even if it is chosen because of its random nature
   */
  private static void mutation(){
    Random rand = new Random();
    int chance = rand.nextInt(1000)+1;
    int queen = 0;
    int num = 0;
    for (Solution solution : solutions){
      if (chance <= (MUTATION_PROBABILITY*1000)){
        queen = rand.nextInt(8);
        num = rand.nextInt(8)+1;
        solution.setQueen(queen, num);
      }
    }
  }
  
  /* The mating method randomly chooses a parent that has been marked for mating by the roulette algorithm. It then
   * randomly chooses a partner based on the MATING_PROBABILITY. If a partner is chosen than it mates both of them
   * to create two children which are added to the population.
   */
  private static void mating()
  {
    int getRand = 0;
    int parent = 0;
    int noOfParents = NUMBER_OF_PARENTS;
    Random randNum = new Random();
    Solution parent1 = new Solution();
    Solution parent2 = new Solution();
    
    int i = 0;
    while( i < NUMBER_OF_CHILDREN/2) //While we still need children
    {
      for (int j = 0; j < NUMBER_OF_PARENTS; j++){
        parent = randNum.nextInt(solutions.size());
        parent1 = solutions.get(parent);
        if(parent1.getSelected() == true){ //First parent
          i++;
          break;
        }
      }
      
      getRand = randNum.nextInt(100);
      
      if (getRand <= MATING_PROBABILITY * 100){
        for (int k = 0; k < NUMBER_OF_PARENTS; k++){
          parent = randNum.nextInt(noOfParents);
          parent2 = solutions.get(parent); 
          if(parent2 != parent1 && parent2.getSelected() == true){ //second parent selected
            break;
          }
        }
        
        Solution child1 = new Solution();
        Solution child2 = new Solution();
        child1 = mateParents(parent1, parent2); //create first child
        child2 = mateParents(parent2, parent1); //create second child
        child1.attackCalculator(); //initialially calculate the childrens number of attacking queens
        child2.attackCalculator();
        solutions.add(child1);//add them to the population
        solutions.add(child2);
        i++;
      }
    }
    return;
  }
  
  /* This method is used to mate two parents, it chooses a random 'crossover' point within one queen of the edge of
   * the board and swaps the two parents queens around the crossover point
   * 
   * @param parent1 the first parent that is mated
   * @param parent2 the second parent that is mated
   * @return        the child created by these parents
   */ 
  private static Solution mateParents(Solution parent1, Solution parent2){
    Solution child = new Solution();
    Random rand = new Random();
    int crossover = rand.nextInt(BOARD_SIZE-1)+1; //randomly select crossover point
    for (int i = 0; i < crossover; i++){
      child.setQueen(i, parent1.getQueen(i));
    }
    for (int j = crossover; j < BOARD_SIZE; j++){
      child.setQueen(j, parent2.getQueen(j));
    }
    return child;
  }
  
  /* The roulette selection method first gets the fitness of all the solutions and then normalises them. This means
   * that when we are selecting parents, they each have a chance of being picked which is proportional to the number
   * of attacking queens they have. So parents with few attacking queens have a higher chance of being randomly picked.
   * Once picked, solutions are marked as being selected for mating and may be mated during the mating method.
   */ 
  private static void rouletteSelection(){
    int j = 0;
    double generationTotal = 0.0;
    double selectionProporiton = 0.0;
    int numberOfParents = NUMBER_OF_PARENTS;
    double randNum = 0.0;
    Solution possibleParent = new Solution();
    Solution parent = new Solution();
    
    //Calculate the total fitness
    for (Solution solution : solutions){
      generationTotal += solution.getFitness();
    }
    generationTotal *= 0.01; //Turn the total fitness to a percentage
    
    //Calculate each solutions possibility of being selected proportional to the population
    for (Solution solution : solutions){
      solution.setProbabilityOfSelection(solution.getFitness()/generationTotal);
    }
    
    //Roulette process
    for(int i = 0; i < numberOfParents; i++){
      Random rand = new Random();
      randNum = rand.nextInt(100)+1;
      j = 0;
      selectionProporiton = 0;
      
      boolean finished = false;
      while(finished == false){
        if (j == solutions.size()){
          j = 0;
        }
        possibleParent = solutions.get(j);
        selectionProporiton += possibleParent.getProbabilityOfSelection();
        if (selectionProporiton >= randNum){ //If the selectionProportion is less than the random number we have found
          if (j == 0){                       //a potential parent
            parent = solutions.get(j);
          } else if (j >= numberOfParents -1){
            parent = solutions.get(numberOfParents-1);
          } else {
            parent = solutions.get(j-1);
          }
          parent.setSelected(true); //mark parent for selection
          finished = true;
        } else {
          j++;
        }
      }
    }
    return;
  }
  
  /* the getFitness method is used to calculate the fitness of every solution in the generation. It calculates the
   * fitness based on how many queens are attacking and then converts it to an average fitness as a percentage.
   */
  private static int getFitness(){
    double bestFitness = 0;
    double worstFitness = 0;
    double divisionVar = 1.0; // this is used for single cases in which the algorithm would otherwise divide by zero
    
    //Call to calculate how many queens are attacking for each solution
    for (Solution solution : solutions){
      solution.attackCalculator();
    }
    
    //find the worst solution / most attacking queens
    Solution lowestSolution = new Solution();
    lowestSolution = solutions.get(1);
    for (Solution solution : solutions){
      if (solution.getAttackingQueens() > lowestSolution.getAttackingQueens()){
        lowestSolution = solution;
      }
    }
    worstFitness = lowestSolution.getAttackingQueens();
    
    //find the best solution / least attacking queens
    Solution highestSolution = new Solution();
    highestSolution = solutions.get(1);
    for (Solution solution : solutions){
      if (solution.getAttackingQueens() < highestSolution.getAttackingQueens()){
        highestSolution = solution;
      }
    }
    bestFitness = worstFitness - highestSolution.getAttackingQueens();
    if (bestFitness == 0){
      bestFitness = divisionVar;
    } else {
      for (Solution solution : solutions){ //for each solution set its fitness average as a percentage
        divisionVar = bestFitness;
        solution.setFitness((worstFitness - solution.getAttackingQueens()) * 100.0 / bestFitness);
      }
    }
    return 0;
  }
  
  /* the readyGeneration method is used to prepare solutions for the next generation. It resets the variable defining 
   * if a solution has been selected for mating, calculates the number of attacking queens for children and prunes a 
   * arbitary number of some of the worst solutions to prevent using to much memory if a solution is hard to produce
   */
  private static void readyGeneration(){
    int av = 0;
    int tmp = 0;
    int numberPruned = 0;
    Solution temp = new Solution();
    for (Solution solution : solutions){
      solution.setSelected(false);
      solution.attackCalculator();
      tmp += solution.getAttackingQueens();
    }
    av = tmp / solutions.size();
    
    while (numberPruned < solutions.size() && numberPruned < NO_OF_PRUNED_SOLUTIONS){
      temp = solutions.get(numberPruned);
      if (temp.getAttackingQueens() > av){
        solutions.remove(numberPruned);
      }
      numberPruned++;
    }
    return;
  }
  
  /* the check solutions method iterates through all the solutions and checks to see if we have found a solution that
   * solves the problem.
   * 
   * @return this will return 'true' if a solution is in the population and 'false' otherwise
   */
  public static boolean checkSolutions(){
    for(Solution solution : solutions){
      solution.attackCalculator();
      if (solution.getAttackingQueens() == 0){ // Solution found
        return true;
      }
    }
    return false; // Solution not found
  }
  
  /*Used to produce a random uniform number inbetween the one and the parameter given.
   * 
   * @param number this is the upper limit of the random uniform number returned
   * @return       the random uniform number returned
   */
  public static int uniformRandom(int number){     
    Random rand = new Random();
    int r = rand.nextInt(number)+1;
    return r;
  } 
}