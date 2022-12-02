package game;

import java.util.concurrent.CountDownLatch;

public class SearcherThread extends Thread {
	
	private Player player;
	private CountDownLatch cdl;
	private State state;
	public SearcherThread(Player player, CountDownLatch cdl) {
		this.player = player;
		this.cdl=cdl;
	}
	
	
	public State getState() {
		return state;
	}
	@Override
	public void run() {
//		if(player.getState() == Thread.State.WAITING) {
			
//			try {
//				player.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			this.interrupt();
		//state =	player.getState();
		cdl.countDown();
			
		
		System.out.println("Player" + player.getIdentification() + " with state: " + state);
			
		
	}

}
