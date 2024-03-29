package hr.foi.rsc.webservice;

/**
 *
 * Created by Maja Vracan on 26.10.2015..
 */
public class ServiceResponse {

    int httpCode;
    String jsonResponse;

    public ServiceResponse() {
    }

    public ServiceResponse(int httpCode, String jsonResponse) {
        this.httpCode = httpCode;
        this.jsonResponse = jsonResponse;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String toString() {
        return this.httpCode + " " + this.jsonResponse;
    }
}
