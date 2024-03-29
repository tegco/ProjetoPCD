package gui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;

import environment.Direction;
import game.Game;
import game.MyServer;

public class GameGuiClient extends GameGuiMain {

	private int socketNum;
	private String address;
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	

	boolean alternativeKeys;

	private Direction direction;
	private BoardJComponent boardListener = boardGui;

	public GameGuiClient(int socket, String address, boolean alternativeKeys) {
		
		super();
		this.socketNum = socket;
		this.address = address;
		this.alternativeKeys = alternativeKeys;
	}

	@Override 
	public void init()  {

		frame.setVisible(true);	
		frame.setLocation(800, 150);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		runClient();
	}

	public void runClient() {

		try {
			connectToServer();
			gameStateThread();
			sendDirection();

		} catch (IOException e) {
		} finally {
			try {
				socket.close();

			} catch (IOException e) {
			}
		}
	}
	private void sendDirection() {

		while(true) {

			direction = boardListener.getLastPressedDirection();

			if(direction !=  null) {

				out.println(direction.name());
				boardListener.clearLastPressedDirection();
			}
		}
	}

	void connectToServer() throws IOException {

		InetAddress ipAddress = InetAddress.getByName(address);
		socket = new Socket(ipAddress, socketNum);

		in = new ObjectInputStream(socket.getInputStream());

		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	private void gameStateThread() {

		new Thread(){

			@Override
			public void run(){
				try { 
					try {
						while(true) {
							
							boardGui.setGame((Game) in.readObject());

							//game.printBoard();

							game.notifyChange();
						}
					} finally {
						in.close();
					}
				}catch(Exception e){}
			}
		}.start();
	}

	public static void main(String[] args) {
		
		GameGuiClient client;

		client = new GameGuiClient(MyServer.PORT, null, false);
		
		client.init();
	}
}