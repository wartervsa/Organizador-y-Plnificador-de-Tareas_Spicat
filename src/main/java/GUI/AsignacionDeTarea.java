package GUI;
import DAO.TareaDAO;
import Funcionalidad.componentes.Tarea;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//import javax.swing.Timer;//ESATO

///ORIGINAL
public class AsignacionDeTarea extends javax.swing.JFrame {
    private DefaultTableModel dtm;//
    private Object[] tb = new Object[6]; //utilizamos la clase object para guardar todo tipo de informacion int,string ,etc
    private int filaseleccionada;
    
    private DefaultListModel<String> modeloNotificaciones = new DefaultListModel<>(); //lista de notificaciones
 
    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
   
  //constructor
    public AsignacionDeTarea() {
        
        initComponents();
        
        dtm = (DefaultTableModel)tabla1.getModel(); //con esto manejas filas del atabla puedes eliminar
        JList.setModel(modeloNotificaciones); // ESATO
        //verificarVencimientos();
        //(ESTO MUESTRA EL VENCIMIENTO CADA 60 SEG Y NNO ME PARECIO BUENO)
        //Timer timer = new Timer(60000, e -> verificarVencimientos()); // cada 60s
        //timer.start();//ESATO
    }

    
    
    
 
    
    
    
private void verificarVencimientos() { //metodopara verificar vencimiento
    
    Date hoy = new Date();    
    for (int i = 0; i < dtm.getRowCount(); i++) {
        try {
            
            Date fecha = dateformat.parse(dtm.getValueAt(i, 4).toString()); //DA EL VALOR DE LA FECHA PUESTA EN LA TABLA
            
            if (fecha.before(hoy)) {            // COMPARA FECHAS
                String tareaVencida = dtm.getValueAt(i, 0).toString();
                String cargo1 = dtm.getValueAt(i, 2).toString();
                
                modeloNotificaciones.addElement("⚠ La tarea '" + tareaVencida +" de "+cargo1+ "' ha vencido."); //AGREGA AL JLIST
                
            }
        } catch (Exception ex) {
            // ignorar errores de parseo
        }
    }
}


