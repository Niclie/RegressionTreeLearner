package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import data.TrainingDataException;
import tree.RegressionTree;

/**
 * Ad ogni richiesta del client viene creata un istanza di questa classe. 
 * Estende la classe Thread quindi consente di avere più thread in esecuzione contemporaneamente. 
 */

class ServerOneClient extends Thread{
	/**
	 * Socket che permette la trasmissione e la ricezione di informazioni.
	 */
	private Socket socket;
	
	/*
	 * Stream per ricevere istruzioni da parte del client. 
	 */
	private ObjectInputStream in;
	
	/**
	 * Stream per inviare messaggi al server.
	 */
	private ObjectOutputStream out;
	
	/**
	 * Nome della tabella a partire dal quale viene generato il training set e poi l'albero di regressione.
	 */
	private String tableName;
	
	/**
	 * Training set generato a partire dalla tabella tableName.
	 */
	private Data trainingSet;
	
	/**
	 * Albero di regressione generato da un client.
	 */
	private RegressionTree regressionTree;
	
	/**
	 * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il thread.
	 * @param socket Socket attraverso il quale il server conmunica con il client.
	 * @throws IOException
	 */
	public ServerOneClient(Socket socket) throws IOException{
		this.socket = socket;
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		start();
	}
	
	/**
	 * Riceve istruzioni da parte del client. Le istruzioni sono numeri interi che vanno da 0 a 4.
	 * 0: in base a tableName che indica il nome della tabella, genera un training set relativo ad essa. 
	 * Invia un "OK" al client se il trainingSet viene acquisito correttamente altrimenti verrà inviata un stringa contenente la descrizione dell'eccezione.
	 * 
	 * 1: in base al training set acquisito in una fase precedente, viene generato l'albero di regressione.
	 * 
	 * 2: in questo caso il client vuole acquisire un albero di regressione da file.
	 * 
	 * 3: lancia la fase di predizione.
	 * 
	 * 4: il client richiede le regole dell'albero di regressione.
	 * 
	 * 5: il client richiede informazioni riguardanti la struttura dell'albero.
	 * 
	 * in tutti gli altri casi il server chiuderà la connessione con il client e non sarà più in grado di ricevere istruzioni da parte di quel client.
	 * 
	 */
	public void run(){
		try {
			while(true) {
				int decision = (int) in.readObject();
			
				try {
					switch(decision) {
					case 0:
						tableName = (String) in.readObject();
						trainingSet = dataAcquisition(tableName);
						out.writeObject("OK");
						break;
						
					case 1:
						regressionTree = learnTreeFromTrainingSet(trainingSet, tableName);
						out.writeObject("OK");
						break;
						
					case 2:
						tableName = (String) in.readObject();
						regressionTree = RegressionTree.carica(tableName);
						out.writeObject("OK");
						break;
						
					case 3:
						regressionTree.predictClass(in, out);
						break;
						
					case 4:
						out.writeObject(regressionTree.getRules());
						out.writeObject("OK");
						break;
						
					case 5:
						out.writeObject(regressionTree.getTree());
						out.writeObject("OK");
						break;
						
					default:
						out.close();
						in.close();
						System.out.println("Connessione con " + socket.getInetAddress() + " chiusa");
						socket.close();
						return;
					}	
				} catch (TrainingDataException | UnknownValueException | FileNotFoundException e) {
					try {
						out.writeObject(e.toString());
					} catch (IOException e1) {
						System.out.println(e1.toString());
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Acquisice un trainingSet da una tabella nel database denominata tableName.
	 * @param tableName Nome della tabella nel database.
	 * @return Il trainingSet relativo alla tabella tableName
	 * @throws TrainingDataException Eccezione che viene lanciata in caso di errori durante l'acquisizione del trainingSet.
	 */
	private Data dataAcquisition(String tableName) throws TrainingDataException{
		Data trainingSet = null;
		trainingSet = new Data(tableName);
		
        return trainingSet;
	}
	
	/**
	 * A partire da trainingSet genera un albero di regressione.
	 * @param trainingSet TrainingSet da cui viene estratto l'albero di regressione.
	 * @param tableName Nome della tabella nel database.
	 * @return Un albero di regressione.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private RegressionTree learnTreeFromTrainingSet(Data trainingSet, String tableName) throws IOException, FileNotFoundException{
		RegressionTree regressionTree = new RegressionTree(trainingSet);
        regressionTree.salva(tableName + ".dmp");
		
		return regressionTree;
	}
}
