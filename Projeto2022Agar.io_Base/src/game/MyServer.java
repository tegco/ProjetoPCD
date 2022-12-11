package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Direction;
import gui.BoardJComponent;

public class MyServer {

	public static final int PORT = 8080;
	private Game game;
	private transient BoardJComponent boardGui;
	private int n_remotePlayers;
	private RemotePlayer remotePlayer;

	public Player[] threads = Game.threads_humanas;
	
	public MyServer(BoardJComponent boardGui) {
		this.boardGui = boardGui;
		this.game = boardGui.getGame();
		this.n_remotePlayers = 0;
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
	public int getN_remotePlayers(){
		return this.n_remotePlayers;
	}

	public class DealWithClient extends Thread {

		private BufferedReader in;
		private ObjectOutputStream out;

		public DealWithClient(Socket socket) throws IOException, InterruptedException {
			doConnections(socket);
			System.out.println("\nServer ESTADO Inicial");
			game.printBoard();
			addRemotePlayer();
		}

		@Override
		public void run() {

			try {
				sendGameState();
			} catch (IOException e) {

				System.out.println("Error accepting connection");
				e.printStackTrace();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		void doConnections(Socket socket) throws IOException, InterruptedException {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("SERVER --> connection established");
		}

		private void addRemotePlayer() throws InterruptedException {
			remotePlayer = new RemotePlayer(n_remotePlayers, game,game.barrier);
			threads[1]= remotePlayer;
			remotePlayer.start();
			n_remotePlayers++;
		}

		private void sendGameState() throws IOException, InterruptedException {
			out.reset();
			out.writeObject(boardGui);
			System.out.println("\nServer ENVIOU este game");
			game.printBoard();
			getDirection();
		}

		private void getDirection() throws IOException, InterruptedException {
			Direction direction;
			while (!remotePlayer.stop) {
				direction = Direction.valueOf(in.readLine());

				if (direction!= null) {
					System.out.println("DIRECAO_CLIENT: " + direction);
					System.out.println("\nServer RECEBEU este game");
					game.printBoard();
					
					if(remotePlayer.getCurrentCell() != null) {
						remotePlayer.move(direction);
					}

					boardGui.clearLastPressedDirection();
					break;
				}
			}
			sendGameState();
		}
	}
}
