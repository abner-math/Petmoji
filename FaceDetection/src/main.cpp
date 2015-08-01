#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "face_detection.h"
#include "histogram_equalization.h"
#include "smoothing.h"
#include "performance_test.h"

#include <iostream>
#include <stdio.h>

using namespace std;
using namespace cv;

#define SMOOTHING_KERNEL_WIDTH 11
#define SMOOTHING_KERNEL_HEIGHT 11
#define SMOOTHING_TYPE NORMALIZED
#define FACE_DETECTION_SCALE_FACTOR 1.1
#define FACE_DETECTION_MIN_NEIGHBORS 2
#define FACE_DETECTION_MIN_SIZE_X 30
#define FACE_DETECTION_MIN_SIZE_Y 30

void check_arguments(int argc, char** argv)
{
	if (argc < 2)
	{
		puts("Numero incorreto de argumentos. Necessario informar um modo de funcionamento.");
		exit(-1);
	}

	string mode = argv[1];

	if (!mode.compare("p"))
	{
		if (argc != 3)
		{
			puts("Numero incorreto de argumentos para o modo performance. Necessario informar o caminho de um arquivo de video.");
			exit(-1);
		}
	}
	else if (!mode.compare("n"))
	{
		if (argc != 3)
		{
			puts("Numero incorreto de argumentos para o modo normal. Necessario informar o caminho de uma imagem");
			exit(-1);
		}
	}
	else if (!mode.compare("r"))
	{
		if (argc != 5)
		{
			puts("Numero incorreto de argumentos para o modo resultados.");
			puts("Uso: <nome do programa> r <caminho da base de imagens> <caminho da base de pontos de imagens> <threshold>");
			exit(-1);
		}
	}
	else
	{
		puts("Modo invalido");
	}
}

vector<Mat> pre_process_faces(vector<Mat> faces, int smoothing_type, int kernel_width, int kernel_height)
{
	for (unsigned int i = 0; i < faces.size(); i++)
	{
		Mat equalized_image = equalize_histogram(faces[i]);
		faces[i] = smoothing(faces[i], smoothing_type, kernel_width, kernel_height);
	}

	return faces;
}

Mat pre_process_face(Mat face, int smoothing_type, int kernel_width, int kernel_height)
{
	Mat equalized_image = equalize_histogram(face);
	return smoothing(face, smoothing_type, kernel_width, kernel_height);
}

void export_faces(vector<Mat> faces, char* original_face_image_filename)
{
	if (faces.size() > 1)
	{
		printf("WARNING: Achou %ld faces!\n", faces.size());
	}

	imwrite(original_face_image_filename, faces[0]);

	for (unsigned int i = 1; i < faces.size(); i++) {
		char extra_face[15];
		sprintf(extra_face, "_%d.jpg", i);
		imwrite(extra_face, faces[i]);
	}
}

void export_face(Mat face, char* original_face_image_filename)
{
	imwrite(original_face_image_filename, face);
}

int main(int argc, char** argv)
{
	check_arguments(argc, argv);

	string mode = argv[1];

	if (!mode.compare("p"))
	{
		test_performance(argv[2], SMOOTHING_TYPE, SMOOTHING_KERNEL_WIDTH, SMOOTHING_KERNEL_HEIGHT, FACE_DETECTION_SCALE_FACTOR, FACE_DETECTION_MIN_NEIGHBORS, FACE_DETECTION_MIN_SIZE_X, FACE_DETECTION_MIN_SIZE_Y);
	}
	else if (!mode.compare("n"))
	{
		Mat image = imread(argv[2], CV_LOAD_IMAGE_COLOR);
		Detector detector("conf/haarcascade_frontalface_alt2.xml",
					"conf/haarcascade_righteye_2splits.xml",
					"conf/haarcascade_lefteye_2splits.xml",
					"conf/haarcascade_smile.xml",
					"conf/haarcascade_eye.xml");

		Mat face = detector.get_face(image, image, FACE_DETECTION_SCALE_FACTOR, FACE_DETECTION_MIN_NEIGHBORS, FACE_DETECTION_MIN_SIZE_X, FACE_DETECTION_MIN_SIZE_Y);
		Mat processed_face = pre_process_face(face, NORMALIZED, SMOOTHING_KERNEL_WIDTH, SMOOTHING_KERNEL_HEIGHT);
		export_face(processed_face, argv[2]);
	}
	else if (!mode.compare("r"))
	{
		test_performance(argv[2], argv[3], SMOOTHING_TYPE, SMOOTHING_KERNEL_WIDTH, SMOOTHING_KERNEL_HEIGHT, FACE_DETECTION_SCALE_FACTOR, FACE_DETECTION_MIN_NEIGHBORS,
									FACE_DETECTION_MIN_SIZE_X, FACE_DETECTION_MIN_SIZE_Y, atof(argv[4]));
	}
	return 0;
}
