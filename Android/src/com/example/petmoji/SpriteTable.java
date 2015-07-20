package com.example.petmoji;

import java.util.HashMap;
import java.util.Map;

public class SpriteTable {

	private static Map<Momentum, Integer> sResources = new HashMap<Momentum, Integer>();
	static {
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.UPWARD, 0), R.drawable.pet0_back0);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.UPWARD, 1), R.drawable.pet0_back1);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.UPWARD, 0), R.drawable.pet0_back0);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.UPWARD, 1), R.drawable.pet0_back1);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.UPWARD, 0), R.drawable.pet0_back0);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.UPWARD, 1), R.drawable.pet0_back1);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.UPWARD, 0), R.drawable.pet0_back0);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.UPWARD, 1), R.drawable.pet0_back1);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.LEFT, 0), R.drawable.pet0_happy_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.LEFT, 1), R.drawable.pet0_happy_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.LEFT, 2), R.drawable.pet0_happy_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.LEFT, 3), R.drawable.pet0_happy_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.RIGHT, 0), R.drawable.pet0_happy_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.RIGHT, 1), R.drawable.pet0_happy_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.RIGHT, 2), R.drawable.pet0_happy_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.RIGHT, 3), R.drawable.pet0_happy_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.DOWNWARD, 0), R.drawable.pet0_happy_front0);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.DOWNWARD, 1), R.drawable.pet0_happy_front1);
		sResources.put(new Momentum(PetType.PET0, Mood.HAPPY, Direction.DOWNWARD, 2), R.drawable.pet0_happy_front2);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.LEFT, 0), R.drawable.pet0_neutral_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.LEFT, 1), R.drawable.pet0_neutral_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.LEFT, 2), R.drawable.pet0_neutral_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.LEFT, 3), R.drawable.pet0_neutral_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.RIGHT, 0), R.drawable.pet0_neutral_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.RIGHT, 1), R.drawable.pet0_neutral_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.RIGHT, 2), R.drawable.pet0_neutral_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.RIGHT, 3), R.drawable.pet0_neutral_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.DOWNWARD, 0), R.drawable.pet0_neutral_front0);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.DOWNWARD, 1), R.drawable.pet0_neutral_front1);
		sResources.put(new Momentum(PetType.PET0, Mood.NEUTRAL, Direction.DOWNWARD, 2), R.drawable.pet0_neutral_front2);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.LEFT, 0), R.drawable.pet0_sad_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.LEFT, 1), R.drawable.pet0_sad_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.LEFT, 2), R.drawable.pet0_sad_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.LEFT, 3), R.drawable.pet0_sad_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.RIGHT, 0), R.drawable.pet0_sad_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.RIGHT, 1), R.drawable.pet0_sad_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.RIGHT, 2), R.drawable.pet0_sad_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.RIGHT, 3), R.drawable.pet0_sad_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.DOWNWARD, 0), R.drawable.pet0_sad_front0);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.DOWNWARD, 1), R.drawable.pet0_sad_front1);
		sResources.put(new Momentum(PetType.PET0, Mood.SAD, Direction.DOWNWARD, 2), R.drawable.pet0_sad_front2);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.LEFT, 0), R.drawable.pet0_upset_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.LEFT, 1), R.drawable.pet0_upset_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.LEFT, 2), R.drawable.pet0_upset_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.LEFT, 3), R.drawable.pet0_upset_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.RIGHT, 0), R.drawable.pet0_upset_left0);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.RIGHT, 1), R.drawable.pet0_upset_left1);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.RIGHT, 2), R.drawable.pet0_upset_left2);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.RIGHT, 3), R.drawable.pet0_upset_left3);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.DOWNWARD, 0), R.drawable.pet0_upset_front0);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.DOWNWARD, 1), R.drawable.pet0_upset_front1);
		sResources.put(new Momentum(PetType.PET0, Mood.UPSET, Direction.DOWNWARD, 2), R.drawable.pet0_upset_front2);
	}
	
	public static int getFrame(Momentum momentum) {
		if (sResources.containsKey(momentum)) {
			return sResources.get(momentum);
		}
		return -1;
	}
	
}
