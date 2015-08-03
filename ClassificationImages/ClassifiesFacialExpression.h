/*
 * ClassifiesFacialExpression.h
 *
 *  Created on: 03/08/2015
 *      Author: daniela
 */

#ifndef CLASSIFIESFACIALEXPRESSION_H_
#define CLASSIFIESFACIALEXPRESSION_H_


#include "opencv2/core/core.hpp"
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <iostream>




using namespace cv;
using namespace std;

class ClassifiesFacialExpression {
public:
	ClassifiesFacialExpression(Mat img, string pathModel, int resolution);
	ClassifiesFacialExpression(Mat img);

	Ptr<FaceRecognizer> loadsFile();

	int getClassLabelImage(Mat img, Ptr<FaceRecognizer> model);

private:
	string pathModel;
	Mat img;
	int resolution;

};



#endif /* CLASSIFIESFACIALEXPRESSION_H_ */
