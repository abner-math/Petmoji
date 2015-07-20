package com.example.petmoji;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends Activity {

	private ImageView mBackground;
	private ImageView mImage;
	private Animation mAnimation;
	private Spinner mSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}
	
}
