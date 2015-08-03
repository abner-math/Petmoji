/*
 * Run.cpp
 *
 *  Created on: 23/07/2015
 *      Author: daniela
 */


#include "ClassifiesFacialExpression.h"


int main(int argc, char **argv) {


		Mat img = imread(argv[2], CV_LOAD_IMAGE_GRAYSCALE);

		ClassifiesFacialExpression cls1(img);

		Ptr<FaceRecognizer> model = cls1.loadsFile();

		int classLabel = cls1.getClassLabelImage(img, model);
		cout<<"Label expression: "<<classLabel<<endl;



	return 0;
}



