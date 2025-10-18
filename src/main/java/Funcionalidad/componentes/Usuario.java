/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funcionalidad.componentes;

import DAO.TareaDAO;
import java.util.LinkedList;
import java.util.List;
import Funcionalidad.componentes.Tarea;
/**
 *
 * @author d23ylan
 */
public class Usuario {
   
    private final String correo;
    private final String contrasena;
    private LinkedList<Tarea> tareas;
    private String nombre;
    private String rol;
    
    public Usuario(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
        nombre = obtenerNombre(correo);
        this.tareas = cargarTareas();
        rol = designarRol(correo);
    }
    
    private String obtenerNombre(String correo) {
        String partNombre = "";
        int contIni = 0;
        for (int i = 0; i <= correo.length() - 1; i++) {
            if (contIni == 0 && correo.charAt(i) == '_') {
                contIni = i + 1;
            } else if (correo.charAt(i) == '@') {
                partNombre = correo.substring(contIni, i);
                break;
            }
        }
        return partNombre;
    }
    
    private String designarRol(String correo) {
        String rol = "";
        for(int i = 0; i <= correo.length() - 1; i++) {
            if (correo.charAt(i) == '.') {
                rol = correo.substring(i + 1);
            }
        } 
        return rol;
    }

    
    public LinkedList<Tarea> cargarTareas() {
        LinkedList<Tarea> resultado = new LinkedList();
        TareaDAO ListaTodasLasTareas = new TareaDAO();
        List<Tarea> tareasTodas = ListaTodasLasTareas.obtenerTareas();
        if (tareasTodas != null && !tareasTodas.isEmpty()) {
            if (nombre.equals("JosePerez")) {
                resultado.addAll(tareasTodas);
            } else {
                for(Tarea t: tareasTodas) {
                    if(t.getNombre() != null && nombre != null && t.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                        resultado.add(t);
                    }
                }
            }
        }
        return resultado;
    }
    
    public boolean puedeEditar() { 
        if (this.rol == null) { 
            return false; 
        } 
        return rol.equalsIgnoreCase("Dueño") || rol.equalsIgnoreCase("secretaria") || rol.equalsIgnoreCase("secret"); 
    } 
    
    public String getCorreo() {
        return correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public LinkedList<Tarea> getTareas() {
        return tareas;
    }
    
    public String getRol() {
        return rol;
    }
}
