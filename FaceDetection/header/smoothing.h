#ifndef SMOOTHING_H_
#define SMOOTHING_H_

using namespace cv;

#define GAUSSIAN 0
#define NORMALIZED 1

Mat smoothing(Mat source, int type, int kernel_size_width, int kernel_size_height);

#endif /* SMOOTHING_H_ */
