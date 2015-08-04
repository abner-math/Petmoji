package com.example.petmoji;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class Animation implements Runnable {

	private static final float S_CELL_SIZE = 10F; // pixels
	private static final long S_MOVEMENT_SPEED = 100L; // miliseconds
	
	private Sprite mSprite;
	private TextView mLabel;
	private List<Coordinate> mMovementQueue;
	private Thread mThread;
	
	public Animation(Activity context, ImageView image, TextView label, PetType type) {
		setSprite(new Sprite(context, image, type));
		this.mMovementQueue = new ArrayList<Coordinate>();
		this.mThread = new Thread(this);
		this.mLabel = label;
	}
	
	public Sprite getSprite() { 
		return this.mSprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.mSprite = sprite;
	}
	
	public void start() {
		this.mThread.start(); 
	}
	
	public void stop() {
		this.mThread.stop();
	}
	
	public void setPetType(PetType type) {
		this.mSprite.setPetType(type);
	}
	
	public void setMood(Mood mood) {
		this.mSprite.setMood(mood);
	} 
	
	public void moveToCoordinate(Coordinate coord) {
		try {
			this.mMovementQueue.add(coord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean move(Coordinate coord) {
		final Coordinate current = new Coordinate(this.mSprite.getX(), this.mSprite.getY());
		boolean canMove = true;
		if (Math.abs(current.getX() - coord.getX()) > S_CELL_SIZE) {
			if (current.getX() < coord.getX()) {
				current.setX(current.getX() + S_CELL_SIZE);
				mSprite.setDirection(Direction.RIGHT);
			} else {
				current.setX(current.getX() - S_CELL_SIZE);
				mSprite.setDirection(Direction.LEFT);
			}
		} else {
			current.setX(coord.getX());
			if (Math.abs(current.getY() - coord.getY()) > S_CELL_SIZE) {
				if (current.getY() < coord.getY()) {
					current.setY(current.getY() + S_CELL_SIZE);
					mSprite.setDirection(Direction.DOWNWARD);
				} else {
					current.setY(current.getY() - S_CELL_SIZE);
					mSprite.setDirection(Direction.UPWARD);
				}
			} else {
				current.setY(coord.getY());
				mSprite.resetAnimation();
				canMove = false;
			}
		}
		mSprite.getContext().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mLabel.setX(current.getX());
				mLabel.setY(current.getY() - 100);
			}
		});
		mSprite.setPosition(current);
		return canMove;
	}

	@Override
	public void run() {
		while (true) {
			if (!mMovementQueue.isEmpty()) {
				if (!move(mMovementQueue.get(0))) {
					mMovementQueue.remove(0);
				}
			}
			try {
				Thread.sleep(S_MOVEMENT_SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
