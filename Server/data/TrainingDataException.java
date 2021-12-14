package data;

/**
 * Gestisce le eccezioni causate dalla creazione di un TrainingSet.
 */
public class TrainingDataException extends Exception {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Invoca il costruttore della super-classe
	 * @param msg Stringa con il messaggio da passare al costruttore della super-classe.
	 */
	public TrainingDataException(String msg){
		super(msg);
	}
}