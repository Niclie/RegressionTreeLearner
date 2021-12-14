package data;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import database.*;

/**
 * Modella l'insieme di esempi di training a partire da una tabella presente nel database contenente i dati di addestramento.
 */
public class Data implements Serializable {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contiene il TrainingSet. E' una lista di Example in cui ogni elemento rappresenta una riga della tabella.
	 */
	private List<Example> data = new ArrayList<Example>();
	
	/**
	 * Numero di elementi di data (Numero di righe della tabella).
	 */
	private final int numberOfExamples;
	
	/*
	 * Lista degli attributi indipendenti.
	 */
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();
	
	/**
	 * Oggetto per modellare l'attributo di classe. L'attributo di classe è numerico.
	 */
	private ContinuousAttribute classAttribute;
	
	/**
	 * Costruttore di classe. Si occupa di stabilire la connessione con la base di dati
	 * e di acquisire explanatorySet, classAttribute e numberOfExamples.
	 * @param tableName Nome della tabella da acquisire.
	 * @throws TrainingDataException Viene lanciata questo tipo di eccezione nel caso in cui la tabella non rispetti alcuni parametri, in particolare:
	 * la tabella denominata tableName non esiste all'interno del database, il numero di colonne è minore di 3, l'ultimo attributo (l'attributo di classe)
	 * non è numerico.
	 */
	public Data(String tableName) throws TrainingDataException {
		try {
			DbAccess dbAccess = new DbAccess();
			dbAccess.initConnection();
			
			/*--- Controlla che la tabella denominata tableName esista all'interno del database ---*/
			DatabaseMetaData dbm = dbAccess.getConnection().getMetaData();
			ResultSet tables = dbm.getTables(null, null, tableName, null);
			if (!tables.next())
				throw new TrainingDataException("La tabella denominata '" + tableName + "' non esiste");
			/*---  ---*/
			
			TableSchema tableSchema = new TableSchema(dbAccess, tableName);
			TableData tableData = new TableData(dbAccess);
			int numberOfAttributes = tableSchema.getNumberOfAttributes();
			
			/*--- Controlla che la tabella abbia almeno 3 colonne ---*/
			if(numberOfAttributes < 3)
				throw new TrainingDataException("La tabella ha meno di 3 colonne");
			/*---  ---*/
			
			/*--- Controlla che l'attributo corrispondente all'ultima colonna sia numerico ---*/
			if(!tableSchema.getColumn(numberOfAttributes - 1).isNumber())
				throw new TrainingDataException("L'attributo corrispondente all'ultima colonna non è numerico");
			/*--- ---*/
			
			for(int i = 0; i < numberOfAttributes - 1; i++) {
				if(tableSchema.getColumn(i).isNumber())
					explanatorySet.add(new ContinuousAttribute(tableSchema.getColumn(i).getColumnName(), i));
				else
					explanatorySet.add(new DiscreteAttribute(tableSchema.getColumn(i).getColumnName(), i, tableData.getDistinctColumnValues(tableName, tableSchema.getColumn(i))));
			}
			
			classAttribute = new ContinuousAttribute(tableSchema.getColumn(numberOfAttributes - 1).getColumnName(), numberOfAttributes - 1);
			
			data = tableData.getTransazioni(tableName);
			numberOfExamples = data.size();
			
			dbAccess.closeConnection();
		} 
		catch(DatabaseConnectionException e){
			throw new TrainingDataException("Si è verificata un eccezione durante la connessione al databse");
		} 
		catch(SQLException e){
			throw new TrainingDataException("Si è verificata una SQLException");
		} 
		catch(EmptySetException e){
			throw new TrainingDataException("La tabella '" + tableName + "' è vuota");
		}	
	}
	
	/**
	 * Restituisce il valore numberOfExamples
	 * @return La cardinalità dell'insieme di esempi.
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}
	
	/**
	 * Restituisce la dimensione di explanatorySet.
	 * @return Cardinalità degli attributi indipendenti.
	 */
	public int getNumberOfExplanatoryAttributes(){
		return explanatorySet.size();
	}
	
	/**
	 * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex.
	 * @param exampleIndex Indice per uno specifico esempio.
	 * @return Valore dell'attributo di classe per l'esempio indicizzato in input.
	 */
	public Double getClassValue(int exampleIndex){
		return (Double) (data.get(exampleIndex)).get(explanatorySet.size());
	}
	
