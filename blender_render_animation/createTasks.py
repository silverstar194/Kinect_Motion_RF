# This creates all the different commands to run blender in order to render all the variations of
# the animations that are needed. These are then pushed to the render cloud farm.
#
#
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
		baseModel = """blender "/home/ubuntu/mocap.blend" --background -S Scene --python-text API -a -- "/home/ubuntu/mocap_files/"""
		mocap = fileToken[0]+"""/"""+file+""".bvh\" """
		focus = " -4.9 .3125 "
		output = """\"/home/ubuntu/brenda/brenda-outdir.tmp/"""+fileToken[0]+"_"+fileToken[1]+"""_"""+model+"\""
		outputs.append((baseModel + mocap +"\""+model+"\""+focus + output))
		count+=1

print "Total Files: "+str(count)




fh = open("frames_to_push", "a") 
for string in outputs:
	fh.write(string+"\n") 
	fh.close 