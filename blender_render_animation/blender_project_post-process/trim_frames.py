import subprocess, os, sys, string, random, math
from Queue import Queue
from threading import Thread

##For trim Ec2 r4.4xlarge

def run_trim(q, i):
	##Make output directories
	count = 0
	while True:
		folder_to_trim = q.get()
		subprocess.check_output(["mkdir", "./"+folder_to_trim+"_trimmed"])
		subprocess.check_output(["mkdir", "./"+folder_to_trim+"_trimmed/color"])
		subprocess.check_output(["mkdir", "./"+folder_to_trim+"_trimmed/depth"])


		##List trim frames
		color_files = []
		first_color = sys.maxint
		last_color = -sys.maxint -1
		for filenames in os.listdir("./"+folder_to_trim+"/color"):
			tokens_color = string.split(filenames, "_")
			num_color_end = tokens_color[len(tokens_color)-1]
			num_color = float(string.split(num_color_end, ".")[0])
			color_files.append(filenames)

			if num_color < first_color:
				first_color  = num_color

			if num_color > last_color:
				last_color = num_color


		depth_files = []
		first_depth = sys.maxint
		last_depth = -sys.maxint -1
		for filenames in os.listdir("./"+folder_to_trim+"/depth"):
			tokens_depth = string.split(filenames, "_")
			num_depth_end = tokens_depth[len(tokens_depth)-1]
			num_depth = float(string.split(num_depth_end, ".")[0])
			depth_files.append(filenames)


			if num_depth < first_depth:
				first_depth  = num_depth

			if num_depth > last_depth:
				last_depth = num_depth


		##Sanity check that color and depth dirs. the same
		if not (len(depth_files) == len(color_files) and first_depth == first_color and last_depth == last_color):
			print "Color and depth do not have same frames...."
			sys.exit()

		from_frame = int(math.floor(last_depth*.333-(.333*.5)))
		to_frame = int(math.floor(last_depth*.666+(.333*.5)))

		subprocess.check_output(["python", "postprocess.py", "-o", folder_to_trim+"_trimmed", "-f", str(from_frame), "-t", str(to_frame), folder_to_trim])
		count += 1
		print "Trimmed "+ folder_to_trim +" on Thread #"+str(i)+" Count: "+str(count)
		f = open("trimmed_thread_"+str(i), "a")
  		f.write(dir_name)
		q.task_done()



num_threads = 48

##Directory to start from
q = Queue(maxsize=0)
file = open("dir_to_trim", "r") 

for line in file:
	q.put(line[:-1])


for i in range(num_threads):
  worker = Thread(target=run_trim, args=(q, i))
  worker.setDaemon(True)
  worker.start()


q.join()



