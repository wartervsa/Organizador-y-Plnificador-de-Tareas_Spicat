package GUI;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.IDateEvaluator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import Funcionalidad.componentes.Usuario;

public class AsignarEnCalendario extends JFrame {

    private final Usuario usuario;
    private JCalendar calendario;
    private JDateChooser filtroFecha;
    private JDateChooser fechaLimiteChooser;
    private JTextField campoActividad;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Map<Date, Actividad> actividades = new HashMap<>();
    private Set<Date> fechasCanceladas = new HashSet<>();
    private boolean isUpdatingTable = false;

    public AsignarEnCalendario(Usuario usuario) {
        this.usuario = usuario;
        if (this.usuario == null || this.usuario.getRol() == null || this.usuario.getRol().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Error Crítico: No se puede abrir el calendario sin un usuario con rol válido.",
                "Error de Permisos",
                JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> dispose());
            return; 
        }
        cargarCalendario();
    }

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

    private void cargarCalendario() {
        setTitle("Calendario SIGA - " + usuario.getRol());
        setSize(1260, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 222, 179));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(new Color(245, 222, 179));

        JPanel panelControles = new JPanel(new GridLayout(4, 3, 5, 5));
        panelControles.setBackground(new Color(245, 222, 179));

        calendario = new JCalendar();
        calendario.setPreferredSize(new Dimension(350, 250));

        campoActividad = new JTextField(15);
        fechaLimiteChooser = new JDateChooser();
        fechaLimiteChooser.setPreferredSize(new Dimension(150, 25));
        filtroFecha = new JDateChooser();
        filtroFecha.setPreferredSize(new Dimension(150, 25));

        JButton guardar = crearBoton("Guardar actividad");
        JButton eliminar = crearBoton("Eliminar actividad");
        JButton editar = crearBoton("Editar actividad/feriado");
        JButton cancelarFecha = crearBoton("Cancelar/Habilitar fecha");
        JButton filtrar = crearBoton("Filtrar por fecha");
        JButton quitarFiltro = crearBoton("Mostrar todo");

        boolean puedeEditar = usuario.puedeEditar();
        guardar.setVisible(puedeEditar);
        eliminar.setVisible(puedeEditar);
        editar.setVisible(puedeEditar);
        cancelarFecha.setVisible(puedeEditar);

        panelControles.add(new JLabel("Actividad:"));
        panelControles.add(campoActividad);
        panelControles.add(guardar);
        panelControles.add(new JLabel("Fecha límite:"));
        panelControles.add(fechaLimiteChooser);
        panelControles.add(eliminar);
        panelControles.add(new JLabel("Filtrar por fecha:"));
        panelControles.add(filtroFecha);
        panelControles.add(filtrar);
        panelControles.add(quitarFiltro);
        panelControles.add(cancelarFecha);
        panelControles.add(editar);

        modelo = new DefaultTableModel(new Object[]{"Fecha", "Actividad", "Límite", "Estado", "Entregado"}, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 4 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);

        panelSuperior.add(calendario);
        panelSuperior.add(panelControles);
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        getContentPane().add(panel);

        guardar.addActionListener(e -> guardarActividad());
        eliminar.addActionListener(e -> eliminarActividad());
        editar.addActionListener(e -> editarActividad());
        cancelarFecha.addActionListener(e -> manejarCancelacionFecha());
        filtrar.addActionListener(e -> filtrarPorFecha());
        quitarFiltro.addActionListener(e -> actualizarTabla());

        calendario.getDayChooser().addDateEvaluator(new CancelDateEvaluator());

        cargarActividades();
        cargarFechasCanceladas();
        actualizarTabla();
        setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(200, 160, 120));
        b.setForeground(Color.BLACK);
        return b;
    }

    private static Date normalizar(Date fecha) {
        if (fecha == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private class CancelDateEvaluator implements IDateEvaluator {
        @Override public boolean isSpecial(Date date) { return fechasCanceladas.contains(normalizar(date)); }
        @Override public Color getSpecialForegroundColor() { return Color.WHITE; }
        @Override public Color getSpecialBackroundColor() { return Color.GRAY; }
        @Override public String getSpecialTooltip() { return "Fecha cancelada/feriado"; }
        @Override public boolean isInvalid(Date date) { return false; }
        @Override public Color getInvalidForegroundColor() { return null; }
        @Override public Color getInvalidBackroundColor() { return null; }
        @Override public String getInvalidTooltip() { return null; }
    }

    private String calcularEstado(Actividad act) {
        if (act == null) return "Error";
        if (act.entregado) return "Entregado";
        if (act.fechaLimite != null && new Date().after(act.fechaLimite)) return "No entregado";
        return "Pendiente";
    }

    private void agregarFila(Date fecha, Actividad act) {
        String estado = calcularEstado(act);
        String fechaLimiteStr = (act.fechaLimite == null) ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(act.fechaLimite);
        modelo.addRow(new Object[]{
                new SimpleDateFormat("dd/MM/yyyy").format(fecha),
                act.descripcion,
                fechaLimiteStr,
                estado,
                act.entregado
        });
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        java.util.List<Date> fechasOrdenadas = new ArrayList<>(actividades.keySet());
        fechasOrdenadas.removeIf(Objects::isNull);
        Collections.sort(fechasOrdenadas);

        for (Date fecha : fechasOrdenadas) {
            Actividad act = actividades.get(fecha);
            if (act != null) {
                agregarFila(fecha, act);
            }
        }

        for (Date fecha : fechasCanceladas) {
            if (fecha != null && !actividades.containsKey(fecha)) {
                modelo.addRow(new Object[]{
                        new SimpleDateFormat("dd/MM/yyyy").format(fecha),
                        "FECHA CANCELADA/FERIADO", "-", "Cancelado", false
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
            boolean entregado = (Boolean) modelo.getValueAt(fila, 4);
            Actividad act = actividades.get(normalizar(fecha));
            if (act != null) {
                act.entregado = entregado;
                modelo.setValueAt(calcularEstado(act), fila, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isUpdatingTable = false;
        }
        guardarEnArchivo();
    }

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter("Base de Datos/Actividades/actividades.txt")) {
            for (Map.Entry<Date, Actividad> entry : actividades.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) continue;
                Actividad a = entry.getValue();
                pw.println(new SimpleDateFormat("dd/MM/yyyy").format(entry.getKey()) + "|" +
                        a.descripcion + "|" +
                        new SimpleDateFormat("dd/MM/yyyy").format(a.fechaLimite) + "|" +
                        a.entregado);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar actividades.");
        }
    }

    private void cargarActividades() {
        File archivo = new File("Base de Datos/Actividades/actividades.txt");
        if (!archivo.exists()) return;
        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea == null || linea.trim().isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length < 4) continue;
                Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(partes[0]);
                String desc = partes[1];
                Date limite = new SimpleDateFormat("dd/MM/yyyy").parse(partes[2]);
                boolean entregado = Boolean.parseBoolean(partes[3]);
                actividades.put(normalizar(fecha), new Actividad(desc, limite, entregado));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar actividades.");
        }
    }

    private void cargarFechasCanceladas() {}

    private void guardarActividad() {
        Date fechaSeleccionada = calendario.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha en el calendario.");
            return;
        }
        String descripcion = campoActividad.getText().trim();
        Date fechaLimite = fechaLimiteChooser.getDate();
        if (descripcion.isEmpty() || fechaLimite == null) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción y una fecha límite válida.");
            return;
        }
        actividades.put(normalizar(fechaSeleccionada), new Actividad(descripcion, normalizar(fechaLimite), false));
        actualizarTabla();
        guardarEnArchivo();
        campoActividad.setText("");
        fechaLimiteChooser.setDate(null);
    }

    private void eliminarActividad() {}
    private void editarActividad() {}
    private void manejarCancelacionFecha() {}
    private void filtrarPorFecha() {}
}