package com.mycompany.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{
    private final Socket client;
    private OutputStream out;
    private BufferedReader in;
    private String req;
    private boolean fullRequest;
    private HttpProtocolHandler http = new HttpProtocolHandler();
    private String BASE_DIRECTORY = "./public";

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();

            String req;
            while((req = in.readLine()) != null){
                System.out.println("Mensaje="+req);
                if(req.isEmpty()){
                    req = "";
                }
                recieveRequest(req);
            }
            /*

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
            */
        } catch(IOException e){

        } finally {
            try {
                out.close();
                client.close();
            } catch (IOException ex) {
            }
        }
    }

    private void response(byte[] response){
        try{
            out.write(response);
            out.flush();
        }catch(IOException ex){
            System.out.println("ERROR: in response");
        }   
    }

    private String readFile(String filePath){
        //String filePath = BASE_DIRECTORY+path;//"C:\\Users\\L1 - PC\\Desktop\\httpServer\\src\\public\\index2.html";
        Path fullPath = Paths.get(BASE_DIRECTORY, filePath);
        StringBuilder body = new StringBuilder();
        try (FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
                //System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        System.out.println("body: "+body.toString());
        return body.toString();
    }

    private void recieveRequest(String req) {
        http.parseRequest(req);
        if(http.isMessageCompleted()){
            System.out.println("REQUEST:");
            System.out.println(http.getRequestMessage());
            parseMethod();
        }
        /*
        String reqLine;
        String headers = null;
        String body = null;

        List<String> lines = Arrays.asList(req.split("\r\n"));

        for(String i:lines){ //print request
            System.out.println(i);
        }

        reqLine = lines.get(0);
        System.out.println("reqLine: "+reqLine);
        boolean bodyFlag = false;
        for(int i=1;i<lines.size();i++){
            String line = lines.get(i);

            if(line.equals("\r\n")){
                bodyFlag = true;
                continue;
            }

            if(bodyFlag){
                body+=line;
            }else{
                headers+=line;
            }
        }

        String[] reqParams = reqLine.split(" ");
        for(String i:reqParams){
            System.out.println(reqParams.length+": "+i);
        }
        String method = reqParams[0];
        String uri = reqParams[1];

        if(method.equalsIgnoreCase("get")){
            uri = uri.replace('/', ' ').trim();
            File file = new File("/public/"+uri);
            if(file.exists()){
                String statusLine = "HTTP/1.1 200 PITITO\r\n";
                body = readFile("C:\\Users\\L1 - PC\\Desktop\\httpServer\\src\\public\\"+uri);
                headers = "Content-Type: text/html\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "Connection: close\r\n" + "\r\n";
                response(statusLine, headers, body);
            } else{
                String statusLine = "HTTP/1.1 404 PITITO\r\n";
                body = readFile("C:\\Users\\L1 - PC\\Desktop\\httpServer\\src\\public\\404.html");
                headers = "Content-Type: text/html\r\n" +"Content-Length: "+body.length()+"\r\n"+"Connection: close\r\n" + "\r\n"+body;
                response(statusLine, headers, body);
            }
        }
    */
    }
    
    private void parseMethod(){
        String method = http.getMethod();
        switch(method){
            case "GET":
                prepareGet();
                break;
        }
    }

    private void prepareGet() {
        String dir = http.getDir();
        String version = http.getVersion();
        String msg;
        String body="";
        try {
            body = readFile(dir);
            msg = "200 success";
        } catch (Exception e) {
            msg = "404 not found";
        }
        
        
        
        
        Path fullPath = Paths.get(BASE_DIRECTORY, dir);
        byte[] fileData = null;
        String contentType=null;
        try {
            contentType = Files.probeContentType(fullPath);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fileData = Files.readAllBytes(fullPath);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String resLine = version+" "+msg+"\r\n";
        String headers = "Content-Type: "+contentType+"\r\n" +"Content-Length: "+fileData.length+"\r\n"+"Connection: close\r\n";
        String response = resLine+headers+"\r\n"+body;
        response(response.getBytes());
        response(fileData);
    }
}
