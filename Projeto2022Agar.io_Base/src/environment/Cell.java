package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell {

	private Coordinate position;
	private Game game;
	private Player player=null;

	private Lock lock = new ReentrantLock();
	private Condition playerAtCell = lock.newCondition();
	private Condition cellFree = lock.newCondition();


	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation

	/*public void setPlayer(Player newPlayer) {
		this.player = newPlayer;
	}*/

	// -> NÃO PARECE ESTAR A FUNCIONAR
	public void setPlayer(Player newPlayer) throws InterruptedException {

		lock.lock();

		try {
			
			if (newPlayer == null) {
				this.player = null;
				cellFree.signalAll();
			}

			while (this.isOcupied()) {

				System.err.println("Position " + this.player.getCurrentCell().getPosition() + "] is occupied by player #" + player.getIdentification() + " and player#" + newPlayer.getIdentification() + " is waiting!");

				cellFree.await();

			}
			
			this.player = newPlayer;
			playerAtCell.signalAll();

		} finally {
			lock.unlock();
		}
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player != null;
	}


	public Player getPlayer() {
		return player;
	}

}
