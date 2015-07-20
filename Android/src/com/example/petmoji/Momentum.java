package com.example.petmoji;

public class Momentum {

	private PetType mType;
	private Mood mMood;
	private Direction mDirection;
	private int mFrameIndex;
	
	public Momentum(PetType type, Mood mood, Direction direction, int frameIndex) {
		setPetType(type);
		setMood(mood);
		setDirection(direction);
		setFrameIndex(frameIndex);
	}
	
	public Momentum(PetType type) {
		this(type, Mood.NEUTRAL, Direction.LEFT, 1);
	}
	
	public PetType getPetType() {
		return this.mType;
	}
	
	public void setPetType(PetType type) {
		this.mType = type;
	}
	
	public Mood getMood() {
		return this.mMood; 
	}
	
	public void setMood(Mood mood) {
		this.mMood = mood;
	}
	
	public Direction getDirection() {
		return this.mDirection;
	}
	
	public void setDirection(Direction direction) {
		this.mDirection = direction;
	}
	
	public int getFrameIndex() {
		return this.mFrameIndex;
	}
	
	public void setFrameIndex(int frameIndex) {
		this.mFrameIndex = frameIndex;
	}

	public void reset() {
		if (this.getDirection() != Direction.LEFT && this.getDirection() != Direction.RIGHT) {
			setDirection(Direction.LEFT);
		}
		setFrameIndex(0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mDirection == null) ? 0 : mDirection.hashCode());
		result = prime * result + mFrameIndex;
		result = prime * result + ((mMood == null) ? 0 : mMood.hashCode());
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Momentum other = (Momentum) obj;
		if (mDirection != other.mDirection)
			return false;
		if (mFrameIndex != other.mFrameIndex)
			return false;
		if (mMood != other.mMood)
			return false;
		if (mType != other.mType)
			return false;
		return true;
	}
	
}
