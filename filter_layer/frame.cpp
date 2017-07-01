#include <iostream>
#include <tuple>
#include <queue>

using namespace std; //So no std: prefix

//Necessary Classes
class Island{
    public:
        Island(){
            vector< tuple<int, int> > xYCoors;
            bool needToCompare = true;
            int commonColors[5];
            int countCommonColors[5];
        }
};


class Frame{
   
    public:
        
        static const int PERCENT = .15;
        static const int WIGGLE_ROOM = 3;
    
        Frame(int rows, int cols,int dep){
            bool visited[row * col] = {0};
            int data[row*col];
            int row = rows;
            int col = cols;
            int depth = dep;
            vector< Island > allIslands = NULL;
            int commonColors;
            
        }

    /**
    Looks for the largest island, every time it finds and an area that is not the 
    background color, we check to see the size of the island.
    */
    void findIsland(){ 

        int largestIslandSize = INT_MIN;
        int largestIslandIndex = INT_MIN;
        
        commonColor = findMostCommonColor();
        //for the whole picture
            //if the place has not been v and that area in the picture has the common color 
            // go expand that island
            // whtever isalnd is returned add it to the vectors of islands
            // check to see which island is thee largest as we add it, store the index value of the the largest island

        for(int i = 0; i < row; i++){
           for(int  = 0; j < col; j++){ 
               
                int xy = getXY(i,j);
                if(!visited[xy] && !(data[xy] > data[xy] - WIGGLE_ROOM && data[xy] < data[xy] + WIGGLE_ROOM)){
                    Island currIsland = NULL; // set it to null right away 
                    currIsland.xYCoors = make_tuple(i,j); // combind this and the above statement
                    currIsland = expandIsland(i,j, currIsland); // take out common color
                    
                    if(!currIsland){ //change to make Sean more comfortable/ make more readable
                    }
                    else{
                        
                        allIslands.push_back(currIsland);
                        
                        if(currIsland.xYCoors.size() > largestIslandSize){

                            largestIslandSize = currIsland.xYCoors.size();
                            largestIslandIndex = allIslands.size();
                        }
                        
                    }         

                }
            }
        }
    
    
    }
    
    /**
    Get XY
    */
     int getXY(int x, int y){
         return row * x + y;
     }
    
    /**
    Get CommonColor
    */
    
    int findMostCommonColor(){

        int maxIndex = INT_MIN;
        int maxCount = INT_MIN;

        int colors[depth];

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                int currColor = data[get(i,j)];
                int currCount = colors[currColor]
                    if(currCount != 0){
                        colors[currColor]++;
                    }
                else{
                    colors[currColor] = 1;
                }

                if(currCount > maxCount){
                    maxIndex = currcolor;
                    maxCount = currCount;
                }

            }
        }

        if(maxCount < 1){
            cout<< "Count did not work"; // need to add this to log file not just print
        }
        return maxIndex;

    }

    /**
    This does a breath first search to see what the size of the island is  
    */
    Island expandIsland(int x, int y, Island currIsland){

        int breath [8] = [[1,0], [0,1], [1,1], [-1,-1], [-1,0], [0,-1], [1,-1], [-1,1]];

        //For BFS
        queue< tuple< int, int > > frontier;
        //Seed
        frontier.push(make_tuple(x,y));
        
        int middleColor = getXY
        
        int possibleCommonColor[WIGGLE_ROOM + 2] = [middleColor - 2, middleColor - 1, middleColor, middleColor + 1, middleColor +2];
        int findingCommonColor[WIGGLE_ROOM + 2] = [0,0,0,0,0];
        //while there is stuff to search

            //for each direction
                //get the current x and y and from the queue and store them as temps
                //add to the temps
                //if that temp has not been visited and is a common color
                    //add new spot to 
                    //add it to the curr island 
                    //mark it as visited

        while(queue.empty()){
            
            tuple<int, int> curr = frontier.pop();

            for(int i = 0; i < breath.length(); i++){
                int x = get<0>(curr);
                int y = get<1>(curr);

                x += breath[i][0];
                y += breath[i][1];

                int xy = getXY(x,y);
                if(!visited[xy] && !(data[xy] > middleColor - WIGGLE_ROOM && data[xy] < middleColor + WIGGLE_ROOM)) {
                    tuple<int, int> currCoor = make_tuple(x,y);
                    queue.push(currCoor);
                    visited[xy] = true;
                    currIsland.xYCoor.push_back(currCoor);
                    
                    for(int j = 0; j < findingCommonColor.size(); j++){
                        if(possibleCommonColor[i] == data[xy]){
                            findingCommonColor[i]++;
                            break;
                        }
                    }
                }
            }

        }
        
        currIsland.commonColors = possibleCommmonColor;
        
        if(currIsland.xYCoor.size() / (row * col) < PERCENT){
            return NULL;
        }
        
        return currIsland;

    }
    
    /*
        see how many colors they share
    */ 
    int amountOfCommonColorsShared(int[] colors1, int[] colors2){
        if(colors1[0] - colors2[0] > 4 || colors1[0] - colors2[0] < -4){
            return 0;
        }
        int count = 0;
        
        for(int i = 0; i < colors1.size(); i++){
            for(int j = 0; j < colors2.size(); j++){
                if(colors1[i] == colors2[j]){
                    count++;
                    break;
                }
            }
        
        }
        
        return count;
    }

    /*
    Finding everything that is not human and whatever is left is most likely human
    */
    vector<Island> comparingIslands(vector<Frame> frames ){
       //Cases that come to mind
       //When user leaves screen
    
        vector<Island> humans = NULL;

       //things to look for, in this island do we have an island that like with excatly like it (with resonable deviation)?
       
       for(int i = 0; i < frames.size() - 1; i++){
           for(int k = 0; k < frames[i].allIslands.size(); k++){
               //need to make sure there is a true on the compare
               bool isHuman = true;
               for(int j = i + 1; frames.size(); j++){
                   for(int l = 0; l < frames[j].allIslands.size(); l++){
                       if(!(frames[j].allIslands[l].needToCompare) && frames[j].allIslands[l].size() / frames[i].allIslands[k] < 1.1 && frames[j].allIslands[l].size() / frames[i].allIslands[k] < .9 && amountOfCommonColors(frames[j].allIslands[l].commonColors, frames[i].allIslands[k].commonSolrs) > 2){
                           frames[j].allIslands[l].needToCompare = false;
                           isHuman = false;
                       }
                           

                   }
               }
               
               if(isHuman){
                   humans.push_back(frames[i].allIsland[k]);
               }
               
           }
       }
            
       

    }
     
};