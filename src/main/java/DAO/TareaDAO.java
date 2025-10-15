/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import Funcionalidad.componentes.Tarea;
import java.io.*;
import java.util.List;
import java.lang.reflect.Type;

/**
 *
 * @author d23ylan
 */
public class TareaDAO {
    
    private Gson gson;
    private File archivo;
    
    
    public TareaDAO() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        archivo = new File("Base de Datos/Tareas/tareas.json");
        try {
            if (!archivo.exists()) {
            archivo.getParentFile().mkdirs();
            archivo.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    
    public void agregarTarea(Tarea nuevaTarea) {
        List<Tarea> tareas = obtenerTareas(); // Sobreescribes el archivo recuperando todo y agregando tu tarea
        tareas.add(nuevaTarea);
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(tareas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Tarea> obtenerTareas() {
        List<Tarea> resultado = new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipoDeLista = new TypeToken<List<Tarea>>(){}.getType();
            List<Tarea> tareas = gson.fromJson(reader, tipoDeLista);
            if (tareas != null) {
                resultado = tareas;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
}
