package environment;

import java.util.Random;

public enum Direction {
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
		//System.out.println("Direção: " + values[index]);
		
		return values[index];
		
	}
}
