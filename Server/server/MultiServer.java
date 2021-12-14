package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server in grado di gestire più client contemporaneamente.
 */
public class MultiServer {
	/**
	 * Porta dove è in ascolto il server.
	 */
	private int PORT;
	
	/**
	 * Fa partire il server sulla porta port
	 * @param port Porta su cui il server si mette in ascolto.
	 */
	public MultiServer(int port) {
		PORT = port;
		run();
	}
	
	/**
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di richiesta di
	 * connessioni da parte del client. Ad ogni nuova richiesta di connessione si istanzia ServerOneClient.
	 */
	private void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server started!");
			
			while(true){
				//Attende la connessione di un client
				socket = serverSocket.accept();
				System.out.println("Connessione con " + socket.getInetAddress() + " stabilita");
				
				new ServerOneClient(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				serverSocket.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}

}
