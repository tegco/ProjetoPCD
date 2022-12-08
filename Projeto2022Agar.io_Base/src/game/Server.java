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


public class Server {
	
	private ExecutorService executor = Executors.newFixedThreadPool(90);

	public class DealWithClient extends Thread {

		public DealWithClient(Socket socket) throws IOException {
			
			doConnections(socket);
		}
		
		private BufferedReader in;

		@Override
		public void run() {
			
			try {
				serve();
				
			} catch (IOException e) {
				
				System.out.println("Error accepting connection");
				e.printStackTrace();
			}
		}
		
		
		private PrintWriter out;

		void doConnections(Socket socket) throws IOException {
			
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}
		
		private void serve() throws IOException {
			
			String direction = in.readLine();
			
//			while (true) {
//				
//				String str = in.readLine();
//				
//				if (str.equals("FIM"))
//					
//					break;
//				
//				System.out.println("Eco:" + str);
//				
//				out.println(str);
//			}
			
			while (direction != null) {
				
				if (direction.equals("FIM")) {
					break;
				}
				
				System.out.println("Eco:" + direction);
				
				out.println(direction);	
				
			}
		}
	}

	public static final int PORT = 8080;
	

	public static void main(String[] args) {
		
		try {
			
			new Server().startServing();
			
		} catch (IOException e) {
			
			System.err.println("Error starting Server");
			
		}
	}

	public void startServing() throws IOException {
		
		ServerSocket ss = new ServerSocket(PORT);
		
		try {
			
			System.out.println("Starting Server");
			
			while(true){
				
				System.out.println("Waiting for request");
				
				Socket socket = ss.accept();
				
				System.out.println("Processing request");
				
				//executor.submit(new ServiceRequest(socket));
				
				new DealWithClient(socket).start();
			}			
		} finally {
			
			ss.close();
			
			System.out.println("Server shutdown");
		}
	}
	
//	class ServiceRequest implements Runnable {
//		
//		private Socket ss;
//		
//		public ServiceRequest(Socket connection) {
//			this.ss = connection;
//			
//		}
//
//		@Override
//		public void run() {
//			
//			try {
//				ss.close();
//				
//			} catch (Exception e) {
//				
//				System.out.println("Error closing client connection");
//			}
//			
//		}
//		
//	}
}