package gui;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import game.Game;
import game.PhoneyHumanPlayer;
import game.Player;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {
	
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
	private Player player;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();

	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);


		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException  {
		
		frame.setVisible(true);

		// Demo players, should be deleted
		try {
			//Thread.sleep(3000);
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//for (int i = 0; i != player.)
		
		Player player = new PhoneyHumanPlayer (1, game, (byte) Player.generateOriginalStrength());
		Player player1 = new PhoneyHumanPlayer (2, game, (byte) Player.generateOriginalStrength());
		
		//game.addPlayerToGame(new PhoneyHumanPlayer(1, game, (byte) Player.generateOriginalStrength()));
		//game.addPlayerToGame(new PhoneyHumanPlayer(2, game, (byte)2));
		//game.addPlayerToGame(new PhoneyHumanPlayer(3, game, (byte)1));
		
		game.addPlayerToGame(player);
		game.searchPlayerInBoard(player);
		player.getCurrentCell().setPlayer(player1);
		game.notifyChange();
		
		
		//System.out.println("Cell:" + player.getCurrentCell().getPosition());
	}
	

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException {
		GameGuiMain game = new GameGuiMain();
		game.init();
		
		
	}

}
