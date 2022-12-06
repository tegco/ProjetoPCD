package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import environment.Direction;

public class Client extends Player {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;

	int id;
	protected Game game;
	private static final int INITIAL_STRENGHT = 5;
	//protected byte originalStrength;

	public Client(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public static void main(String[] args) {

		//new Client(id, game, currentStrength).runClient();
	}

	public void runClient() {

		try {
			connectToServer();
			sendMessages();
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//... 
			}
		}
	}

	void connectToServer() throws IOException {

		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, Server.PORTO);
		System.out.println("Socket:" + socket);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	void sendMessages() throws IOException {

		for (int i = 0; i < 10; i++) {
			out.println("Ola " + i);
			String str = in.readLine();
			System.out.println(str);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {//...  
			}
		}
		out.println("FIM");
	}

	@Override
	public void move(Direction direction) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isHumanPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

}

