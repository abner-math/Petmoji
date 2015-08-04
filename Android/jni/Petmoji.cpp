#include <jni.h>

#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include <iostream>
#include <stdio.h>

#define SMOOTHING_KERNEL_WIDTH 11
#define SMOOTHING_KERNEL_HEIGHT 11
#define SMOOTHING_TYPE NORMALIZED
#define FACE_DETECTION_SCALE_FACTOR 2.0
#define FACE_DETECTION_MIN_NEIGHBORS 0
#define FACE_DETECTION_MIN_SIZE_X 20
#define FACE_DETECTION_MIN_SIZE_Y 20
#define IMAGE_SCALE 5

using namespace std;
using namespace cv;

class Detector
{
public:
	Rect get_face(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
	{
		vector<Mat> faces = get_faces(source, scale_factor, min_neighbors, min_size_x, min_size_y);
		vector<Rect> facesRect = detect_faces(source, scale_factor, min_neighbors, min_size_x, min_size_y);
		return facesRect[get_best_face_candidate(faces)];
	}

	bool get_face_rect(Mat source, Mat last, double scale_factor, int min_neighbors, int min_size_x, int min_size_y, Rect* face)
	{
		vector<Rect> faces = detect_faces(source, scale_factor, min_neighbors, min_size_x, min_size_y);
		return get_best_face_candidate(faces, source, face);
	}

	vector<Mat> get_faces(Mat input_image, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
	{
		vector<Rect> faces_rects = detect_faces(input_image, scale_factor, min_neighbors, min_size_x, min_size_y);
		vector<Mat> faces;

		for (unsigned int i = 0; i < faces_rects.size(); i++)
		{
			faces.push_back(input_image(faces_rects[i]));
		}

		return faces;
	}

	vector<Rect> detect_faces(Mat image, double scale_factor, int min_neighbors, int min_size_x, int min_size_y)
	{
		vector<Rect> faces;
		frontal_face_classifier.detectMultiScale(image, faces, scale_factor, min_neighbors, 0|CV_HAAR_SCALE_IMAGE, Size(min_size_x, min_size_y));
		return faces;
	}

	vector<Rect> detect_right_eye(Mat image)
	{
		vector<Rect> right_eyes;
		right_eye_classifier.detectMultiScale(image, right_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
		return right_eyes;
	}

	vector<Rect> detect_left_eye(Mat image)
	{
		vector<Rect> left_eyes;
		left_eye_classifier.detectMultiScale(image, left_eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
		return left_eyes;
	}

	vector<Rect> detect_smile(Mat image)
	{
		vector<Rect> smiles;
		smile_classifier.detectMultiScale(image, smiles, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(100, 100));
		return smiles;
	}

	vector<Rect> detect_eyes(Mat image)
	{
		vector<Rect> eyes;
		eyes_classifier.detectMultiScale(image, eyes, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));
		return eyes;
	}

	Detector(string frontal_face_cascade_path,
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

private:
	CascadeClassifier frontal_face_classifier;
	CascadeClassifier right_eye_classifier;
	CascadeClassifier left_eye_classifier;
	CascadeClassifier smile_classifier;
	CascadeClassifier eyes_classifier;

	bool has_eyes(Mat image)
	{
		return detect_eyes(image).size() > 0;
	}

	int get_best_face_candidate(vector<Mat> faces_candidates)
	{
		assert(faces_candidates.size() > 0);
		/*
		Mat best_candidate = faces_candidates[0];

		for (unsigned int i = 0; i < faces_candidates.size(); i++)
		{
			if (has_eyes(faces_candidates[i]))
			{
				return i;
			}
		}
		*/
		return 0;
	}

	bool get_best_face_candidate(vector<Rect> faces_candidates, Mat image, Rect* best_candidate)
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

};

extern "C" {
	JNIEXPORT jlong JNICALL Java_com_example_petmoji_MainActivity_loadCascadeClassifier(JNIEnv * env, jobject thiz,
			jstring eyeFile, jstring frontalFaceFile, jstring leftEyeFile,
			jstring rightEyeFile, jstring smileFile)
	{
		string stdEyeFile(env->GetStringUTFChars(eyeFile, NULL));
		string stdFrontalFaceFile(env->GetStringUTFChars(frontalFaceFile, NULL));
		string stdLeftEyeFile(env->GetStringUTFChars(leftEyeFile, NULL));
		string stdRightEyeFile(env->GetStringUTFChars(rightEyeFile, NULL));
		string stdSmileFile(env->GetStringUTFChars(smileFile, NULL));
		return (jlong)(new Detector(stdFrontalFaceFile,
				stdRightEyeFile, stdLeftEyeFile, stdSmileFile, stdEyeFile));
	}

	JNIEXPORT jintArray JNICALL Java_com_example_petmoji_MainActivity_getFace(JNIEnv * env, jobject thiz,
			jlong inputImageAddr, jlong cascadeClassifierAddr)
	{
		Mat* inputImage = (Mat*) inputImageAddr;
		Detector* detector = (Detector*) cascadeClassifierAddr;

		Mat resizedImage;
		resize(*inputImage, resizedImage, inputImage->size(), 1 / IMAGE_SCALE, 1 / IMAGE_SCALE, INTER_LINEAR);
		Rect r = detector->get_face(resizedImage, resizedImage, FACE_DETECTION_SCALE_FACTOR, FACE_DETECTION_MIN_NEIGHBORS, FACE_DETECTION_MIN_SIZE_X, FACE_DETECTION_MIN_SIZE_Y);
		jintArray result = env->NewIntArray(4);
		jint fill[4];
		fill[0] = r.tl().x;
		fill[1] = r.tl().y;
		fill[2] = r.br().x;
		fill[3] = r.br().y;
		env->SetIntArrayRegion(result, 0, 4, fill);
		return result;
	}
}
