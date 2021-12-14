package MapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/** 
 * Permette la comunicazione con il server, genera e mette insieme le componenti della GUI
 * e mostra la finestra a schermo.
 * Si possono distinguere componenti per la fase di acquisizione e componenti per la fase di predizione.
 * */
public class Main extends Application{
	/**
	 * Indirizzo IP del server
	 */
	private static String IP;
	
	/**
	 * Porta su cui comunicare con il server
	 */
	private static int PORT;
	
	/**
	 * Larghezza della finestra.
	 * */
	private static final int WIDTH = 600;
	/**
	 * Altezza della finestra.
	 * */
	private static final int HEIGHT = 400;
	
	/**
	 * Indica la scelta dell'utente: se imparare da una tabella nel database (= 1) o se caricare il regression Tree da file (= 2).
	 * */
	private int choice = 0;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	/**
	 * Finestra dove vengono mostrate gli elementi della GUI.
	 * */
	private Stage primaryStage;
	
	private TextArea textAreaRules;
	private TextArea textAreaPrediction;
	
	private static TextField pathField;
	
	private static Button setPath;
	private static Button applyButton;
	
	private static Text repeat;
	
	/**
	 * Regole dell'albero appreso.
	 */
	private String rules;
	
	/**
	 * Informazioni relative alla struttura dell'albero appreso.
	 */
	private String structure;
	private Text showRules;
	private Text showStructure;
	
	
	/**
	 * Indica se si sta eseguendo la predictionPhase (= true) o meno (= false).
	 * */
	private static boolean onPrediction = false;
	
	/**
	 * Inizializza la socket con indirizzo ip e porta per mettersi in cumunicazione con il server e si occupa di lanciare l'interfaccia grafica.
	 * */
	public static void main(String[] args){
		IP = args[0];
		PORT = Integer.valueOf(args[1]);
		launch(args);
	}
	
