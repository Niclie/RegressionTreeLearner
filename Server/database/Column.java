package database;

/**
 * Modella (con la classe Table_Schema) lo schema di una tabella nel database relazionale.
 */
public class Column{
	/**
	 * Nome della colonna.
	 */
	private String name;
	
	/**
	 * Tipo di dati contenuti nella colonna.
	 */
	private String type;
	
	/**
	 * Costruttore di classe, rapppresenta una colonna di nome name contenente valori di tipo type.
	 * @param name Nome della colonna.
	 * @param type Tipo dei dati contenuti in quella colonna.
	 */
	public Column(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Restituisce il nome della colonna
	 * @return Stringa contenente il nome della colonna.
	 */
	public String getColumnName(){
		return name;
	}
	
	/**
	 * Controlla che il tipo di valori contenuti in una colonna sia numerico o meno.
	 * @return True se il tipo è numerico, False altrimenti.
	 */
	public boolean isNumber(){
		return type.equals("number");
	}
	
	/**
	 * Restituisce una stringa rappresentante lo stato dell'oggetto.
	 */
	public String toString(){
		return name + ":" + type;
	}
}