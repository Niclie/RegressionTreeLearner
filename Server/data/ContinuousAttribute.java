package data;

/**
 * Estende la classe Attribute e rappresenta un attributo continuo.
 */
public class ContinuousAttribute extends Attribute {
	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Invoca il costruttore della super-classe.
	 * @param name Nome simbolico dell'attributo.
	 * @param index Identificativo numerico dell'attributo.
	 */
	public ContinuousAttribute(String name, int index){
		super(name, index);
	}
	
	public String toString() {
		return super.toString();
	}

}
