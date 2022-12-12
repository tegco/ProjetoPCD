package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import environment.Direction;
import gui.BoardJComponent;

public class MyServer {

	public static final int PORT = 8080;
	private Game game;
	private BoardJComponent boardGui;
	private RemotePlayer remotePlayer;
	public Player[] threads = Game.threads_humanas;

	public MyServer(BoardJComponent boardGui) {

		this.boardGui = boardGui;
		this.game = boardGui.getGame();
	}

	public void startServing() throws IOException, InterruptedException {

		ServerSocket serverSocket = new ServerSocket(PORT);

		try {
			System.out.println("Starting Server");

			while(true){

				System.out.println("Waiting for request");
				Socket socket = serverSocket.accept();
				System.out.println("Processing request");

				new DealWithClient(socket).start();
			}			
		} finally {

			serverSocket.close();
			System.out.println("Server shutdown");
		}
	}

	public class DealWithClient extends Thread {

		private BufferedReader in;
		private ObjectOutputStream out;

		public DealWithClient(Socket socket) throws IOException, InterruptedException {

			doConnections(socket);
			//game.printBoard();
			addRemotePlayer();
		}

		@Override
		public void run() {

			new Thread() {
				@Override
				public void run() {

					try {
						getDirection();
						
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}

				}}.start();

				try {
					sendGameState();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
		}

		void doConnections(Socket socket) throws IOException, InterruptedException {
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new ObjectOutputStream(socket.getOutputStream());
		}

		private void addRemotePlayer() throws InterruptedException {
			
			remotePlayer = new RemotePlayer(0, game, game.barrier);
			threads[1] = remotePlayer;
			remotePlayer.start();
		}

		private void sendGameState() throws IOException, InterruptedException {
			
			while(true) {

				Thread.sleep(Game.REFRESH_INTERVAL);

				out.writeObject(game);
				//game.printBoard();
				out.reset();
			}
		}

		private void getDirection() throws IOException, InterruptedException {
			
			Direction direction;
			
			while (!remotePlayer.stop) {
				
				String dir = in.readLine();
				
				if(dir!=null) {
					
					direction = Direction.valueOf(dir);

					//game.printBoard();

					if(remotePlayer.getCurrentCell() != null) {
						remotePlayer.setDirection(direction);
					}
				}
			}
		}
	}
}
