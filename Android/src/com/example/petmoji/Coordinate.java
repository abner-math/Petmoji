package com.example.petmoji;

public class Coordinate {

	private float mX;
	private float mY;
	
	public Coordinate(float x, float y) {
		setX(x);
		setY(y);
	}
	
	public float getX() {
		return this.mX;
	}
	
	public void setX(float x) {
		this.mX = x;
	}
	
	public float getY() {
		return this.mY;
	}
	
	public void setY(float y) {
		this.mY = y;
	}
	
	public float getDistance(Coordinate other) {
		return Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj.getClass().equals(this.getClass()))) {
			return false;
		}
		Coordinate other = (Coordinate) obj;
		return this.getX() == other.getX() && this.getY() == other.getY();
	}
	
	@Override
	public String toString() { 
		return "[" + this.mX + ", " + this.mY + "]";
	}
	
}
