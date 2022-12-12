package environment;

import java.io.Serializable;
import java.util.Random;

public enum Direction implements Serializable {
	
	UP(0,-1),DOWN(0,1),LEFT(-1,0),RIGHT(1,0);
	private Coordinate vector;
	Direction(int x, int y) {
		vector=new Coordinate(x, y);
	}
	public Coordinate getVector() {
		return vector;
	}
	
	//Generates a random direction
	public static Direction randomDirectionGenerator () {
		
		Direction [] values = Direction.values();
		int index = new Random().nextInt(values.length);
		
		return values[index];
		
	}
}
