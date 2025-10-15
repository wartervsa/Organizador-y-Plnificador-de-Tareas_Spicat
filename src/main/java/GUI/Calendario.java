package GUI;

import com.toedter.calendar.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import Login.*;

public class Calendario<T extends PanelUsuario> extends JFrame  {

    private JCalendar calendario;
    private JDateChooser filtroFecha;
    private JTextField campoActividad;
    private JComboBox<String> comboLimite;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Map<Date, Actividad> actividades = new HashMap<>();
    private Set<Date> fechasCanceladas = new HashSet<>();
    private boolean esAdmin; 
    private boolean isUpdatingTable = false; // Bandera para controlar la recursión del listener
    private T panelUsuario;
    
    public void panelAnterior(T panelUsuario) {
        this.panelUsuario = panelUsuario;
    }
    
    // Definición de Actividad (Clase interna estática para buen diseño)
    private static class Actividad {
        String descripcion;
        Date fechaLimite;
        boolean entregado;

        Actividad(String descripcion, Date fechaLimite, boolean entregado) {
            this.descripcion = descripcion;
            this.fechaLimite = fechaLimite;
            this.entregado = entregado;
        }
    }

    // Evaluador de Fechas Canceladas
    private class CancelDateEvaluator implements IDateEvaluator {
        @Override
        public boolean isSpecial(Date date) {
            return fechasCanceladas.contains(Calendario.normalizar(date));
        }

        @Override
        public Color getSpecialForegroundColor() { return Color.WHITE; }

        @Override
        public Color getSpecialBackroundColor() { return Color.GRAY; }

        @Override
        public String getSpecialTooltip() { return "Fecha cancelada/feriado"; }

        @Override
        public boolean isInvalid(Date date) { return false; }

        @Override
        public Color getInvalidForegroundColor() { return null; }

        @Override
        public Color getInvalidBackroundColor() { return null; }
        
        @Override
        public String getInvalidTooltip() { return null; }
    }
    
