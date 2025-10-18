package GUI;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import Funcionalidad.componentes.Usuario; 

public class CalendarioGeneral extends JFrame {

    private final Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Map<String, String> actividadesDeEjemplo = new HashMap<>();

    public CalendarioGeneral(Usuario usuario) {
        if (usuario == null || usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Se intentó abrir el calendario con un usuario inválido.", "Error de Usuario", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> dispose());
            this.usuario = null; 
            return;
        }
        
        this.usuario = usuario;
        cargarDatosDeEjemplo();
        inicializarComponentes();
    }

    private void cargarDatosDeEjemplo() {
        actividadesDeEjemplo.put("18/10/2025", "Reunión de equipo");
        actividadesDeEjemplo.put("22/10/2025", "Entregar reporte trimestral");
        actividadesDeEjemplo.put("31/10/2025", "Fiesta de Halloween");
    }

    private void inicializarComponentes() {
        setTitle("Calendario Limpio - Rol: " + usuario.getRol());
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panelBotones = new JPanel();
        JTextField campoNuevaActividad = new JTextField(20);
        JButton botonAgregar = new JButton("Agregar Actividad");
        if (usuario.puedeEditar()) {
            panelBotones.add(new JLabel("Nueva Actividad:"));
            panelBotones.add(campoNuevaActividad);
            panelBotones.add(botonAgregar);
        } else {
            panelBotones.add(new JLabel("Modo de solo lectura."));
        }
        
        modelo = new DefaultTableModel(new Object[]{"Fecha", "Actividad"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        actualizarTabla();
        add(panelBotones, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        botonAgregar.addActionListener(e -> {
            String nuevaActividad = campoNuevaActividad.getText();
            if (!nuevaActividad.isEmpty()) {
                String hoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                actividadesDeEjemplo.put(hoy, nuevaActividad);
                actualizarTabla();
                campoNuevaActividad.setText("");
            }
        });
    }
    
    private void actualizarTabla() {
        modelo.setRowCount(0); 
        for (Map.Entry<String, String> entrada : actividadesDeEjemplo.entrySet()) {
            modelo.addRow(new Object[]{entrada.getKey(), entrada.getValue()});
        }
    }
    public static void main(String[] args) {
        Usuario usuarioDueno = new Usuario("test@empresa.dueño", "123");
        Usuario usuarioMiembro = new Usuario("test@empresa.miembro", "123");
        SwingUtilities.invokeLater(() -> {
            CalendarioGeneral calendarioDueno = new CalendarioGeneral(usuarioDueno);
            calendarioDueno.setVisible(true);
            CalendarioGeneral calendarioMiembro = new CalendarioGeneral(usuarioMiembro);
            calendarioMiembro.setVisible(true);
        });
    }
}