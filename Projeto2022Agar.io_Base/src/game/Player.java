package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

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
	public static int generateOriginalStrength() {

		Random random = new Random();

		int originalStrength = random.nextInt(3 - 1 + 1) + 1;
		//System.out.println("Energy:" + originalStrength);
		return originalStrength;
	}

	public boolean isValidPosition (Coordinate newCoord) {

		if (game.isWithinBounds(newCoord)) {
			return true;
		}

		return false;
	}

	public abstract void move(Direction direction) throws InterruptedException;

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
