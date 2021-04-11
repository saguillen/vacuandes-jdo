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

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase para modelar el concepto BEBEDOR del negocio de los Parranderos
 *
 * @author Germán Bravo
 */
public class Ciudadano implements VOCiudadano
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del bebedor
	 */
	private long id;	
	
	
	private long edad;
	
	private String ocupacion;
	
	private long enfermedad;
	
	private String grupo;

	
	private long etapa;

	private String estado;
	
	private long idusuario;

	private long idpuntov;


	



	/**
	 * El nombre del bebedor
	
	

	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public Ciudadano() {
		this.id = 0;
		this.edad = 0;
		this.ocupacion = "";
		this.enfermedad = 0;
		this.grupo = "";
		this.etapa = 0;
		this.estado = "";
		this.idusuario = 0;
		this.idpuntov = 0;
	}

	/**
	 * Constructor con valores
	 * @param id - El id del bebedor
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 */

	public Ciudadano(long id, long edad, String ocupacion, long enfermedad, String grupo, long etapa, String estado,
			long idusuario, long idpuntov) {
		this.id = id;
		this.edad = edad;
		this.ocupacion = ocupacion;
		this.enfermedad = enfermedad;
		this.grupo = grupo;
		this.etapa = etapa;
		this.estado = estado;
		this.idusuario = idusuario;
		this.idpuntov = idpuntov;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getEdad() {
		return edad;
	}



	public void setEdad(long edad) {
		this.edad = edad;
	}



	public String getOcupacion() {
		return ocupacion;
	}



	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}



	public long getEnfermedad() {
		return enfermedad;
	}



	public void setEnfermedad(long enfermedad) {
		this.enfermedad = enfermedad;
	}



	public String getGrupo() {
		return grupo;
	}



	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}



	public long getEtapa() {
		return etapa;
	}



	public void setEtapa(long etapa) {
		this.etapa = etapa;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public long getIdusuario() {
		return idusuario;
	}



	public void setIdusuario(long idusuario) {
		this.idusuario = idusuario;
	}



	public long getIdpuntov() {
		return idpuntov;
	}



	public void setIdpuntov(long idpuntov) {
		this.idpuntov = idpuntov;
	}



	/**
	 * @return Una cadena de caracteres con la información básica del bebedor
	 */
	@Override
	public String toString() 
	{
		return "Ciudadano [id=" + edad + ", nombre=" + ocupacion + ", ciudad=" + enfermedad + ", presupuesto=" +", grupo: "+ grupo + ", etapa: "+etapa+", estado: "+estado+"idUsuario: "+idusuario+"idPuntov: "+idpuntov+"]";
	}

}