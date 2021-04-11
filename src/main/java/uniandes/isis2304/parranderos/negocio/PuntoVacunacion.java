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
public class PuntoVacunacion implements VOPuntoVacunacion
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long id;
	
	/**

	 */
	private long capacidad;
	
	/**
	 * El presupuesto del bar (ALTO, MEDIO, BAJO)
	 */
	private String localizacion;
	
	private String infraestructura;

	private long idEps;
	
	private long idAdmin;

	private long idOperador;


	/**
	 * El número de sedes del bar en la ciudad
	 */

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public PuntoVacunacion() 
    {
    	this.id = 0;
		this.capacidad = 0;
		this.localizacion = "";
		this.infraestructura = "";
		this.idAdmin = 0;
		this.idEps = 0;
		this.idOperador = 0;

	}

	/**
	 * Constructor con valores
	 * @param id - El id del bart
	 * @param nombre - El nombre del bar
	 * @param ciudad - La ciudad del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param cantSedes - Las sedes del bar (Mayor que 0)
	 */

    public PuntoVacunacion(long id, long capacidad, String localizacion, String infraestructura, long idEps,
			long idAdmin, long idOperador) {
		this.id = id;
		this.capacidad = capacidad;
		this.localizacion = localizacion;
		this.infraestructura = infraestructura;
		this.idEps = idEps;
		this.idAdmin = idAdmin;
		this.idOperador = idOperador;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(long capacidad) {
		this.capacidad = capacidad;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getInfraestructura() {
		return infraestructura;
	}

	public void setInfraestructura(String infraestructura) {
		this.infraestructura = infraestructura;
	}

	public long getIdEps() {
		return idEps;
	}

	public void setIdEps(long idEps) {
		this.idEps = idEps;
	}

	public long getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin(long idAdmin) {
		this.idAdmin = idAdmin;
	}

	public long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(long idOperador) {
		this.idOperador = idOperador;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString() 
	{
		return "Punto de Vacunacion [id=" + id + "," + capacidad+ ","+ localizacion+","+infraestructura+","+idEps+","+idAdmin+","+idOperador+"]";
	}
	

}