	/**
	 * Restituisce il valore dell'attributo indicizzato da attributeIndex per l'esempio exampleIndex.
	 * @param exampleIndex Indice per uno specifico esempio.
	 * @param attributeIndex Indice per uno specifico Attributo.
	 * @return Object associato all'attributo indipendente per l'esempio indicizzato in input.
	 */
	public Object getExplanatoryValue(int exampleIndex, int attributeIndex){
		return (data.get(exampleIndex)).get(attributeIndex);
	}
	
	/**
	 * Restituisce l'attributo indicizzato da index in explanatorySet.
	 * @param index Indice in explanatorySet per uno specifico attributo indipendente.
	 * @return Attribute indicizzato da index.
	 */
	public Attribute getExplanatoryAttribute(int index){
		return explanatorySet.get(index);
	}
	
	/**
	 * Restituisce l'oggetto corrispondente all'attributo di classe.
	 * @return ContinuousAttribute associato al membro classAttribute.
	 */
	public ContinuousAttribute getClassAttribute(){
		return this.classAttribute;
	}
	
	/**
	 * Legge i valori di tutti gli attributi per ogni esempio da data e li concatena
	 * in un oggetto String che restituisce come risultato finale in forma di sequenza di testi.
	 */
	public String toString(){
		String value="";
		
		for(int i = 0; i < numberOfExamples; i++){
			for(int j = 0; j < explanatorySet.size(); j++)
				value += (data.get(i)).get(j) + ",";
			
			value += (data.get(i)).get(explanatorySet.size());
		}
		
		return value;
	}

	/**
	 * Ordina il sottoinsieme di esempi compresi nell'intervallo [inf,sup] in data rispetto
	 * all'Attribute attribute. Usa l'algoritmo quicksort per l'ordinamento usando come relazione d'ordine totale minore o uguale.
	 * @param attribute Attributo in base al quale ordinare data.
	 * @param inf Indice di inizio.
	 * @param sup Indice di fine.
	 */
	public void sort(Attribute attribute, int inf, int sup){
		if(sup >= inf){
			int pos;
			if(attribute instanceof DiscreteAttribute)
				pos = partition((DiscreteAttribute) attribute, inf, sup);
			else
				pos = partition((ContinuousAttribute) attribute, inf, sup);

			if ((pos - inf) < (sup - pos + 1)) {
				sort(attribute, inf, pos - 1);
				sort(attribute, pos + 1, sup);
			}
			else {
				sort(attribute, pos + 1, sup);
				sort(attribute, inf, pos - 1);
			}
		}
	}
	
	/**
	 * Partiziona il vettore rispetto al DiscreteAttribute attribute e restiutisce il punto di separazione.
	 * @param attribute Attributo in base al quale ordinare data.
	 * @param inf Indice in data.
	 * @param sup Indice in data.
	 * @return Indice in data, punto di separazione.
	 */
	private int partition(DiscreteAttribute attribute, int inf, int sup){
		int i, j;
		i = inf;
		j = sup;
		int	med = (inf + sup) / 2;
		String x = (String) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);
		
		while(true){
			while(i <= sup && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0)
				i++;

			while(((String)getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0)
				j--;

			if(i < j)
				swap(i, j);
			else
				break;
		}
		swap(inf, j);
		
		return j;
	}

	/**
	 * Partiziona il vettore rispetto al ContinuousAttribute attribute e restiutisce il punto di separazione
	 * @param attribute Attributo in base al quale ordinare data.
	 * @param inf Indice in data.
	 * @param sup Indice in data.
	 * @return Indice in data punto di separazione.
	 */
	private int partition(ContinuousAttribute attribute, int inf, int sup){
		int i, j;
		i = inf;
		j = sup;
		int	med = (inf + sup) / 2;
		Double x = (Double) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);

		while (true){
			while(i <= sup && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0)
				i++;

			while(((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0)
				j--;

			if(i < j)
				swap(i, j);
			else 
				break;
		}
		swap(inf, j);
		
		return j;
	}
	
	/**
	 * Scambia l'esempio i con esempio j.
	 * @param i Indice in data.
	 * @param j Indice in data.
	 */
	private void swap(int i, int j){
		Example temp;
		
		for(int k = 0; k < getNumberOfExplanatoryAttributes() + 1; k++){
			temp = data.get(i);
			data.set(i, data.get(j));
			data.set(j, temp);
		}
	}
}
