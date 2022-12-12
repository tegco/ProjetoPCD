package gui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import game.AutomaticPlayer;
import game.Game;
import game.MyServer;
import game.Player;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {

	protected JFrame frame = new JFrame("pcd.io");
	protected BoardJComponent boardGui;
	protected Game game;
	public Player player;
	private final boolean alternativeKeys;
	
	public Player[] threads = Game.threads;

	public GameGuiMain() {
		super();
		this.alternativeKeys = true;
		game = new Game();
		game.addObserver(this);

		buildGui();
	}

	private void buildGui() {

		frame.setSize(600, 600);
		frame.setLocation(150, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		boardGui = new BoardJComponent(game, alternativeKeys);
		frame.add(boardGui);
	}
	
	public void init() throws InterruptedException  {

		frame.setVisible(true);

		for (int i = 1; i < Game.NUM_PLAYERS; i++) {
			player = new AutomaticPlayer (i, game, (byte) Player.generateOriginalStrength(),Game.barrier);
			threads[i] = player;
			player.start();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		GameGuiMain gameGui = new GameGuiMain();

		MyServer server = new MyServer(gameGui.boardGui);
		gameGui.init();
		server.startServing();
	}
}
