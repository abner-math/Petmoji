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

void face_detection(string input_image, string output_image);

void face_detection(string input_image, string output_image)
{
	// Load Face cascade (.xml file)
	Detector detector("conf/haarcascade_frontalface_alt2.xml",
			"conf/haarcascade_righteye_2splits.xml",
			"conf/haarcascade_lefteye_2splits.xml",
			"conf/haarcascade_smile.xml",
			"conf/haarcascade_eye.xml");

	std::vector<Rect> faces;

	Mat image = imread(input_image, CV_LOAD_IMAGE_COLOR);

	// Detect faces
	faces = detector.detect_faces(image);

	if (faces.size() > 1)
	{
		puts("WARNING: Achou mais de uma face!");
	}

	imwrite(output_image, image(faces[0]));
}

void check_arguments(int argc, char** argv)
{
	if (argc != 3)
	{
		puts("Numero incorreto de argumentos");
		exit(-1);
	}
}

int main(int argc, char** argv)
{
	check_arguments(argc, argv);
	face_detection(argv[1], argv[2]);
}

