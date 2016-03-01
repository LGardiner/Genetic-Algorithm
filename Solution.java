/* Leo Gardiner
 * 484261
 * 
 * This class defines a Solution object. This object holds all the data required to use and manipulate a solution.
 * It includes the number of queens. Where they are on the board based on an integer array where the position in the 
 * array represents one side of the board say x and the number represents the other side say y. So if a 1
 * at the first position in an array would repesent a queen in a corner of the board. This class also holds variables
 * that refer to the solutions fitness, probability of selection, number of attacking queens and whether or not it
 * has been selected for mating.
 */
public class Solution{
  
  private  final int BOARD_SIZE = 8;  //the size of the chess board, *BOARD_SIZE must match that of the GeneticAlgorithm class
  private  int queens[] = new int[BOARD_SIZE]; //the array representing where the queens are on the board
  private  double fitness = 0.0; //fitness of solution
  private  double probabilityOfSelection = 0.0; //probability of solution being selected for mating
  private  boolean selected = false; //whether or not the solution has been selected for mating
  private int attackingQueens = 0; //number of attacking queens on this solution
  
  /* this method initializes a solution with an empty board*/
  public Solution()
  {
    for(int i = 0; i < BOARD_SIZE; i++){
      this.queens[i] = 0;
    }
    return;
  }
  
  /* @return the solutions probability of selection */
  public  double getProbabilityOfSelection()
  {
    return probabilityOfSelection;
  }
  
  /* @param selectionProbability :used to set the probability of this solution being selected for mating */
  public void setProbabilityOfSelection(double selectionProbability)
  {
    probabilityOfSelection = selectionProbability;
    return;
  }
  
  /* @return returns whether or not this solution has been selected for mating, true if it has benn, false otherwise */
  public boolean getSelected()
  {
    return selected;
  }
  
  /* @param selected :used to set the boolean defining whether the solution has been selected for mating */
  public void setSelected(boolean selected)
  {
    this.selected = selected;
    return;
  }
  
  /* @param fitness :used to define the fitness of a solution */
  public void setFitness(double fitness){
    this.fitness = fitness;
  }
  
  /* @return this method returns the fitness value of a solution */
  public double getFitness(){
    return this.fitness;
  }
  
  /* @return this method returns a string representation of the solution */
  public  String printSolution(){
    String print = "";
    for (int i = 0; i < 8; i++){
      print += this.queens[i];
    }
    return print;
  }
  
  /* @return this method returns the number of attacking queens in a solution */
  public int getAttackingQueens(){
    return this.attackingQueens;
  }
  
  /* this method is used to calculate the number of queens which are capable of attacking each other in a solution. It
   * iterates through the solution and if a queen can attack either horizontally or diagonally up or down it counts it
   * as a collision and sums them all. Afterwhich it sets the attackingQueens value of the solution
   */
  public void attackCalculator(){
    int attackingNum = 0;
    int offset = 1;    
    
    for(int i=0; i < 8; i++){
      offset = 1;
      for(int j=i+1; j < 8; j++){
        if((this.queens[j] == (this.queens[i] + offset)) || (this.queens[j] == (this.queens[i] - offset)) || 
           (this.queens[i] == this.queens[j])){ //if this queen (j) can attack the other (i) increment the sum of attacking queens.
          attackingNum++;
        }
        offset++;
      }
    }
    
    if(attackingNum == 0){ //We have found a perfect solution but the algorithm wont finish until the generation has
      this.fitness = 1;    //been completely processed
      this.attackingQueens = 0;
      return;
    }
    this.attackingQueens = attackingNum;
    return;
  }
  
  /* this algorithm is used to return the position of a queen that is at a particular position in the solution
   * 
   * @param index :the index of the array we want to check for a queen position
   * @return       the position of queen at the given index
   */
  public int getQueen(int index){
    return this.queens[index];
  }
  
  /* This method is used to set the position of a queen at a given index in the array
   * 
   * @param index :the position/index in the array where we want to place the queen on the x axis
   * @param position :the poisiton on the board where we want to place the queen (y axis)
  */
  public void setQueen(int index, int position){
    queens[index] = position;
    return;
  }
}