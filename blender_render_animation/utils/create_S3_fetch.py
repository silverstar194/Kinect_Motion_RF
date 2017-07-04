# This creates all the different paths for the s3 bucket fetching.
#
#

import string

file = open("cmu-mocap-index-text.txt", "r") 
excludeTwoSubjects = [""]

files = []
for line in file:
	if line[0] < "9" and line[0] > "0":
		tokens = string.split(line, "\t")
		
		excludeTwoSubjectsCheck = string.split(tokens[0], "_")
		if excludeTwoSubjectsCheck[0] not in excludeTwoSubjects:
			files.append(tokens[0])



count = 0
outputs = []
for file in files:
	models = ["Child_androgynous", "Teenager_androgynous", "Female_normal", "Male_normal", "Human_heavy"]
	for model in models:
		fileToken = string.split(file, "_")
		outputs.append(fileToken[0]+"_"+fileToken[1]+"""_"""+model)
		count+=1

print "Total Files: "+str(count)




fh = open("s3_dir_to_fetch", "a") 
for string in outputs:
	fh.write(string+"\n") 
	fh.close 