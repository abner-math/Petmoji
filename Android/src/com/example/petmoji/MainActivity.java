package com.example.petmoji;

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
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import android.app.Activity;
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
    
	private ImageView mBackground;
	private ImageView mImage;
	private Animation mAnimation;
	private Spinner mSpinner;
	private CameraBridgeViewBase mOpenCvCameraView;
	private Mat mGray;
	private Mat mRgba;
	
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
				mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
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
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        return mRgba;
    }

}
