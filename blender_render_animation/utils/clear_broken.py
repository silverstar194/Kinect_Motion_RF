#
# This clear out broken nodes by comparing task counts over time.
# DO NOT USE, this turned out to be a messy way of doing it
#
def clear_broken_instances():
	import subprocess, string

# ##Build last output for compare
	ec2_map = {}
	map_ip_id = getIDs()
	##Read old token from file
	tokens = ""
	with open("last_call.txt", 'r') as last_call:
		for i in last_call:
			tokens += i

	tokens = string.split(tokens, "\n")
	tokens = tokens[:-1]

	for i in range(0,len(tokens),2):
		if tokens[i][0] == "e":
			ec2_map[tokens[i]] = tokens[i+1]




	# ##Read in new token from AWS
	result = subprocess.check_output(["brenda-tool", "ssh", "tail", "task_count"])
	output = open("last_call.txt", 'w')

	tokens = string.split(result, "\n")
	tokens = tokens[:-1]

	check_one = None
	instance_id = None
	need_to_stop = []
	for token in tokens:
		if token[0] == "-":
			output.write(token[len("------- "):]+"\n")
			#print "Instance: "+token[len("------- "):] + "\n\tOld Value: " + ec2_map[token[len("------- "):]]
			#print ec2_map
			check_one = ec2_map[token[len("------- "):]]

			instance_id = token[len("------- "):]

		elif token[0] == "c":
			output.write("Starting...\nN/A\n")
			print "Starting..."

		else:
			output.write(token+"\n")
			#print "\tNew Value: "+token

			if int(check_one) == int(token):
				print " Old: "+str(check_one)+ " New: "+str(token) + " "+instance_id
				result = subprocess.check_output(["aws", "ec2", "terminate-instances", "--instance-ids", map_ip_id[instance_id], "--dry-run"])
				#print result
				#need_to_stop.append(instance_id)
				print "\tKilling instance.... "

			else:
				"\tKeep running "+instance_id+".... "

	output.close()

#
# Clear out broken instances by looking at logs.
#
#
def clear_broken_instances_easy_way():
	import subprocess, string, re, os

	## Fetch logs from nodes
	result = subprocess.check_output(["brenda-tool", "ssh", "tail", "-1", "log"])

	tokens = string.split(result, "\n")
	# map_ip_id = getIDs()
	#print result


	for i in range(0,len(tokens),2):
		if tokens[i] != "" and tokens[i][0] == "-":
			machine = tokens[i][len("------- "):]
			status = tokens[i+1]

	#print status
	## Find broken nodes
		if status == "Blender quit" or "<?xml" in status or "ValueError" in status:
			print machine + " "+status

			##Terminate them
			result = os.system("ssh -i ~/.ssh/id_rsa.brenda root@"+machine+" 'shutdown now'")
			print "\tKilling instance.... "

#
# This maps the IPs to amazons instance ids.
# Don't need this for easy way.
#
def getIDs():
	map_ip_id = {}
	import subprocess, string, re
	result = subprocess.check_output(["brenda-tool", "ssh", "curl http://169.254.169.254/latest/meta-data/instance-id"])
	ids = re.findall(r"i-[A-Za-z0-9]*", result)
	ips = re.findall(r"ec2-[A-Za-z0-9-.]*", result)
	


	for i in range(len(ids)):
		#print str(i)+" Id: "+ids[i][0:18] + " Ip: "+ips[i]
		map_ip_id[ips[i]] = ids[i]

	return map_ip_id


## Run loop clearing them every 10 minutes
import time, datetime
while True:
	print ""
	print datetime.datetime.now()
	clear_broken_instances_easy_way()
	for i in range(1,20):
		print "Sleeping "+str(i)+" of 20...."
		time.sleep(30)


