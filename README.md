# Kinect_Motion_RF
Building a Kinect from Scratch With Random Forests and Blender animations

## Final Flow
	### Input Layer
		The input layer uses OpenKinect (https://openkinect.org/wiki/Main_Page) to pull the raw depth and RBG images off of the Kinect hardware. The depth image is sent to filter layer for pre-processing before it is run through the random forest for classification.
	
	### Filter Layer
		The filter layer isolates the people (or person) from the background. This processed image is then sent to random forest for classification.

	### Random Forest
		The random forest runs each pixel for classification based on difference of depth. This information is stored for markup and query.

	### Markup and User API
		The RBG image is marked with body parts and users are able to query API for body part locations.



## Gathering and Processing Training Data
	### Overview
		Blender 3D render software (https://www.blender.org/) was paired with Carnegie Mellon motion capture library (http://mocap.cs.cmu.edu/subjects.php) to produce both a depth image and marked color image for random forest training.

	### Pre and Post Processing

	### Scale

	### Costs

	### Flow


## Building Input Layer



## Building Filter Layer

	### Overview

	### Algorithm

	### Implementation

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
