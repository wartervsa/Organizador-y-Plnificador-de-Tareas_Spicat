package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import Login.DuenoFrame;
import GUI.AsignacionDeTarea;

public class EspacioDeTrabajo extends JFrame {

    private static final String DIRECTORIO_RAIZ = "Mis_Espacios_De_Trabajo";
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    
    private JPanel panelWorkspaces;
    private JPanel panelWorkspacesCentro; // Panel dedicado para los botones de workspaces

    private JPanel panelElementos;
    private JPanel panelElementosCentro; // Panel dedicado para los botones de elementos
    private JLabel labelTituloElementos; // Etiqueta para el título que cambia

    private File workspaceSeleccionado;

    public EspacioDeTrabajo() {
        setTitle("Gestor de Espacios de Trabajo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        new File(DIRECTORIO_RAIZ).mkdirs();

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        crearPanelWorkspaces();
        crearPanelElementos();

        panelContenedor.add(panelWorkspaces, "WORKSPACES");
        panelContenedor.add(panelElementos, "ELEMENTOS");
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        JMenuItem menuItem = new JMenuItem("Abrir Espacio de Trabajo");
        menuItem.addActionListener(e -> {
            cargarWorkspaces();
            cardLayout.show(panelContenedor, "WORKSPACES");
        });
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        add(panelContenedor);
        
        // Cargar los workspaces al inicio y mostrar el panel
        cargarWorkspaces();
        cardLayout.show(panelContenedor, "WORKSPACES");
    }

    private void crearPanelWorkspaces() {
        panelWorkspaces = new JPanel(new BorderLayout(10, 10));
        panelWorkspaces.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel central para los botones
        panelWorkspacesCentro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelWorkspaces.add(new JScrollPane(panelWorkspacesCentro), BorderLayout.CENTER);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JButton btnVolver = new JButton(); // Boton Volver
        btnVolver.setText("Volver");
        btnVolver.addActionListener(e -> {
            this.setVisible(false);
            dispose();
        });
        
        panelSuperior.add(btnVolver, BorderLayout.WEST);
        
        JLabel lblTitulo = new JLabel();
        lblTitulo.setText("Espacios de Trabajo");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        JButton btnCrearWorkspace = new JButton("+ Crear Nuevo Espacio de Trabajo");
        btnCrearWorkspace.addActionListener(e -> crearNuevoWorkspace());
        panelWorkspaces.add(btnCrearWorkspace, BorderLayout.SOUTH);
        panelWorkspaces.add(panelSuperior, BorderLayout.NORTH);
    }

    private void crearPanelElementos() {
        panelElementos = new JPanel(new BorderLayout(10, 10));
        panelElementos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        labelTituloElementos = new JLabel("Elementos en...", SwingConstants.CENTER);
        panelElementos.add(labelTituloElementos, BorderLayout.NORTH);

        // Panel central para los botones
        panelElementosCentro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelElementos.add(new JScrollPane(panelElementosCentro), BorderLayout.CENTER);
        
        JButton btnCrearElemento = new JButton("+ Crear Nuevo Elemento de Trabajo");
        btnCrearElemento.addActionListener(e -> crearNuevoElemento());
        
        JButton btnVolver = new JButton("<- Volver");
        btnVolver.addActionListener(e -> cardLayout.show(panelContenedor, "WORKSPACES"));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelSur.add(btnVolver);
        panelSur.add(btnCrearElemento);

        panelElementos.add(panelSur, BorderLayout.SOUTH);
    }
    
    // --- LÓGICA DE WORKSPACES ---

    private void cargarWorkspaces() {
        // 1. Limpiar el panel central
        panelWorkspacesCentro.removeAll();
        
        File dirRaiz = new File(DIRECTORIO_RAIZ);
        File[] directorios = dirRaiz.listFiles(File::isDirectory); // Obtener solo directorios

        // 2. Llenar el panel con los botones nuevos
        if (directorios != null) {
            for (File dir : directorios) {
                JButton btn = new JButton(dir.getName());
                btn.setPreferredSize(new Dimension(150, 50));
                btn.addActionListener(e -> {
                    workspaceSeleccionado = dir;
                    cargarElementos();
                    cardLayout.show(panelContenedor, "ELEMENTOS");
                });
                panelWorkspacesCentro.add(btn);
            }
        }
        
        // 3. Refrescar la UI
        panelWorkspacesCentro.revalidate();
        panelWorkspacesCentro.repaint();
    }

    private void crearNuevoWorkspace() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo espacio de trabajo:", "Crear Workspace", JOptionPane.PLAIN_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            File nuevoWorkspace = new File(DIRECTORIO_RAIZ, nombre.trim());
            if (nuevoWorkspace.mkdir()) {
                cargarWorkspaces();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el espacio de trabajo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // --- LÓGICA DE ELEMENTOS ---

    private void cargarElementos() {
        labelTituloElementos.setText("Elementos en: " + workspaceSeleccionado.getName());
        
        // 1. Limpiar el panel central
        panelElementosCentro.removeAll();
        
        File[] directorios = workspaceSeleccionado.listFiles(File::isDirectory);

        // 2. Llenar el panel con los botones nuevos
        if (directorios != null) {
            for (File dir : directorios) {
                JButton btn = new JButton(dir.getName());
                btn.setPreferredSize(new Dimension(150, 50));
                btn.addActionListener(e -> {
                    AsignacionDeTarea asignarTarea = new AsignacionDeTarea();
                    asignarTarea.setVisible(true);
                    // new RegistroDeTareas(dir.getAbsolutePath()).setVisible(true);
                });
                panelElementosCentro.add(btn);
            }
        }
        
        // 3. Refrescar la UI
        panelElementosCentro.revalidate();
        panelElementosCentro.repaint();
    }

    private void crearNuevoElemento() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo elemento:", "Crear Elemento", JOptionPane.PLAIN_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            File nuevoElemento = new File(workspaceSeleccionado, nombre.trim());
            if (nuevoElemento.mkdir()) {
                cargarElementos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el elemento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EspacioDeTrabajo().setVisible(true));
    }
}