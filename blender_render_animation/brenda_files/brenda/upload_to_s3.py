#
# Function to upload output slides to S3 bucket
# This was added to brenda/utils.py
#
#

from boto.s3.connection import S3Connection
from boto.s3.key import Key
import os

def upload(dirName):
	print "Starting"
	conn = S3Connection("AWS_SECRET","AWS_ACCESS", )
	print "Auth..."
	bucket = conn.get_bucket('blender_mocap_frames')
	print "Connected S3..."
	for root, dirs, files in os.walk(dirName):
		for name in files:
			path = root.split(os.path.sep)[1:]
			path.append(name)
			key_id = os.path.join(*path)
			k = Key(bucket)
			k.key = key_id
			k.set_contents_from_filename(os.path.join(root, name))
			os.remove(os.path.join(root, name))


upload("Directory")