	/**
	 * Setta le dimensioni della finestra e mette insieme le componenti della GUI. 
	 * Inoltre si assicura che, alla chiusura della finestra, il client comunichi al server la chiusura della connessione.
	 */
	public void start(Stage primaryStage) {
		try {
			initializeConnection(IP, PORT);
		} catch (IOException e1) {
			this.sendAlert(e1.toString());
			
			return;
		}
		
		this.primaryStage = primaryStage;
		
		Element.setStage(primaryStage, WIDTH, HEIGHT);

		Pane mainLayout = Element.setMainLayout(); 		
		mainLayout.getChildren().setAll(getAcquisitionPhaseElement(), getPredictionPhaseElement());
		
		Scene scene = new Scene(mainLayout, WIDTH, HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					if(onPrediction)
						out.writeObject(-1);
					
					out.writeObject(6);
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					sendAlert(e.toString());
				}
			}
		});
		
		primaryStage.show();
	}
	
	/**
	 * Inizializza la connessione con il server.
	 * @param ip Indirizzo IP del server.
	 * @param port Porta su cui il client e il server comunicano.
	 * @throws IOException
	 */
	private void initializeConnection(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());// stream con richieste del client
	}
	
	/**
	 * Invia un Alert all'utente avvisandolo di un errore
	 * @param message Messaggio da stampare sull'allert
	 */
	private void sendAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
	 * Si occupa di reperire le componenti della GUI riguardanti la fase di acquisizione e setta 
	 * browse e choiceBox in modo tale da essere in grado di rispondere agli input dell'utente.
	 * @return Group contenente tutte le componenti necessarie alla fase di acquisizione.
	 */
	private Group getAcquisitionPhaseElement() {
		TextField textField = Element.nameField();

		Text browse = Element.fileChooser();
		browse.setOnMouseClicked(e -> {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DMP files (*.dmp)", "*.dmp");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("Load Regression Tree from archive");
			textField.setText(fileChooser.showOpenDialog(primaryStage).getAbsolutePath());	
		});
		
		ChoiceBox<String> choiceBox = Element.choiceMod();
		choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if(choiceBox.getItems().get((Integer)number2) == "Load Regression Tree from archive") {
					browse.setVisible(true);
					choice = 2;
				}
				else {
					browse.setVisible(false);
					choice = 1;
				}
			}
		});
		choiceBox.setValue("Learn Regression Tree from data");//Setta "Learn Regression Tree from data" come scelta standard
		
		applyButton = Element.apply();
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		    	String tableName = textField.getText();
		    	if(tableName.equals(""))
		    		return;
		    	
		    	apply(choice, tableName);
		    }
		});
		
		Line line = Element.divider();

		showRules = Element.rulesButton();
		showRules.setVisible(false);
		showRules.setOnMouseClicked(e -> {
			textAreaRules.setText(rules);
		});
		
		showStructure = Element.structureButton();
		showStructure.setVisible(false);
		showStructure.setOnMouseClicked(e -> {
			textAreaRules.setText(structure);
		});
		
		Group acquisitionElement = new Group(choiceBox, line, textField, browse, applyButton, showRules, showStructure);
		
		return acquisitionElement;
	}
	
	/**
	 * Si occupa di reperire le componenti della GUI riguardanti la fase di predizione e setta 
	 * repeat e setPath in modo tale da essere in grado di rispondere agli input dell'utente.
	 * @return Group contenente tutte le componenti necessarie alla fase di predizione.
	 */
	private Group getPredictionPhaseElement() {
		textAreaPrediction = Element.predictionArea();
		
		textAreaRules = Element.rulesArea();
		
		pathField = Element.pathField();
		
		repeat = Element.repeatButton();
		repeat.setOnMouseClicked(e -> {
			repeat.setVisible(false);
			try {
				Istruction.predictionPhase(in, out, textAreaPrediction);
			} catch (ClassNotFoundException | IOException e1) {
				this.sendAlert(e1.toString());
			}
		});
		
		setPath = Element.confirmPath();
		setPath.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		    	Istruction.predictionPhase(in, out, textAreaPrediction, pathField.getText());
		    }
		});
		
		Group predictionElement = new Group(textAreaPrediction, textAreaRules, pathField, setPath, repeat);
		
		return predictionElement;
	}
	
	/**
	 * Si occupa di lanciare, in base a decision, la aquisitionPhase e la learningPhase (decision = 1) 
	 * oppure se caricare l'albero di regressione da file (decision = 2). 
	 * Una volta terminata una delle 2 fasi si prosegue alla stampa delle regole e al lancio della predictionPhase.
	 * 
	 * Nel caso in cui il server non risponda con "OK", che indica la corretta esecuzione delle azioni,
	 * si procede alla stampa dell'errore e all'uscita da questo metodo.
	 * 
	 * @param decision Indica l'intenzione dell'utente di generare un albero di regressione o caricarlo da file.
	 * @param tableName Indica il nome della tabella da acquisire o il percorso del file contenente l'albero di regressione.
	 */
	private void apply(int decision, String tableName){
		repeat.setVisible(false);
		
		String answer = null;
		try {
			if(decision == 1) {
				answer = Istruction.acquisitionPhase(in, out, tableName);
				if(!answer.equals("OK")) {
					showRules.setVisible(false);
					showStructure.setVisible(false);
					textAreaRules.setText(answer);
					return;
				}
				
				answer = Istruction.learningPhase(in, out);
			}
			else if(decision == 2)
				answer = Istruction.loadTreeFromData(in, out, tableName);
				
			if(!answer.equals("OK")) {
				showRules.setVisible(false);
				showStructure.setVisible(false);
				textAreaRules.setText(answer);
				return;
			}
			
			showRules.setVisible(true);
			showStructure.setVisible(true);
			
			String[] temp = new String[2];
			temp = Istruction.getRules(in, out);
			answer = temp[1];
			if(!answer.equals("OK")) {
				textAreaRules.setText(answer);
				return;
			}
			rules = temp[0];
			textAreaRules.setText(rules);
			
			temp = Istruction.getTreeStructure(in, out);
			answer = temp[1];
			if(!answer.equals("OK")) {
				textAreaRules.setText(answer);
				return;
			}
			structure = temp[0];			
			
			Istruction.predictionPhase(in, out, textAreaPrediction);
		}
		catch(IOException | ClassNotFoundException e){
			showRules.setVisible(false);
			showStructure.setVisible(false);
			this.sendAlert(e.toString());
		}
	}
	
	/**
	 * Si occupa di settare la visibilità delle componenti della GUI all'interno della finestra
	 * durante la predictionPhase.
	 */
	static void setElementVisibilityOnPrediction() {
		onPrediction = true;
		applyButton.setDisable(true);
		pathField.setDisable(false);
		setPath.setDisable(false);
	}
	
	/**
	 * Si occupa di settare la visibilità delle componenti della GUI all'interno della finestra
	 * fuori dalla predictionPhase.
	 */
	static void setElementVisibilityOutPrediction(){
		onPrediction = false;
		repeat.setVisible(true);
		applyButton.setDisable(false);
		pathField.setDisable(true);
		setPath.setDisable(true);
	}
}
