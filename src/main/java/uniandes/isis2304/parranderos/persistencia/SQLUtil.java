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

package uniandes.isis2304.parranderos.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLUtil
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaParranderos.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaParranderos pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaParranderos pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqVacuandes()+ ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarParranderos (PersistenceManager pm)
	{
        Query qCiudadano = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCiudadano());          
        Query qEPSRegional = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEPSRegional());
        Query qLoteVacuna = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLoteVacuna());
        Query qPuntoVacunacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacion());
        Query qUsuario = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario());
        Query qVacuna = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVacuna());
        Query qVacunacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVacunacion());

        long CiudadanosEliminados = (long) qCiudadano.executeUnique ();
        long EPSSEliminados = (long) qEPSRegional.executeUnique ();
        long LotesEliminadas = (long) qLoteVacuna.executeUnique ();
        long PuntosEliminadas = (long) qPuntoVacunacion.executeUnique ();
        long usuariosEliminados = (long) qUsuario.executeUnique ();
        long vacunasEliminados = (long) qVacuna.executeUnique ();
        long vacunacionesEliminados = (long) qVacunacion.executeUnique ();
        return new long[] {CiudadanosEliminados, EPSSEliminados, LotesEliminadas, PuntosEliminadas, 
        		usuariosEliminados, vacunasEliminados, vacunacionesEliminados};
	}

}
