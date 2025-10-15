/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Funcionalidad.componentes.Usuario;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.LinkedList;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
/**
 *
 * @author d23ylan
 */
public class UsuarioDAO {
    
    private Gson gson;
    private File archivo;
    
    public UsuarioDAO() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        archivo = new File("Base de Datos/Usuario/Usuarios.json");
        try {
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
            } 
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void agregarUsuario(Usuario usuario) {
        List<Usuario> usuarios = obtenerUsuarios();
        usuarios.add(usuario);
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean accederUsuario(Usuario usuario) {
        boolean encontrado = false;
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario usuarioAEncontrar: usuarios) {
            if(usuarioAEncontrar.equals(usuario)) {
                encontrado = true;
            }
        }
        return encontrado;
    }
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> resultado = new LinkedList();
        try (Reader reader = new FileReader(archivo)) {
            Type tipoDeLista = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios =  gson.fromJson(reader, tipoDeLista);
            if (usuarios != null) {
                resultado = usuarios;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
}
