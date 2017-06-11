#
# This is a demo script for testing idea for the filter layer w/o using C.
#
# Main idea is
# 1. Find island
# 2. Identify which islands are humans
#
#
# Open Issues:
# What is a human is standing in front of something that is not the common background color?
#	Solution:
#	Probabilistic model for if island ends... Possible delta cut off of something a little cleaner
#	like 2 standard deviations above normal.
#
# What is humans islands are connected?
#	Solution:
#	Find average human cluster size and try to separate or just discard
#
# What is island moves but not human? Like a cat...
#	Solution:
#	Again island sizing....
#
#


from random import randint

##Constants for size of test grid
X_MAX =30
Y_MAX = 60

##1D array to hold images
dataOne = [0 for x in range((Y_MAX*X_MAX))]
dataTwo = [0 for x in range((Y_MAX*X_MAX))]


##Generates the random images for testing of the algorithm
def generateRandomCluster(data, minSize, maxSize, numClusters):

	for b in range(0,numClusters):
		size = randint(minSize, maxSize)
		starty = randint(0,Y_MAX-size)
		startx = randint(0,X_MAX-size)

		for x in range(startx, startx+size):
			for y in range(starty, starty+size):

				##adjust range of ints here
				setBase = randint(5,8)

				##offset for random number for some noise in data 
				setBase = setBase+randint(-2,2)
				setXY(x, y, data, (setBase  if setBase > 0 else 1))


## Translates 1D array to 2D
def getXY(x,y, data):
	if x >= X_MAX or y >= Y_MAX or x < 0 or y < 0:
		return -2;
	return data[(y*X_MAX-1)+x]


## Translates 1D array to 2D
def setXY(x, y, data, value):
	data[(y*X_MAX-1)+x] = value


##Counts the number of each pixel color to determine the mode color
## Runs: O(n) using HashMap (python calls it a map/set I think)
def findMostCommonColor(data):
	dictOfValues = {}
	for i in data[:]:
		if i in dictOfValues:
			dictOfValues[i] = dictOfValues.get(i) + 1
		else:
			dictOfValues[i] = 1
	return max(dictOfValues, key=dictOfValues.get)


## Prints the grid out to view
def printData(data):
	for x in range(X_MAX):
		print "\n"
		for y in range(Y_MAX):
			print getXY(x, y, data),

## Find the largest island and overwrites all member with 1's for viewing
def findBiggestIsland(data, commonColor):

	## Boolean grid to track visited
	dataVisted = [False for x in range(len(data))]

	##Holds tuples of islands found
	islandsFound = []

	##Loops through each pixel in grid
	for x in range(X_MAX):
		for y in range(Y_MAX):

			##Set as visited
			setXY(x, y, dataVisted, True)

			##Changed this if statement and it broke....
			##If not visited already and not background
			if not getXY(x, y, dataVisted) and getXY(x, y, data) is not commonColor:
				(dataVisted, island) = exspandIsland(x, y, data, dataVisted, [(x, y)], commonColor)
				
				##If we found and island (sanity check)
				if len(island) > 0:
					islandsFound.append(island)
	
	##Finds the largest island
	## Note: Lazy way to do it adds extra loop, should be done as islands are appended for speed
	maxIsland = None
	vMax = -1
	for v in islandsFound:
		if vMax < len(v):
			maxIsland = v
			vMax = len(v)

	## Write over the island for viewing
	for x1,y1 in maxIsland:
		setXY(x1,y1,data,1)

	return data

## Expands the islands to find all members
def exspandIsland(x, y, data, dataVisted, island, commonColor):
	## The bounding pixels up, down, right, left etc.
	directions = [(1,0), (0,1), (1,1), (-1,-1), (-1,0), (0,-1), (1,-1), (-1,1)]
	queue = []

	##Seed queue
	queue.append((x, y))

	##Basic breath first search iteratively
	while len(queue) > 0:
		point = queue.pop()

		##Add each direction
		for x,y in directions:
			x += point[0]
			y += point[1]

			## is not visited already
			if getXY(x, y, dataVisted) == False  and getXY(x, y, data) > 0:			
				setXY(x, y, dataVisted, True)
				island.append((x,y))
				queue.append((x,y))


	return (dataVisted, island)

## Not done yet
##NOTE: This needs to be amended for noisy data with some thresholding by percent
##This method assume the islands are in same order when passed
## this should be case if they are both generated in the same way
def diffingLayer(dataOne, dataTwo):
	diff = [abs(a - b) for a, b in zip(dataOne, dataTwo)]
	common = findMostCommonColor(diff)
	diff = findBiggestIsland(diff, common)
	return diff






print "Start",
# printData(data)
generateRandomCluster(dataOne, 14, 20, 1)
generateRandomCluster(dataOne, 1, 2, 5)
generateRandomCluster(dataOne, 1, 1, 5)

## Copies array to add another layer
dataTwo = dataOne[:]
generateRandomCluster(dataTwo, 14, 20, 1)

printData(dataOne);

#printData(data)

		