    //METODO PARA LA configuracion del combobox secundario
  private void cargarSubOpciones(String categoria) {
    comboboxsec1.removeAllItems(); // Limpiar los ítems anteriores

    if ("Docentes ".equals(categoria)) { 
        comboboxsec1.addItem("Biologia");
        comboboxsec1.addItem("Quimica");
        comboboxsec1.addItem("Anatomia");
        comboboxsec1.addItem("Lenguaje");
        comboboxsec1.addItem("Matematica");
        comboboxsec1.addItem("Fisica");
        comboboxsec1.addItem("Razonamiento");
        comboboxsec1.addItem("Historia");
    } else if ("Marketing".equals(categoria)) {
        comboboxsec1.addItem("Daniel_Rios");
        comboboxsec1.addItem("Paola_Fernandez");
        comboboxsec1.addItem("Andres_Silva");
    } else if ("Auxiliar".equals(categoria)) {
        comboboxsec1.addItem("Daniel_Rios");
        comboboxsec1.addItem("Paola_Fernandez");
    } else if ("Coordinador Academico".equals(categoria)) {
        comboboxsec1.addItem("Marcelo_lopez");
        
    }else if ("Secretaria".equals(categoria)) {
        comboboxsec1.addItem("Josefina_Mamani");
    }else if ("Soporte Tecnico".equals(categoria)) {
        comboboxsec1.addItem("Natalia_Reyes");
    }
}
    
    
   


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        txttarea = new javax.swing.JTextField();
        combobox1 = new javax.swing.JComboBox<>();
        combobox2 = new javax.swing.JComboBox<>();
        combobox3 = new javax.swing.JComboBox<>();
        jfecha = new com.toedter.calendar.JDateChooser();
        txtTarea = new javax.swing.JButton();
        txtEditar = new javax.swing.JButton();
        txtModificar = new javax.swing.JButton();
        txtterminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        comboboxsec1 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(204, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txttarea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        txttarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttareaActionPerformed(evt);
            }
        });

        combobox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Docentes ", "Secretaria", "Marketing", "Auxiliar", "Coordinador Academico", "Soporte Tecnico" }));
        combobox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        combobox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox1ActionPerformed(evt);
            }
        });

        combobox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "En Curso", "En Espera ", "Detenido" }));
        combobox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));

        combobox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alta ", "Media", "Baja", "Critica" }));
        combobox3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));

        jfecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jfecha.setDateFormatString("dd/MM/yyyy");

        txtTarea.setBackground(new java.awt.Color(204, 255, 255));
        txtTarea.setText("Agregar Tarea");
        txtTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTareaActionPerformed(evt);
            }
        });

        txtEditar.setBackground(new java.awt.Color(204, 255, 255));
        txtEditar.setText("Editar Tarea");
        txtEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditarActionPerformed(evt);
            }
        });

        txtModificar.setBackground(new java.awt.Color(204, 255, 255));
        txtModificar.setText("Modificar");
        txtModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModificarActionPerformed(evt);
            }
        });

        txtterminar.setBackground(new java.awt.Color(204, 255, 255));
        txtterminar.setText("Terminar Tarea");
        txtterminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtterminarActionPerformed(evt);
            }
        });

        tabla1.setBackground(new java.awt.Color(204, 255, 255));
        tabla1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 255)));
        tabla1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tarea", "Responsable", "Nombre", "Estado", "Vencimiento ", "Prioridad"
            }
        ));
        jScrollPane1.setViewportView(tabla1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Tarea :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Responsable :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText(" Estado :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Vencimiento :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Prioridad:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Lista de tareas :");

        comboboxsec1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxsec1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txttarea, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(comboboxsec1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(combobox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(combobox2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combobox3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(24, 24, 24))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtterminar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtTarea, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel5)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jfecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(combobox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(combobox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txttarea))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboboxsec1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(combobox3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTarea)
                    .addComponent(txtEditar)
                    .addComponent(txtModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtterminar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jTabbedPane1.addTab("Registro", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        JList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(JList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Notificaciones", jPanel3);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 780, 540));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txttareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttareaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttareaActionPerformed
//BOTON TERMINAR
    private void txtterminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtterminarActionPerformed
       if (tabla1.getSelectedRow()== -1){       //compara si no selecciona nada
            
            JOptionPane.showMessageDialog(null,"seleccione el elementoa eliminar","ERROR",JOptionPane.ERROR_MESSAGE);
        }else{
             String tareaEliminada = dtm.getValueAt(tabla1.getSelectedRow(),0).toString(); 
             String cargo2 = dtm.getValueAt(tabla1.getSelectedRow(),2).toString(); 
             dtm.removeRow(tabla1.getSelectedRow());       //REMUEVE LA FILA
             modeloNotificaciones.addElement("Tarea eliminada: " + tareaEliminada + " de " +cargo2 );// MUESTRA EN EL JLIST
            
        }
                                                 
    }//GEN-LAST:event_txtterminarActionPerformed
//BOTON AÑADIR TAREA ,AÑADE LOS DATOS A LAS FILAS DEL JTABLE
    private void txtTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTareaActionPerformed
        
        tb[0]= txttarea.getText().trim();
        tb[1]= combobox1.getSelectedItem().toString();
         tb[2]= comboboxsec1.getSelectedItem().toString();
         tb[3]= combobox2.getSelectedItem().toString();
         tb[4]= dateformat.format(jfecha.getDate());
         tb[5]= combobox3.getSelectedItem().toString();
      
        dtm.addRow(tb);

        modeloNotificaciones.addElement("Nueva tarea asignada: " + tb[0]+" para "+tb[2]); // ESATO
        
        // Codigo Dylan : Inicio
        
        TareaDAO tareas = new TareaDAO();
        Tarea tarea = new Tarea(txttarea.getText().trim(),  combobox1.getSelectedItem().toString(), comboboxsec1.getSelectedItem().toString(), dateformat.format(jfecha.getDate()), combobox3.getSelectedItem().toString());
        tareas.agregarTarea(tarea); 
        
        // Codigo Dylan : Fin
        
        verificarVencimientos();
         txttarea.setText("");

    }//GEN-LAST:event_txtTareaActionPerformed
//BOTON AGARRA LOS DATOS DE LA FILA SELECCIONADA  PARA LUEGO CON EL BOTON MODIFICAR CAMBIARLOS
    private void txtEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditarActionPerformed
            
        if (tabla1.getSelectedRow() == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione el elemento a modificar", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
        filaseleccionada = tabla1.getSelectedRow();

        // Cargar valores de la fila seleccionada en los campos
        txttarea.setText(dtm.getValueAt(filaseleccionada, 0).toString());
        combobox1.setSelectedItem(dtm.getValueAt(filaseleccionada, 1).toString());
         comboboxsec1.setSelectedItem(dtm.getValueAt(filaseleccionada, 2).toString());
        combobox2.setSelectedItem(dtm.getValueAt(filaseleccionada, 3).toString());
        
        try {
            Date fecha = dateformat.parse(dtm.getValueAt(filaseleccionada, 4).toString());      //AQUI DEVUELVE EL VALOR AL JCHOSER
            jfecha.setDate(fecha);
        } catch (Exception e) {
            jfecha.setDate(null); // por si falla el parseo
        }

        combobox3.setSelectedItem(dtm.getValueAt(filaseleccionada, 5).toString());
        }
       
    }//GEN-LAST:event_txtEditarActionPerformed
//BOTON QUE MODIFICA LOS DATOS DE LA FILA
    private void txtModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModificarActionPerformed
        
        dtm.setValueAt(txttarea.getText().trim(), filaseleccionada, 0);
        dtm.setValueAt(combobox1.getSelectedItem(), filaseleccionada, 1);
        dtm.setValueAt(comboboxsec1.getSelectedItem(), filaseleccionada, 2);
        dtm.setValueAt(combobox2.getSelectedItem(), filaseleccionada,3);
        dtm.setValueAt(dateformat.format(jfecha.getDate()), filaseleccionada, 4);
        dtm.setValueAt(combobox3.getSelectedItem(), filaseleccionada, 5);
        modeloNotificaciones.addElement("Tarea modificada: " + txttarea.getText().trim()+" para  "+comboboxsec1.getSelectedItem()); // ESATO
        
        verificarVencimientos();
        
    }//GEN-LAST:event_txtModificarActionPerformed


//AQUI CONFIGURAMOS EL COMBOBOX1 PARA QUE PUEDA LEER EL COMBOBOXSEC1
    private void combobox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox1ActionPerformed
    
    String categoriaSeleccionada = (String) combobox1.getSelectedItem();
    cargarSubOpciones(categoriaSeleccionada);
        
        
        
    }//GEN-LAST:event_combobox1ActionPerformed

    private void comboboxsec1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxsec1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboboxsec1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsignacionDeTarea().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JList;
    private javax.swing.JComboBox<String> combobox1;
    private javax.swing.JComboBox<String> combobox2;
    private javax.swing.JComboBox<String> combobox3;
    private javax.swing.JComboBox<String> comboboxsec1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jfecha;
    private javax.swing.JTable tabla1;
    private javax.swing.JButton txtEditar;
    private javax.swing.JButton txtModificar;
    private javax.swing.JButton txtTarea;
    private javax.swing.JTextField txttarea;
    private javax.swing.JButton txtterminar;
    // End of variables declaration//GEN-END:variables
}