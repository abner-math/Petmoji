#ifndef HISTOGRAM_EQUALIZATION_H_
#define HISTOGRAM_EQUALIZATION_H_

#include "opencv2/imgproc/imgproc.hpp"

using namespace std;
using namespace cv;

Mat equalize_histogram(const Mat& rgb_image);

#endif /* HISTOGRAM_EQUALIZATION_H_ */
