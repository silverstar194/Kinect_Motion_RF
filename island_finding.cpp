#include <iostream>

/**
  Finds the most common color in order to figure out what the back
  -ground is
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
    
    int row = sizeof(picture) / sizeof(picture[0]);
    int col = sizeof(picture[0]) / sizeof(int);
    
    bool visited[row][col] = {0}; 
    
    
    
    for(int i = 0; i < row; i++){
        for(int j = 0; j < col; j++){
                
            if(visited[i][j] && picture[i][j] != commonColor){//if it has not been visited
                                                              //&& it is not a common color
                
                
                //need to expand island
            }
        }
    }
    
    return null
    
}
/**
    This does a breath first search to see what the size of the island is 
*/
void expandIsland(){
        
    int breath [8][2] = [[1,0], [0,1], [1,1], [-1,-1], [-1,0], [0,-1], [1,-1], [-1,1]];
}