/*
 * histogram_equalization.cpp
 *
 *  Created on: Jul 20, 2015
 *      Author: armstrong
 */

#include "opencv2/imgproc/imgproc.hpp"

using namespace std;
using namespace cv;

/*
 * Baseado no codigo achado em
 * http://prateekvjoshi.com/2013/11/22/histogram-equalization-of-rgb-images/
 */
Mat equalize_histogram(const Mat& rgb_image)
{

	Mat ycrcb_image;
	cvtColor(rgb_image,ycrcb_image,CV_BGR2YCrCb);

	vector<Mat> ycrcb_channels;
	split(ycrcb_image,ycrcb_channels);

	equalizeHist(ycrcb_channels[0], ycrcb_channels[0]);

	Mat result;
	merge(ycrcb_channels,ycrcb_image);
	cvtColor(ycrcb_image,result,CV_YCrCb2BGR);

	return result;
}


