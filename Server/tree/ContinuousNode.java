package tree;

import data.Attribute;
import data.*;

import java.io.Serializable;
import java.util.*;

/**
 * Modella l'entità nodo di split relativo ad un attributo indipendente continuo.
 * */
class ContinuousNode extends SplitNode implements Serializable {

    /**
     * Defult serialVersionUID
     */
	private static final long serialVersionUID = 1L;

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse con il parametro attribute. 
	 * @param trainingSet Training set complessivo.
	 * @param beginExampelIndex Indice che indica il primo estremo del sotto-insieme di training.
	 * @param endExampleIndex Indice che indica il secondo estremo del sotto-insieme di training.
	 * @param attribute Attributo continuo sul quale si definisce lo split.
	 */
	ContinuousNode(Data trainingSet, int beginExampelIndex, int endExampleIndex, ContinuousAttribute attribute) {
        super(trainingSet, beginExampelIndex, endExampleIndex, attribute);
    }

	/**
	 * Istanzia oggetti SpliInfo (definita come inner class in Splitnode) con ciascuno dei valori
	 * continui di attribute relativamente al sotto-insieme di training corrente
	 * (ossia la porzione di trainingSet compresa tra beginExampelIndex e endExampelIndex)
	 * quindi popola mapSplit con tali oggetti.
	 */
    void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
    	Double currentSplitValue = (Double) trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex());
    	Double bestInfoVariance = new Double(0);
        List<SplitInfo> bestMapSplit = null;

        for(int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
        	Double value = (Double) trainingSet.getExplanatoryValue(i, attribute.getIndex());
            if(value != currentSplitValue) {
                double localVariance = new LeafNode(trainingSet, beginExampleIndex,i - 1).getVariance();
                double candidateSplitVariance = localVariance;
                localVariance = new LeafNode(trainingSet, i, endExampleIndex).getVariance();
                candidateSplitVariance += localVariance;

                if(bestMapSplit == null){
                    bestMapSplit = new ArrayList<SplitInfo>();
                    bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
                    bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
                    bestInfoVariance = candidateSplitVariance;
                }
                else if(candidateSplitVariance < bestInfoVariance){
                    bestInfoVariance = candidateSplitVariance;
                    bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
                    bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
                }
                currentSplitValue = value;
            }
        }

        mapSplit = bestMapSplit;
        //rimuovo split inutili (che includono tutti gli esempi nella stessa partizione)
        if((mapSplit.get(1).getBeginindex() == mapSplit.get(1).getEndIndex())){
            mapSplit.remove(1);
        }
    }

	/**
	 * Invoca il metodo della superclasse specializzandolo per i nodi discreti.
	 */
    @Override
    public String toString() {
        return super.toString();
    }
}