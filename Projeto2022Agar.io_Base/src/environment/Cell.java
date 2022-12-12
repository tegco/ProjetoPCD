package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell implements Serializable {

	private Coordinate position;
	private Game game;
	private Player player=null;

	private Lock lock = new ReentrantLock();
	private Condition cellFree = lock.newCondition();


	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public void setPlayer(Player newPlayer) throws InterruptedException {

		lock.lock();
		try {

			if (newPlayer == null) {
				this.player = null;
				cellFree.signalAll();
			}

			while (this.isOcupied()) {
				//VOLTAR A POR
				System.err.println("\nPosition " + this.player.getCurrentCell().getPosition().toString() + " is occupied by Player#" + player.getIdentification() + " and Player#" + newPlayer.getIdentification() + " is waiting!\n");
				
				if(this.player.isDead()) {
					
					newPlayer.resolveBlockedMovement();
					Cell initialPos= game.getRandomCell();
					initialPos.setPlayer(newPlayer);
				}
				
				cellFree.await();
			}

			this.player = newPlayer;


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
