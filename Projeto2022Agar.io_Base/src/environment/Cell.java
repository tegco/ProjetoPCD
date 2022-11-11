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

	public void setPlayer(Player newPlayer) {
		this.player = newPlayer;
	}

	// -> NÃO PARECE ESTAR A FUNCIONAR
	/*public void setPlayer(Player player) throws InterruptedException {

			lock.lock();

			try {

				while (this.isOcupied()) {
					cellFree.await();
				}

				this.player = newPlayer;
				playerAtCell.signalAll();

				System.err.println("Position [" + p_cell.getPosition().x + "][" + p_cell.getPosition().y + "] is occupied by player #" + p_cell.getPlayer().id + "and player#" + p.id + "is waiting!");


			} finally {
				lock.unlock();
			}

			//game.notifyChange();
		}*/

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
