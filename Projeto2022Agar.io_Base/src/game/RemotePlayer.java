package game;

import java.util.concurrent.CyclicBarrier;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

public class RemotePlayer extends Player {

	private static final byte INITIAL_STRENGHT = 5;
	private Direction direction;

	public RemotePlayer(int id, Game game, CyclicBarrier barrier) throws InterruptedException {
		super(id, game, INITIAL_STRENGHT, barrier);
	}

	@Override
	public void run () {

		try {
			game.addPlayerToGame(this);
			Thread.sleep(Game.INITIAL_WAITING_TIME);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (true) {

			try {
				
				if(direction!=null) {
					
					this.move(direction);
					direction = null;
				}

				Thread.sleep(Game.REFRESH_INTERVAL);
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
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
