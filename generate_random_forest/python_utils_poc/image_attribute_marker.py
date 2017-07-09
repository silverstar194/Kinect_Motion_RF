from PIL import Image
import math
FILE_PATH = "sample_trimmed/120_17_Male_normal_trimmed/color/120_17_c_0190.png"
im = Image.open(FILE_PATH )

def PointsInCircum(r, xCor, yCor, n=100):
    return [(xCor+math.cos(2*3.14/n*x+r)*r, yCor+math.sin(2*3.14/n*x+r)*r) for x in xrange(0,n+1)]

rgb_im = im.convert('RGB')
width, height = im.size
done = False
count = 0
for h in range(1):
	for w in range(1):
		r, g, b = rgb_im.getpixel((100, 100))
		if ((r != 0 or g != 0 or b != 0) and not done) or True:
			for o in range(7,42,7):

				points = PointsInCircum(o, 100, 100, 5)
				for i,j in points:
					if int(i) < height and int(j) < width and int(i) > 0 and int(j) > 0:
						rgb_im.putpixel((int(j), int(i)), 255)
						count+=1
						done = True
						print str(int(j)) +" "+str(int(i))
					
# print "Points: "+str(count)

rgb_im.show()