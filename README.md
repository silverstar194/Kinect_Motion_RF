# Kinect_Motion_RF
Building a Kinect from Scratch With Random Forests and Blender animations

## Final Flow
## Input Layer
The input layer uses OpenKinect (https://openkinect.org/wiki/Main_Page) to pull the raw depth and RBG images off of the Kinect hardware. The depth image is sent to filter layer for pre-processing before it is run through the random forest for classification.
	
## Filter Layer
The filter layer isolates the people (or person) from the background. This processed image is then sent to random forest for classification.

## Random Forest
The random forest runs each pixel for classification based on difference of depth. This information is stored for markup and query.

## Markup and User API
The RBG image is marked with body parts and users are able to query API for body part locations.



## Gathering and Processing Training Data
### Overview
Blender 3D render software (https://www.blender.org/) was paired with Carnegie Mellon motion capture library (http://mocap.cs.cmu.edu/subjects.php) to produce both a depth image and marked color image for random forest training.

### Pre and Post Processing

In post processing the frames needed to be trimmed to scaled. I found a memory issue in PIL image proccessing library and was only able to use 3 threads even with 144gb of memory. Some images ended up comsuming 30gb+.

### Scale
The humans needed to be scaled in two ways:
+ 1. Body proportions needed to be accurate to real life
+ 2. The depth scale from black to white needed to be repersentative of the data th kinect hardware would provide. There were three paramaters to consider.

++ rate of change from white to black
++ range of shades between white to black
++ range in meters that kinect data is collected from

### Costs
AWS was leveraged heavly for computational power, storage, queuing and data transfer.

Break down in services and cost follows:

+ EBS (Elastic Block Storage)
+ EC2 (Elastic Compute Cloud)
+ SQS (Simple Queue Service)
+ S3 (Simple Storage Service)


### Flow


## Building Input Layer



## Building Filter Layer

### Overview
This layer is responsible for reducing the amount of noise in data before it is sent for classification. It must be flexible enough isolate a person or persons from background elements.

### Algorithm

	find common color
	for each pixel in cur. image != common color
		expand and find island

	for each pixel in prev. image != common color
		expand and find island

	if island moved from prev. to cur
		isolate to separate layer for classification


### Implementation
Nitty gitty bits here of why, how and trade offs


### Challenges



## Building Random Forests

### Overview

### Algorithm

### Implementation

### Challenges


## Building Markup and User API


### Overview

### Algorithm

### Implementation

### Challenges


## Using the API

	DOCS FOR API USAGE
	
## The C++ Part TODO:[Move this section and split later]

### Pre-built decision tree:
To start off the program we need to built the decision tree that will be used for classication of body parts. 
Given a list of parent child relationships and split information build decisions trees.

```
Example:
Tree #	Parent Node	Child Node	Split Info. (index, value - less then/more then)
1	asdf8x8		mba80nd		9 < 87 		##Inner Node
1	asdf8x8		hasdf78		80 < 2001 	##Inner Node
1	null		asdf8x8		52 > 10 	##Root Node
1	mba80nd		[1, 2, 3, 4]	32 < 09 	##Leaf Node 
…
more nodes and nodes for other trees as well

This will build tree 1 like this (given rows 1-4):
		 asdf8x8
		/	\
      	       /	 \
	mba80nd		hasdf78
		\		\
		 \		 \
 	 dis:[1, 2, 3, 4]	  …
```

This leaf node holds a distribution of likelyhood (as %) where:

	dis[index_of_category] / sum(dis)

thus likelihood of category 3 would be 40% (4/10)


We must also remember there are multiple trees so for the end decision of what the part is it needs to be the most likely across all trees.

### Flow Post-Tree Building:
1. Input (Thread #1)
+ Interacts with the Kinect Layer to pull frames off of hardware

2. Island Forming (Thread #2)
+ Pulls frames from Input queue and filters out islands

3. Island Classification (Thread #3)
+ Pulls frames from Island Forming and decides which islands are human-like based on movement between frames

4. Calculate Attributes (Thread #4 - Thread #N —- Concurrent threads for each pixel up to core limit)
+ Produces delta vector of attribute vales
+ The attributes are compulted from the differnce in current pixel and pixels surrounding it:
	```
	Example:
		********************************
		********************************
		********2******-1***************
		*********100*****-1***6*********
		********5****2*X*0***5**********
		**********3****-1*****7*********
		*********2*****-3****4**********
		********9*******-4**************
		********************************
		********************************
	Say pixel X is current pixel up for classification its attiribution vector is:
	attrib: [2,-1,100,-1,6,5,2,0,5,3,-1,7,2,-3,4,9,-4]
	```


5. RTree Classification  (Thread #4 - Thread #N —- Concurrent threads for each pixel up to core limit)
+ Classifies each point based on delta vector


6. Annotate + API
+ Marked up frame with body part color and output to video stream
+ Provide query API that will return array of tuples (x, y) of body part when asked for specific body part
	
	```
	Example:
	def getLeftArm():
		return array[ALL_LEFT_ARM_PIXELS]
	```
		


### General Flow and Threads
One important thing to one is how the threads are used through the proccessing steps:

```
Kinect Hardware
|
|
V
Thread 1: Input <--Queue-->
	Thread 2: Island Forming <--Queue--> 
				Thread 3: Island Classification --> Calculate Attributes --> RTree Classification --> Annotate + API
				Thread 6: Island Classification --> Calculate Attributes --> RTree Classification --> Annotate + API
					.....
				Thread Core Limit: Island Classification --> Calculate Attributes --> RTree Classification --> Annotate + API
```
										
Input and Island Forming: 
+ They will runs continuely polling loops to look for an proccess new data
+ They will all run asynchronous


Island Classification, RTree Classification and Annotate + API:
+ They will draw from a queue and use a thread pool to manage and dis
tribute work load
+ Will write and read from a single passed frame and will run synchronous with eachother but asynchronous with threads 1 and 2



Notes for later reference:
+ Exspanding file system:
++ http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ebs-expand-volume.html#recognize-expanded-volume-linux

+ Building Kinect
++ https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/BodyPartRecognition.pdf
++ http://www.cs.cornell.edu/courses/cs7670/2011fa/lectures/zhaoyin_kinect.pdf
++ https://archive.org/details/Microsoft_Research_Video_148136
++ https://docs.google.com/presentation/d/1tzhm_HBEcg4_a-Wx52E0ACrzy0jmAphHpwhFFITo4g0/edit#slide=id.p53

+ Real-time human pose recognition in parts from a single depth image
++ http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.297.3923&rep=rep1&type=pdf
++ http://www.makehumancommunity.org/forum/viewtopic.php?f=7&t=13313
++ http://vestan.github.io/#

+ Excellent post on data
++ https://alastaira.wordpress.com/2013/07/24/using-free-mocap-data-for-game-character-animation-in-unity/

+ Shading Unity:
+ +http://willychyr.com/2013/11/unity-shaders-depth-and-normal-textures-part-3/

+ Get Kinect Data (Java):
++ http://peterabeles.com/blog/?p=226


```growpart /dev/xvdb 1
```
