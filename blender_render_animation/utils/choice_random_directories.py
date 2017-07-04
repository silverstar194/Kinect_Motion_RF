# Used to randomly select directories to create a sample of the entire space.
#
# This command copies all of the files in list to new directory
#for file in `cat choices.txt`; do cp -r "$file" sample_trimmed ; done

import random
file = open("dir_to_trim", "r") 


NUM_TO_PICK = 15
array = []
for l in file:
	array.append(l)


for i in range(NUM_TO_PICK):
	e = random.choice(array)
	print(e)
	f = open("choices.txt", "a")
	f.write(e[:-1]+"_trimmed\n")
	f.write(e)



