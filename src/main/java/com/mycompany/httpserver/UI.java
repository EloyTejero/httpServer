/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
/**
 *
 * @author Eloy
 */

public class UI {
    private JFrame frame;
    private JTextField filePathField;
    private File selectedFile;

    // Directorio destino donde se almacenarán los archivos
    private static final String TARGET_DIRECTORY = "./public"; // Cambia según sea necesario

    public UI() {
        // Crear la ventana principal
        frame = new JFrame("Cargar Archivos");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Crear el campo de texto para mostrar la ruta del archivo seleccionado
        filePathField = new JTextField();
        filePathField.setEditable(false);
        frame.add(filePathField, BorderLayout.NORTH);

        // Crear el botón para seleccionar un archivo
        JButton selectFileButton = new JButton("Seleccionar Archivo");
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        // Crear el botón para cargar el archivo
        JButton uploadButton = new JButton("Cargar Archivo");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        saveFile(selectedFile);
                        JOptionPane.showMessageDialog(frame, "Archivo cargado con éxito.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error al cargar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, selecciona un archivo primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Panel con botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectFileButton);
        buttonPanel.add(uploadButton);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Mostrar la ventana
        frame.setVisible(true);

        // Crear el directorio destino si no existe
        createTargetDirectory();
    }

    private void saveFile(File file) throws IOException {
        // Ruta destino donde se copiará el archivo
        Path targetPath = Path.of(TARGET_DIRECTORY, file.getName());

        // Copiar el archivo al directorio destino
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void createTargetDirectory() {
        // Crear el directorio destino si no existe
        File targetDir = new File(TARGET_DIRECTORY);
        if (!targetDir.exists()) {
            boolean created = targetDir.mkdir();
            if (!created) {
                System.err.println("No se pudo crear el directorio de destino.");
            }
        }
    }
}


