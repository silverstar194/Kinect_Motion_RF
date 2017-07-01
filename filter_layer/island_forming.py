##This class pulls frame available frame queue and isolates islands from frames
##Must be safe for concurrent reads and writes
##This should run asynchronous
class island_forming:

	##queue of frames to process
	##queue of processed frames

	class frame_island_forming:
		##ref to frame_input class
		##array of islands

	def input_to_queue_from_pull_frames():
		##Gets frame from input queue as long as !isEmpty

	def clear_queue():
		##Empties queue of all island

	def pull_frames():
		##Allows frame to be taken out of queue
		##return frame_island_forming

	def isEmpty():
		##checks if queue is empty

	def filter_outlier_islands():
		##Removes any islands the are too unlikely to be humans


