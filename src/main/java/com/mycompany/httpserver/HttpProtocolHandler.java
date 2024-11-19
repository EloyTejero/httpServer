package com.mycompany.httpserver;

public class HttpProtocolHandler {
    HttpMsg httpMessage = new HttpMsg();
    boolean messageCompleted;
    boolean headersCompleted;

    public HttpProtocolHandler() {
    }
    
    public void parseRequest(String message){
        message = message.trim();
        System.out.println("Mensaje vacio:"+message.isEmpty());
        System.out.println("Message recieved: "+message);
        if(httpMessage.getReqLine().isEmpty()){
            httpMessage.setReqLine(message);
            System.out.println("REQLINE ");
            return;
        }
        if(message.isEmpty()){
            headersCompleted = true;
            System.out.println("Mensaje VACIOOO");
        }
        if(!headersCompleted){
            sumHeader(message);
            return;
        }
        if(!messageCompleted){
            sumBody(message);
        }
        if(httpMessage.getBody().length() == getContentLength() || getMethod().equalsIgnoreCase("GET")){
            messageCompleted = true;
            System.out.println("mensaje COMPLETOOOOo");
        }
    }
    
    public String getRequestMessage(){
        String reqLine = httpMessage.getReqLine();
        String headers = httpMessage.getHeaders();
        String body = httpMessage.getBody();
        return reqLine+"\r\n"+headers+"\r\n"+body;
    }
    
    public String getMethod(){
        return httpMessage.getReqLine().split(" ")[0];
    }
    
    public String getDir(){
        return httpMessage.getReqLine().split(" ")[1];
    }
    
    public String getVersion(){
        return httpMessage.getReqLine().split(" ")[2];
    }
    
    private void sumHeader(String header){
        String totalHeaders = httpMessage.getHeaders();
        httpMessage.setHeaders(totalHeaders+header+"\r\n");
    }
    
    private void sumBody(String bodyPart){
        String body = httpMessage.getBody();
        httpMessage.setBody(body+bodyPart);
    }
    
    private int getContentLength() {
        String headers = httpMessage.getHeaders();
        for (String header : headers.split("\r\n")) {
            if (header.startsWith("Content-Length:")) {
                return Integer.parseInt(header.split(":")[1].trim());
            }
        }
        return 0;
    }

    public boolean isMessageCompleted() {
        return messageCompleted;
    }
    
    
}
