#include <tuple>
#include <queue>
    
/**
    Questions that came up as  I was doing this:
For both thread 2 and Thread 3 are we havinga queue of tuples?

From drawn out of diagram looks like human is a seperate class

image array colors in array of tuples in the members of frame, what is that for

what is the id image for. what's the point?

*/
    
class BuildingRandomForest{
    //how this is getting read in from file will decide how we should actually build the tree
};
    
class Island{
    
    //Constructors() don't need to pass in any booleans
        //Vector of tuples of ints for corrdinates
        //Id numbers //I would assume that would be to connect the frame and the islands tofther
        //is human boolean = false

};
    
class Frame{
    
    //Constructor(int forDepth, int ID)
        //id number, Should the Id number be randomly Generate?
        //int depth[fordepth]
        //vector<islands> = NULL;  
        //image array of color <These are tuples that I don't know what they ar>
};


    
    
 
//possible fields maybe it should be variables in scope in of main, possibly global
//queue<frames>
//queue<island>    
 
   
    
int main(){
    
    //build tree
    
    //after the tree is build have 4 threads that will be running conccurrently
    /**
       Thread 1 is to retrieve frames from the hardware and queueing them into a queue of frames
       
       Thread 2 is dequeing those frames and adding them to a queue of tuples: islands that are found in the frame and the frames that they were found in
       
       Thread 3 is dequeueing the the tuples of islands and frames then takes them and puts them in another queue with tuples of island, frame, and human. the human should be from those islands from that frame
       
       Thread 4 is dequeue from the queue Thread 3 was creating, take the human, the island and the frame and pass it into the a mthod that will assign the right pixels the right colors
       meaning background white, left leg certain color, right leg certain color etc
       This is going to be done throught the decision tree built early
        
    */ 
    
    //After all 4 threads are done, build the image with the appropriate colors showing
    
    
    //things to make sure of 
    //1.) Threads know to wait for each other
    //2.) make sure no writing from memory while reading from memory
    //3.)
    
    
}