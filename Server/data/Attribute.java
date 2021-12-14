package data;

import java.io.Serializable;

/**
 * Classe astratta, modella un generico attributo discreto o continuo.
 */
public abstract class Attribute implements Serializable{
	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nome dell'attributo.
	 */
	private final String name;
	
	/**
	 * Identificativo numerico dell'attributo.
	 */
	private final int index;
	
	/**
	 * Costruttore di classe. Inizializza i valori dei membri name e index.
	 * @param name Nome simbolico dell'attributo.
	 * @param index Identificativo numerico dell'attributo.
	 */
	public Attribute (String name, int index){
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Restituisce il valore di name.
	 * @return String nome simbolico dell'attributo.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Restituisce il valore di index.
	 * @return int identificativo numerico dell'attributo.
	 */
	public int getIndex(){
		return this.index;
	}
	
	public String toString() {
		return name;
	}
}
