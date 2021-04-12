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

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.Parranderos;
import uniandes.isis2304.parranderos.negocio.VOCiudadano;
import uniandes.isis2304.parranderos.negocio.VOEPSRegional;
import uniandes.isis2304.parranderos.negocio.VOLoteVacuna;
import uniandes.isis2304.parranderos.negocio.VOPuntoVacunacion;
import uniandes.isis2304.parranderos.negocio.VOTipoBebida;
import uniandes.isis2304.parranderos.negocio.VOUsuario;
import uniandes.isis2304.parranderos.negocio.VOVacuna;
import uniandes.isis2304.parranderos.negocio.VOVacunacion;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazParranderosApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazParranderosApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private Parranderos parranderos;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazParranderosApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        parranderos = new Parranderos (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de USUARIO
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarUsuario( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: Nombre, Cedula, Correo, Contraseña, Tipo (CIUDADANO; ADMINISTRADOR;OPERADOR) ", 
    				"Adicionar Usuario", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			String nombre=datos[0];
    			long cedula=Long.parseLong(datos[1]);
    			String correo= datos[2];
    			String contrasenia= datos[3];
    			String tipo = datos[4];
    			
    			
        		VOUsuario tb = parranderos.adicionarUsuario(nombre, cedula, correo, contrasenia,tipo);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear el usuario con cedula: " + cedula);
        		}
        		String resultado = "En adicionarUsuario\n\n";
        		resultado += "Usuario adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarUsuarios( )
    {
    	try 
    	{
			List <VOUsuario> lista = parranderos.darVOUsuario();

			String resultado = "En listarUsuarios";
			resultado +=  "\n" + listarUsuarios (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarUsuarioPorCedula( )
    {
    	try 
    	{
    		String cedulaStr = JOptionPane.showInputDialog (this, "cedula del usuario?", "Borrar usuario por cedula", JOptionPane.QUESTION_MESSAGE);
    		if (cedulaStr != null)
    		{
    			long cednum = Long.valueOf (cedulaStr);
    			long tbEliminados = parranderos.eliminarUsuarioPorCedula (cednum);

    			String resultado = "En eliminar Usuario\n\n";
    			resultado += tbEliminados + " Usuarios eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarTipoBebidaPorNombre( )
    {
    	try 
    	{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del tipo de bedida?", "Buscar tipo de bebida por nombre", JOptionPane.QUESTION_MESSAGE);
    		if (nombreTb != null)
    		{
    			VOTipoBebida tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTb);
    			String resultado = "En buscar Tipo Bebida por nombre\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
   
	/* ****************************************************************
	 * 			CRUD de VACUNA
	 *****************************************************************/
    public void adicionarVacuna( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: fabricante,id del lote", 
    				"Adicionar Vacuna", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			String fabricante=datos[0];
    			long idlote = Long.parseLong(datos[1]);
    			
    			
        		VOVacuna tb = parranderos.adicionarVacuna(fabricante,idlote);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear la vacuna del fabricante: " + fabricante);
        		}
        		String resultado = "En adicionarVacuna\n\n";
        		resultado += "Vacuna adicionada exitosamente :) : " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void listarVacunas( )
    {
    	try 
    	{
			List <VOVacuna> lista = parranderos.darVOVacunas();

			String resultado = "En listarUsuarios";
			resultado +=  "\n" + listarVacunas (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarVacunaPorId( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "id de la vacuna?", "Borrar vacuna por id", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			long cednum = Long.valueOf (id);
    			long tbEliminados = parranderos.eliminarVacunaPorID (cednum);

    			String resultado = "En eliminar Vacuna\n\n";
    			resultado += tbEliminados + " Vacunas eliminadas\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }


    
	/* ****************************************************************
	 * 			CRUD de LOTEVACUNA
	 *****************************************************************/
    public void adicionarLoteVacuna( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: cantidad de vacunas, Id de EPS, id de punto de vacunacion", 
    				"Adicionar LoteVacuna", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			long cantidad = Long.parseLong(datos[0]);
    			long idEPS= Long.parseLong(datos[1]);
    			long idPuntoV= Long.parseLong(datos[2]);
    			
    			
    			
        		VOLoteVacuna tb = parranderos.adicionarLoteVacuna(cantidad,idEPS,idPuntoV);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear el lote de vacuna con cantidad: " + cantidad);
        		}
        		String resultado = "En adicionarLote\n\n";
        		resultado += "Lote adicionada exitosamente :) : " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void listarLoteVacuna( )
    {
    	try 
    	{
			List <VOLoteVacuna> lista = parranderos.darVOLoteVacuna();

			String resultado = "En listarUsuarios";
			resultado +=  "\n" + listarLoteVacuna (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarLoteVacunaPorId( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "id de la vacuna?", "Borrar vacuna por id", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			long cednum = Long.valueOf (id);
    			long tbEliminados = parranderos.eliminarVacunaPorID (cednum);

    			String resultado = "En eliminar Vacuna\n\n";
    			resultado += tbEliminados + " Vacunas eliminadas\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * 			CRUD de CIUDADANO
	 *****************************************************************/
    
//    public void adicionarCiudadano( )
//    {
//    	try 
//    	{
//    		String id = JOptionPane.showInputDialog (this, "Insertar: edad,ocupacion(SALUD/PUBLICO/EXPUESTO/NINGUNO),enfermedadGrave(SI, NO),grupo(PRIMERO,SEGUNDO,TERCERO,CUARTO),etapa(1,2,3,4,5),estado(VACUNADO,NO VACUNADO, NO VACUNABLE),idusuario,idpuntov ", 
//    				"Adicionar CIUDADANo", JOptionPane.QUESTION_MESSAGE);
//    		if (id != null)
//    		{
//    			
//    			String[] datos=id.split(",");
//    			long edad=Long.parseLong(datos[0]);
//    			String ocupacion= datos[1];
//    			long enfermedad = 0;
//    			if(datos[2]=="SI"){
//    			 enfermedad = 1;}
//    			String grupo = datos[3];
//    			long etapa=Long.parseLong(datos[4]);
//    			String estado = datos[5];
//    			long idUsuario=Long.parseLong(datos[6]);
//    			long idPuntoV=Long.parseLong(datos[7]);
//
//    			
//    			
//        		VOCiudadano tb = parranderos.adicionarCiudadano(edad,ocupacion,enfermedad,grupo,etapa,estado,idUsuario,idPuntoV);
//        		if (tb == null)
//        		{
//        			throw new Exception ("No se pudo crear el ciudadano con usuario: " + idUsuario);
//        		}
//        		String resultado = "En adicionarCiudadano\n\n";
//        		resultado += "Ciudadano adicionado exitosamente: " + tb;
//    			resultado += "\n Operación terminada";
//    			panelDatos.actualizarInterfaz(resultado);
//    		}
//    		else
//    		{
//    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
//    		}
//		} 
//    	catch (Exception e) 
//    	{
////			e.printStackTrace();
//			String resultado = generarMensajeError(e);
//			panelDatos.actualizarInterfaz(resultado);
//		}
//    }
    
    public void adicionarCiudadano( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: edad,ocupacion(SALUD/PUBLICO/EXPUESTO/NINGUNO),enfermedadGrave(SI, NO),grupo(PRIMERO,SEGUNDO,TERCERO,CUARTO),idusuario,idpuntov ", 
    				"Adicionar CIUDADANO: La Ocupacion es entre: (SALUD/PUBLICO/EXPUESTO/NINGUNO) ", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			long edad=Long.parseLong(datos[0]);
    			String ocupacion= datos[1];
    			long enfermedad = 0;
    			if(datos[2]=="SI"){
    			 enfermedad = 1;}
    			String grupo = datos[3];
    			long etapa=0;
    			String estado = "NO VACUNADO";
    			long idUsuario=Long.parseLong(datos[4]);
    			long idPuntoV=Long.parseLong(datos[5]);
    		
    			String[] etapasC = new String[5];
    			etapasC= reqCondiciones();
    			
    			if(edad<=15){
    				estado="NO VACUNABLE";
    			}
    			

    			System.out.println(edad);
    			System.out.println(enfermedad);
    			System.out.println(ocupacion);

    			System.out.println((edad>=60&&edad<=79)&&(enfermedad==0)&&(ocupacion.equals("SALUD")));
    			
    			if(edad>=80){
    				etapa=1;
    			}else if((edad>=60&&edad<=79)&&(enfermedad==0)&&(ocupacion.equals("SALUD"))){
    				etapa=2;
    			}else if((edad>16&&edad<59&&enfermedad==0)||(edad>16&&edad<59&&enfermedad==1)||(ocupacion.equals("PUBLICO"))){
    				etapa=3;
    			}else if((edad>16&&enfermedad==0&&ocupacion.equals("EXPUESTO"))){
    				etapa=4;
    			}else if(edad>16&&enfermedad==0&&ocupacion.equals("NINGUNO")){
    				etapa=5;
    			}
    			
    			 


    			//25,PUBLICO,NO,TERCERO,8,64
    			
        		VOCiudadano tb = parranderos.adicionarCiudadano(edad,ocupacion,enfermedad,grupo,etapa,estado,idUsuario,idPuntoV);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear el ciudadano con usuario: " + idUsuario);
        		}
        		String resultado = "En adicionarCiudadano\n\n";
        		resultado += "Ciudadano adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    			
    			registrarAvance("Ciudadano asignado a un punto de vacunacion");
    			
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarCiudadano( )
    {
    	try 
    	{
			List <VOCiudadano> lista = parranderos.darVOCiudadano();

			String resultado = "En listarCiudadano";
			resultado +=  "\n" + listarCiudadano (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarCiudadanoPorCedula( )
    {
    	try 
    	{
    		String cedulaStr = JOptionPane.showInputDialog (this, "cedula del usuario?", "Borrar usuario por cedula", JOptionPane.QUESTION_MESSAGE);
    		if (cedulaStr != null)
    		{
    			long cednum = Long.valueOf (cedulaStr);
    			long tbEliminados = parranderos.eliminarUsuarioPorCedula (cednum);

    			String resultado = "En eliminar Usuario\n\n";
    			resultado += tbEliminados + " Usuarios eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    
	/* ****************************************************************
	 * 			CRUD de VACUNACION
	 *****************************************************************/
    public void adicionarVacunacion( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: fecha,id del punto de vacunacion, id del ciudadano ", 
    				"Adicionar cita de vacunacion(Insertar Fechas en formato yyyy-MM-dd HH:mm))", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			String fechaStr=datos[0];
    			long idpunto=Long.parseLong(datos[1]);
    			long idciudadano=Long.parseLong(datos[2]);

    			final String FORMAT = "yyyy-MM-dd HH:mm";
    			DateFormat formatter = new SimpleDateFormat(FORMAT);
    			
                Date fecha = formatter.parse(fechaStr);
                Timestamp  ts1 = new Timestamp(fecha.getTime());

        		VOVacunacion tb = parranderos.adicionarVacunacion(ts1, idpunto, idciudadano);
        		
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear la cita de vacunacion: " + fechaStr );
        		}
        		String resultado = "En adicionarVacunacion\n\n";
        		resultado += "Vacunacion adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    			
    			registrarAvance("Ciudadano asignado a una cita de vacunacion: "+fechaStr);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarVacunacion( )
    {
    	try 
    	{
			List <VOVacunacion> lista = parranderos.darVOVacunacion();

			String resultado = "En listarvacunacion";
			resultado +=  "\n" + listarVacunacion (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarVacunacionPorId( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "id de la Vacunacion?", "Borrar vacunacion por id", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			long idVac = Long.valueOf (id);
    			long tbEliminados = parranderos.eliminarVacunacionPorId (idVac);

    			String resultado = "En Eliminar Vacunacion\n\n";
    			resultado += tbEliminados + " Vacunaciones eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * 			CRUD de PUNTOVACUNACION
	 *****************************************************************/
    public void adicionarPuntoVacunacion( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: capacidad de vacunacion,localizacion,infraestructura,id de EPS, id de admin, id del operador", 
    				"Adicionar PuntoVacunacion", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			long capacidad = Long.parseLong(datos[0]);
    			String localizacion=datos[1];
    			String infraestructura=datos[2];
    			long idEps = Long.parseLong(datos[3]);
    			long idAdmin = Long.parseLong(datos[4]);
    			long idOperador = Long.parseLong(datos[5]);

        		VOPuntoVacunacion tb = parranderos.adicionarPuntoVacunacion(capacidad,localizacion,infraestructura,idEps,idAdmin,idOperador);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear el punto con locacion en: " + localizacion);
        		}
        		String resultado = "En adicionarPuntoVacunacion\n\n";
        		resultado += "Punto vacunacion adicionada exitosamente :) : " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void listarPuntoVacunacion( )
    {
    	try 
    	{
			List <VOPuntoVacunacion> lista = parranderos.darVOPuntoVacunacion();

			String resultado = "En listarPuntos";
			resultado +=  "\n" + listarPuntoVacunacion (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void eliminarPuntoVacunacionPorId( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "id del Punto de Vacunacion?", "Borrar Punto por id", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			long idPunto = Long.valueOf (id);
    			long tbEliminados = parranderos.eliminarPuntoVacunacionPorId (idPunto);

    			String resultado = "En Eliminar punto\n\n";
    			resultado += tbEliminados + " Puntos de Vacunacion eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    
	/* ****************************************************************
	 * 			CRUD de EPSRegional
	 *****************************************************************/
    public void adicionarEPSRegional( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Insertar: cantidad dosis totales,region,nombre, id del administrador", 
    				"Adicionar EPSRegional", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			
    			String[] datos=id.split(",");
    			long dosis = Long.parseLong(datos[0]);
    			String region=datos[1];
    			String nombre=datos[2];
    			long idadmin = Long.parseLong(datos[3]);
    			
        		VOEPSRegional tb = parranderos.adicionarEPSRegional(dosis,region,nombre,idadmin);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear la eps con nombre: " + nombre);
        		}
        		String resultado = "En adicionarEPSRegional\n\n";
        		resultado += "EPS adicionada exitosamente :) : " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void listarEPSRegional( )
    {
    	try 
    	{
			List <VOEPSRegional> lista = parranderos.darVOEPSRegional();

			String resultado = "En listarUsuarios";
			resultado +=  "\n" + listarEPSRegional (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void eliminarEPSPorId( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "id de la EPS?", "Borrar EPS por id", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
    			long ideps = Long.valueOf (id);
    			long tbEliminados = parranderos.eliminarEPSRegionalPorId (ideps);

    			String resultado = "En eliminar EPS\n\n";
    			resultado += tbEliminados + " EPS eliminadas\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    
	/* ****************************************************************
	 * 			REQS
	 *****************************************************************/
    public  String[] reqCondiciones()
    {
		String[] etapas = new String[5];

    	try{
    	String c1 = JOptionPane.showInputDialog (this, "Insertar condiciones etapa 1: ", 
				"Adicionar condicion (edad>#,enfermedadGrave==SI/NO,ocupacion==SALUD/PUBLICO/EXPUESTO/NINGUNO)", JOptionPane.QUESTION_MESSAGE);
    	//edad>=80,NO,NINGUNO
    	String c2 = JOptionPane.showInputDialog (this, "Insertar condiciones etapa 2: ", 
				"Adicionar condicion (edad>#,enfermedadGrave==SI/NO,ocupacion==SALUD/PUBLICO/EXPUESTO/NINGUNO)", JOptionPane.QUESTION_MESSAGE);
    	//edad>=60&&edad<=79,enfermedad==NO,ocupacion==SALUD
    	String c3 = JOptionPane.showInputDialog (this, "Insertar condiciones etapa 3: ", 
				"Adicionar condicion (edad>#,enfermedad==SI/NO,ocupacion==SALUD/PUBLICO/EXPUESTO/NINGUNO)", JOptionPane.QUESTION_MESSAGE);
    	//edad>16&&edad<59,enfermedad==SI,ocupacion==publico
    	String c4 = JOptionPane.showInputDialog (this, "Insertar condiciones etapa 4: ", 
				"Adicionar condicion (edad>#,enfermedadGrave==SI/NO,ocupacion==SALUD/PUBLICO/EXPUESTO/NINGUNO)", JOptionPane.QUESTION_MESSAGE);
    	//edad>16,enfermedad==NO,ocupacion==EXPUESTO
    	String c5 = JOptionPane.showInputDialog (this, "Insertar condiciones etapa 5: ", 
				"Adicionar condicion (edad>#,enfermedadGrave==SI/NO,ocupacion==SALUD/PUBLICO/EXPUESTO/NINGUNO)", JOptionPane.QUESTION_MESSAGE);
    	//edad>16,enfermedad==NO,ocupacion==NINGUNO
    	

    	
    	if (c1 != null||c2 != null||c3 != null||c4 != null||c5 != null)
		{
			
			String[] datos=c1.split(",");
			String condc11=datos[0].trim();
			String condc12=datos[1].trim();
			String condc13=datos[2].trim();
			
			etapas[0] = condc11+"&&"+condc12+"&&"+condc13; 
			System.out.println(etapas[0]);
			String[] datos2=c2.split(",");
			String condc21=datos2[0].trim();
			String condc22=datos2[1].trim();
			String condc23=datos2[2].trim();
			

			etapas[1] = condc21+"&&"+condc22+"&&"+condc23; 

			String[] datos3=c3.split(",");
			String condc31=datos3[0].trim();
			String condc32=datos3[1].trim();
			String condc33=datos3[2].trim();
			

			etapas[2] = condc31+"&&"+condc32+"&&"+condc33; 
			
			String[] datos4=c4.split(",");
			String condc41=datos4[0].trim();
			String condc42=datos4[1].trim();
			String condc43=datos4[2].trim();
			

			etapas[3] = condc41+"&&"+condc42+"&&"+condc43; 
			
			String[] datos5=c5.split(",");
			String condc51=datos5[0].trim();
			String condc52=datos5[1].trim();
			String condc53=datos5[2].trim();
			
			etapas[4] = condc51+"&&"+condc52+"&&"+condc53;
			
			String resultado = "Adicionadas Condiciones\n\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
			
			

		}
    	else{
    		throw new Exception("No se pudieron registrar las condiciones de priorizacion");
    	}
    	}
    	catch(Exception e){
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);

    	}
    	
    	etapas[0]="edad>=80";
    	etapas[1]="edad>=60&&edad<=79&&enfermedad==0&&ocupacion.equals("+"SALUD"+")";
    	etapas[2]="edad>16&&edad<59,enfermedad==0,ocupacion.equals("+"PUBLICO"+")";
    	etapas[3]="edad>16,enfermedad==0,ocupacion.equals("+"EXPUESTO"+")";
    	etapas[4]="edad>16,enfermedad==0,ocupacion.equals("+"NINGUNO"+")";

    	
    	
    	
		return etapas;
		
    }
    
    public String registrarAvance(String avance)
    {
		String resp = "";
		resp += avance+"\n\n";
		
		return avance;
    }
    
    public void reqfc12()
    {
    	try 
    	{
//			List <String> lista = parranderos.darRfc12();

			String resultado = "En listarUsuarios";
//			resultado +=  "\n" + listar (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = parranderos.limpiarParranderos();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada tipo de bebida recibido
     */
    private String listarUsuarios(List<VOUsuario> lista) 
    {
    	String resp = "Los Usuarios existentes son:\n";
    	int i = 1;
        for (VOUsuario tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    private String listarVacunas(List<VOVacuna> lista) 
    {
    	String resp = "Los Usuarios existentes son:\n";
    	int i = 1;
        for (VOVacuna tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarEPSRegional(List<VOEPSRegional> lista) 
    {
    	String resp = "Las EPS existentes son:\n";
    	int i = 1;
        for (VOEPSRegional tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarPuntoVacunacion(List<VOPuntoVacunacion> lista) 
    {
    	String resp = "Los Puntos existentes son:\n";
    	int i = 1;
        for (VOPuntoVacunacion tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarVacunacion(List<VOVacunacion> lista) 
    {
    	String resp = "Las vacunaciones son:\n";
    	int i = 1;
        for (VOVacunacion tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarCiudadano(List<VOCiudadano> lista) 
    {
    	String resp = "Los Ciudadanos son:\n";
    	int i = 1;
        for (VOCiudadano tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarLoteVacuna(List<VOLoteVacuna> lista) 
    {
    	String resp = "Los Lotes son:\n";
    	int i = 1;
        for (VOLoteVacuna tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazParranderosApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazParranderosApp interfaz = new InterfazParranderosApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
