package com.mycompany.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

            Socket socket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            OutputStream out = socket.getOutputStream();
            out.write(
                    "HTTP/1.1 200 PITITO\r\n\r\n".getBytes()
            );
            out.flush();
            
            //socket.close();
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
    }
}
