package com.mycompany.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Eloy
 */
public class HttpServer {

    public static void main(String[] args) {
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
    
    public static byte[] readHtmlFile(String filePath) throws IOException {
        // Reads the entire HTML file into a byte array
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}