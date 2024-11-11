package com.mycompany.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientHandler extends Thread{
    private final Socket client;
    private OutputStream out;
    private BufferedReader in;
    private String req;
    private boolean fullRequest;

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
                parseRequest(req);
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

    private void response(String statusLine, String headers, String body){
        String response = statusLine + headers + body;
        try{
            out.write(response.getBytes());
            out.flush();
        }catch(IOException ex){

        }   
    }

    private String readFile(String path){
        String filePath = path;//"C:\\Users\\L1 - PC\\Desktop\\httpServer\\src\\public\\index2.html";
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
        System.out.println(body.toString());
        return body.toString();
    }

    private void parseRequest(String req) {
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
    }
}
