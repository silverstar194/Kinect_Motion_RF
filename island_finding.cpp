#include <iostream>
#include <tuple>
#include <queue>

using namespace std; //So no std: prefix

//Global Variables: 
bool visited[60][60] = {0}; //I useed magic numbers because I don't know what the exact ones are

//Necessary Structs
struct XYcoor{
    
    tuple<int, int> XY;
};

struct Island{
    
    XYcoor (*islandCoors);
}

struct ListIslands{
    
    Island (*allIslands);
}


/**
  Finds the most common color in order to figure out what the background is
*/
int findMostCommonColor(int data[][]){
    
    int maxIndex = -1;
    int maxCount = -1;
    
    int row =  sizeof(data) / sizeof(data[0]);
    int col =  sizeof(data[0]) / sizeof(int);
    
    int colors[];
    
    for(int i = 0; i < row; i++){
        for(int j = 0; j < col; j++){
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
int** findBiggestIsalnd(int picture[][], int commonColor){
    
    int row = sizeof(picture) / sizeof(picture[0]); //Techincally since it is the same size every time this shouldn't be necessary, but it could be used as a check, you decide
    int col = sizeof(picture[0]) / sizeof(int);
 
    
    
    for(int i = 0; i < row; i++){
        for(int j = 0; j < col; j++){
                
            if(visited[i][j] && picture[i][j] != commonColor){//if it has not been
                
                
                //need to expand island
            }
        }
    }
    
    return null
    
}
/**
    This does a breath first search to see what the size of the island is 
*/
void expandIsland(int x, int y){
        
    int breath [8][2] = [[1,0], [0,1], [1,1], [-1,-1], [-1,0], [0,-1], [1,-1], [-1,1]];
 
    queue<XYcoor> frontier;
    
    //Creating a starting point
    frontier.push(make_tuple(x,y));
    
    //Breath first search
    
    while(queue.size() > 0)
    {
        XYcoor curr = frontier.pop();
        
        for(int i = 0; i < breath.size(); i++)
        {
            breath[i][0] += get<0>(curr); // row
            breath[i][1] += get<1>(curr); // col
            
            if(visited[breath[i][0]][breath[i][1]] 
        }
        
    }
 
    
}