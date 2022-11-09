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

		Cell initialCell = this.getCurrentCell();
		Coordinate initialPos = initialCell.getPosition();
		
		System.out.println("Posição atual: " + "[" + initialPos.x + "][" + initialPos.y + "]");

		Coordinate newPos = (initialPos.translate(direction.getVector()));
		Cell newCell;
		
		while (!isValidPosition(newPos)) {
			
			direction = Direction.randomDirectionGenerator();
			newPos = (initialPos.translate(direction.getVector()));
			
			System.out.println("FORA DO BOARD!" + "[" + newPos.x + "][" + newPos.y + "]");

		}
		
		Coordinate finalPos = (initialPos.translate(direction.getVector()));
		
		newCell = game.getCell(finalPos);

		newCell.setPlayer(this);
		
		Coordinate currentPos = this.getCurrentCell().getPosition(); 
		
		initialPos = currentPos;
		
		game.notifyChange();

		System.out.println("Posição nova: " + "[" + finalPos.x + "][" + finalPos.y + "]");
			
		
		}

	

	}


