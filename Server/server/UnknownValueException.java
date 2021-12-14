package server;

/**
 * Gestisce il caso di acquisizione di un valore mancante o fuori range di un attributo di un nuovo esempio da classificare.
 */
public class UnknownValueException extends Exception{
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Invoca il costruttore della super-classe.
	 * @param msg Stringa con il messaggio da passare al costruttore della super-classe.
	 */
	public UnknownValueException(String msg){
		super(msg);
	}
}
