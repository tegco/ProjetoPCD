package gui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import game.AutomaticPlayer;
import game.Game;
import game.MyServer;
import game.Player;
//import game.SearcherThread;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {

	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
	public Player player;
	private final boolean alternativeKeys;

	public GameGuiMain() {
		super();
		this.alternativeKeys = true;
		game = new Game();
		game.addObserver(this);

		buildGui();
	}

	private void buildGui() {
		
		boardGui = new BoardJComponent(game, alternativeKeys);
		frame.add(boardGui);

		//VOLTAR A POR COMO ORIGINALMENTE ESTAVA
		//frame.setSize(800,800);
		frame.setSize(400, 400);
		frame.setLocation(300, 100);
		//frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException  {

		System.out.println("INITIAL PLACEMENT: \n" + "------------------");

		frame.setVisible(true);
		
		for (int i = 0; i < Game.NUM_PLAYERS; i++) {

			player = new AutomaticPlayer (i, game, (byte) Player.generateOriginalStrength());
			player.start();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		GameGuiMain game = new GameGuiMain();
		game.init();
		
		MyServer server = new MyServer(game.boardGui);
		server.startServing();
		
	}
}
