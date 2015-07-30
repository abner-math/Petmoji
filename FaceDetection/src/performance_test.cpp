#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "face_detection.h"
#include "histogram_equalization.h"
#include "smoothing.h"

#include <sstream>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

using namespace std;

void detect_faces_in_video(VideoCapture video, int smoothing_type, int kernel_size_width, int kernel_size_height,
							double scale_factor, int min_neighbors, int min_x, int min_y)
{
	Detector detector("conf/haarcascade_frontalface_alt2.xml",
					"conf/haarcascade_righteye_2splits.xml",
					"conf/haarcascade_lefteye_2splits.xml",
					"conf/haarcascade_smile.xml",
					"conf/haarcascade_eye.xml");
	Mat frame;
	vector<Rect> faces;
	long int frames_processed = 0;
	int start = time(0);

	if (!video.isOpened())
	{
		puts("Erro ao acessar o video");
		exit(-1);
	}

	while (video.read(frame))
	{
		frame = equalize_histogram(frame);
		frame = smoothing(frame, smoothing_type, kernel_size_width, kernel_size_height);
		faces = detector.detect_faces(frame, scale_factor, min_neighbors, min_x, min_y);
		for (unsigned int i = 0; i < faces.size(); i++)
		{
			rectangle(frame, faces[i], Scalar(255, 0, 255));
		}
		frames_processed++;
		imshow("video", frame);
		if(waitKey(30) >= 0) break;
	}

	int time_spent = time(0) - start;
	printf("Frames processados: %ld\n", frames_processed);
	printf("Tempo utilizado: %d\n", time_spent);
	printf("%f frames/sec\n", frames_processed*1.0/time_spent);
}

void test_performance(string test_video_file, int smoothing_type, int kernel_size_width, int kernel_size_height,
						double scale_factor, int min_neighbors, int min_x, int min_y)
{
	VideoCapture video(test_video_file);
	detect_faces_in_video(video, smoothing_type, kernel_size_width, kernel_size_height, scale_factor, min_neighbors, min_x, min_y);
}

vector<Point> get_image_points(char* correct_measures_directory, int image_index)
{
	char file_name[100];
	char* line;
	size_t k = 0;

	sprintf(file_name, "%s/bioid_%04d.pts", correct_measures_directory, image_index);
	FILE *f = fopen(file_name, "r");

	getline(&line, &k, f);
	getline(&line, &k, f);
	getline(&line, &k, f);

	vector<Point> points;
	for (int i = 0; i < 20; i++)
	{
		int integer_x = 0, integer_y = 0, decimal_x = 0, decimal_y = 0;
		getline(&line, &k, f);
		sscanf(line, "%d.%d %d.%d", &integer_x, &decimal_x, &integer_y, &decimal_y);
		Point new_point(integer_x, integer_y);
		points.push_back(new_point);
	}

	fclose(f);
	return points;
}

bool evaluate_detection(vector<Point> image_points, Rect detected_face, double max_error)
{
	Point nose = image_points[14];
	Point center(detected_face.x + detected_face.width/2, detected_face.y + detected_face.height/2);

	double error = sqrt(pow(nose.x - center.x, 2) + pow(nose.y - center.y, 2));

	return error <= max_error;
}

void test_performance(char* input_images_directory, char* correct_measures_directory,
								int smoothing_type, int kernel_size_width, int kernel_size_height,
								double scale_factor, int min_neighbors, int min_x, int min_y, double max_error)
{
	int num_images = 1521;
	int detected_correctly = 0;
	int faces_detected = 0;
	Detector detector("conf/haarcascade_frontalface_alt2.xml",
								"conf/haarcascade_righteye_2splits.xml",
								"conf/haarcascade_lefteye_2splits.xml",
								"conf/haarcascade_smile.xml",
								"conf/haarcascade_eye.xml");

	for (int image_index = 0; image_index < num_images; image_index++)
	{
		char image_name[100];
		sprintf(image_name, "%s/BioID_%04d.pgm", input_images_directory, image_index);
		Mat image = imread(image_name);
		image = smoothing(image, smoothing_type, kernel_size_width, kernel_size_height);
		image = equalize_histogram(image);
		Rect detected_face;
		bool any_face = detector.get_face_rect(image, image, scale_factor, min_neighbors, min_x, min_y, &detected_face);

		if (any_face)
		{
			faces_detected++;
			vector<Point> image_points = get_image_points(correct_measures_directory, image_index);

			if (evaluate_detection(image_points, detected_face, max_error))
			{
				detected_correctly++;
			}
		}
	}

	printf("faces detectadas: %d\n", faces_detected);
	printf("recuperação: %f\n", detected_correctly*1.0/num_images);
}
