package game;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gui.BoardJComponent;

public class MyServer {

	public static final int PORT = 8080;
	private Game game;
	private BoardJComponent boardGui;

	public MyServer(BoardJComponent boardGui) {
		this.boardGui = boardGui;
		this.game = boardGui.getGame();
	}
	
	ExecutorService executorService = Executors.newFixedThreadPool(90);

	public void startServing() throws IOException {

		//System.out.println("OLA");
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

	public class DealWithClient extends Thread {

		private BufferedReader in;
		private ObjectOutputStream out;

		public DealWithClient(Socket socket) throws IOException {
			doConnections(socket);
		}

		@Override
		public void run() {

			try {
				getDirection();
				sendGameState();

			} catch (IOException e) {

				System.out.println("Error accepting connection");
				e.printStackTrace();
			}
		}


		void doConnections(Socket socket) throws IOException {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("SERVER --> connection established");
		}

		private void sendGameState() throws IOException {
			
			//for (int i = 0; i < 90; i++) {

				out.writeObject(game);

				try {
					Thread.sleep(Game.REFRESH_INTERVAL);

				} catch (InterruptedException e) {}
			}
			//out.println("FIM");
		//}
		
		private void getDirection() throws IOException {
			
			String direction = in.readLine();
			
		}
	}
}
