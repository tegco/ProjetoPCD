package game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CyclicBarrier;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable implements Serializable  {

	public static final int DIMY = 12;
	public static final int DIMX = 12;
	//private static final int NUM_PLAYERS = 90;
	public static final int NUM_PLAYERS = 6;
	public static final int NUM_HUMANS = 2;
	//RESTORE
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	//RESTORE
	//public static final long REFRESH_INTERVAL = 400;

	public static final long REFRESH_INTERVAL = 400;

	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final double MAX_POSSIBLE_STRENGTH = 10;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 8000;

	public static Player[] threads = new Player[NUM_PLAYERS];
	public static Player[] threads_humanas = new Player[NUM_HUMANS];
	public static CyclicBarrier barrier =  new CyclicBarrier(NUM_FINISHED_PLAYERS_TO_END_GAME,new Runnable() {

		//System.out.println("Estou aqui");

		//@Override
		public void run() {

			System.out.println("YOOOOO");
			for (int i = 1; i < Game.NUM_PLAYERS; i++) {
				threads[i].stop=true;
				
			}
			
				threads_humanas[1].stop=true;
				
			
			System.out.println("GAME OVER");

		}

	});

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
		if (newCell.isOcupied()){
			System.out.println("___________OCUPADA________");
		}
		return newCell;
	}

	public boolean isWithinBounds(Coordinate coord) {
		return coord.x >= 0 && coord.y >= 0 && coord.x < Game.DIMX && coord.y < DIMY;
	}

	// Searchs game board and if a cell is occupied, compares the player in that cell with we player we are locking for. 
	// If its a match, return its coordinate
	public Coordinate searchPlayerInBoard (Player player) {

		for (int x = 0; x < Game.DIMX; x++) {
			
			for (int y = 0; y < Game.DIMY; y++) {
				Coordinate coord = new Coordinate (x, y); 

				if ((this.getCell(coord)).isOcupied() && this.getCell(coord).getPlayer().id == player.id) {
					return coord;
				}
			}
		}
		return null;
	}

	public void printBoard(){
		for (int y = 0; y < Game.DIMY; y++) {
			if (y == 0){
				System.out.print("     "+ y + " ");

			}else System.out.print(" "+ y + " ");
		}
		System.out.println("\n---------------------------");
		for (int y = 0; y < Game.DIMY; y++) {
			System.out.print(y + " | ");
			for (int x = 0; x < Game.DIMX; x++) {
					Player p = this.getCell(new Coordinate(x,y)).getPlayer();
					if (p != null) {
						if (p.isHumanPlayer()) {
							System.out.print(" X ");
						} else if (!p.isHumanPlayer()) {
							System.out.print(" B ");
						}
					}
					else System.out.print(" - ");
				}
			System.out.println();
		}
		System.out.println();
	}
}