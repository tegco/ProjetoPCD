package gui;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import game.AutomaticPlayer;
import game.Game;
import game.PhoneyHumanPlayer;
import game.Player;
import game.SearcherThread;

import javax.swing.JFrame;

import environment.Cell;
import environment.Coordinate;
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
		frame.setSize(400, 400);
		frame.setLocation(300, 100);
		//frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException  {

		System.out.println("INITIAL PLACEMENT: \n" + "------------------");

		frame.setVisible(true);
		
//		SearcherThread[] threads=new SearcherThread[Game.NUM_PLAYERS];
//		CountDownLatch cdl = new CountDownLatch(Game.NUM_PLAYERS);
		
		for (int i = 0; i < Game.NUM_PLAYERS; i++) {

			player = new AutomaticPlayer (i, game, (byte) Player.generateOriginalStrength());
			player.start();
			//threads[i]=new SearcherThread(player, cdl);
			//threads[i].start();
		}


		//		for(int i=0; i!=game.NUM_PLAYERS;i++){
		//			
		//		}
		//while(true) {
//		try {
//			cdl.await();
//		} catch (Exception e) {
//		}
//		
//		
//		for(SearcherThread t:threads) {
//			if(t.getState() == Thread.State.TIMED_WAITING) {
//				System.out.println("OLA ENTREI AQUI");
//				//player.sleep(10000);
//				player.interrupt();
//			}
		//}
		//}

		//Testes de funções auxiliares

		//winnerPlayer &&  setAfterConfrontationStrength

		//			Player p1 = new AutomaticPlayer (1, game, (byte) Player.generateOriginalStrength());
		//			Player p2 = new AutomaticPlayer (2, game, (byte) Player.generateOriginalStrength());


		//	System.out.println("Player#" + p1.getIdentification() + " " + "Energy " + p1.getCurrentStrength());
		//	System.out.println("Player#" + p2.getIdentification() + " " + "Energy " + p2.getCurrentStrength());
		//	System.out.println("Winner: Player#"+ Player.confrontationWinner(p1, p2).getIdentification());
		//	
		//	
		//	Player.setAfterConfrontationStrength(p1, p2);
		//	
		//	System.out.println("Player#" + p1.getIdentification() + " " + "Energy " + p1.getCurrentStrength());
		//	System.out.println("Player#" + p2.getIdentification() + " " + "Energy " + p2.getCurrentStrength());

		//---------------------------------------------------------------------------------------------------//

		//game.searchPlayerInBoard(player);
		//player.getCurrentCell().setPlayer(player);


		//game.notifyChange();

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
