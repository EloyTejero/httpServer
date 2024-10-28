package com.mycompany.httpserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    private static class ClientHandler implements Runnable {
        
        private final Socket client;
        private OutputStream out;

        public ClientHandler(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try{
                
                out = client.getOutputStream();
                String statusLine = "HTTP/1.1 200 PITITO\r\n";
                //byte[] body = readHtmlFile("C:\\Users\\Eloy\\Desktop\\-\\java\\httpServer\\src\\public");
                //String body = "<h1>Welcome to My HTTP Server</h1>";
                //String body = "";
                String body = readFile("C:\\Users\\Eloy\\Desktop\\-\\java\\httpServer\\src\\public");
                System.out.println(body+"bodyy");
                String headers = "Content-Type: text/html\r\n" +
                                "Content-Length: " + body.length() + "\r\n" +
                                "Connection: close\r\n" + "\r\n";
                String crlf = "\r\n";
                response(statusLine, headers, body);
            } catch(IOException e){
                
            } finally {
                try {
                    out.close();
                    client.close();
                } catch (IOException ex) {
                }
            }
        }
        
        private void response(String statusLine, String headers, String body){
            String response = statusLine + headers + body;
            try{
                out.write(response.getBytes());
                out.flush();
            }catch(IOException ex){
                
            }   
        }
        
        private String readFile(String path) throws FileNotFoundException{
            String filePath = "C:\\Users\\Eloy\\Desktop\\-\\java\\httpServer\\src\\public\\index.html";
            StringBuilder body = new StringBuilder();
            try (FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    body.append(line);
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            System.out.println(body.toString());
            return body.toString();
        }
    }
}
