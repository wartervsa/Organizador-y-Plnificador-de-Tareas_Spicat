package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Funcionalidad.componentes.Tarea;
import java.util.LinkedList;

public class RegistroDeTarea extends JFrame {
 
    private LinkedList<Tarea> tareas;
    private JTable tabla;
    private DefaultTableModel modelo;

    public RegistroDeTarea() {
        
        setTitle("Registro de Tareas del Docente");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- Columnas de la tabla ---
        String[] columnas = {"ID", "Tarea", "Fecha Vencimiento", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo permitir editar la columna Estado
                return column == 3;
            }
        };
        
        // --- JTable ---
        tabla = new JTable(modelo);

        // Crear combo box para los estados
        String[] estados = {"Pendiente", "En Proceso", "Completada"};
        JComboBox<String> comboEstados = new JComboBox<>(estados);

        // Asignar combo box a la columna "Estado"
        TableColumn columnaEstado = tabla.getColumnModel().getColumn(3);
        columnaEstado.setCellEditor(new DefaultCellEditor(comboEstados));

        // Mejorar apariencia
        tabla.setRowHeight(30);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Botón para guardar los cambios
        JButton btnGuardar = new JButton("Guardar Cambios");
        add(btnGuardar, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarCambios());
    }
    
    public void cargarTareas(LinkedList<Tarea> tareas) {
        modelo.setRowCount(0); // Limpia la tabla antes de agregar nuevas tareas
        int i = 1;
        for (Tarea tarea: tareas) {
            i++;
            modelo.addRow(new Object[]{i, tarea.getTarea(), tarea.getFecha(), tarea.getEstado()});
        }
    }

    private void guardarCambios() {
        // Aquí puedes guardar en base de datos o archivo
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String tarea = modelo.getValueAt(i, 1).toString();
            String estado = modelo.getValueAt(i, 3).toString();
            System.out.println("Tarea: " + tarea + " | Estado: " + estado);
        }
        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroDeTarea().setVisible(true));
    }

    
}
