#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "face_detection.h"
#include "histogram_equalization.h"
#include "smoothing.h"

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

vector<Rect> Detector::detect_faces(Mat image, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
{
	vector<Rect> faces;
	frontal_face_classifier.detectMultiScale(image, faces, scale_factor, min_neighbors, 0|CV_HAAR_SCALE_IMAGE, Size(min_size_x, min_size_y));
	return faces;
}

vector<Rect> Detector::detect_right_eye(Mat image)
{
	vector<Rect> right_eyes;
	right_eye_classifier.detectMultiScale(image, right_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return right_eyes;
}

vector<Rect> Detector::detect_left_eye(Mat image)
{
	vector<Rect> left_eyes;
	left_eye_classifier.detectMultiScale(image, left_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return left_eyes;
}

vector<Rect> Detector::detect_smile(Mat image)
{
	vector<Rect> smiles;
	smile_classifier.detectMultiScale(image, smiles, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(100, 100));
	return smiles;
}

vector<Rect> Detector::detect_eyes(Mat image)
{
	vector<Rect> eyes;
	eyes_classifier.detectMultiScale(image, eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
	return eyes;
}

vector<Mat> Detector::get_faces(Mat input_image, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
{
	vector<Rect> faces_rects = detect_faces(input_image, scale_factor, min_neighbors, min_size_x, min_size_y);
	vector<Mat> faces;

	for (unsigned int i = 0; i < faces_rects.size(); i++)
	{
		faces.push_back(input_image(faces_rects[i]));
	}

	return faces;
}

bool Detector::has_eyes(Mat image)
{
	return detect_eyes(image).size() > 0;
}

Mat Detector::get_best_face_candidate(vector<Mat> faces_candidates)
{
	assert(faces_candidates.size() > 0);

	Mat best_candidate = faces_candidates[0];

	for (unsigned int i = 0; i < faces_candidates.size(); i++)
	{
		if (has_eyes(faces_candidates[i]))
		{
			return faces_candidates[i];
		}
	}

	return best_candidate;
}

bool Detector::get_best_face_candidate(vector<Rect> faces_candidates, Mat image, Rect* best_candidate)
{
	for (unsigned int i = 0; i < faces_candidates.size(); i++)
	{
		if (has_eyes(image(faces_candidates[i])))
		{
			best_candidate->x = faces_candidates[i].x;
			best_candidate->y = faces_candidates[i].y;
			best_candidate->width = faces_candidates[i].width;
			best_candidate->height = faces_candidates[i].height;

			return true;
		}
	}

	return false;
}

Mat Detector::get_face(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
{
	vector<Mat> faces = get_faces(source, scale_factor, min_neighbors, min_size_x, min_size_y);
	Mat best_candidate = get_best_face_candidate(faces);
	return best_candidate;
}

bool Detector::get_face_rect(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y, Rect* face)
{
	vector<Rect> faces = detect_faces(source, scale_factor, min_neighbors, min_size_x, min_size_y);
	return get_best_face_candidate(faces, source, face);
}
