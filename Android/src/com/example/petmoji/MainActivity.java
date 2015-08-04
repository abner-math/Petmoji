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
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    public static final String TAG = "com.example.petmoji";
    public static final int S_SADDNESS = 0;
    public static final int S_HAPPINESS = 1;
    public static final int S_ANGER = 2;
    public static final int S_FRAME_RATE = 5;
	public static final float S_RELATIVE_FACE_SIZE = 0.2f;
	
	private ImageView mBackground;
	private ImageView mImage;
	private Animation mAnimation;
	private View mUserView;
	private Spinner mSetMoodSpinner;
	private TextView mDeveloperMood;
	private TextView mMoodLabel;
	 
	private CameraBridgeViewBase mOpenCvCameraView;
	private CascadeClassifier mCascadeClassifier;
	private Mat mGray;
	private Mat mRgba;
	private Rect mFaceTrack;
	private Spinner mCameraNeighbors;
	private Spinner mCameraScaleFactor;
	private int mFaceDetectorNeighbors; 
	private double mFaceDetectorScaleFactor;
	private int mAbsoluteFaceSize;
	private int mFrameCount;
	private long mFaceRecognizer;
	private int mCurrentMood;
	
	private native int predict(long inputImageAddr, long faceRecognizerModelAddr);
	
	private native long createFaceRecognizer();
	
	private native void loadFaceRecognizer(long faceRecognizerAddr, String modelFile);

	private native long loadCascadeClassifier(String eyeFile, String frontalFace,
		String leftEyeFile, String rightEyeFile, String smileFile);
	 
	private native int[] getFace(long inputImageAddr, long cascadeClassifierAddr);
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
         
		this.mMoodLabel = (TextView) findViewById(R.id.moodLabel);
		this.mDeveloperMood = (TextView) findViewById(R.id.developerMood);
		this.mCameraNeighbors = (Spinner) findViewById(R.id.cameraNeighbors);
		this.mCameraScaleFactor = (Spinner) findViewById(R.id.cameraScaleFactor);
		this.mUserView = findViewById(R.id.userView);
		this.mBackground = (ImageView) findViewById(R.id.background);
		this.mImage = (ImageView) findViewById(R.id.character);
		this.mSetMoodSpinner = (Spinner) findViewById(R.id.setMood);
		this.mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera);
        this.mOpenCvCameraView.setCvCameraViewListener(MainActivity.this);
        
        initializeAnimation();
		setCameraProperties();
		switchDisplayMode();
		setMood();
	}

	private void initializeAnimation() {
		Display display = getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		this.mImage.setX(size.x / 2 - this.mImage.getWidth() / 2);
		this.mImage.setY(size.y / 2 - this.mImage.getHeight() / 2);
		this.mAnimation = new Animation(this, mImage, mMoodLabel, PetType.PET0);
		this.mAnimation.start();
		this.mBackground.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mAnimation.moveToCoordinate(new Coordinate(event.getX(), event.getY()));
				return true;
			}
		});
	}
	
	private void setMood() {
		List<String> moods = new ArrayList<String>();
		moods.add("Neutral");
		moods.add("Happy");
		moods.add("Sad");
		moods.add("Upset");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moods);
		this.mSetMoodSpinner.setAdapter(adapter);
		this.mSetMoodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	}
	
	private void switchDisplayMode() {
		final Button btnActivateCamera = (Button) findViewById(R.id.developerMode);
		btnActivateCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUserView.getVisibility() == View.GONE) {
					mUserView.setVisibility(View.VISIBLE);
					btnActivateCamera.setText("Developer mode");
				} else {
					mUserView.setVisibility(View.GONE);
					btnActivateCamera.setText("User mode");
				}
			}
		});
	} 
	
	private void setCameraProperties() {
		this.mFaceDetectorNeighbors = 4;
		this.mFaceDetectorScaleFactor = 1.5;
		List<String> neighbors = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			neighbors.add(String.valueOf(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, neighbors);
		mCameraNeighbors.setAdapter(adapter);
		mCameraNeighbors.setOnItemSelectedListener(new OnItemSelectedListener() {
 
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				mFaceDetectorNeighbors = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		}); 
		List<String> scales = new ArrayList<String>();
		for (float i = 1.1f; i <= 2.0f; i += 0.1f) {
			scales.add(String.valueOf(i));
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scales);
		mCameraScaleFactor.setAdapter(adapter);
		mCameraScaleFactor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				mFaceDetectorScaleFactor = 1.1f + position * 0.1f;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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

    private File loadAssetFile(String file) {
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

    private void loadCascadeClassifier() {
    	File cascadeFile = loadAssetFile("lbpcascade_frontalface.xml");
		mCascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
    }
        
    private void loadFaceRecognizer() {
    	final File modelFile = loadAssetFile("cv_lbp_model_1_4_4_4.yaml");
    	mFaceRecognizer = createFaceRecognizer();
    	loadFaceRecognizer(mFaceRecognizer, modelFile.getAbsolutePath());
    	System.out.println("Face recognizer loaded");
    }
    
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                	System.loadLibrary("Petmoji");
                    Log.i(TAG, "OpenCV loaded successfully");
                    loadCascadeClassifier();
                    loadFaceRecognizer();
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

    private void updateAnimation() {
    	mAnimation.moveToCoordinate(new Coordinate(mFaceTrack.x + mFaceTrack.width / 2, 
    			mFaceTrack.y + mFaceTrack.height / 2));
    	switch (mCurrentMood) {
    	case 0:
    		runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mDeveloperMood.setText("Mood: SAD");
					mMoodLabel.setText("Estou me sentindo triste...");
				}
			});
    		mAnimation.setMood(Mood.SAD);
    		break;
    	case 1:
    		runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mMoodLabel.setText("Estou alegre!");
					mDeveloperMood.setText("Mood: HAPPY");
				}
			});
    		mAnimation.setMood(Mood.HAPPY);
    		break;
    	case 2: 
    		runOnUiThread(new Runnable() {
				
				@Override 
				public void run() {
					mMoodLabel.setText("Estou chateado...");
					mDeveloperMood.setText("Mood: UPSET");
				}
			});
    		mAnimation.setMood(Mood.UPSET);
    	}
    }
    
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        if (this.mFrameCount % S_FRAME_RATE == 0) {
        	if (mAbsoluteFaceSize == 0) {
                int height = mGray.rows();
                if (Math.round(height * S_RELATIVE_FACE_SIZE) > 0) {
                    mAbsoluteFaceSize = Math.round(height * S_RELATIVE_FACE_SIZE);
                }
            }
        	MatOfRect faces = new MatOfRect();
        	if (mFaceDetectorScaleFactor <= 1) {
        		mFaceDetectorScaleFactor = 1.5;
        	}
        	mCascadeClassifier.detectMultiScale(mGray, faces, this.mFaceDetectorScaleFactor, this.mFaceDetectorNeighbors, 2,
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        	Rect[] rects = faces.toArray();
        	if (rects.length > 0) {
        		mFaceTrack = faces.toArray()[0];
        		mCurrentMood = predict(mGray.getNativeObjAddr(), mFaceRecognizer);
        		System.out.println("Mood: " + mCurrentMood);
        		updateAnimation();
        	} else {
        		mFaceTrack = null;
        	}
        }
        if (mFaceTrack != null) {
        	Core.rectangle(mRgba, mFaceTrack.tl(), mFaceTrack.br(), new Scalar(255, 0, 0), 3);
        }
        ++this.mFrameCount; 
        return mRgba;
    }

}
