package MapClient;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Ha il compito di raccogliere e fornire tutti gli elementi dell'interfaccia grafica.
 */
class Element {
	/**
	 * Si occupa di settare le dimensioni della finestra (stage).
	 * @param stage Finestra di dimensione width x height.
	 * @param width Larghezza di stage.
	 * @param height Altezza di stage.
	 */
	static void setStage(Stage stage, int width, int height) {
		stage.setTitle("Client");
		
		stage.setMinHeight(height);
		stage.setMaxHeight(height);
		stage.setMinWidth(width + 15);
		stage.setMaxWidth(width + 15);
		stage.setResizable(false);
	}
	
	
	/**
	 * Si occupa di settare le caratteristiche del Pane principale.
	 * @return Un oggetto Pane.
	 */
	static Pane setMainLayout() {
		Pane mainLayout = new Pane();
		mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, Insets.EMPTY)));
		
		return mainLayout;
	}
	
	
	
	//Elementi per la fase di acquisizione:
	
	/**
	 * Restituisce un TextField dove vengono inseriti i nomi delle tabelle e i percorsi dei file.
	 * @return Un textField in posizione x = 250 e y = 15.
	 */
	static TextField nameField(){
		TextField textField = new TextField();
		textField.setLayoutX(250);
		textField.setLayoutY(15);
		
		return textField;
	}
	
	/**
	 * Restituisce un TextField "Browse". Permette di selezionare un file con estensione .dmp.
	 * @return Un Text con scritto "Browse" in stile "Verdana" di dimensione 13 in posizione x = 410 e y = 30.
	 */
	static Text fileChooser(){
		Text browse = new Text("Browse");
		browse.setFill(Color.rgb(0, 102, 204));
		browse.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
		browse.setUnderline(true);
		browse.setLayoutX(410);
		browse.setLayoutY(30);
		
		return browse;
	}
	
	/**
	 * Restituisce un ChoiceBox che descrive le 2 possibili scelte (Load o Learn).
	 * @return Un ChoiceBox con due possibili scelte: "Learn Regression Tree from data" e "Load Regression Tree from archive".
	 */
	static ChoiceBox<String> choiceMod(){
		ChoiceBox<String> cb = new ChoiceBox<String>(FXCollections.observableArrayList("Learn Regression Tree from data", "Load Regression Tree from archive"));
		cb.setLayoutX(15);
		cb.setLayoutY(15);
		
		return cb;
	}
	
	/**
	 * Restituisce un Button che esegue l'istruzione selezionata nel ChoiceBox.
	 * @return Un Button con scritto "Apply" in posizione x = 538 e y = 15.
	 */
	static Button apply() {
		Button applyButton = new Button("Apply");
		applyButton.setLayoutX(538);
		applyButton.setLayoutY(15);
		
		return applyButton;
	}
	
	/**
	 * Restituisce una Line che divide in 2 la finestra: parte superiore di acquisizione, parte inferiore di predizione.
	 * @return una Line.
	 */
	static Line divider() {
		Line line = new Line(0, 55, 10000, 55);
		line.setStroke(Color.rgb(97, 97, 97));
		line.setStrokeWidth(2.5);
		
		return line;
	}
	
	
	
	//Elementi per la fase di predizione:
	
	/**
	 * Restituisce una TextArea dove vengono stampate le possibili scelte durante la fase di predizione.
	 * @return Una TextArea in posizione x = 15, y = 100.
	 */
	static TextArea predictionArea() {
		TextArea textAreaPrediction = new TextArea();
		textAreaPrediction.setLayoutX(15);
		textAreaPrediction.setLayoutY(100);
		textAreaPrediction.setPrefWidth(277.5);
		textAreaPrediction.setEditable(false);
		
		return textAreaPrediction;
	}
	
	/**
	 * Restituisce una TextArea dove vengono stampate le regole dell'albero di regressione.
	 * @return Una TextArea in posizione x = 307.5, y = 100 .
	 */
	static TextArea rulesArea() {
		TextArea textAreaRules = new TextArea();
		textAreaRules.setLayoutX(307.5);
		textAreaRules.setLayoutY(100);
		textAreaRules.setPrefWidth(277.5);
		textAreaRules.setEditable(false);
		
		return textAreaRules;
	}


	/**
	 * Restituisce un Text che mostra le regole dell'albero.
	 * @return Text con scritto "Show rules" in posizione x= 330, y = 305.
	 */
	static Text rulesButton(){
		Text rules = new Text("Show rules");
		rules.setFill(Color.rgb(0, 102, 204));
		rules.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
		rules.setUnderline(true);
		rules.setLayoutX(330);
		rules.setLayoutY(305);
		
		return rules;
	}
	
	/**
	 * Restituisce un Text che mostra le informazioni sulla struttura dell'albero.
	 * @return Text con scritto "Show structure" in posizione x= 440, y = 305.
	 */
	static Text structureButton(){
		Text structure = new Text("Show tree structure");
		structure.setFill(Color.rgb(0, 102, 204));
		structure.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
		structure.setUnderline(true);
		structure.setLayoutX(440);
		structure.setLayoutY(305);
		
		return structure;
	}
	
	/**
	 * Restituisce un TextField dove vengono inserite le scelte dell'utente durante la fase di predizione.
	 * @return un TextField in posizione x = 15, y = 290.
	 */
	static TextField pathField() {
		TextField field = new TextField();
		field.setLayoutX(15);
		field.setLayoutY(290);
		field.setPrefWidth(40);
		field.setDisable(true);
		
		return field;
	}
	
	/**
	 * Restituisce un Text che se cliccato permette di ripetere la predictionPhase.
	 * @return Text con scritto "Repeat" in posizione x = 115, y = 305.
	 */
	static Text repeatButton() {
		Text repeat = new Text("Repeat");
		repeat.setFill(Color.rgb(0, 102, 204));
		repeat.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
		repeat.setUnderline(true);
		repeat.setLayoutX(115);
		repeat.setLayoutY(305);
		repeat.setVisible(false);
		
		return repeat;
	}
	
	/**
	 * Restituisce un Button che conferma la scelta dell'utente durante la predictionPhase.
	 * @return Un Button con scritto "OK" in posizione x = 70, y = 290.
	 */
	static Button confirmPath() {
		Button setPath = new Button("OK");
		setPath.setLayoutX(70);
		setPath.setLayoutY(290);
		setPath.setDisable(true);
		
		return setPath;
	}
	
}
