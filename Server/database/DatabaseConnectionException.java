package database;

/**
 * Gestisce le eccezioni causate dalla connessione al database.
 */
public class DatabaseConnectionException extends Exception{
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Invoca il costruttore della super-classe.
	 * @param msg Stringa con il messaggio da passare al costruttore della super-classe.
	 */
	public DatabaseConnectionException(String msg){
		super(msg);
	}
}
