package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Modella (con la classe Column) lo schema di una tabella nel database relazionale.
 */
public class TableSchema implements Iterable<Column>{
	/**
	 * Lista che rappresenta le colonne del db.
	 */
	private List<Column> tableSchema = new ArrayList<Column>();
	
	/**
	 * Legge dal db le colonne e le inserice in tableSchema.
	 * @param db Permette l'accesso al database.
	 * @param tableName Nome della tabella di riferimento.
	 * @throws SQLException
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
	    ResultSet res = meta.getColumns(null, null, tableName, null);
		   
	     while (res.next())
	    	 if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	    		 tableSchema.add(new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
	     
	     res.close();
	}
	
	/**
	 * Restituisce il numero di colonne del db
	 * @return Numero di colonne del db.
	 */
	public int getNumberOfAttributes(){
		return tableSchema.size();
	}
		
	/**
	 * Dato l'indice di una colonna restituisce la relativa rappresentazione come istanza della classe Columm 
	 * @param index Posizione della colonna nel db,
	 * @return Oggetto istanza di Column che rappresenta la colonna index.
	 */
	public Column getColumn(int index){
		return tableSchema.get(index);
	}


	@Override
	public Iterator<Column> iterator() {
		return tableSchema.iterator();	
	}
	
}

		     