    public void cargarCalendario() {
        // --- Configuración Inicial ---
        String[] opciones = {"Admin", "Usuario"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Selecciona el tipo de usuario:", "Modo de acceso",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        esAdmin = "Admin".equals(tipo);

        setTitle("Calendario SIGA - " + tipo);
        setSize(1260, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // --- Paneles y Diseño ---
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 222, 179));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(new Color(245, 222, 179));
        
        JPanel panelControles = new JPanel(new GridLayout(4, 3, 5, 5));
        panelControles.setBackground(new Color(245, 222, 179));

        calendario = new JCalendar();
        calendario.setPreferredSize(new Dimension(350, 250));
        
        campoActividad = new JTextField(15);
        comboLimite = new JComboBox<>(new String[]{"1 día", "1 semana", "1 mes"});
        filtroFecha = new JDateChooser();
        filtroFecha.setPreferredSize(new Dimension(150, 25));

        // --- Botones ---
        JButton guardar = crearBoton("Guardar actividad");
        JButton eliminar = crearBoton("Eliminar actividad"); // Eliminará SÓLO actividades
        JButton editar = crearBoton("Editar actividad/feriado");
        JButton cancelarFecha = crearBoton("Cancelar/Habilitar fecha");
        JButton filtrar = crearBoton("Filtrar por fecha");
        JButton quitarFiltro = crearBoton("Mostrar todo");

        // ---Botones con Icono---
        ImageIcon volver = new ImageIcon("/home/d23ylan/Pictures/Images Projects Java Netbeans/Icon_volver.png");
        JButton btnVolver = new JButton("Volver", volver);
        btnVolver.addActionListener(e -> {
            panelUsuario.mostrarPanel();
            this.setVisible(false);
            dispose();
        });
        
        guardar.setEnabled(esAdmin);
        eliminar.setEnabled(esAdmin);
        editar.setEnabled(esAdmin);
        cancelarFecha.setEnabled(esAdmin);
        
        // --- Tabla y Modelo ---
        modelo = new DefaultTableModel(new Object[]{"Fecha", "Actividad", "Límite", "Estado", "Entregado"}, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 4 ? Boolean.class : String.class; // Columna 4 para JCheckBox
            }
            
            @Override
            public boolean isCellEditable(int row, int col) {
                // El checkbox (col 4) es editable SI y SOLO SI el usuario NO es Admin.
                return col == 4 && !esAdmin; 
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, selected, focus, row, col);
                String estado = (String) table.getValueAt(row, 3);
                
                Color bgColor;
                if ("Entregado".equals(estado)) {
                    bgColor = new Color(200, 255, 200); // Verde claro
                } else if ("No entregado".equals(estado)) {
                    bgColor = new Color(255, 200, 200); // Rojo claro
                } else {
                    bgColor = new Color(255, 255, 200); // Amarillo claro (Pendiente/Cancelado)
                }

                if (!selected) {
                    c.setBackground(bgColor);
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        
        // --- Armado de Paneles ---
        panelControles.add(new JLabel("Actividad:"));
        panelControles.add(campoActividad);
        panelControles.add(guardar);
        
        panelControles.add(new JLabel("Límite:"));
        panelControles.add(comboLimite);
        panelControles.add(eliminar);
        
        panelControles.add(new JLabel("Filtrar por fecha:"));
        panelControles.add(filtroFecha);
        panelControles.add(filtrar);
        
        panelControles.add(quitarFiltro);
        panelControles.add(cancelarFecha);
        panelControles.add(editar);

        panelSuperior.add(calendario);
        panelSuperior.add(panelControles);
        panelSuperior.add(btnVolver, BorderLayout.NORTH);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        getContentPane().add(panel);

        // --- Listeners ---
        guardar.addActionListener(e -> guardarActividad());
        eliminar.addActionListener(e -> eliminarActividad());
        editar.addActionListener(e -> editarActividad()); 
        cancelarFecha.addActionListener(e -> manejarCancelacionFecha());
        filtrar.addActionListener(e -> filtrarPorFecha());
        quitarFiltro.addActionListener(e -> actualizarTabla());
        
        // Listener principal para actualizar el estado cuando se marca/desmarca una actividad
        modelo.addTableModelListener(e -> {
            if (e.getColumn() == 4 && e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                actualizarEstado(e.getFirstRow()); 
            }
        });
        
        calendario.getDayChooser().addDateEvaluator(new CancelDateEvaluator());

        // --- Carga Inicial ---
        cargarActividades();
        cargarFechasCanceladas();
        actualizarTabla();
        setVisible(true);
    }
    
    // --- Métodos de Utilidad ---

    private JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(200, 160, 120));
        b.setForeground(Color.BLACK);
        return b;
    }

    private static Date normalizar(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date calcularLimite(Date base, String tipo) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        switch (tipo) {
            case "1 día": c.add(Calendar.DAY_OF_MONTH, 1); break;
            case "1 semana": c.add(Calendar.WEEK_OF_YEAR, 1); break;
            case "1 mes": c.add(Calendar.MONTH, 1); break;
        }
        return c.getTime();
    }

    private String calcularEstado(Actividad act) {
        if (act.entregado) {
            return "Entregado";
        } else if ((new Date()).after(act.fechaLimite)) {
            return "No entregado";
        } else {
            return "Pendiente";
        }
    }
    
    // --- Lógica de Actividades y Fechas ---

    private void guardarActividad() {
        Date fecha = calendario.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha en el calendario.");
            return;
        }
        fecha = normalizar(fecha);

        if (fechasCanceladas.contains(fecha)) {
            JOptionPane.showMessageDialog(this, "No se puede agregar actividad: la fecha está cancelada/feriado.");
            return;
        }
        if (actividades.containsKey(fecha)) {
            JOptionPane.showMessageDialog(this, "Ya existe una actividad para esta fecha. Usa 'Editar'.");
            return;
        }

        String desc = campoActividad.getText().trim();
        if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe una actividad.");
            return;
        }

        String tipo = (String) comboLimite.getSelectedItem();
        if (tipo == null) tipo = "1 día";
        Date limite = calcularLimite(fecha, tipo);

        actividades.put(fecha, new Actividad(desc, limite, false));
        actualizarTabla();
        guardarEnArchivo();
        campoActividad.setText("");
    }
    
    private void eliminarActividad() {
        Date fecha = calendario.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha en el calendario.");
            return;
        }
        final Date fechaNormalizada = normalizar(fecha);
        
        if (fechasCanceladas.contains(fechaNormalizada)) {
            JOptionPane.showMessageDialog(this, "Esta fecha es un feriado/cancelación. Usa 'Cancelar/Habilitar fecha' para gestionarla.");
            return;
        }
        
        if (!actividades.containsKey(fechaNormalizada)) {
            JOptionPane.showMessageDialog(this, "No hay actividad para esa fecha que eliminar.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar la actividad?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
             actividades.remove(fechaNormalizada);
             actualizarTabla();
             guardarEnArchivo();
             JOptionPane.showMessageDialog(this, "Actividad eliminada correctamente.");
        }
    }
    
    private void manejarCancelacionFecha() {
        Date fecha = calendario.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha.");
            return;
        }
        fecha = normalizar(fecha);
        
        if (fechasCanceladas.contains(fecha)) {
            // Habilitar fecha (Quitar cancelación)
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas HABILITAR esta fecha?", "Confirmar Habilitación", JOptionPane.YES_NO_OPTION);
             if (confirm == JOptionPane.YES_OPTION) {
                 fechasCanceladas.remove(fecha);
                 JOptionPane.showMessageDialog(this, "Fecha habilitada correctamente.");
             }
        } else {
            // Cancelar fecha
            if (actividades.containsKey(fecha)) {
                JOptionPane.showMessageDialog(this, "No se puede cancelar una fecha con actividad.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas CANCELAR esta fecha?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fechasCanceladas.add(fecha);
                JOptionPane.showMessageDialog(this, "Fecha cancelada/feriado correctamente.");
            }
        }
        
        guardarFechasCanceladas();
        actualizarTabla();
        calendario.repaint(); 
    }
    
    /**
     * CORRECCIÓN 2: Se hace la variable 'act' efectivamente final.
     * Al usar lambdas (el listener del botón 'guardar'), las variables locales
     * que se acceden desde dentro deben ser final o 'effectively final'.
     * La solución es declarar 'act' como final o crear una referencia final.
     */
    private void editarActividad() {
        Date fechaSeleccionada = calendario.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha en el calendario para editar.");
            return;
        }
        final Date fechaNormalizada = normalizar(fechaSeleccionada);
        
        // 1. Editar Actividad
        if (actividades.containsKey(fechaNormalizada)) {
             // La variable 'act' se convierte en efectivamente final.
             final Actividad act = actividades.get(fechaNormalizada);
             
             JDialog d = new JDialog(this, "Editar Actividad", true);
             d.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
             d.setSize(380, 200);
             
             JTextField campo = new JTextField(act.descripcion, 25);
             JComboBox<String> nuevoLimite = new JComboBox<>(new String[]{"1 día", "1 semana", "1 mes"});
             
             // Lógica simple para preseleccionar el límite (aproximado)
             long diff = act.fechaLimite.getTime() - fechaNormalizada.getTime();
             if (diff > 25 * 24 * 3600 * 1000) nuevoLimite.setSelectedItem("1 mes"); 
             else if (diff > 6 * 24 * 3600 * 1000) nuevoLimite.setSelectedItem("1 semana"); 
             else nuevoLimite.setSelectedItem("1 día");
             
             JCheckBox entregado = new JCheckBox("Entregado", act.entregado);

             d.add(new JLabel("Descripción:"));
             d.add(campo);
             d.add(new JLabel("<html>Nuevo Límite:<br>(Desde: " + new SimpleDateFormat("dd/MM/yyyy").format(fechaNormalizada) + ")</html>"));
             d.add(nuevoLimite);
             d.add(new JLabel("Estado:"));
             d.add(entregado);

             JButton guardar = crearBoton("Guardar cambios");
             
             // Aquí se usa 'act', que ahora es efectivamente final
             guardar.addActionListener(ev -> {
                 String nuevaDesc = campo.getText().trim();
                 if (nuevaDesc.isEmpty()) {
                      JOptionPane.showMessageDialog(d, "La descripción no puede estar vacía.");
                      return;
                 }
                 act.descripcion = nuevaDesc;
                 // Uso de fechaNormalizada, que también es final
                 act.fechaLimite = calcularLimite(fechaNormalizada, (String) nuevoLimite.getSelectedItem()); 
                 act.entregado = entregado.isSelected();
                 
                 actualizarTabla();
                 guardarEnArchivo();
                 d.dispose();
             });
             
             d.add(guardar);
             d.setLocationRelativeTo(this);
             d.setVisible(true);
             
        } 
        // 2. Editar Feriado/Cancelación (Si es una fecha cancelada)
        else if (fechasCanceladas.contains(fechaNormalizada)) {
            manejarCancelacionFecha(); 
        } else {
             JOptionPane.showMessageDialog(this, "No hay actividad ni fecha cancelada para editar.");
        }
    }

    private void filtrarPorFecha() {
        Date filtro = filtroFecha.getDate();
        if (filtro == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha para filtrar.");
            return;
        }

        filtro = normalizar(filtro);
        modelo.setRowCount(0);

        Actividad act = actividades.get(filtro);
        boolean hayResultados = false;

        if (act != null) {
            agregarFila(filtro, act);
            hayResultados = true;
        } else if (fechasCanceladas.contains(filtro)) {
            modelo.addRow(new Object[]{
                new SimpleDateFormat("dd/MM/yyyy").format(filtro),
                "FECHA CANCELADA/FERIADO",
                "-",
                "Cancelado",
                false
            });
            hayResultados = true;
        }

        if (!hayResultados) {
            JOptionPane.showMessageDialog(this, "No hay actividades ni la fecha está cancelada para este día.");
        }
    }

    private void agregarFila(Date fecha, Actividad act) {
        String estado = calcularEstado(act);

        modelo.addRow(new Object[]{
                new SimpleDateFormat("dd/MM/yyyy").format(fecha),
                act.descripcion,
                new SimpleDateFormat("dd/MM/yyyy").format(act.fechaLimite),
                estado,
                act.entregado
        });
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        
        List<Date> fechasOrdenadas = new ArrayList<>(actividades.keySet());
        Collections.sort(fechasOrdenadas);

        for (Date fecha : fechasOrdenadas) {
            Actividad act = actividades.get(fecha);
            agregarFila(fecha, act);
        }
        
        for (Date fecha : fechasCanceladas) {
            if (!actividades.containsKey(fecha)) {
                 modelo.addRow(new Object[]{
                    new SimpleDateFormat("dd/MM/yyyy").format(fecha),
                    "FECHA CANCELADA/FERIADO",
                    "-",
                    "Cancelado",
                    false 
                });
            }
        }
    }

    private void actualizarEstado(int fila) {
        if (isUpdatingTable) return;

        isUpdatingTable = true;
        try {
            String fechaStr = (String) modelo.getValueAt(fila, 0);
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            // El valor de la celda de la columna 4 es un Boolean (True/False)
            boolean entregado = (Boolean) modelo.getValueAt(fila, 4);

            Actividad act = actividades.get(normalizar(fecha));
            if (act != null) {
                act.entregado = entregado;
                
                // Actualiza la columna de estado (Columna 3)
                String estado = calcularEstado(act);
                modelo.setValueAt(estado, fila, 3);
            }
        } catch (ParseException | NullPointerException | ClassCastException e) {
             // Si el error persiste, descomentar e.printStackTrace() para depurar.
             // e.printStackTrace(); 
        } finally {
            isUpdatingTable = false;
        }
        guardarEnArchivo();
    }
    
    // --- Métodos de Persistencia (IO) ---

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter("Base de Datos/Actividades/actividades.txt")) {
            for (Map.Entry<Date, Actividad> e : actividades.entrySet()) {
                Actividad a = e.getValue();
                pw.println(new SimpleDateFormat("dd/MM/yyyy").format(e.getKey()) + "|" +
                        a.descripcion + "|" +
                        new SimpleDateFormat("dd/MM/yyyy").format(a.fechaLimite) + "|" +
                        a.entregado);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar actividades en archivo.");
        }
    }

    private void cargarActividades() {
        File archivo = new File("actividades.txt");
        if (!archivo.exists()) return;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split("\\|");
                Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(partes[0]);
                String desc = partes[1];
                Date limite = new SimpleDateFormat("dd/MM/yyyy").parse(partes[2]);
                boolean entregado = Boolean.parseBoolean(partes[3]);
                actividades.put(normalizar(fecha), new Actividad(desc, limite, entregado));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar actividades desde archivo.");
        }
    }

    private void guardarFechasCanceladas() {
        try (PrintWriter pw = new PrintWriter("feriados.txt")) {
            for (Date d : fechasCanceladas) {
                pw.println(new SimpleDateFormat("dd/MM/yyyy").format(d));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar fechas canceladas.");
        }
    }

    private void cargarFechasCanceladas() {
        File archivo = new File("feriados.txt");
        if (!archivo.exists()) return;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(linea);
                fechasCanceladas.add(normalizar(fecha));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar fechas canceladas.");
        }
    }

    // --- Main ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calendario::new);
    }
}