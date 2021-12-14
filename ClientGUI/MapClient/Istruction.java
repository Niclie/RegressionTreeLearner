package MapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.control.TextArea;


/**
 * Questa classe raccoglie le richieste che è possibile fare al server attraverso gli elementi della GUI. Ogni richiesta al server
 * è seguita da una riposta da parte del server che indica se la richiesta è andata a buon fine, in questo caso invierà un "OK",
 * oppure se non è andata a buon fine. Nell'ultimo caso la stringa inviata dal server descriverà l'eccezione. 
 */
class Istruction {
	
	/**
	 * Lancia la fase di acquisizione della tabella TableName.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @param tableName Nome della tabella da acquisire da database. 
	 * @return Messaggio da parte del server dove comunica l'eventuale successo o fallimento dell'acquisizione della tabella.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static String acquisitionPhase(ObjectInputStream in, ObjectOutputStream out, String tableName) throws IOException, ClassNotFoundException {
		out.writeObject(0);
		out.writeObject(tableName);	
		
		return in.readObject().toString();
	}
	
	/**
	 * Lancia la fase di apprendimento dell'albero da dati acquisiti in una acquisitionPhase precedente.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @return Messaggio da parte del server che comunica l'eventuale successo o fallimento dell'apprendimento della tabella acquisita. 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static String learningPhase(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.writeObject(1);	
		
		return in.readObject().toString();
	}
	
	/**
	 * Lancia la fase di caricamento dell'albero di regressione da file.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @param tableName Percorso del file dove risiede l'albero di regressione da caricare.
	 * @return Messaggio da parte del server che comunica l'eventuale successo o fallimento del caricamento dell'albero da file.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static String loadTreeFromData(ObjectInputStream in, ObjectOutputStream out, String tableName) throws IOException, ClassNotFoundException{
		out.writeObject(2);
		out.writeObject(tableName);
		
		return in.readObject().toString();
	}
	
	/**
	 * Restituisce un Messaggio da parte del server che comunica l'eventuale successo o fallimento e le regole di un albero appreso in una fase precedente.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @return Messaggio da parte del server che comunica l'eventuale successo o fallimento della restituzione delle regole e le regole dell'albero.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static String[] getRules(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException{
		out.writeObject(4);
		String[] rules = new String[2];
		rules[0] = in.readObject().toString();
		rules[1] = in.readObject().toString();
				
		return rules;
	}
	
	/**
	 * Restituisce un Messaggio da parte del server che comunica l'eventuale successo o fallimento e informazioni
	 * relative alla strutture di un albero appreso in una fase precedente.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @return Messaggio da parte del server che comunica l'eventuale successo o fallimento della restituzione della struttura e la struttura dell'albero.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static String[] getTreeStructure(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException{
		out.writeObject(5);
		String[] structure = new String[2];
		structure[0] = in.readObject().toString();
		structure[1] = in.readObject().toString();
		
		
		return structure;
	}
	
	/**
	 * Lancia la fase di predizione e stampa le opzioni di scelta.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @param textAreaPrediction Campo dove stampare le opzioni di scelta.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static void predictionPhase(ObjectInputStream in, ObjectOutputStream out, TextArea textAreaPrediction) throws IOException, ClassNotFoundException {
		Main.setElementVisibilityOnPrediction();
		
		textAreaPrediction.setText("Starting prediction phase! ('e' to exit)\n");
		out.writeObject(3);
		
		String answer = in.readObject().toString();
		if(answer.equals("QUERY")) {
			// Formualting query, reading answer
			answer = in.readObject().toString();
			textAreaPrediction.appendText(answer + "\n");	
		}
	}
	
	/**
	 * In base alla scelta dell'utente stampa le opzioni relative a quella scelta.
	 * Nel caso in cui l'utente inserisca "e" come valore il risultato sarà uscire dalla fase di predizione.
	 * @param in Stream per ricevere messaggi da parte del server.
	 * @param out Stream per inviare istruzioni al server.
	 * @param textAreaPrediction Campo dove stampare le opzioni di scelta.
	 * @param pathFieldValue Valore che indica la scelta dell'utente.
	 */
	static void predictionPhase(ObjectInputStream in, ObjectOutputStream out, TextArea textAreaPrediction, String pathFieldValue){
		int path;
		try {
			
			if(pathFieldValue.equals("e"))
				out.writeObject(-1);
			else {
			    try {
					path = Integer.valueOf(pathFieldValue);
					out.writeObject(path);
			    } catch (NumberFormatException nfe) {
			    	textAreaPrediction.appendText("The value should be numeric!\n");
			    	return;
			    }    
			}
			
			String answer = in.readObject().toString();
			if(answer.equals("QUERY")) {
				answer = in.readObject().toString();
				textAreaPrediction.appendText(answer + "\n");
			}
			else if(answer.equals("OK")){ 
				// Reading prediction
				answer = in.readObject().toString();
				textAreaPrediction.appendText("Predicted class: " + answer);
				
				Main.setElementVisibilityOutPrediction();
			}
			else { 
				//Printing error message
				textAreaPrediction.appendText(answer);
				
				Main.setElementVisibilityOutPrediction();	
			}
			
		} catch (IOException | ClassNotFoundException e) {
			textAreaPrediction.appendText(e.toString());
		}
	}
}
