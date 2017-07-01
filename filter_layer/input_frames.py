
##Runs in separate thread to fill queue with frames
##This class can be implemented with standard C++ queue from multithreading
## Must be safe for concurrent reads and writes
##This should run asynchronous
class input_from_kinect:

	class frame_input:
		##holds reference to color frame for kinetic
		##holds reference to depth frame for kinetic
		##hold timestamps

	def input_to_queue():
		##Stream frames to queue to await processing

	def clear_queue():
		##Empties queue of all frames

	def pull_frames():
		##Allows frame to be taken out of queue

	def isEmpty():
		##checks if queue is empty
