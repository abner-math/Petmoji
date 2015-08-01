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

class ClassificationMetrics
{
	public:
	double precision;
	double recall;
	double f_measure;

	ClassificationMetrics(double precision, double recall, double f_measure)
	{
		this->precision = precision;
		this->recall = recall;
		this->f_measure = f_measure;
	}
};

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

/*
 * Esta função lê os arquivos de pontos das imagens e coloca essas informações em um vetor de pontos
 */
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

/*
 * Esta função deriva a localização real da face na imagem a partir dos pontos pré-rotulados
 */
Rect get_correct_faces_from_points(vector<Point> image_points)
{
	/*
		4 = outer end of right eye brow
		5 = inner end of right eye brow
		6 = inner end of left eye brow
		7 = outer end of left eye brow
		8 = right temple
		13 = left temple
		18 = centre point on outer edge of lower lip
		19 = tip of chin
	 */
	Point left_temple = image_points[13];
	Point right_temple = image_points[8];
	Point tip_of_chin = image_points[19];
	Point inner_end_right_eye_brow = image_points[5];
	Point inner_end_left_eye_brow = image_points[6];
	Point center_point_lower_lip = image_points[18];
	int y_eyebrow = min(inner_end_right_eye_brow.y, inner_end_left_eye_brow.y);

	int correct_face_x = right_temple.x;
	int correct_face_y = y_eyebrow;
	int correct_face_width = left_temple.x - right_temple.x;
	int correct_face_height = center_point_lower_lip.y - y_eyebrow;
	Rect correct_face(correct_face_x, correct_face_y, correct_face_width, correct_face_height);
	return correct_face;
}

/*
 * Esta função avalia a detecção de face comparando a face detectada com a face real e levando em conta
 * um threshold. Retorna true se a detecção foi considerada correta, false caso contrário.
 */
bool evaluate_detection(vector<Point> image_points, Rect detected_face, double min_fmeasure)
{
	Rect correct_face = get_correct_faces_from_points(image_points);
	int x_intersection(max(detected_face.x, correct_face.x));
	int y_intersection(max(detected_face.y, correct_face.y));
	int width_intersection = min(detected_face.x + detected_face.width, correct_face.x + correct_face.width) - max(detected_face.x, correct_face.x);
	int height_intersection = min(detected_face.y + detected_face.height, correct_face.y + correct_face.height) - max(detected_face.y, correct_face.y);

	Rect correct_detected_intersection2(x_intersection, y_intersection, width_intersection, height_intersection);
	Rect correct_detected_intersection = correct_face & detected_face;

	int correct_face_area = correct_face.area();
	int detected_face_area = detected_face.area();
	int intersection_area = correct_detected_intersection.area();

	double precision = ((double)intersection_area)/detected_face_area;
	double recall = ((double)intersection_area)/correct_face_area;
	double fmeasure = 2*precision*recall/(precision + recall);
	return fmeasure >= min_fmeasure;
}

void test_performance(char* input_images_directory, char* correct_measures_directory,
								int smoothing_type, int kernel_size_width, int kernel_size_height,
								double scale_factor, int min_neighbors, int min_x, int min_y, double min_fmeasure)
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

			if (evaluate_detection(image_points, detected_face, min_fmeasure))
			{
				detected_correctly++;
			}
		}
	}

	printf("Numero total de imagens: %d\n", num_images);
	printf("faces detectadas: %d\n", faces_detected);
	printf("faces detectadas corretamente: %d\n", detected_correctly);
	printf("faces detectadas corretamente / faces detectadas: %f\n", ((double)detected_correctly)/faces_detected);
	printf("recuperação (detectadas corretamente/numero de imagens): %f\n", ((double)detected_correctly)/num_images);
}
