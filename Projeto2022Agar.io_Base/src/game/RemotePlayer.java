package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

public class RemotePlayer extends Player {

	private static final byte INITIAL_STRENGHT = 5;

	BoardJComponent boardJComponent;

	public RemotePlayer(int id, Game game, byte INITIAL_STRENGHT) {
		super(id, game, INITIAL_STRENGHT);
	}

	@Override
	public void move(Direction direction) throws InterruptedException {

		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();

		direction = boardJComponent.getLastPressedDirection();

		Coordinate newPos = initialPos.translate(direction.getVector());
		Cell newCell;


		if(isValidPosition(newPos)) {

			newCell = game.getCell(newPos);

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

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

}
