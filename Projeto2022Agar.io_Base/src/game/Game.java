package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	//private static final int NUM_PLAYERS = 90;
	public static final int NUM_PLAYERS = 3;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;


	protected Cell[][] board;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}

	/** 
	 * @param player 
	 */

	public void addPlayerToGame(Player player) throws InterruptedException {
		Cell initialPos=getRandomCell();
		initialPos.setPlayer(player);

		// To update GUI
		notifyChange();

	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}

	public boolean isWithinBounds(Coordinate coord) {
		return coord.x >= 0 && coord.y >= 0 && coord.x < this.DIMX && coord.y < this.DIMY;
	}

	// Searchs game board and if a cell is occupied, compares the player in that cell with we player we are locking for. 
	// If its a match, return its coordinate
	public Coordinate searchPlayerInBoard (Player player) {

		for (int x = 0; x < this.DIMX; x++) {

			for (int y = 0; y < this.DIMY; y++) {

				Coordinate coord = new Coordinate (x, y); 

				if ((this.getCell(coord)).isOcupied() && this.getCell(coord).getPlayer().id == player.id) {
					return coord;
				}
			}
		}
		return null;
	}
}