package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modella una tupla della tabella letta dalla base di dati.
 */
public class Example implements Iterable<Object>{
	/**
	 * Lista rappresentante una tupla della tabella.
	 */
	private List<Object> example = new ArrayList<Object>();

	/**
	 * Aggiunge o come elemento della tupla.
	 * @param o Oggetto qualsiasi.
	 */
	public void add(Object o){
		example.add(o);
	}
	
	/**
	 * Restituisce il valore relativo alla colonna i.
	 * @param i Colonna dell'esempio.
	 * @return Elemento della colonna i-esima relativa all'esempio.
	 */
	public Object get(int i){
		return example.get(i);
	}
	
	public String toString(){
		String str="";
		
		for(Object o:example)
			str += o.toString()+ " ";
		
		return str;
	}

	@Override
	public Iterator<Object> iterator() {
		return null;
	}	
}