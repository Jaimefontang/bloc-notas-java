package ventanaPrincipal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Ventana principal de la aplicación tipo "Bloc de notas". Permite crear,
 * abrir, editar y guardar archivos de texto plano.
 */
public class ventanaPrincipal extends javax.swing.JFrame {

    //Atributos
    // Logger para registrar errores en la ejecución (por ejemplo en el main).
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName());

    /**
     * Referencia al archivo de texto con el que está trabajando actualmente la
     * aplicación. Puede ser: - null : documento nuevo, todavía no se ha
     * guardado nunca. - File : archivo que se ha abierto o guardado en disco.
     */
    private File archivoActual = null;

    /**
     * Constructor de la ventana principal. Inicializa todos los componentes
     * gráficos (menús, área de texto, etc.).
     */
    public ventanaPrincipal() {
        initComponents();
        //Setea el nombre de la App como "Bloc de notas - Jaime Fontan"
        setTitle("Bloc de notas - Jaime Fontán");
        //Añade un icono de un bloc de notas
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("recursos/icono.png")).getImage());
    }

//------MÉTODOS DE LÓGICA DE LA APLICACIÓN------
    /**
     * Crea un nuevo documento: - Limpia el área de texto. - "Olvida" cualquier
     * archivo que estuviera abierto. Equivale a la opción "Nuevo" de un bloc de
     * notas.
     */
    private void nuevoArchivo() {
        //Vacia el textArea
        textArea.setText("");
        //Olvida el archivo anterior, ahora es uno nuevo.
        archivoActual = null;
    }

    /**
     * Muestra un cuadro de diálogo para que el usuario seleccione un archivo de
     * texto que quiere abrir.
     *
     * @return objeto File con la ruta del archivo seleccionado, o null si el
     * usuario cancela el cuadro de diálogo.
     */
    private File abrirArchivo() {
        //Creamos un objeto de JFileChooser (Cuadro de dialogo estandar de seleccion de archivos)
        JFileChooser selector = new JFileChooser();

        //Permitimos tan solo seleccionar un archivo
        selector.setMultiSelectionEnabled(false);

        //Abrimos la ventana de dialogo de Abrir.
        selector.setDialogType(JFileChooser.OPEN_DIALOG);

        //Creamos una variable para ver el resultado de la eleccion del usuario      
        int resultado = selector.showOpenDialog(this);
        // Si el usuario pulsa "Abrir", obtenemos el archivo seleccionado.
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return selector.getSelectedFile();
        } else {
            // Si cancela o cierra la ventana, no se selecciona archivo.
            return null;
        }
    }

    /**
     * Lee el contenido completo de un archivo de texto y lo devuelve como
     * String.
     *
     * @param fichero archivo que se desea leer.
     * @return texto completo del archivo, o null si el archivo no existe o se
     * produce un error de lectura.
     */
    private String leerArchivo(File fichero) {
        //Comprobamos que el archivo se ha abierto, sino es null
        if (fichero == null || !fichero.exists()) {
            return null;
        }
        //Contenido ira guardando el texto del fichero.
        StringBuilder contenido = new StringBuilder();

        //Abre el fichero para leerlo linea a linea. try-with-resources: el BufferedReader se cierra automáticamente.
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;

            // Bucle que lee cada línea mientras no sea null
            while ((linea = br.readLine()) != null) {
                // Se añade la línea y un salto de línea adecuado al sistema.
                contenido.append(linea).append(System.lineSeparator());
            }

        } catch (IOException ex) {
            // Si ocurre un error, mostramos mensaje y devolvemos null
            JOptionPane.showMessageDialog(this, "Error al leer archivo: " + ex.getMessage());
            return null;
        }
        //Se devuelve todo el contenido en un unico String
        return contenido.toString();
    }

    /**
     * Guarda el texto indicado en el archivo indicado. No pregunta por el
     * nombre ni por la ruta; simplemente escribe en el File.
     *
     * @param fichero archivo de destino donde se escribirá el contenido.
     * @param contenido texto que se desea guardar en el archivo.
     */
    private void guardarArchivo(File fichero, String contenido) {

        //Si no hay fichero válido, no se realiza ninguna operación.
        if (fichero == null) {
            return;
        }

        //Abre el flujo de escritura del archivo.
        // try-with-resources cierra automáticamente el BufferedWriter.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {

            //Escribe todo el contenido en el archivo
            bw.write(contenido);

            //Mensaje confirmacion guardado hacia el usuario
            JOptionPane.showMessageDialog(
                    this,
                    "Archivo guardado con éxito.",
                    "Guardar",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            //En caso de error, se informa al usuario
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el archivo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Método auxiliar para la lógica de Guardar / Guardar como. Abre un cuadro
     * de diálogo Guardar que: Permite elegir nombre y ruta. Si el archivo ya
     * existe, pregunta si se desea sobrescribir. Si se responde que NO, permite
     * elegir otro nombre.
     *
     * @return File con el archivo elegido para guardar, o null si el usuario
     * cancela en cualquier momento.
     */
    private File seleccionarFicheroGuardar() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogType(JFileChooser.SAVE_DIALOG);

        // Bucle para permitir cambiar de nombre si no se desea sobrescribir.
        while (true) {
            int resultado = selector.showSaveDialog(this);

            // Usuario ha cancelado o cerrado la ventana devuelve null
            if (resultado != JFileChooser.APPROVE_OPTION) {
                return null;
            }

            File fichero = selector.getSelectedFile();

            // Si el fichero no existe, lo devolvemos directamente
            if (!fichero.exists()) {
                return fichero;
            }

            // Si existe, pregunta si quiere sobrescribir
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "El archivo ya existe. ¿Deseas sobrescribirlo?",
                    "Confirmar sobrescritura",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                // Sobrescribir: devolvemos este fichero
                return fichero;
            } else {
                //No quiere sobreescribir, vuelve al principio del bucle para que elija otro nombre
                // (el usuario puede cancelar en el siguiente diálogo si quiere salir)
            }
        }

    }

    //------CÓDIGO GENERADO DE LA INTERFAZ------
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        menu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        menuArchivoNuevo = new javax.swing.JMenuItem();
        menuArchivoAbrir = new javax.swing.JMenuItem();
        menuArchivoGuardar = new javax.swing.JMenuItem();
        menuArchivoGuardarComo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textArea.setBackground(new java.awt.Color(245, 240, 220));
        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        menuArchivo.setText("Archivo");

        menuArchivoNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuArchivoNuevo.setText("Nuevo");
        menuArchivoNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(menuArchivoNuevo);

        menuArchivoAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuArchivoAbrir.setText("Abrir");
        menuArchivoAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(menuArchivoAbrir);

        menuArchivoGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuArchivoGuardar.setText("Guardar");
        menuArchivoGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(menuArchivoGuardar);

        menuArchivoGuardarComo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuArchivoGuardarComo.setText("Guardar como");
        menuArchivoGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoGuardarComoActionPerformed(evt);
            }
        });
        menuArchivo.add(menuArchivoGuardarComo);

        menu.add(menuArchivo);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //------MANEJADORES DE EVENTOS DEL MENÚ------
    /**
     * Evento del menú "Nuevo". Limpia el área de texto y deja el documento sin
     * archivo asociado.
     */
    private void menuArchivoNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoNuevoActionPerformed
        //Llamamos al metodo nuevoArchivo, al pulsar sobre Nuevo, se activa.
        nuevoArchivo();
    }//GEN-LAST:event_menuArchivoNuevoActionPerformed

    /**
     * Evento del menú Abrir. Permite seleccionar un archivo de texto, lo lee y
     * muestra su contenido en el área de texto.
     */
    private void menuArchivoAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoAbrirActionPerformed
        // Se muestra el cuadro de diálogo para elegir el archivo.
        File fichero = abrirArchivo();

        // Si el usuario ha cancelado, no hace nada.
        if (fichero == null) {
            return;
        }
        //Lee el contenido del archivo seleccionado
        String contenido = leerArchivo(fichero);

        //Para si hay un error
        if (contenido == null) {
            return;
        }
        //muestra el contenido en el textArea
        textArea.setText(contenido);

        //Actualiza el archivo actual para que Guardar sepa dónde escribir.
        archivoActual = fichero;
    }//GEN-LAST:event_menuArchivoAbrirActionPerformed
    /**
     * Evento del menú Guardar. Si el documento es nuevo, pide nombre y ruta. Si
     * ya tiene archivo asociado, guarda directamente en él.
     */
    private void menuArchivoGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoGuardarActionPerformed
        // Texto actual escrito en el área de edición.
        String contenido = textArea.getText();

        // CASO A: documento nuevo (sin archivo asociado todavía)
        if (archivoActual == null) {

            // Usa el método auxiliar que gestiona sobrescritura o nuevo nombre
            File fichero = seleccionarFicheroGuardar();

            // Si el usuario ha cancelado, no se guarda nada.
            if (fichero == null) {
                return;
            }

            // Guardamos y actualizamos archivoActual
            guardarArchivo(fichero, contenido);
            archivoActual = fichero;

        } else {
            // CASO B: ya tenemos archivoActual, guardamos directamente en él
            guardarArchivo(archivoActual, contenido);
        }
    }//GEN-LAST:event_menuArchivoGuardarActionPerformed
    /**
     * Evento del menú Guardar como. Siempre pide al usuario un nombre y una
     * ruta nuevos, gestionando también el caso de sobrescritura.
     */
    private void menuArchivoGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoGuardarComoActionPerformed
        // Texto que se va a guardar en el nuevo archivo.
        String contenido = textArea.getText();

        // Se pide nombre y ruta nueva, usando el método auxiliar
        File fichero = seleccionarFicheroGuardar();
        // Si el usuario cancela, no se realiza ninguna acción.
        if (fichero == null) {
            return;
        }
        // Se guarda el contenido en el archivo elegido
        // y se actualiza archivoActual al nuevo archivo.
        guardarArchivo(fichero, contenido);
        archivoActual = fichero;
    }//GEN-LAST:event_menuArchivoGuardarComoActionPerformed

    /**
     * Método main de la aplicación. Configura la apariencia (LookAndFeel) y
     * muestra la ventana principal.
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ventanaPrincipal().setVisible(true));
    }

    //------DECLARACIÓN DE COMPONENTES GRÁFICOS------
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuArchivoAbrir;
    private javax.swing.JMenuItem menuArchivoGuardar;
    private javax.swing.JMenuItem menuArchivoGuardarComo;
    private javax.swing.JMenuItem menuArchivoNuevo;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
