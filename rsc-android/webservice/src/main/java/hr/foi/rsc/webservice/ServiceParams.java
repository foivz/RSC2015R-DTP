package hr.foi.rsc.webservice;

import org.springframework.http.HttpMethod;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceParams {

    private String url;
    private HttpMethod method;
    private Serializable object;
    private Serializable token;
    private boolean tokenRequest;

    public ServiceParams(String url, HttpMethod method, Serializable object) {
        this.url = url;
        this.method = method;
        this.object = object;
        this.tokenRequest = false;
    }

    public ServiceParams(String url, HttpMethod method, Serializable object, Serializable token) {
        this.url = url;
        this.method = method;
        this.object = object;
        this.token = token;
        this.tokenRequest = false;
    }

    public ServiceParams(String url, HttpMethod method, Serializable object, Serializable token, boolean tokenRequest) {
        this.url = url;
        this.method = method;
        this.object = object;
        this.token = token;
        this.tokenRequest = tokenRequest;
    }

    public void setToken(Serializable object){ this.token=object; }

    public Serializable getToken(){ return this.token; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Serializable getObject() {
        return object;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    public boolean isTokenRequest() {
        return tokenRequest;
    }

    public void setTokenRequest(boolean tokenRequest) {
        this.tokenRequest = tokenRequest;
    }

    @Override
    public String toString() {
        return this.url + " " + this.method;
    }

}
