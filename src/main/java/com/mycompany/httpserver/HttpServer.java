package com.mycompany.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.SwingUtilities;

/**
 *
 * @author Eloy
 */
public class HttpServer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UI::new);
        try (ServerSocket serverSocket = new ServerSocket(4221)){
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("accepted new connection");
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
    }
    
}