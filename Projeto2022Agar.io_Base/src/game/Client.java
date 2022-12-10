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
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Direction;
import game.MyServer.DealWithClient;
import gui.BoardJComponent;

public class Client {

	private Socket socket;
	private static InetAddress address;

	ExecutorService executorService = Executors.newFixedThreadPool(90);
	
	protected Game game;
	private BoardJComponent boardGui;
	
	static KeyEvent left, right, up, down;

	public Client(int PORT, InetAddress address, KeyEvent left, KeyEvent right, KeyEvent up, KeyEvent down) {
		Client.address = address;
		Client.left = left;
		Client.right = right;
		Client.up = up;
		Client.down = down;	
	}

	public static void main(String[] args) {

		new Client(MyServer.PORT, address, left,right, up, down).runClient();
	}

		private ObjectInputStream in;
		private PrintWriter out;
	public void runClient() {

		try {

			connectToServer();
			sendDirection();

		} catch (IOException e) {

		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

		void connectToServer() throws IOException {

			address = InetAddress.getByName(null);

			System.out.println("Endereco:" + address);

			socket = new Socket(address, MyServer.PORT);
			System.out.println("Socket:" + socket);

			in = new ObjectInputStream(socket.getInputStream());

			try {
				game = (Game) in.readObject();

				System.out.println("CLIENT  "  + game.toString());
				
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}

		void sendDirection() throws IOException {

			Direction direction = boardGui.getLastPressedDirection();
			
			System.out.println("DIRECTION:  " + direction.name());

			while (true) {

				if(direction == null) {
					
					System.out.println("NULL");
					break;
				}

				//System.out.println("HumanPlayer:" + direction);

				out.println(direction);	
			}
		}

		//
		//	void sendDirection() throws IOException {
		//
		//		for (int i = 0; i < 90; i++) {
		//			
		//			out.println("Ola " + i);
		//			String str = in.readLine();
		//			System.out.println(str);
		//			
		//			try {
		//				Thread.sleep(Game.REFRESH_INTERVAL);
		//				
		//			} catch (InterruptedException e) {}
		//		}
		//		out.println("FIM");
		//	}
}

