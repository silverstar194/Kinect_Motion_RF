#!/usr/bin/python
from Queue import Queue
from threading import Thread
import subprocess

S3_PATH = "s3://blender_mocap_frames/home/ubuntu/brenda/brenda-outdir.tmp/"
##Max threads
num_threads = 48

##Get in directory names from pre-build list
file = open("s3_dir_to_fetch_copy", "r") 

##load up queue with tasks
q = Queue(maxsize=0)
for line in file:
	q.put(line[:-1])


def fetch_s3_data(q, i):
	count = 0
	while True:
  		dir_name = q.get()
  		subprocess.check_output(["mkdir", dir_name])
  		result = subprocess.check_output(["aws", "s3", "cp", "--recursive", S3_PATH+dir_name+"/", "./"+dir_name])
  		f = open("fetched_thread_"+str(i), "a")
  		f.write(dir_name)
  		print "Fetched "+ dir_name +" on Thread #"+str(i)+" Count: "+str(count)
  		count+=1
  		q.task_done()


for i in range(num_threads):
  worker = Thread(target=fetch_s3_data, args=(q, i))
  worker.setDaemon(True)
  worker.start()


q.join()


