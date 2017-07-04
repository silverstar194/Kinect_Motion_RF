# This scripted was used to list all files in a directory recursively. Very useful for verifying properly execution of other scripts
# when coupled with diff and an expected list.
#
#
#
import os

FILE_PATH = None

f = open("list_files.txt", "w")

count = 0;
for path, subdirs, files in os.walk(FILE_PATH):
    for name in files:
        print os.path.join(path, name)
        if "png" in name:
        	f.write((os.path.join(path, name))+"\n")
        	count+=1
        	print count

f.close()
