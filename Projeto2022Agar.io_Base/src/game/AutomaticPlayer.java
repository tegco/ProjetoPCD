package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class AutomaticPlayer extends Player {

	public AutomaticPlayer(int id, Game game, byte strength) {
		super(id, game, strength);

	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {

		try {

			game.addPlayerToGame(this);

			System.out.println("Player#" + this.getIdentification() + " "  + this.getCurrentCell().getPosition().toString() + " Energy = " + this.getCurrentStrength());

			Thread.sleep(Game.INITIAL_WAITING_TIME);


		} catch (InterruptedException e) {

			e.printStackTrace();
		} 

		int count = 1;

		while (true) {

			for (int i = 0; i != game.NUM_PLAYERS; i++) {
				
				try {
					
					Thread.sleep(game.REFRESH_INTERVAL);
					
				} catch (Exception e) {
					
				}

				if (this.originalStrength == 2 && count% this.originalStrength == 0) {

					System.out.println("Sou o 2");

					try {

						System.out.println("-> Player#" + this.getIdentification() + " " + "Energy = " + this.getCurrentStrength());

						this.move(Direction.randomDirectionGenerator());

						System.out.println("------------------------------");

						//Thread.sleep(game.REFRESH_INTERVAL);
					}
					catch (Exception e) {

					} 
				}

				if (this.originalStrength == 3 && count% this.originalStrength == 0) {

					System.out.println("Sou o 3");

					try {

						System.out.println("-> Player#" + this.getIdentification() + " " + "Energy = " + this.getCurrentStrength());

						this.move(Direction.randomDirectionGenerator());

						System.out.println("------------------------------");

						//Thread.sleep(game.REFRESH_INTERVAL);
					}
					catch (Exception e) {

					}
				}

				if (this.originalStrength == 1) {

					System.out.println("Sou o 1 ");

					try {

						System.out.println("-> Player#" + this.getIdentification() + " " + "Energy = " + this.getCurrentStrength());

						this.move(Direction.randomDirectionGenerator());

						System.out.println("------------------------------");

						
					}
					catch (Exception e) {

					} 

				}


				count++;

				System.out.println("Count: " + count);

			}
		}
	}



	@Override
	public void move(Direction direction) {

		// Get initial position of the player
		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();

		System.out.println("Current Position: " + initialPos.toString() + "\n" + "Direction: " + direction.toString());

		// Generate player's new position
		Coordinate newPos = (initialPos.translate(direction.getVector()));
		Cell newCell;

		// While we don't get a valid new position, we ask for a new direction that might lead to a valid position
		while (!isValidPosition(newPos)) {

			direction = Direction.randomDirectionGenerator();
			System.out.println("****FORA DO BOARD!**** -> " + initialPos.translate(direction.getVector()).toString());
			newPos = (initialPos.translate(direction.getVector()));
		}

		// When we get a valid new position, the player is set in the corresponding cell in the board
		Coordinate finalPos = (initialPos.translate(direction.getVector()));
		newCell = game.getCell(finalPos);

		try {

			initialCell.setPlayer(null);
			newCell.setPlayer(this);

		} catch (Exception e) {}

		game.notifyChange();

		System.out.println("New Position: " + finalPos.toString());

	}
}