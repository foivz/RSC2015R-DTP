package hr.foi.rsc.webservice;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceParams {

    private String url;
    private String method;
    private Serializable object;
    private Serializable token;

    public ServiceParams(String url, String method, Serializable object) {
        this.url = url;
        this.method = method;
        this.object = object;
    }
    public ServiceParams(String url, String method, Serializable object,Serializable token) {
        this.url = url;
        this.method = method;
        this.object = object;
        this.token = token;
    }

    public void setToken(Serializable object){ this.token=object; }

    public Serializable getToken(){ return this.token; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Serializable getObject() {
        return object;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return this.url + " " + this.method;
    }

}
