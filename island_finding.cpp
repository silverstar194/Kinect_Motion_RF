#include <iostream>
#include <tuple>
#include <queue>

using namespace std; //So no std: prefix

//Necessary Classes

class Island{
    public:
        vector< tuple<int, int> > xYCoors;
};


//Global Variables: 
bool visited[60][60] = {0}; //I useed magic numbers because I don't know what the exact ones are
vector< Island > allIslands;

/**
  Finds the most common color in order to figure out what the background is
*/
int findMostCommonColor(int data[][]){
    
    int maxIndex = -1;
    int maxCount = -1;
    
    int colors[];
    
    for(int j = 0; j < col; j++){
        for(int i = 0; i < row; i++){
                int currColor = data[i][j];
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
        cout<< "Count did not work";
    }
    return maxIndex;

}

/**
    Looks for the largest island, every time it finds and an area that is not the background color, we check to see the size of the island.
*/
void findBiggestIsalnd(int commonColor){
 
    int largestIslandSize = -1;
    int largestIslandIndex = -1;
    //for the whole picture
        //if the place has not been visited and that area in the picture has the common color 
        // go expand that island
        // whtever isalnd is returned add it to the vectors of islands
        // check to see which island is thee largest as we add it, store the index value of the the largest island
    
    for(int j = 0; j < col; j++){
        for(int i = 0; i < row; i++){
                
            if(visited[i][j] && data[i][j] > commonColor -3 && data[i][j] < commonColor + 3){//if it has not been visited and the color is the common color
                Island currIsland;
                currIsland.xYCoors = make_tuple(i,j);
                currIsland = expandIsland(i,j, currIsland, commonColor);
                if(currIsland.size() > largestIslandSize){
                    
                    largestIslandSize = currIsland.size();
                    largestIslandIndex = allIslands.size();
                }
                
                allIslands.push_back(currIsland);
                    
                
            }
        }
    }
    
    
}
/**
    This does a breath first search to see what the size of the island is 
*/
void expandIsland(int x, int y, Island currIsland, int commonColor){
        
    int breath [8][2] = [[1,0], [0,1], [1,1], [-1,-1], [-1,0], [0,-1], [1,-1], [-1,1]];
 
    //For BFS
    queue< tuple< int, int > > frontier;
    //Seed
    frontier.push(make_tuple(x,y));
    
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
            
            if(!visited[x][y] data[i][j] > commonColor -3 && data[i][j] < commonColor + 3){
                tuple<int, int> currCoor = make_tuple(x,y);
                queue.push(currCoor);
                visited[x][y] = true;
                currIsland.xYCoor.push_back(currCoor);
            }
        }
        
    }
    
    
}

