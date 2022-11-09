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
	public void move(Direction direction) {

		// Get initial position of the player
		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();
		
		System.out.println("Posição atual: " + "[" + initialPos.x + "][" + initialPos.y + "]");
		
		// Generate player's new position
		Coordinate newPos = (initialPos.translate(direction.getVector()));
		
		Cell newCell;
		
		// While we dont get a valid new position, we ask for a new direction that might lead to a valid position
		while (!isValidPosition(newPos)) {
			
			direction = Direction.randomDirectionGenerator();
			
			System.out.println("FORA DO BOARD!" + "[" + initialPos.translate(direction.getVector()).x + "][" + initialPos.translate(direction.getVector()).y + "]");
			
			newPos = (initialPos.translate(direction.getVector()));
			
		}
		
		// When we get a valid new position, the player is set in the corresponding cell in the board
		
		Coordinate finalPos = (initialPos.translate(direction.getVector()));
		
		newCell = game.getCell(finalPos);
		
		System.out.println("New Cell:" + newCell.getPosition().toString());
		
		newCell.setPlayer(this);
		Cell oldCell = game.getCell(initialPos);
		oldCell.setPlayer(null);
		
		game.notifyChange();
		
		System.out.println("SetPlayer da finalPos: " + this.getCurrentCell().getPosition().toString());
		
		
		
		Coordinate currentPos = this.getCurrentCell().getPosition(); 
		
		initialPos = currentPos;
		
		game.notifyChange();

		System.out.println("Posição nova: " + "[" + finalPos.x + "][" + finalPos.y + "]");
			
		
		}

	

	}


