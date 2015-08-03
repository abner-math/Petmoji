package com.example.petmoji;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    public static final String TAG = "com.example.petmoji";
    public static final int S_FRAME_RATE = 10;
    
	private ImageView mBackground;
	private ImageView mImage;
	private Animation mAnimation;
	private Spinner mSpinner;
	private CameraBridgeViewBase mOpenCvCameraView;
	private Mat mGray;
	private Mat mRgba;
	private long mCascadeClassifier;
	private Rect mFaceTrack;
	private int mCount;
	
	private native long loadCascadeClassifier(String eyeFile, String frontalFace,
		String leftEyeFile, String rightEyeFile, String smileFile);
	 
	private native int[] getFace(long inputImageAddr, long cascadeClassifierAddr);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
        
		this.mBackground = (ImageView) findViewById(R.id.background);
		this.mImage = (ImageView) findViewById(R.id.character);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.mImage.setX(size.x / 2 - this.mImage.getWidth() / 2);
		this.mImage.setY(size.y / 2 - this.mImage.getHeight() / 2);
		this.mAnimation = new Animation(this, mImage, PetType.PET0);
		this.mAnimation.start();
		this.mBackground.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mAnimation.moveToCoordinate(new Coordinate(event.getX(), event.getY()));
				return true;
			}
		});
		this.mSpinner = (Spinner) findViewById(R.id.setMood);
		List<String> moods = new ArrayList<String>();
		moods.add("Neutral");
		moods.add("Happy");
		moods.add("Sad");
		moods.add("Upset");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moods);
		this.mSpinner.setAdapter(adapter);
		this.mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					mAnimation.setMood(Mood.NEUTRAL);
					break;
				case 1:
					mAnimation.setMood(Mood.HAPPY);
					break;
				case 2:
					mAnimation.setMood(Mood.SAD);
					break;
				case 3:
					mAnimation.setMood(Mood.UPSET);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera);
        mOpenCvCameraView.setCvCameraViewListener(MainActivity.this);
		final Button btnActivateCamera = (Button) findViewById(R.id.activatePreview);
		btnActivateCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOpenCvCameraView.getVisibility() == SurfaceView.GONE) {
					mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
					btnActivateCamera.setText("Desactivate camera");
				} else {
					mOpenCvCameraView.setVisibility(SurfaceView.GONE);
					btnActivateCamera.setText("Activate camera");
				}
			}
		});
	}

	@Override
	public void onDestroy()	{
		super.onDestroy();
		if (mOpenCvCameraView != null) {
			mOpenCvCameraView.disableView();
		}
	}

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
			mOpenCvCameraView.disableView();
		}
    }

	@Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, MainActivity.this, mLoaderCallback);
    }

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                	System.loadLibrary("Petmoji");
                    Log.i(TAG, "OpenCV loaded successfully");
                    loadCascade();
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    private void loadCascade()
    {
    	File cascadeEye = readCascadeXML("haarcascade_eye.xml");
        File cascadeFrontalFace = readCascadeXML("haarcascade_frontalface_alt2.xml");
        File cascadeLeftEye = readCascadeXML("haarcascade_lefteye_2splits.xml");
        File cascadeRightEye = readCascadeXML("haarcascade_righteye_2splits.xml");
        File cascadeSmile = readCascadeXML("haarcascade_smile.xml");
		mCascadeClassifier = loadCascadeClassifier(cascadeEye.getAbsolutePath(),
        		cascadeFrontalFace.getAbsolutePath(), cascadeLeftEye.getAbsolutePath(),
        		cascadeRightEye.getAbsolutePath(), cascadeSmile.getAbsolutePath());
    }
    
    private File readCascadeXML(String file) {
    	File cascadeFile = null;
    	final InputStream is;
    	FileOutputStream os;
    	try {
    	    is = getResources().getAssets().open(file);
    	    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
    	    cascadeFile = new File(cascadeDir, "face_frontal.xml");
    	    
    	    os = new FileOutputStream(cascadeFile);
    	            
    	    byte[] buffer = new byte[4096];
    	    int bytesRead;
    	    while ((bytesRead = is.read(buffer)) != -1) {
    	        os.write(buffer, 0, bytesRead);
    	    }

    	    is.close();
    	    os.close();
    	} catch (IOException e) {
    	    Log.i(TAG, "face cascade not found");
    	}
    	return cascadeFile;
    }
    
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        if (this.mCount % S_FRAME_RATE == 0) {
        	
            int[] faceRect = getFace(mGray.getNativeObjAddr(), mCascadeClassifier);
            mFaceTrack = new Rect(faceRect[0], faceRect[1], faceRect[2], faceRect[3]);
            mAnimation.moveToCoordinate(new Coordinate(mFaceTrack.x + mFaceTrack.width / 2, mFaceTrack.y + mFaceTrack.height / 2));
        }
        ++this.mCount;
        Core.rectangle(mRgba, mFaceTrack.br(), mFaceTrack.tl(), new Scalar(255, 0, 0));
        return mRgba;
    }

}
