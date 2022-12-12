package game;

import java.util.concurrent.CyclicBarrier;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

public class RemotePlayer extends Player {

	private static final byte INITIAL_STRENGHT = 5;
	Direction d;
	BoardJComponent boardJComponent;

	public RemotePlayer(int id, Game game, CyclicBarrier barrier) throws InterruptedException {
		super(id, game, INITIAL_STRENGHT,barrier);
	}

	@Override
	public void run () {

		try {
			game.addPlayerToGame(this);
			//System.out.println("Player#" + this.getIdentification() + " "  + this.getCurrentCell().getPosition().toString() + " Energy = " + this.getCurrentStrength());
			Thread.sleep(Game.INITIAL_WAITING_TIME);
			
			System.out.println("\nNEW PLAYER ON POSITION (" + this.getCurrentCell().getPosition().y + ", " + this.getCurrentCell().getPosition().x+")");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (!stop) {

			try {
				//System.out.println("-> Player#" + this.getIdentification() + " " + "Energy = " + this.getCurrentStrength());
				if(d!=null) {
					this.move(d);
					d=null;
				}
				
				Thread.sleep(Game.REFRESH_INTERVAL);
				//System.out.println("------------------------------");
			}
			catch (Exception e) {}

			if (!this.isActive()) {
				break;
			}
		}
		
	}
	@Override
	public void move(Direction direction) throws InterruptedException {

		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();

		Coordinate newPos = initialPos.translate(direction.getVector());

		Cell newCell;

		if(isValidPosition(newPos)) {

			newCell = game.getCell(newPos);

			if (newCell.getPlayer()!= null) {
				this.movementOutcome(newCell.getPlayer());
			}
			else {
				try {
					initialCell.setPlayer(null);
					newCell.setPlayer(this);

				} catch (Exception e) {}
			}
			game.notifyChange();
		}
		System.out.println("Initial CELL ---> (" + initialPos.y+", "+initialPos.x + ")  ==> (" + newPos.y+", "+newPos.x+")");
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}
	
	public void setDirection(Direction d) {
		this.d = d;
	}

}
