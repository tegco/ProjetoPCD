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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Direction;
import game.MyServer.DealWithClient;
import gui.BoardJComponent;

public class Client {

	private static Socket socket;
	private static InetAddress address;

	static ExecutorService executorService = Executors.newFixedThreadPool(90);

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

	public static void main(String[] args) throws IOException, InterruptedException {
		//Remover
		Thread.sleep(3000);
		new Client(MyServer.PORT, address, left,right, up, down).runClient();
	}

	private void runClient() throws IOException, InterruptedException {
		DealWithServer server = new DealWithServer();
		executorService.submit(server);
	}

	public class DealWithServer extends Thread {
		private ObjectInputStream in;
		private PrintWriter out;

		public DealWithServer() throws IOException {
			connectToServer();
		}

		@Override
		public void run() {

			try {
				//Thread.sleep(Game.INITIAL_WAITING_TIME);
				System.out.println("Run");
				getGameState();

			} catch (IOException e) {

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally {
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

			//System.out.println("CLIENT IN_CHANNEL: " + in);

			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}

		void getGameState() throws ClassNotFoundException, IOException {
				boardGui = (BoardJComponent) in.readObject();
				game = boardGui.getGame();
				System.out.println("\nCliente RECEBEU este game");
				game.printBoard();
				//System.out.println("\nCliente Received Game" + boardGui.getGame().toString());
				sendDirection();
		}

		void sendDirection() throws IOException, ClassNotFoundException {
			Direction direction;
			while(true) {
				direction = Direction.randomDirectionGenerator();
				//direction = boardGui.getLastPressedDirection();
				if (direction != null) {
					System.out.println("HumanPlayer:" + direction);
					out.println(direction.name());
					System.out.println("\nCliente ENVIOU este game");
					game.printBoard();
					try {
						System.out.println("I Sleep!!!!!!");
						System.out.println("MANDOU DIR. --> " + direction);
						Thread.sleep(Game.REFRESH_INTERVAL);
						getGameState();
					} catch (InterruptedException e) {

					}
				}
			}
			/**Direction direction ;

			while (true) {
				direction = boardGui.getLastPressedDirection();

				if (direction != null) {
					break;
				}
			}
			System.out.println("HumanPlayer:" + direction);

			out.println(direction.name());
			//boardGui.clearLastPressedDirection();
			**/
		}
	}

}

