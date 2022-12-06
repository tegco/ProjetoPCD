package game;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client /*extends Player*/ {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	public static final int PORT = Server.PORT;
	private static InetAddress address;

	//int id;
	protected Game game;
	static KeyEvent left, right, up, down;

	public Client(int PORT,InetAddress address, KeyEvent left, KeyEvent right, KeyEvent up, KeyEvent down) {
		Client.address = address;
		Client.left = left;
		Client.right = right;
		Client.up = up;
		Client.down = down;	
	}

	public static void main(String[] args) {

		new Client(PORT, address, left,right, up, down).runClient();
	}

	public void runClient() {

		try {
			connectToServer();
			sendMessages();
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
		socket = new Socket(address, Server.PORT);
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
			} catch (InterruptedException e) { 
			}
		}
		out.println("FIM");
	}

}

