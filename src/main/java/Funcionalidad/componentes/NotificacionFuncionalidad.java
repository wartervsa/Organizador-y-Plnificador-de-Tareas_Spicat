/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funcionalidad.componentes;
import Funcionalidad.componentes.Tarea;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author d23ylan
 */
public class NotificacionFuncionalidad {
    private Usuario usuario;
    private LocalDate hoy;
    
    public NotificacionFuncionalidad(Usuario usuario) {
        this.usuario = usuario;
        
        hoy = LocalDate.now();
    }
    
    public List<Tarea> buscarTareasPorVencer(){
        List<Tarea> tareasPorVencer = new ArrayList();
        
        for (Tarea tarea: usuario.getTareas()) {
            LocalDate fechaTarea = cambiarFormatoFecha(tarea.getFecha());
            
                long diasFaltantes = ChronoUnit.DAYS.between(hoy, fechaTarea);
                
                if (diasFaltantes >= 1 && diasFaltantes <= 3) {
                    tareasPorVencer.add(tarea);
                    System.out.println("La tarea agregada es: " + tarea.getNombre() + " de fecha " + tarea.getFecha());
                }

        }
        return tareasPorVencer;
    }
    
    public LocalDate cambiarFormatoFecha(String fecha) { // Ej: "10/10/2025"
        int dia = Integer.parseInt(fecha.substring(0, 2));   // "10" → 10
        int mes = Integer.parseInt(fecha.substring(3, 5));   // "10" → 10
        int año = Integer.parseInt(fecha.substring(6));      // "2025" → 2025

        return LocalDate.of(año, mes, dia);
    }

    
    public List<Tarea> buscarTareasAsignadas() {
        List<Tarea> tareasAsignadas = new ArrayList();
        
        for (Tarea tarea: usuario.getTareas()) {
            
            LocalDate fechaTarea = cambiarFormatoFecha(tarea.getFecha());
            
            if (fechaTarea.equals(hoy)) {
                tareasAsignadas.add(tarea);
            }
        }
        return tareasAsignadas;
    }
    
    public List<Tarea> buscarTareasVencidas() {
        List<Tarea> tareasVencidas = new ArrayList();
        
        for (Tarea tarea: usuario.getTareas()) {
            LocalDate fechaTarea = cambiarFormatoFecha(tarea.getFecha());
            long diasFaltantes = ChronoUnit.DAYS.between(fechaTarea, hoy);
            
            if (diasFaltantes >= 0) {
                tareasVencidas.add(tarea);
                System.out.println("Tarea agregada: " + tarea.getTarea());
            } else {
                
            }
        }
        
        return tareasVencidas;
    }
    
    public List<Tarea> buscarTareasCompletadas() {
        List<Tarea> tareasCompletadas = new ArrayList();
        
        for (Tarea tarea: usuario.getTareas()) {
            String estado = tarea.getEstado();
            
            if (estado.equals("Completado")) {
                tareasCompletadas.add(tarea);
            } else {
                
            }
            
        }
        
        return tareasCompletadas;
    }
}
