from random import randint

X_MAX =30
Y_MAX = 60
dataOne = [0 for x in range((Y_MAX*X_MAX))]
dataTwo = [0 for x in range((Y_MAX*X_MAX))]

def generateRandomCluster(data, minSize, maxSize, numClusters):

	for b in range(0,numClusters):
		size = randint(minSize, maxSize)
		starty = randint(0,Y_MAX-size)
		startx = randint(0,X_MAX-size)

		for x in range(startx, startx+size):
			for y in range(starty, starty+size):
				setBase = randint(0,2)
				setBase = setBase+randint(0,0)
				setXY(x, y, data, (setBase  if setBase > 0 else 1))



def getXY(x,y, data):
	if x >= X_MAX or y >= Y_MAX or x < 0 or y < 0:
		return -2;
	return data[(y*X_MAX-1)+x]


def setXY(x, y, data, value):
	data[(y*X_MAX-1)+x] = value


def findMostCommonColor(data):
	dictOfValues = {}
	for i in data[:]:
		if i in dictOfValues:
			dictOfValues[i] = dictOfValues.get(i) + 1
		else:
			dictOfValues[i] = 1
	return max(dictOfValues, key=dictOfValues.get)



def printData(data):
	for x in range(X_MAX):
		print "\n"
		for y in range(Y_MAX):
			print getXY(x, y, data),


def findBiggestIsland(data, commonColor):
	dataVisted = [False for x in range(len(data))]
	islandsFound = []
	cor = []
	for x in range(X_MAX):
		for y in range(Y_MAX):
			setXY(x, y, dataVisted, -1)
			if getXY(x, y, data) > 0:
				(dataVisted, island) = exspandIsland(x, y, data, dataVisted, [(x, y)], commonColor)
				if len(island) > 0:
					islandsFound.append(island)
	
	maxIsland = None
	vMax = -1
	for v in islandsFound:
		if vMax < len(v):
			maxIsland = v
			vMax = len(v)

	for x1,y1 in maxIsland:
		setXY(x1,y1,data,1)

	return data


def exspandIsland(x, y, data, dataVisted, island, commonColor):
	directions = [(1,0), (0,1), (1,1), (-1,-1), (-1,0), (0,-1), (1,-1), (-1,1)]
	queue = []
	queue.append((x, y))

	while len(queue) > 0:
		point = queue.pop()
		for x,y in directions:
			x += point[0]
			y += point[1]

			if getXY(x, y, dataVisted) == False and getXY(x, y, data) is not commonColor and getXY(x, y, data) > 0:			
				setXY(x, y, dataVisted, True)
				island.append((x,y))
				queue.append((x,y))


	return (dataVisted, island)

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

dataTwo = dataOne[:]
generateRandomCluster(dataTwo, 14, 20, 1)

printData(diffingLayer(dataOne, dataTwo))

#printData(data)

		