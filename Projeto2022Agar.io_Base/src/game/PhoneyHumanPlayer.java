package game;

import java.io.Serializable;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player{

	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}
	 
	@Override
	public void move(Direction direction) {
		
	}

}
