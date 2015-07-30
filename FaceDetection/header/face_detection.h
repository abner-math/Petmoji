#ifndef HEADER_FACE_DETECTION_H_
#define HEADER_FACE_DETECTION_H_

#include <iostream>
#include <stdio.h>

using namespace std;
using namespace cv;

class Detector
{
	private:
	CascadeClassifier frontal_face_classifier;
	CascadeClassifier right_eye_classifier;
	CascadeClassifier left_eye_classifier;
	CascadeClassifier smile_classifier;
	CascadeClassifier eyes_classifier;

	bool has_eyes(Mat image);
	Mat get_best_face_candidate(vector<Mat> faces_candidates);
	bool get_best_face_candidate(vector<Rect> faces_candidates, Mat image, Rect* best_candidate);

	public:
	Mat get_face(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y);
	bool get_face_rect(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y, Rect* face);
	vector<Mat> get_faces(Mat source, double scale_factor, int min_neighbors, int min_size_x, int min_size_y);
	vector<Rect> detect_faces(Mat image, double scale_factor, int min_neighbors, int min_size_x, int min_size_y);
	vector<Rect> detect_right_eye(Mat image);
	vector<Rect> detect_left_eye(Mat image);
	vector<Rect> detect_smile(Mat image);
	vector<Rect> detect_eyes(Mat image);
	Detector(string frontal_face_cascade_path,
			 string right_eye_cascade_path,
			 string left_eye_cascade_path,
			 string smile_cascade_path,
			 string eyes_cascade_path);
};

#endif /* HEADER_FACE_DETECTION_H_ */
