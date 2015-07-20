package com.example.petmoji;

import android.app.Activity;
import android.widget.ImageView;

public class Sprite {

	private ImageView mImage;
	private Activity mContext;
	private Momentum mMomentum;
	private int mRepetitions;
	
	public Sprite(Activity context, ImageView image, PetType type) {
		setImage(image);
		setContext(context);
		setMomentum(new Momentum(type));
		resetAnimation();
	}
	
	public Activity getContext() {
		return this.mContext;
	}
	
	public void setContext(Activity context) {
		this.mContext = context;
	}
	
	public ImageView getImage() { 
		return this.mImage;
	}
	
	public void setImage(ImageView image) {
		this.mImage = image;
	}
	
	public Momentum getMomentum() {
		return this.mMomentum;
	}
	
	public void setMomentum(Momentum momentum) {
		this.mMomentum = momentum;
	}
	
	public float getX() {
		return this.mImage.getX();
	}
	
	private void setX(float x) {
		this.mImage.setX(x);
	}
	
	public float getY() {
		return this.mImage.getY();
	}
	
	private void setY(float y) {
		this.mImage.setY(y);
	}

	public Mood getMood() {
		return this.mMomentum.getMood();
	}
	
	public void setMood(Mood mood) {
		this.mMomentum.setMood(mood);
	}
	
	public Direction getDirection() {
		return this.mMomentum.getDirection();
	}
	
	public void setDirection(Direction direction) {
		this.mMomentum.setDirection(direction);
	}
	
	public PetType getPetType() {
		return this.mMomentum.getPetType();
	}
	
	public void setPetType(PetType type) {
		this.mMomentum.setPetType(type);
	}
	
	public void resetAnimation() {
		this.mMomentum.reset();
	}
	
	public void setPosition(final float x, final float y) {
		this.mContext.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setX(x);
				setY(y);
				animate();
			}
		});
	}
	
	public void setPosition(Coordinate position) {
		setPosition(position.getX(), position.getY());
	}
	
	private void animate() {
		if (this.mMomentum.getDirection() == Direction.RIGHT) {
			this.mImage.setScaleX(-1);
		} else {
			this.mImage.setScaleX(1);
		}
		// blink eye
		if ((this.mMomentum.getDirection() == Direction.LEFT || this.mMomentum.getDirection() == Direction.RIGHT)
				&& this.mMomentum.getFrameIndex() == 2 && this.mRepetitions % 2 != 0) {
			this.mMomentum.setFrameIndex(-1);
		}
		this.mMomentum.setFrameIndex(this.mMomentum.getFrameIndex() + 1);
		int nextFrame = SpriteTable.getFrame(this.mMomentum);
		if (nextFrame == -1) {
			this.mMomentum.setFrameIndex(0);
			this.mRepetitions++;
			nextFrame = SpriteTable.getFrame(this.mMomentum);
		}
		this.mImage.setImageResource(nextFrame);
	}
	
}
