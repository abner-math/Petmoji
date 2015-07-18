/*
 * main.cpp
 *
 *  Created on: Jul 16, 2015
 *      Author: armstrong
 */

#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "face_detection.h"

#include <iostream>
#include <stdio.h>

using namespace std;
using namespace cv;

int face_detection_demo();

int face_detection_demo()
{
    Mat image;
	char input_image[100];
	char output_image[100];

	// Load Face cascade (.xml file)
	Detector detector("conf/haarcascade_frontalface_alt2.xml",
			"conf/haarcascade_righteye_2splits.xml",
			"conf/haarcascade_lefteye_2splits.xml",
			"conf/haarcascade_smile.xml",
			"conf/haarcascade_eye.xml");

	std::vector<Rect> faces;
	std::vector<Rect> right_eyes;
	std::vector<Rect> left_eyes;
	std::vector<Rect> smiles;
	std::vector<Rect> eyes;

	for (int j = 1; j <= 11; j++)
	{
		sprintf(input_image, "images/%d.jpg", j);
		sprintf(output_image, "result/%d.jpg", j);
		image = imread(input_image, CV_LOAD_IMAGE_COLOR);

		// Detect faces
		faces = detector.detect_faces(image);
		right_eyes = detector.detect_right_eye(image);
		left_eyes = detector.detect_left_eye(image);
		smiles = detector.detect_smile(image);
		eyes = detector.detect_eyes(image);
		// Draw circles on the detected faces
		for( int i = 0; i < faces.size(); i++ )
		{
			rectangle(image, faces[i], Scalar(0, 255, 0));
		}

		/*for( int i = 0; i < right_eyes.size(); i++ )
		{
			rectangle(image, right_eyes[i], Scalar(255, 0, 0));
		}

		for( int i = 0; i < left_eyes.size(); i++ )
		{
			rectangle(image, left_eyes[i], Scalar(0, 0, 255));
		}*/

		for( int i = 0; i < smiles.size(); i++ )
		{
			rectangle(image, smiles[i], Scalar(255, 0, 255));
		}

		for( int i = 0; i < eyes.size(); i++ )
		{
			rectangle(image, eyes[i], Scalar(255, 0, 0));
		}

		imwrite(output_image, image);
	}

	return 0;
}

int main( int argc, char** argv )
{
	face_detection_demo();
}

