#ifndef PERFORMANCE_TEST_H_
#define PERFORMANCE_TEST_H_

using namespace std;

void test_performance(string test_video_file, int smoothing_type, int kernel_size_width, int kernel_size_height,
						double scale_factor, int min_neighbors, int min_x, int min_y);
void test_performance(char* input_images_directory, char* correct_measures_directory,
		int smoothing_type, int kernel_size_width, int kernel_size_height, double scale_factor,
		int min_neighbors, int min_x, int min_y, double max_error);

#endif /* PERFORMANCE_TEST_H_ */
