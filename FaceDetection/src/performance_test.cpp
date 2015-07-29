#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "face_detection.h"
#include "histogram_equalization.h"
#include "smoothing.h"

#include <iostream>
#include <stdio.h>
#include <stdlib.h>

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
