/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funcionalidad.componentes;

/**
 *
 * @author d23ylan
 */
public class Tarea {
    
    private String tarea;
    private String responsable;
    private String nombre;
    private String estado;
    private String fecha;
    private String prioridad;
    
    public Tarea(String tarea, String responsable, String nombre, String fecha, String prioridad) {
        this.tarea = tarea;
        this.responsable = responsable;
        this.nombre = nombre;
        this.fecha = fecha;
        this.prioridad = prioridad;
        estado = "En Proceso";
    }
    
    public String getTarea() {
        return tarea;
    }
    
    public String getResponsable() {
        return responsable;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public String getPrioridad() {
        return prioridad;
    }
    
    public void setTarea(String tarea) {
        this.tarea = tarea;
    }
    
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    
}
