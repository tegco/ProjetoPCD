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

				System.err.println("\nPosition " + this.player.getCurrentCell().getPosition().toString() + " is occupied by Player#" + player.getIdentification() + " and Player#" + newPlayer.getIdentification() + " is waiting!\n");
				
				cellFree.await();
			}

			this.player = newPlayer;

		} finally {
			
			lock.unlock();
		}
	}
	

	public void confrontationOutcome(Player newPlayer) throws InterruptedException {
		
		byte currentPlayerStrength = this.player.getCurrentStrength();
		
		//Se for um player ativo
		if (currentPlayerStrength > 0 && currentPlayerStrength < 10) {
			
			Player winner = Player.confrontationWinner(this.player, newPlayer);
			winner.setAfterConfrontationStrength(this.player, newPlayer);
	
		}
		
		if (currentPlayerStrength == 10 || currentPlayerStrength == 0 ) {
			//bloqueado
			//this.player.
			
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
