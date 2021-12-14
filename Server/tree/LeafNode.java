package tree;

import data.*;

import java.io.Serializable;

/**
 * Modella l'entità nodo fogliare.
 */
class LeafNode extends Node implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Valore dell'attributo di classe espresso nella foglia corrente.
	 */
	private Double predictedClassValue;
	
	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse e avvalora
	 * l'attributo predictedClassValue (come media dei valori dell’attributo di classe che ricadono nella partizione
	 * ossia la porzione di trainingSet compresa tra beginExampelIndex e endExampelIndex).
	 * @param trainingSet Training set complessivo.
	 * @param beginExampleIndex Indice nel training set del primo esempio coperto.
	 * @param endExampleIndex Indice nel training set dell'ultimo esempio coperto.
	 */
	LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		
		predictedClassValue = (double) 0;
		for(int i = beginExampleIndex; i <= endExampleIndex; i++)
			predictedClassValue += trainingSet.getClassValue(i);
		
		predictedClassValue = predictedClassValue / (endExampleIndex - beginExampleIndex + 1);
	}
	
	/**
	 * Restituisce il valore della foglia.
	 * @return Il valore contenuto in predictClassValue.
	 */
	Double getPredictedClassValue() {
		return predictedClassValue;
	}
	
	/**
	 * Restituisce il numero di split originanti dal nodo foglia, ovvero 0.
	 * @return numero di split originanti dal nodo foglia (0).
	 */
	int getNumberOfChildren() {
		return 0;
	}
	
	/**
	 * Invoca il metodo della superclasse il cui valore restituito viene concatenato con il valore di classe della foglia.
	 */
	public String toString() {
		return "LEAF: class= " + predictedClassValue + " " +  super.toString();
	}

}
