/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.negocio;

/**
 * Clase para modelar el concepto BAR del negocio de los Parranderos
 *
 * @author Germán Bravo
 */
public class LoteVacuna implements VOLoteVacuna
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long id;
	
	/**
	 * El nombre del bar
	 */
	private long cantidad;

	private long ideps;

	private long idpuntov;



	/**
	 * El número de sedes del bar en la ciudad
	 */

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public LoteVacuna() 
    {
    	this.id = 0;
    	this.cantidad = 0;
    	this.ideps = 0;
    	this.idpuntov = 0;


	}



	/**
	 * Constructor con valores
	 * @param id - El id del bart
	 * @param nombre - El nombre del bar
	 * @param ciudad - La ciudad del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param cantSedes - Las sedes del bar (Mayor que 0)
	 */
	public LoteVacuna(long id, long cantidad, long ideps, long idpuntov) {
		this.id = id;
		this.cantidad = cantidad;
		this.ideps = ideps;
		this.idpuntov = idpuntov;
	}

    /**
	 * @return El id del bar
	 */
	public long getId() 
	{
		return id;
	}
	
	/**
	 * @param id - El nuevo id del bar
	 */
	public void setId(long id) 
	{
		this.id = id;
	}
	
	public long getCantidad() {
		return cantidad;
	}



	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}



	public long getIdeps() {
		return ideps;
	}



	public void setIdeps(long ideps) {
		this.ideps = ideps;
	}



	public long getIdpuntov() {
		return idpuntov;
	}



	public void setIdpuntov(long idpuntov) {
		this.idpuntov = idpuntov;
	}



	/**
	 * @return el nombre del bar
	 */


	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString() 
	{
		return "Vacuna [id=" + id + ","+cantidad+ideps+idpuntov + "]";
	}



}
