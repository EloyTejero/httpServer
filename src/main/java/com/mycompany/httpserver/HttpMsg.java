/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver;

/**
 *
 * @author Eloy
 */
public class HttpMsg {
    private String reqLine;
    private String headers;
    private String body;

    public HttpMsg() {
        this.reqLine="";
        this.headers="";
        this.body="";
    }

    public String getReqLine() {
        return reqLine;
    }

    public void setReqLine(String reqLine) {
        this.reqLine = reqLine;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
