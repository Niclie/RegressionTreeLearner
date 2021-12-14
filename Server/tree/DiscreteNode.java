package tree;

import java.io.Serializable;
import java.util.Iterator;

import data.*;

/**
 * Modella l'entità nodo di split relativo ad un attributo indipendente discreto.
 */
class DiscreteNode extends SplitNode implements Serializable {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse con il parametro attribute. 
	 * @param trainingSet Training set complessivo.
	 * @param beginExampelIndex Indice che indica il primo estremo del sotto-insieme di training.
	 * @param endExampleIndex Indice che indica il secondo estremo del sotto-insieme di training.
	 * @param attribute Attributo discreto sul quale si definisce lo split.
	 */
	DiscreteNode(Data trainingSet, int beginExampelIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampelIndex, endExampleIndex, attribute);
	}
	
	/**
	 * Istanzia oggetti SpliInfo (definita come inner class in SplitNode) con ciascuno dei valori
	 * discreti di attribute relativamente al sotto-insieme di training corrente
	 * (ossia la porzione di trainingSet compresa tra beginExampelIndex e endExampelIndex)
	 * quindi popola mapSplit con tali oggetti.
	 */
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		int id = 0;
		final Iterator<Object> itr = ((DiscreteAttribute) attribute).iterator();
		
		int i;
		for(i = beginExampleIndex; (i <= endExampleIndex) && (trainingSet.getExplanatoryValue(i, attribute.getIndex())).equals(trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex())); i++);
				
		mapSplit.add(new SplitInfo(itr.next(), beginExampleIndex, i - 1, id++));
		
		if(i - 1 != endExampleIndex)
			setSplitInfo(trainingSet, i, endExampleIndex, attribute, itr, id);
	}

	private void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute, Iterator<Object> itr, int id) {
		int i;
		for(i = beginExampleIndex; (i <= endExampleIndex) && (trainingSet.getExplanatoryValue(i, attribute.getIndex())).equals(trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex())); i++);

		mapSplit.add(new SplitInfo(itr.next(), beginExampleIndex, i - 1, id++));

		if(i - 1 != endExampleIndex)
			setSplitInfo(trainingSet, i, endExampleIndex, attribute, itr, id);

	}
	
	/**
	 * Invoca il metodo della superclasse specializzandolo per i nodi discreti.
	 */
	public String toString() {
		return super.toString();
	}
}
