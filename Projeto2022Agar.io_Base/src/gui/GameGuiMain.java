package gui;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import game.AutomaticPlayer;
import game.Game;
import game.PhoneyHumanPlayer;
import game.Player;

import javax.swing.JFrame;

import environment.Direction;

public class GameGuiMain implements Observer {

	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
	public Player player;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();

	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);

		//VOLTAR A POR COMO ORIGINALMENTE ESTAVA

		//frame.setSize(800,800);
		frame.setSize(650, 650);
		frame.setLocation(0, 0);
		//frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException  {

		frame.setVisible(true);

		//VOLTAR A POR, É UM REQUISITO OBRIGATÓRIO

		/*try {

			//Thread.sleep(Game.INITIAL_WAITING_TIME);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}*/

		System.out.println("Initial Placement:\n");

		for (int i = 0; i < game.NUM_PLAYERS; i++) {

			player = new AutomaticPlayer (i, game, (byte) Player.generateOriginalStrength());
			game.addPlayerToGame(player);

			System.out.println("Jogador#" + player.getIdentification() + " "  + player.getCurrentCell().getPosition().toString() + " Energy=" + player.getCurrentStrength());

			player.start();
		}

		System.out.println("-------------------------------");
	}

	//Testes antigos de funções iniciais


	/*Player player = new PhoneyHumanPlayer (i, game, (byte) Player.generateOriginalStrength());
	game.searchPlayerInBoard(player);
	player.getCurrentCell().setPlayer(player);
	game.notifyChange();
	player.start();
	System.out.println("Cell:" + player.getCurrentCell().getPosition()); */

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException {

		GameGuiMain game = new GameGuiMain();
		game.init();

	}
}
