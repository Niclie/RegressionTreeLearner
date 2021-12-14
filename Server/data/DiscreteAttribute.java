package data;

import java.util.*;

/**
 * Estende la classe Attribute e modella un attributo discreto.
 */
public class DiscreteAttribute extends Attribute implements Iterable<Object>{
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * TreeSet di Object, uno per ciascun valore discreto che l'attributo può assumere. 
	 */
	private Set<Object> values = new TreeSet<>();
	
	/**
	 * Invoca il costruttore della super-classe e avvalora il TreeSet values con i valori discreti in input.
	 * @param name Nome simbolico dell'attributo.
	 * @param index Identificativo numerico dell'attributo.
	 * @param values Valori discreti.
	 */
	public DiscreteAttribute(String name, int index, Set<Object> values){
		super(name, index);
		this.values = values;
	}
	
	/**
	 * Restituisce il numero di elementi di values.
	 * @return Numero di valori discreti dell'attributo.
	 */
	public int getNumberOfDistinctValues(){
		return values.size();
	}

	@Override
	public Iterator<Object> iterator(){
		return values.iterator();
	}
}
