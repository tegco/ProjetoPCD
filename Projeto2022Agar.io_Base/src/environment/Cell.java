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

	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	// -> NÃO PARECE ESTAR A FUNCIONAR
	public void setPlayer(Player player) throws InterruptedException {
		
		lock.lock();
		
		try {
			
			while (this.isOcupied()) {
				cellFree.await();
			}
			
			this.player = player;
			playerAtCell.signalAll();
			
			
		} finally {
			lock.unlock();
		}
	}
	

	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game=g;
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
