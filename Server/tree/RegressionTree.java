package tree;

import java.io.*;
import java.util.*;

import data.*;
import server.UnknownValueException;

/**
 * Modella l'entità albero di decisione come insieme di sotto-alberi.
 */
public class RegressionTree implements Serializable {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * Radice del sotto-albero corrente
	 */
	private Node root;
	
	/**
	 * Array di sotto-alberi originanti nel nodo root: vi è un elemento nell'array per ogni figlio del nodo
	 */
	private RegressionTree childTree[];
	
	/**
	 * Istanzia un nuovo albero di regressione.
	 * @param trainingSet TrainingSet a partire dal quale generare l'albero.
	 */
	public RegressionTree(Data trainingSet) {
		learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1, trainingSet.getNumberOfExamples() * 10 / 100);
	}

	/**
	 * Istanzia un albero vuoto
	 */
	public RegressionTree() {
	}

	/**
	 * Genera un sotto-albero con il sotto-insieme di input ([begin, end]) istanziando un nodo
	 * fogliare (isLeaf()) o un nodo di split. Nell'ultimo caso determina il miglior nodo rispetto al sotto-insieme di
	 * input (determineBestSplitNode()), ed a tale nodo esso associa un sotto-albero avente come radice il nodo
	 * medesimo (root) e avente un numero di rami pari al numero dei figli determinati dallo split (childTree[]).
	 * Ricorsivamente per ogni oggetto DecisionTree in childTree[] sarà re-invocato il metodo
	 * learnTree() per l'apprendimento su un insieme ridotto del sotto-insieme attuale (begin... end). Nella
	 * condizione in cui il nodo di split non origina figli, il nodo diventa fogliare.
	 * @param trainingSet Training set complessivo.
	 * @param begin Indice nel training set del primo esempio coperto.
	 * @param end Indice nel training set dell'ultimo esempio coperto.
	 * @param numberOfExamplesPerLeaf numero max che una foglia deve contenere.
	 */
	private void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if(isLeaf(begin, end, numberOfExamplesPerLeaf))
			//determina la classe che compare più frequentemente nella partizione corrente
			root = new LeafNode(trainingSet, begin, end);
		else /*split node*/{
			root = determineBestSplitNode(trainingSet, begin, end);
			if(root.getNumberOfChildren() > 1){
				childTree = new RegressionTree[root.getNumberOfChildren()];
				for(int i = 0; i < root.getNumberOfChildren(); i++){
					childTree[i] = new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).getBeginindex(), ((SplitNode)root).getSplitInfo(i).getEndIndex(), numberOfExamplesPerLeaf);
				}
			}
			else root = new LeafNode(trainingSet, begin, end);
		}
	}

	/**
	 * Verifica se il sotto-insieme corrente può essere coperto da un nodo foglia
	 * controllando che il numero di esempi del training set compresi tra begin e end sia minore uguale di
	 * numberOfExamplesPerLeaf.
	 * @param begin Indice nel training set del primo esempio coperto.
	 * @param end Indice nel training set dell'ultimo esempio coperto.
	 * @param numberOfExamplesPerLeaf numero max che una foglia deve contenere.
	 * @return Esito sulle condizioni per i nodi fogliari.
	 */
	private boolean isLeaf(int begin, int end, int numberOfExamplesPerLeaf) {
		return (end - begin + 1) <= numberOfExamplesPerLeaf;
	}

	/**
	 * Per ciascun attributo indipendente istanzia lo SplitNode associato e seleziona il
	 * nodo di split con minore varianza tra gli SplitNode istanziati. Restituisce il nodo selezionato.
	 * @param trainingSet Training set complessivo.
	 * @param begin Indice nel training set del primo esempio coperto.
	 * @param end Indice nel training set dell'ultimo esempio coperto.
	 * @return Nodo di split migliore per il sotto-insieme di training.
	 */
	private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
		TreeSet <SplitNode> ts = new TreeSet<SplitNode>();
		SplitNode currentNode;

		for(int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
			Attribute a = trainingSet.getExplanatoryAttribute(i);
			if(a instanceof DiscreteAttribute) {
				DiscreteAttribute attribute = (DiscreteAttribute) trainingSet.getExplanatoryAttribute(i);
				currentNode = new DiscreteNode(trainingSet, begin, end, attribute);
			}
			else{
				ContinuousAttribute attribute = (ContinuousAttribute) trainingSet.getExplanatoryAttribute(i);
				currentNode = new ContinuousNode(trainingSet, begin, end, attribute);
			}

			ts.add(currentNode);
		}

		trainingSet.sort(ts.first().getAttribute(), begin, end);

		return ts.first();
	}

	/**
	 * Stampa le informazioni dell'intero albero (compresa una intestazione).
	 */
	public void printTree(){
		System.out.println("********* TREE **********");
		System.out.println(toString());
		System.out.println("*************************\n");
	}
	
	/**
	 * Restituisce la stringa generata dal metodo toString() + un intestazione
	 * @return Stringa con informazioni riguardanti la struttura dell'albero.
	 */
	public String getTree(){
		String tree = "********* TREE **********\n";
		tree += toString() + "\n";
		tree += "*************************\n";
		
		return tree;
	}

	/**
	 * Concatena in una String tutte le informazioni di root-childTree[] correnti
	 * invocando i relativi metodo toString(): nel caso il root corrente è di split vengono concatenate anche
	 * le informazioni dei rami.
	 */
	public String toString(){
		String tree = root.toString() + "\n";
		
		if(root instanceof LeafNode) {

		}
		else //split node
			for (RegressionTree regressionTree : childTree)
				tree += regressionTree;

		return tree;
	}

	/**
	 * Scandisce ciascun ramo dell'albero completo dalla radice alla foglia concatenando
	 * le informazioni dei nodi di split fino al nodo foglia. In particolare per ogni sotto-albero (oggetto
	 * DecisionTree) in childTree[] concatena le informazioni del nodo root: se è di split discende
	 * ricorsivamente l'albero per ottenere le informazioni del nodo sottostante (necessario per
	 * ricostruire le condizioni in AND) di ogni ramo-regola, se è di foglia (leaf) termina
	 * l'attraversamento restiuendo la regola. 
	 * @return Una stringa che descrive le regole relative a tutto l'albero di split.
	 */
	private String generateRules() {
		String rules = "********* RULES **********\n";
		
		if(root instanceof LeafNode){

		}
		else {
			Attribute a = ((SplitNode) root).getAttribute();

			if(a instanceof DiscreteAttribute){
				Iterator<Object> itr = ((DiscreteAttribute)((SplitNode) root).getAttribute()).iterator();
				for (RegressionTree regressionTree : childTree) {
					rules = regressionTree.generateRules(a.getName() + "= " + itr.next(), rules);
				}
			}
			else if(a instanceof ContinuousAttribute){
				int i = 0;
				for (RegressionTree regressionTree : childTree) {
					rules = regressionTree.generateRules(a.getName() + " " + ((SplitNode) root).getSplitInfo(i).getComparator() + " " + ((SplitNode) root).getSplitInfo(i).getSplitValue(), rules);
					i++;
				}
			}
		}
		
		return rules;
	}
	
	/**
	 * Concatena alle informazioni in current del precedente nodo 
	 * quelle del nodo root del corrente sotto-albero (oggetto DecisionTree): se il nodo corrente
	 * è di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente,
	 * se è fogliare (leaf) restituisce tutte le informazioni concatenate.
	 * @param current Informazioni del nodo di split del sotto-albero al livello superiore.
	 * @param currentRules Regole reperite fino al momento dell'esecuzione di questo metodo.
	 * @return Una stringa con tutte le regole relative all'albero di regressione corrente. 
	 */
	private String generateRules(String current, String currentRules) {
		if(root instanceof LeafNode)
			currentRules += current + " ==> Class= " + ((LeafNode) root).getPredictedClassValue() + "\n";
		else {
			Attribute currentAttribute = ((SplitNode) root).getAttribute();

			if(currentAttribute instanceof  DiscreteAttribute) {
				Iterator<Object> itr = ((DiscreteAttribute) currentAttribute).iterator();

				for (RegressionTree regressionTree : childTree) {
					String x = current;
					current = x + " AND " + currentAttribute.getName() + "= " + itr.next();
					currentRules = regressionTree.generateRules(current, currentRules);
					current = x;
				}
			}
			else if(currentAttribute instanceof ContinuousAttribute){
				int i = 0;
				for (RegressionTree regressionTree : childTree) {
					String x = current;
					current = x + " AND " + currentAttribute.getName() + " " + ((SplitNode) root).getSplitInfo(i).getComparator() + " " + ((SplitNode) root).getSplitInfo(i).getSplitValue();
					i++;
					currentRules = regressionTree.generateRules(current, currentRules);
					current = x;
				}
			}
		}
		
		return currentRules;
	}
	
	/**
	 * Restituisce una stringa con tutte le regole relative al corrente albero di regressione.
	 * @return Stringa con tutte le regole relative al corrente albero di regressione.
	 */
	public String getRules() {
		return generateRules();
	}
	
	/**
	 * Visualizza le informazioni di ciascuno split dell'albero (SplitNode.formulateQuery()) 
	 * e per il corrispondente attributo acquisisce il valore dell'esempio da predire dallo stream in. 
	 * Se il nodo root corrente è leaf termina l'acquisizione e scrive sullo stream out la predizione
	 * per l’attributo classe, altrimenti invoca ricorsivamente sul figlio di root in childTree
	 * individuato dal valore acquisito dallo stream in.
	 * @param in Stream da cui leggere la scelta dell'utente.
	 * @param out Stream su cui scrivere la relativa predizione.
	 * @throws UnknownValueException
	 */
	public void predictClass(ObjectInputStream in, ObjectOutputStream out) throws UnknownValueException{
		try {
			if(root instanceof LeafNode) {
				out.writeObject("OK");
				out.writeObject(((LeafNode) root).getPredictedClassValue());
			}
			else {
				out.writeObject("QUERY");
				int risp = -1;
				out.writeObject(((SplitNode)root).formulateQuery());
				risp = (int) in.readObject();
				
				if(risp <= -2 || risp >= root.getNumberOfChildren()) {
					throw new UnknownValueException("The answer should be an integer between 0 and " + (root.getNumberOfChildren() - 1) + "!");
				}
				else if(risp == -1) {
					out.writeObject("Exit form prediction phase...");
				}
				else
					childTree[risp].predictClass(in, out);
			}
		}catch (IOException | ClassNotFoundException e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Serializza l'albero in un file.
	 * @param nomeFile Nome del file in cui salvare l'albero
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void salva(String nomeFile) throws FileNotFoundException, IOException {
		FileOutputStream outFile = new FileOutputStream(nomeFile);
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);

		outStream.writeObject(this);

		outFile.close();
		outStream.close();
	}

	/**
	 * Carica un albero di regressione salvato in un file.
	 * @param nomeFile Nome del file in cui è salvato l'albero.
	 * @return L'albero contenuto nel file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static RegressionTree carica(String nomeFile) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream inFile = new FileInputStream(nomeFile);
		ObjectInputStream inStream = new ObjectInputStream(inFile);

		RegressionTree r = (RegressionTree) inStream.readObject();
		
		inFile.close();
		inStream.close();

		return r;
	}
}