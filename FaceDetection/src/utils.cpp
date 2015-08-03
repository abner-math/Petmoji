# include "utils.h"
# include <stdio.h>

using namespace std;

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

void visualize_images(char* input_images_directory, char* correct_measures_directory)
{
	int num_images = 1521;

	for (int image_index = 0; image_index < num_images; image_index++)
	{
		char image_name[100];
		sprintf(image_name, "%s/BioID_%04d.pgm", input_images_directory, image_index);
		Mat image = imread(image_name);
		vector<Point> points = get_image_points(correct_measures_directory, image_index);

		for (unsigned int point_index = 0; point_index < points.size(); point_index++)
		{
			line(image, points[point_index], points[point_index], Scalar(255, 0, 0), 10);
		}
		imshow("imagem", image);
		waitKey(0);
	}
}

ClassificationMetrics::ClassificationMetrics(double precision, double recall, double f_measure)
{
	this->precision = precision;
	this->recall = recall;
	this->f_measure = f_measure;
}
