#include "ClassifiesFacialExpression.h"


ClassifiesFacialExpression::ClassifiesFacialExpression(Mat img, string pathModel, int resolution)
{
	this->img = img;
	this->pathModel = pathModel;
	this->resolution = resolution;
}


ClassifiesFacialExpression::ClassifiesFacialExpression(Mat img)
: pathModel("t4_lbp_model_2_8_8_8.yaml"), resolution(96)
{
}


//Loads the model trained
Ptr<FaceRecognizer> ClassifiesFacialExpression::loadsFile(){
	Ptr<FaceRecognizer> model = createLBPHFaceRecognizer(2,8,8,8);
	model->load(this->pathModel);

	return model;
}

//Returns the classLabel
int ClassifiesFacialExpression::getClassLabelImage(Mat img, Ptr<FaceRecognizer> model)
{

	/*
	 * Chamar função de Armstrong para pre processamento
	 * A imagem deve está na escala de cinza,
	 * Equalizada: equalizeHist(img, img) e
	 * Redimensionada para a resolução passada no contrutor, de acordo com o modelo utilizado
	 */
	equalizeHist(img, img);
	resize(img, img, Size(this->resolution,this->resolution));
	return model->predict(img);;
}

