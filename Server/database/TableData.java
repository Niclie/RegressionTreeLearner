package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Modella l’insieme di righe (Ogni singola riga è rappresentata dalla classe Example) collezionate in una tabella.
 */
public class TableData {
	/**
	 * Permette l'accesso alla base di dati.
	 */
	private DbAccess db;
	
	/**
	 * Costruttore di classe, avvalora db.
	 * @param db Riferimento da associare a db.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione
	 * per estrarre le tuple distinte da tale tabella. Per ogni tupla del resultset, si crea un oggetto,
	 * istanza della classe Example, il cui riferimento va incluso nella lista da restituire. In particolare,
	 * per la tupla corrente nel resultset, si estraggono i valori dei singoli campi (usando getFloat() o
	 * getString()), e li si aggiungono all’oggetto istanza della classe Example che si sta costruendo.
	 * @param table Nome della tabella nel database.
	 * @return Lista di tuple memorizzate nella tabella.
	 * @throws SQLException
	 * @throws EmptySetException
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db, table);
		String query = "select ";
		
		for(int i = 0; i < tSchema.getNumberOfAttributes(); i++){
			Column c = tSchema.getColumn(i);
			if(i > 0)
				query += ",";
			query += c.getColumnName();
		}
		
		if(tSchema.getNumberOfAttributes() == 0)
			throw new SQLException();
		
		query += (" FROM " + table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty = true;
		while (rs.next()) {
			empty = false;
			
			Example currentTuple = new Example();
			for(int i = 0; i < tSchema.getNumberOfAttributes(); i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i + 1));
				else
					currentTuple.add(rs.getString(i + 1));
			
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		
		if(empty)
			throw new EmptySetException("EmptySetException: set vuoto");
		
		
		return transSet;
	}
	
	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di column e popolare
	 * un insieme da restituire.
	 * @param table Nome della tabella nel database.
	 * @param column Nome della colonna nella tabella.
	 * @return Insieme di valori distinti ordinati in modalità ascendente che l’attributo identificato da nome column assume nella tabella identificata dal nome table.
	 * @throws SQLException
	 * @throws DatabaseConnectionException
	 */
	public Set<Object> getDistinctColumnValues (String table, Column column) throws SQLException, DatabaseConnectionException {
		Statement s = db.getConnection().createStatement();
		
		String query = "SELECT " + column.getColumnName() + " FROM " + table + ";";
		ResultSet r = s.executeQuery(query);		
		
		Set<Object> columnValues = new TreeSet<Object>();
		while(r.next())
			columnValues.add(r.getObject(1));
		
		
		return columnValues;
	}
}
