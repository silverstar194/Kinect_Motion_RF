#include <iostream>
#include <tuple>
#include <queue>

using namespace std; //So no std: prefix

//Necessary Classes

class Island
{
    public:
        Island() 
        {
            vector< tuple<int, int> > xYCoors;
        }
};

class Frame: Island
{
   
    public:
        Frame(int row, int col)
        {
            bool visited[row * col] = {0};
            int data[row*col];
            int row = row;
            int col = col;
            vector< Island > allIslands = NULL;
        }

    /**
    Looks for the largest island, every time it finds and an area that is not the background color, we check to see the size of the island.
    */
    void findIsland()
    { 

        int largestIslandSize = INT_MIN;
        int largestIslandIndex = INT_MIN;
        //for the whole picture
            //if the place has not been visited and that area in the picture has the common color 
            // go expand that island
            // whtever isalnd is returned add it to the vectors of islands
            // check to see which island is thee largest as we add it, store the index value of the the largest island

        for(int i = 0; i < row; i++)
        {
           for(int  = 0; j < col; j++) 
           {
                    //common colors is the 
                if(!visited[i * row + j] && !(data[i * row + j] > data[i * row + j] -3 && data[i * row + j] < data[i * row + j] + 3))
                {
                    Island currIsland; // set it to null right away 
                    currIsland.xYCoors = make_tuple(i,j); // combind this and the above statement
                    currIsland = expandIsland(i,j, currIsland, commonColor); // take out common color
                    
                    if(!currIsland)
                    {
                    }
                    else
                    {
                        allIslands.push_back(currIsland);
                        
                        
                        if(currIsland.xYCoors.size() > largestIslandSize)
                        {

                            largestIslandSize = currIsland.xYCoors.size();
                            largestIslandIndex = allIslands.size();
                        }
                        
                    }         

                }
            }
        }
    
    
    }

    /**
    This does a breath first search to see what the size of the island is  
    */
    Island expandIsland(int x, int y, Island currIsland)
    {

        int breath [8] = [[1,0], [0,1], [1,1], [-1,-1], [-1,0], [0,-1], [1,-1], [-1,1]]; // no, need to create 8 objects

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

        while(queue.empty())
        {
            tuple<int, int> curr = frontier.pop();

            for(int i = 0; i < breath.length(); i++)
            {
                int x = get<0>(curr);
                int y = get<1>(curr);

                x += breath[i][0];
                y += breath[i][1];

                if(!visited[x][y] && !(data[i][j] > data[i][j] - 3 && data[i][j] < data[i][j] + 3))
                {
                    tuple<int, int> currCoor = make_tuple(x,y);
                    queue.push(currCoor);
                    visited[x][y] = true;
                    currIsland.xYCoor.push_back(currCoor);
                }
            }

        }
        
        if(currIsland.xYCoor.size() / (row * col) < .15)
        {
            return NULL;
        }
        
        return currIsland;

    }

    Island comparingIslands(vector< vector<Island> > frames) //not sure what I should return
    {
       //Cases that come to mind
       //When user leaves screen
    
        Island human = NULL;

       //things to look for, in this island do we have an island that like with excatly like it (with resonable deviation)?
       
        for(int j = 0; j < frames[j].size(); j++)
        {
            for(int i = j + 1; i < frames.size(); i++)
            {
                for(int k = 0; k < frames[i].size(); k++)
                {
                    
                }
            }
        }
            
       

    }
     
};











