package game;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread implements Serializable {

	protected Game game;
	int id;
	private byte currentStrength;
	protected byte originalStrength;
	public boolean stop = false;

	static CyclicBarrier barrier;

	public Cell getCurrentCell() {
		return this.game.getCell(game.searchPlayerInBoard(this));
	}

	public Player(int id, Game game, byte strength, CyclicBarrier barrier) throws InterruptedException {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
		this.barrier = barrier;
	}

	// Generate a player's initial strength randomly, from 1 to 3 (inclusive)
	public static int generateOriginalStrength() {

		Random random = new Random();
		int originalStrength = random.nextInt(Game.MAX_INITIAL_STRENGTH - 1 + 1) + 1;
		return originalStrength;
	}

	public void movementOutcome(Player otherPlayer) throws InterruptedException {

		if (otherPlayer.isActive()) {
			setAfterConfrontationStrength(this, otherPlayer);
		}

		if (otherPlayer.isDead()) {

			if (!isHumanPlayer()) {
				resolveBlockedMovement();
			}
		}
	}

	public void resolveBlockedMovement() throws InterruptedException {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		executorService.schedule(() -> {
			this.interrupt();
		}, 2, TimeUnit.SECONDS);

		this.wait();

	}

	public static Player confrontationWinner(Player p1, Player p2) {

		if (p1.currentStrength > p2.currentStrength) {
			return p1;
		}

		if (p1.currentStrength == p2.currentStrength) {

			Random random = new Random();
			int randomPlayer = random.nextInt(2);

			if (randomPlayer == 0) {
				return p1;
			}
			return p2;
		}
		return p2;
	}

	public static void setAfterConfrontationStrength (Player p1, Player p2) {

		Player winner = confrontationWinner(p1, p2);

		int s = p1.currentStrength += p2.currentStrength;

		if (s > 10) {
			s = 10;
		}

		if (p1 == winner) {
			p1.currentStrength = (byte) s;
			p2.currentStrength = 0;
		}
		else {
			p2.currentStrength = (byte) s;
			p1.currentStrength = 0;
		}

		if (winner.hasMaxStrenght()) {
			winner.getState().toString();

			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
			}
		}
	}


	public boolean isValidPosition (Coordinate newCoord) {
		if (game.isWithinBounds(newCoord)) {
			return true;
		}
		return false;
	}

	public abstract void move(Direction direction) throws InterruptedException;

	public abstract boolean isHumanPlayer();

	public boolean hasMaxStrenght () {
		return this.currentStrength == Game.MAX_POSSIBLE_STRENGTH;
	}

	public boolean isActive () {
		return (this.currentStrength > 0 && this.currentStrength < 10);
	}

	public boolean isDead() {
		return this.currentStrength == 0;
	}

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
