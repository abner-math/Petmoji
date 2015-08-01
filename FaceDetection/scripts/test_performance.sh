#!/bin/bash

TEST_APPLICATION="Release/FaceDetection"
MODE="r"
IMAGE_DATABASE_DIRECTORY="$1"
IMAGE_POINTS_DIRECTORY="$2"

echo "Starting test"
echo "............."
echo
echo "Current Configuration"
echo "Image database directory: $1"
echo "Image points directory: $2"

for MIN_FMEASURE in {5..9..1}
do
	echo "Starting min f-measure: $MIN_FMEASURE"
	echo "....................................."
	$TEST_APPLICATION $MODE $IMAGE_DATABASE_DIRECTORY $IMAGE_POINTS_DIRECTORY "0.$MIN_FMEASURE"
	echo "....................................."
	echo
done
