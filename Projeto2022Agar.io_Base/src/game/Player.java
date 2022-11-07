package game;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {


	protected  Game game;

	int id;

	private byte currentStrength;
	protected byte originalStrength;

	// TODO: get player position from data in game
	// -> A FUNCIONAR
	public Cell getCurrentCell() {
		return this.game.getCell(game.searchPlayerInBoard(this));
	}


	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}

	// Generate a player's initial strength randomly, from 1 to 3 (inclusive)
	//  -> A FUNCIONAR
	public static int generateOriginalStrength() {

		Random random = new Random();

		int originalStrength = random.nextInt(3 - 1 + 1) + 1;

		System.out.println("Random:" + originalStrength);

		return originalStrength;

	}

	

	public abstract boolean isHumanPlayer();

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}


	public int getIdentification() {
		return id;
	}
}
