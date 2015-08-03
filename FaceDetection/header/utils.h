#ifndef UTILS_H_
#define UTILS_H_

#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

using namespace cv;
using namespace std;

/*
 * Esta função lê os arquivos de pontos das imagens e coloca essas informações em um vetor de pontos
 */
vector<Point> get_image_points(char* correct_measures_directory, int image_index);
void visualize_images(char* input_images_directory, char* correct_measures_directory);

class ClassificationMetrics
{
	public:
	double precision;
	double recall;
	double f_measure;

	ClassificationMetrics(double precision, double recall, double f_measure);
};

#endif /* UTILS_H_ */
