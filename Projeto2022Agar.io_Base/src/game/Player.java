package game;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	BoardJComponent boardJComponent;
	int id;
	private byte currentStrength;
	protected byte originalStrength;
	public boolean stop = false;

	static CyclicBarrier barrier;
	//static Player[] threads = new Player[3];

	public Cell getCurrentCell() {
		return this.game.getCell(game.searchPlayerInBoard(this));
	}

	public Player(int id, Game game, byte strength) throws InterruptedException {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength; //ver
	}

	// Generate a player's initial strength randomly, from 1 to 3 (inclusive)
	public static int generateOriginalStrength() {
		Random random = new Random();
		int originalStrength = random.nextInt(3 - 1 + 1) + 1;
		//System.out.println("Energy:" + originalStrength);
		return originalStrength;
	}

	public void movementOutcome(Player otherPlayer) throws InterruptedException {

		if (otherPlayer.isActive()) {

			setAfterConfrontationStrength(this, otherPlayer);
			System.err.println("Player#" + this.getIdentification() + " e Player#" + otherPlayer.getIdentification()  + " confrontation!!!" );
			//this.getCurrentCell().setPlayer(this);
			//otherPlayer.getCurrentCell().setPlayer(otherPlayer);
		}


		if (otherPlayer.isDead()) {

			if (!isHumanPlayer()) {

				System.err.println("BLOCKED - OTHER PLAYER DEAD " + " Player#"+ this.getIdentification() + " Energia: " + this.currentStrength);

				ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

				executorService.schedule(() -> {

					this.interrupt();

					System.out.println("Player#" + this.getIdentification() + " Energia: " + this.currentStrength +  " ESTOU A MEXER");

				}, 5, TimeUnit.SECONDS);

				this.wait();
			}
		}
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
		//System.out.println("Winner is Player#" + winner.getIdentification());

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
			System.out.println("Player#" + winner.getIdentification() + " ATINGOU PONTUAÇÃO MÁXIMA");
			winner.getState().toString();
			//winner.stop = true;

			barrier =  new CyclicBarrier(3); new Runnable() {

				//System.out.println("Estou aqui");

				//@Override
				public void run() {

					System.out.println("Estou aqui");

					try {

						System.out.println("Thread " + currentThread().getName() + "is calling await");

						barrier.await();

						System.out.println("Thread " + currentThread().getName() + "started running again");

					} catch (InterruptedException | BrokenBarrierException e) {

						e.printStackTrace();
					}

				}

			};
		}

	}

	//System.out.println("Player#" + p1.getIdentification() + ": " + p1.currentStrength + "; " + "Player#" + p2.getIdentification() + ": " + p2.currentStrength);

	public boolean isValidPosition (Coordinate newCoord) {
		if (game.isWithinBounds(newCoord)) {
			return true;
		}
		return false;
	}

	public abstract void move(Direction direction) throws InterruptedException;

	public void move(KeyEvent keyPressed) throws InterruptedException {

	}

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
