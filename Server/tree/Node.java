package tree;

import data.*;

import java.io.Serializable;

/**
 * Modella l'entità nodo (fogliare o intermedio) dell'albero di decisione.
 */
abstract class Node implements Serializable {
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contatore dei nodi generati nell'albero
	 */
	private static int idNodeCount = 0;
	
	/**
	 * Identificativo numerico del nodo
	 */
	private final int idNode;
	
	/**
	 * Indice nel training set del primo esempio coperto dal nodo corrente
	 */
	private final int beginExampleIndex;
	
	/**
	 * Indice nel training set dell'ultimo esempio coperto dal nodo corrente.
	 */
	private final int endExampleIndex;
	
	//beginExampleIndex e endExampleIndex individuano un sotto-insieme di training.
	
	/**
	 * Valore della varianza calcolata, rispetto all'attributo
	 */
	private double variance;
	
	/**
	 * Avvalora gli attributi primitivi di classe, inclusa la varianza che viene calcolata
	 * rispetto all'attributo da predire nel sotto-insieme di training coperto dal nodo.
	 * @param trainingSet Oggetto di classe Data contenente il training set completo.
	 * @param beginExampleIndex Indice che indica il primo estremo del sotto-insieme di training coperto dal nodo corrente.
	 * @param endExampleIndex Indice che indica il secondo estremo del sotto-insieme di training coperto dal nodo corrente.
	 */
	Node(Data trainingSet, int beginExampleIndex, int endExampleIndex){
		Double media = (double) 0;
		
		idNode = idNodeCount++;
		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		
		/*Calcola la varianza*/
		for(int i = beginExampleIndex; i <= endExampleIndex; i++)
			media += trainingSet.getClassValue(i);
		
		media = media / (endExampleIndex - beginExampleIndex + 1);	

		for(int i = beginExampleIndex; i <= endExampleIndex; i++)
			variance += ((trainingSet.getClassValue(i) - media) * (trainingSet.getClassValue(i) - media));
		/*-------------------*/
	}
	
	/**
	 * Restituisce il valore del membro idNode.
	 * @return Identificativo numerico del nodo.
	 */
	int getIdNode() {
		return idNode;
	}
	
	/**
	 * Restituisce il valore del membro beginExampleIndex.
	 * @return Indice del primo esempio del sotto-insieme rispetto al training set complessivo.
	 */
	int getBeginExampleIndex() {
		return beginExampleIndex;
	}
	
	/**
	 * Restituisce il valore del membro endExampleIndex.
	 * @return indice dell'ultimo esempio del sotto-insieme rispetto al training set complessivo.
	 */
	int getEndExampleIndex() {
		return endExampleIndex;
	}
	
	/**
	 * Restituisce il valore del membro variance.
	 * @return Valore della varianza dell’attributo da predire rispetto al nodo corrente.
	 */
	double getVariance() {
		return variance;
	}
	
	/**
	 * E' un metodo astratto la cui implementazione riguarda gli split node dai quali
	 * si possono generare figli, uno per ogni split prodotto. Restituisce il numero di tali nodi figli.
	 * @return valore del numero di nodi sottostanti.
	 */
	abstract int getNumberOfChildren();
		
	/**
	 * Concatena in un oggetto String i valori di beginExampleIndex, endExampleIndex, variance e restituisce la stringa finale. 
	 */
	public String toString() {
		return "[examples: " + beginExampleIndex + "-" + endExampleIndex + "]" + " variance= " + variance;
	}
}

