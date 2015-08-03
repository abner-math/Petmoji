CPP = g++ -g -fPIC  -Wl,-rpath="/home/daniela/Downloads/opencv-2.4.9_install/lib"

DIRINCLUDE = -I /home/daniela/Downloads/opencv-2.4.9_install/include/opencv/ -I /home/daniela/Downloads/opencv-2.4.9_install/include/

DIRLIB = -L/home/daniela/Downloads/opencv-2.4.9_install/lib/

LIB = -l opencv_objdetect -l opencv_ml -l opencv_imgproc -l opencv_legacy -l opencv_highgui -l opencv_core -l opencv_contrib -l opencv_flann -l opencv_features2d

default : all
all : Run

ClassifiesFacialExpression.o : ClassifiesFacialExpression.cpp ClassifiesFacialExpression.h
	$(CPP) -c ClassifiesFacialExpression.cpp -o ClassifiesFacialExpression.o $(DIRINCLUDE)
	

	
Run : Run.cpp ClassifiesFacialExpression.o
	$(CPP)  ClassifiesFacialExpression.o  Run.cpp -o Run $(DIRLIB) $(LIB) $(DIRINCLUDE)



clean:
	rm *.o; rm Run
