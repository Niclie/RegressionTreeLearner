package tree;

import java.io.Serializable;
import java.util.*;

import data.*;

/**
 * Modella l'entità nodo di split (continuo o discreto) estendendo la superclasse Node.
 */
abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Classe che aggrega tutte le informazioni riguardanti un nodo di split.
	 */
	public class SplitInfo implements Serializable{
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Valore di tipo Object (di un attributo indipendente) che definisce uno split.
		 */
		private final Object splitValue;
		
		/**
		 * Indice nel training set del primo esempio coperto.
		 */
		private final int beginIndex;
		
		/**
		 * Indice nel training set dell'ultimo esempio coperto.
		 */
		private final int endIndex;
		
		/**
		 * Numero di split (nodi figli) originanti dal nodo corrente.
		 */
		private final int numberChild;
		
		/**
		 * Operatore matematico che definisce il test nel nodo corrente ("=" per valori discreti).
		 */
		private String comparator = "=";
		
		/**
		 * Costruttore che avvalora gli attributi di classe per split a valori discreti.
		 * @param splitValue Valore di un attributo indipendente che definisce lo split
		 * @param beginIndex Indice nel training set del primo esempio coperto.
		 * @param endIndex Indice nel training set dell'ultimo esempio coperto.
		 * @param numberChild Numero di figli generati dal nodo corrente.
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild){
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}
		

		/**
		 * Costruttore che avvalora gli attributi di classe per split a valori continui.
		 * @param splitValue Valore di un attributo indipendente che definisce lo split
		 * @param beginIndex Indice nel training set del primo esempio coperto.
		 * @param endIndex Indice nel training set dell'ultimo esempio coperto.
		 * @param numberChild Numero di figli generati dal nodo corrente.
		 * @param comparator stringa "minore uguale" o "maggiore".
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator){
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;
		}
		
		/**
		 * Restituisce il Primo indice del sotto-insieme di training.
		 * @return Primo indice del sotto-insieme di training.
		 */
		int getBeginindex(){
			return beginIndex;			
		}
		
		/**
		 * Restituisce il secondo indice del sotto-insieme di training.
		 * @return Secondo indice del sotto-insieme di training.
		 */
		int getEndIndex(){
			return endIndex;
		}
		
		/**
		 * Restituisce il valore dello split 
		 * @return Object rappresentante il valore dello split.
		 */
		Object getSplitValue(){
			return splitValue;
		}
		
		/**
		 * Concatena in un oggetto String i valori di beginExampleIndex, endExampleIndex, child, splitValue, comparator
		 * e restituisce la stringa finale. 
		 */
		public String toString(){
			return "child " + numberChild + " split value" + comparator + " " + splitValue + " [Examples:" + beginIndex + "-" + endIndex + "]";
		}
		
		/**
		 * Restituisce il valore dell'operatore matematico che definisce il test.
		 * @return Operatore matematico che definisce il test.
		 */
		String getComparator(){
			return comparator;
		}
	}

	/**
	 * Oggetto Attribute che modella l'attributo indipendente sul quale lo split è generato.
	 */
	private final Attribute attribute;
	
	/**
	 * Lista per memorizzare gli split candidati.
	 */
	protected List<SplitInfo> mapSplit = new ArrayList<SplitInfo>();
	
	/**
	 * Attributo che contiene il valore di varianza a seguito del partizionamento indotto dallo split corrente.
	 */
	private double splitVariance;
	
	/**
	 * Invoca il costruttore della superclasse, ordina i valori dell'attributo
	 * di input per gli esempi [beginExampleIndex, endExampleIndex] e sfrutta questo
	 * ordinamento per determinare i possibili split e popolare mapSplit,
	 * computa la varianza (splitVariance) per l'attributo usato nello split sulla base del partizionamento
	 * indotto dallo split.
	 * @param trainingSet Training set complessivo.
	 * @param beginExampleIndex Indice che indica il primo estremo del sotto-insieme di training.
	 * @param endExampleIndex Indice che indica il secondo estremo del sotto-insieme di training.
	 * @param attribute Attributo indipendente sul quale si definisce lo split.
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute){
			super(trainingSet, beginExampleIndex, endExampleIndex);
			this.attribute = attribute;
			trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute
			setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
						
			//compute variance
			splitVariance = 0;
			for(SplitInfo s: mapSplit){
				double localVariance = new LeafNode(trainingSet, s.getBeginindex(), s.getEndIndex()).getVariance();
					
				splitVariance += (localVariance);
			}
	}
	
	/**
	 * Metodo abstract per generare le informazioni necessarie per ciascuno degli split candidati (in mapSplit).
	 * @param trainingSet Training set complessivo.
	 * @param beginExampleIndex Indice che indica il primo estremo del sotto-insieme di training.
	 * @param endExampleIndex Indice che indica il secondo estremo del sotto-insieme di training.
	 * @param attribute Attributo indipendente sul quale si definisce lo split.
	 */
	abstract void setSplitInfo(Data trainingSet,int beginExampleIndex, int endExampleIndex, Attribute attribute);
	
	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split 
	 * @return Oggetto per l'attributo usato per lo split.
	 */
	Attribute getAttribute(){
		return attribute;
	}

	/**
	 * Restituisce la varianza per lo split corrente.
	 */
	double getVariance(){
		return splitVariance;
	}
	
	/**
	 * Restituisce il numero dei rami originanti nel nodo corrente.
	 */
	int getNumberOfChildren(){
		return mapSplit.size();
	}
	
	/**
	 * Restituisce le informazioni per il ramo in mapSplit indicizzato da child.
	 * @param child ramo di cui si vogliono reperire le informazioni.
	 * @return SplitInfo relativo a child.
	 */
	SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

	/**
	 * Concatena le informazioni di ciascuno test (attributo, operatore e valore) in una String finale. 
	 * @return informazioni (attributo, operatore e valore) di un test concatenate.
	 */
	String formulateQuery(){
		String query = "";
		int i = 0;
		for(SplitInfo s: mapSplit)
			query+= (i++ + ": " + attribute + s.getComparator() + " " + s.getSplitValue()) + "\n";
		
		return query;
	}
	
	/**
	 * Concatena le informazioni di ciascuno test (attributo, esempi coperti, varianza, varianza di Split) in una String finale. 
	 */
	public String toString(){
		String v= "SPLIT: attribute= " +attribute + super.toString()+  " split Variance: " + getVariance();
		
		for(SplitInfo s: mapSplit)
			v+= "\t" + s + "\n";
		
		return v;
	}
	
	public int compareTo(SplitNode o) {
		if(this.getVariance() > o.getVariance())
			return 1;
		
		if(this.getVariance() < o.getVariance())
			return -1;
	
		return 0;
	}
}
