package database;

import java.sql.*;

/**
 * Realizza l'accesso alla base di dati.
 */
public class DbAccess {

	/**
	 * Nome del driver che permette la connessione al database.
	 */
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
	
	/**
	 * Nome del DBMS.
	 */
	private final String DBMS = "jdbc:mysql";
	
	/**
	 * Indirizzo del server.
	 */
	private String SERVER = "localhost";
	
	/**
	 * Nome della base di dati.
	 */
	private final String DATABASE = "MapDB";
	
	/**
	 * Porta su cui il DBMS MySQL accetta le connessioni.
	 */
	private final int PORT = 3306;
	
	/**
	 * Nome dell’utente per l’accesso alla base di dati.
	 */
	private final String USER_ID = "MapUser";
	
	/**
	 * Contiene la password di autenticazione per l’utente identificato da USER_ID
	 */
	private final String PASSWORD = "map";
	
	/**
	 * Gestisce una connessione.
	 */
	private Connection conn;

	/**
	 * Impartisce al class loader l’ordine di caricare il driver mysql, inizializza la connessione riferita da conn.
	 * @throws DatabaseConnectionException Il metodo solleva e propaga una eccezione di tipo DatabaseConnectionException
	 * nei casi in cui: Il driver non viene trovato, si verifica un errore durante l'istanziazione del driver,
	 * non è possibile accedere al driver, si verifica una SQL exception.
	 */
	public void initConnection() throws DatabaseConnectionException {
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
			
			String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE 
					+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
			
			conn = DriverManager.getConnection(connectionString);
		} catch(ClassNotFoundException e) {
			throw new DatabaseConnectionException("[!] Driver not found: " + e.getMessage());
		} catch(InstantiationException e){
			throw new DatabaseConnectionException("[!] Error during the instantiation : " + e.getMessage());
		} catch(IllegalAccessException e){
			throw new DatabaseConnectionException("[!] Cannot access the driver : " + e.getMessage());
		} catch(SQLException e) {
		throw new DatabaseConnectionException(
			"[!] SQLException: " + e.getMessage() + "\n" + 
			"[!] SQLState: " + e.getSQLState() + "\n" +
			"[!] VendorError: " + e.getErrorCode());
		}
	}
	
	/**
	 * Restituisce conn.
	 * @return conn
	 */
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 * Chiude la connessione conn.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException{
		conn.close();
	}
}
