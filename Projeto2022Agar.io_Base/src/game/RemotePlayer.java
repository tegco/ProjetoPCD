package game;

import environment.Direction;

public class RemotePlayer extends Player {

	public RemotePlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	@Override
	public void move(Direction direction) throws InterruptedException {
		
	
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

}
