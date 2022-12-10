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

	public MyServer(BoardJComponent boardGui) {
		this.boardGui = boardGui;
		this.game = boardGui.getGame();
		this.n_remotePlayers = 0;
	}

	ExecutorService executorService = Executors.newFixedThreadPool(90);

	public void startServing() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT);

		try {

			System.out.println("Starting Server");

			while(true){

				System.out.println("Waiting for request");
				Socket socket = serverSocket.accept();
				System.out.println("Processing request");

				executorService.submit(new DealWithClient(socket));
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

		private RemotePlayer remotePlayer;

		public DealWithClient(Socket socket) throws IOException, InterruptedException {
			doConnections(socket);
		}

		@Override
		public void run() {

			try {

				sendGameState();
				getDirection();

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

			addRemotePlayer();
		}

		private void addRemotePlayer() throws InterruptedException {
			remotePlayer = new RemotePlayer(n_remotePlayers, game);
			game.addPlayerToGame(remotePlayer);
			n_remotePlayers++;
		}

		private void sendGameState() throws IOException {

			//for (int i = 0; i < 90; i++) {
			System.out.println("ENVIEI O Board_Gui");

			out.writeObject(boardGui);

			try {
				Thread.sleep(Game.REFRESH_INTERVAL);

			} catch (InterruptedException e) {}
		}
		//out.println("FIM");
		//}

		private void getDirection() throws IOException, InterruptedException {

			String direction = in.readLine();
			System.out.println("DIRECAO_CLIENT: " + direction);
			remotePlayer.move(Direction.UP);

		}
	}
}
