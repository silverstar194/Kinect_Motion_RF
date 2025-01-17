#include "libfreenect.hpp"
#include <iostream>
#include <vector>
#include <cmath>
#include <pthread.h>
#include <cv.h>
#include <cxcore.h>
#include <highgui.h>


using namespace cv;
using namespace std;


//sets up class to protect threads info when multi. threaded
class myMutex {
	public:
		myMutex() {
			pthread_mutex_init( &m_mutex, NULL );
		}
		void lock() {
			pthread_mutex_lock( &m_mutex );
		}
		void unlock() {
			pthread_mutex_unlock( &m_mutex );
		}
	private:
		pthread_mutex_t m_mutex;
};

//handles interaction with hardware
//left this as I found it from hacking_the_kinect.pdf
class MyFreenectDevice : public Freenect::FreenectDevice {
	public:
		MyFreenectDevice(freenect_context *_ctx, int _index)
	 		: Freenect::FreenectDevice(_ctx, _index), m_buffer_depth(FREENECT_DEPTH_11BIT),
			m_buffer_rgb(FREENECT_VIDEO_RGB), m_gamma(2048), m_new_rgb_frame(false),
			m_new_depth_frame(false), depthMat(Size(640,480),CV_16UC1),
			rgbMat(Size(640,480), CV_8UC3, Scalar(0)),
			ownMat(Size(640,480),CV_8UC3,Scalar(0)) {
			
			for( unsigned int i = 0 ; i < 2048 ; i++) {
				float v = i/2048.0;
				v = std::pow(v, 3)* 6;
				m_gamma[i] = v*6*256;
			}
		}
		
		// Do not call directly even in child
		//Device calls this methoid when it has new rgb data to push
		void VideoCallback(void* _rgb, uint32_t timestamp) {
			std::cout << "RGB callback" << std::endl;
			m_rgb_mutex.lock();

			uint8_t* rgb = static_cast<uint8_t*>(_rgb);
					
			// rgbMat.data[0] = (uint8_t)0;
			m_new_rgb_frame = true;
			m_rgb_mutex.unlock();
		}
		
		//allows to set color for x,y value in RBG
		void setPixalVideoValue(uint8_t* depthData, int x, int y, int colorR, int colorG, int colorB){
			if(x < 0 || x > 479){
				std::cout << "Error in X Bounds: RGB";
				exit(1);
			}

			if(y < 0 || y > 639){
				std::cout << "Error in Y Bounds: RGB";
				exit(1);
			}
			depthData[(x*640)*3+y*3] = (uint8_t)colorR;
			depthData[(x*640)*3+1+y*3] = (uint8_t)colorG;
			depthData[(x*640)*3+2+y*3] = (uint8_t)colorB;
		}


		// Do not call directly even in child
		//Device calls this methoid when it has new depth data to push
		void DepthCallback(void* _depth, uint32_t timestamp) {
			m_depth_mutex.lock();
			uint16_t* depth = static_cast<uint16_t*>(_depth);
			depthMat.data = (uchar*) depth;

			///set x,y values here


			m_new_depth_frame = true;
			m_depth_mutex.unlock();
		}

		//allows to set color for x,y value
		void setPixalDepthValue(uchar* depthData, int x, int y, int color){
			if(x < 0 || x > 479){
				std::cout << "Error in X Bounds: Depth";
				exit(1);
			}

			if(y < 0 || y > 639){
				std::cout << "Error in Y Bounds: Depth";
				exit(1);
			}

			depthData[(x*640)*2+y*2] = (uint16_t)color;
			depthData[(x*640)*2+1+y*2] = (uint16_t)color;
		}
		
		//checks if new frame is there and converts to usable format
		bool getVideo(Mat& output) {
			m_rgb_mutex.lock();
			if(m_new_rgb_frame) {
				cv::cvtColor(rgbMat, output, CV_RGB2BGR);
				m_new_rgb_frame = false;
				m_rgb_mutex.unlock();
				return true;
			} else {
				m_rgb_mutex.unlock();
				return false;
			}
		}
		
		bool getDepth(Mat& output) {
				m_depth_mutex.lock();
				if(m_new_depth_frame) {
					depthMat.copyTo(output);
					m_new_depth_frame = false;
					m_depth_mutex.unlock();
					return true;
				} else {
					m_depth_mutex.unlock();
					return false;
				}
			}
	private:
		std::vector<uint8_t> m_buffer_depth;
		std::vector<uint8_t> m_buffer_rgb;
		std::vector<uint16_t> m_gamma;
		Mat depthMat;
		Mat rgbMat;
		Mat ownMat;
		myMutex m_rgb_mutex;
		myMutex m_depth_mutex;
		bool m_new_rgb_frame;
		bool m_new_depth_frame;
};


int main(int argc, char **argv) {
	bool die(false);
	
	Mat depthMat(Size(640,480),CV_16UC1);
	Mat depthf (Size(640,480),CV_8UC1);
	Mat rgbMat(Size(640,480),CV_8UC3,Scalar(0));
	Mat ownMat(Size(640,480),CV_8UC3,Scalar(0));
	
	// The next two lines must be changed as Freenect::Freenect
	// isn't a template but the method createDevice:
	// Freenect::Freenect<MyFreenectDevice> freenect;
	// MyFreenectDevice& device = freenect.createDevice(0);
	// by these two lines:
	
	Freenect::Freenect freenect;
	MyFreenectDevice& device = freenect.createDevice<MyFreenectDevice>(0);
	
	namedWindow("rgb",CV_WINDOW_AUTOSIZE);
	namedWindow("depth",CV_WINDOW_AUTOSIZE);
	device.startVideo();
	device.startDepth();


	while (!die) {
		device.getVideo(rgbMat);
		device.getDepth(depthMat);


		cv::imshow("rgb", rgbMat);
		//print out rgbMat

		depthMat.convertTo(depthf, CV_8UC1, (255.0/2048.0)+.1);

	   	cv::imshow("depth",depthf);

	   	//clean up
		char k = cvWaitKey(5);
		if( k == 27 ){
			cvDestroyWindow("rgb");
			cvDestroyWindow("depth");
			break;
		}

	}
	
	device.stopVideo();
	device.stopDepth();
	return 0;
}

