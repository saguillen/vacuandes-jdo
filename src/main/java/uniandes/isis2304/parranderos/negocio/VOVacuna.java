package uniandes.isis2304.parranderos.negocio;

public interface VOVacuna {

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El id del bar
	 */
	public long getId();
	
	/**
	 * @return el nombre del bar
	 */
	public String getFabricante();
	

	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString();

}
