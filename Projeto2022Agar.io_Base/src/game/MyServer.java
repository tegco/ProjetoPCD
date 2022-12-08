package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

	public static final int PORT = 8080;

	ExecutorService executorService = Executors.newFixedThreadPool(90);

	public void startServing() throws IOException {

		System.out.println("OLA");
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

	public static void main(String[] args) {

		try {

			new MyServer().startServing();

		} catch (IOException e) {

			System.err.println("Error starting Server");

		}
	}


	public class DealWithClient extends Thread {

		private BufferedReader in;
		private PrintWriter out;

		public DealWithClient(Socket socket) throws IOException {
			doConnections(socket);
		}

		@Override
		public void run() {

			try {
				sendGameState();

			} catch (IOException e) {

				System.out.println("Error accepting connection");
				e.printStackTrace();
			}
		}


		void doConnections(Socket socket) throws IOException {

			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}

		private void sendGameState() throws IOException {
			for (int i = 0; i < 90; i++) {

				out.println("Ola " + i);
				String str = in.readLine();
				System.out.println(str);

				try {
					Thread.sleep(Game.REFRESH_INTERVAL);

				} catch (InterruptedException e) {}
			}
			out.println("FIM");
		}
		}
	}
