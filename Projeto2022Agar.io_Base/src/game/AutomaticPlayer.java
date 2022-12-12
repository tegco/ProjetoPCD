package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;

public class AutomaticPlayer extends Player implements Serializable {

	public AutomaticPlayer(int id, Game game, byte strength, CyclicBarrier barrier) throws InterruptedException {
		super(id, game, strength, barrier);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run () {

		try {
			game.addPlayerToGame(this);
			Thread.sleep(Game.INITIAL_WAITING_TIME);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

		while (!stop) {

			try {

				this.move(Direction.randomDirectionGenerator());
				Thread.sleep(Game.REFRESH_INTERVAL * this.originalStrength);
			}
			catch (Exception e) {}

			if (!this.isActive()) {
				break;
			}
		}
	}

	@Override
	public synchronized void move(Direction direction) throws InterruptedException {

		// Get initial position of the player
		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();

		// Generate player's new position
		Coordinate newPos = initialPos.translate(direction.getVector());
		Cell newCell;

		// While we don't get a valid new position, we ask for a new direction that might lead to a valid position
		while (!isValidPosition(newPos)) {
			direction = Direction.randomDirectionGenerator();
			newPos = initialPos.translate(direction.getVector());
		}

		// When we get a valid new position, the player is set in the corresponding cell in the board
		Coordinate finalPos = (initialPos.translate(direction.getVector()));
		newCell = game.getCell(finalPos);

		if (newCell.getPlayer()!= null) {
			this.movementOutcome(newCell.getPlayer());
			game.notifyChange();
		}
		else {
			try {
				initialCell.setPlayer(null);
				newCell.setPlayer(this);
				game.notifyChange();
			} catch (Exception e) {}
		}
		game.notifyChange();
	}
}