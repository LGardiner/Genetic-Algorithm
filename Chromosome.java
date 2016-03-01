public class Chromosome
    {
  private final int MAX_LENGTH = 10;  
        private int mData[] = new int[MAX_LENGTH];
        private double mFitness = 0.0;
        private boolean mSelected = false;
        private double mSelectionProbability = 0.0;
        private int mConflicts = 0;
    
        public Chromosome()
        {
            for(int i = 0; i < MAX_LENGTH; i++)
            {
                this.mData[i] = i;
            }
            return;
        }
        
        public void computeConflicts()
        {
            int x = 0;
            int y = 0;
            int tempx = 0;
            int tempy = 0;
            String board[][] = new String[MAX_LENGTH][MAX_LENGTH];
            int conflicts = 0;
            int dx[] = new int[] {-1, 1, -1, 1};
            int dy[] = new int[] {-1, 1, 1, -1};
            boolean done = false;

            // Clear the board.
            for(int i = 0; i < MAX_LENGTH; i++)
            {
                for(int j = 0; j < MAX_LENGTH; j++)
                {
                    board[i][j] = "";
                }
            }

            for(int i = 0; i < MAX_LENGTH; i++)
            {
                board[i][this.mData[i]] = "Q";
            }

            // Walk through each of the Queens and compute the number of conflicts.
            for(int i = 0; i < MAX_LENGTH; i++)
            {
                x = i;
                y = this.mData[i];

                // Check diagonals.
                for(int j = 0; j <= 3; j++)
                {
                    tempx = x;
                    tempy = y;
                    done = false;
                    while(!done)
                    {
                        tempx += dx[j];
                        tempy += dy[j];
                        if((tempx < 0 || tempx >= MAX_LENGTH) || (tempy < 0 || tempy >= MAX_LENGTH)){
                            done = true;
                        }else{
                            if(board[tempx][tempy].compareToIgnoreCase("Q") == 0){
                                conflicts++;
                            }
                        }
                    }
                }
            }

            this.mConflicts = conflicts;
        }
        
        public void conflicts(int value)
        {
            this.mConflicts = value;
            return;
        }
        
        public int conflicts()
        {
            return this.mConflicts;
        }
    
        public void fitness(double value){
          this.mFitness = value;
          return;
        }
        
        public double fitness(){
          return this.mFitness;
        }
        
        public void selected(boolean value){
          this.mSelected = value;
        }
        
        public boolean selected(){
          return this.mSelected;
        }
        
        public void selectionProbability(double value){
          this.mSelectionProbability = value;
          return;
        }
        
        public double selectionProbability(){
          return this.mSelectionProbability;
        }
    
        public int data(final int index)
        {
            return mData[index];
        }
        
        public void data(final int index, final int value)
        {
            mData[index] = value;
            return;
        }
    } // Chromosome
    