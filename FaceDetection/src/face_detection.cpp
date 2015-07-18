/*
 * facedetection.cpp
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

Detector::Detector(string frontal_face_cascade_path,
					string right_eye_cascade_path,
					string left_eye_cascade_path,
					string smile_cascade_path,
					string eyes_cascade_path)
{
	frontal_face_classifier.load(frontal_face_cascade_path);
	right_eye_classifier.load(right_eye_cascade_path);
	left_eye_classifier.load(left_eye_cascade_path);
	smile_classifier.load(smile_cascade_path);
	eyes_classifier.load(eyes_cascade_path);
}

vector<Rect> Detector::detect_faces(Mat image)
{
	std::vector<Rect> faces;
	frontal_face_classifier.detectMultiScale(image, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return faces;
}

vector<Rect> Detector::detect_right_eye(Mat image)
{
	std::vector<Rect> right_eyes;
	right_eye_classifier.detectMultiScale(image, right_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return right_eyes;
}

vector<Rect> Detector::detect_left_eye(Mat image)
{
	std::vector<Rect> left_eyes;
	left_eye_classifier.detectMultiScale(image, left_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return left_eyes;
}

vector<Rect> Detector::detect_smile(Mat image)
{
	std::vector<Rect> smiles;
	smile_classifier.detectMultiScale(image, smiles, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(100, 100));
	return smiles;
}

vector<Rect> Detector::detect_eyes(Mat image)
{
	std::vector<Rect> eyes;
	eyes_classifier.detectMultiScale(image, eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return eyes;
}
