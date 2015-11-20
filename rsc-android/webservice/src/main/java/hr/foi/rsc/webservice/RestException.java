package hr.foi.rsc.webservice;

import java.io.IOException;

/**
 * Created by paz on 19.11.15..
 * Custom exception that holds code of error and msg from web server
 */
public class RestException extends RuntimeException  {

    private int statusCode;

    private String body;

    public RestException(String msg) {

        super(msg);

    }

    public RestException(int statusCode, String body, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
