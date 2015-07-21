#include "opencv2/imgproc/imgproc.hpp"
#include "smoothing.h"

#include <stdio.h>
using namespace cv;

Mat smoothing(Mat source, int type, int kernel_width, int kernel_height)
{
	Mat result;

	switch (type)
	{
		case GAUSSIAN: GaussianBlur(source, result, Size(kernel_width, kernel_height), 0, 0); break;
		case NORMALIZED: blur( source, result, Size(kernel_width, kernel_height), Point(-1,-1)); break;
	}

	return result;
}


