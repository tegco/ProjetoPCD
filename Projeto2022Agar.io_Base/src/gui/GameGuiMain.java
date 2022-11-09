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

		// Demo players, should be deleted
		try {
			//Thread.sleep(3000);
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//while(frame.isActive()) {

		for (int i = 1; i < 3; i++) {

			player = new AutomaticPlayer (i, game, (byte) Player.generateOriginalStrength());
			game.playersList.add(player);

			//Player player = new AutomaticPlayer (i, game, (byte) (i+1));

			game.addPlayerToGame(player);

		}
	}

	//Testes
	
	
	/*Player player = new PhoneyHumanPlayer (i, game, (byte) Player.generateOriginalStrength());
	game.searchPlayerInBoard(player);
	player.getCurrentCell().setPlayer(player);
	game.notifyChange();
	player.start();
	System.out.println("Cell:" + player.getCurrentCell().getPosition()); */

	//}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException {


		GameGuiMain game = new GameGuiMain();
		game.init();
		
		
		for (int i = 1; i < 6; i++) {
			
			for(Player p : Game.playersList) {
				
				System.out.println("-> Jogador#" + p.getIdentification() + " " + "Energy=" + p.getCurrentStrength() + "\n");
				p.move(Direction.randomDirectionGenerator());
				
				System.out.println("-------------------------------------------");
			}
			
			
			
		}
		
		

	}

}


