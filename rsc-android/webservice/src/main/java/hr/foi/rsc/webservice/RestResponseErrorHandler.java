package hr.foi.rsc.webservice;

import android.util.Log;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paz on 19.11.15..
 * Error handler for response with http status code
 */
public class RestResponseErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler=new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws RestException,IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws RestException,IOException {

        Log.i("hr.foi.debug", "ErorHandler -- handlercreated");
        RestException exception = new RestException("Error with code" + response.getStatusCode().value());

        exception.setBody(response.getBody().toString());
        exception.setStatusCode(response.getStatusCode().value());

        Log.i("hr.foi.debug", "ErorHandler " + exception.getBody());
        Log.i("hr.foi.debug", "ErorHandler "+ exception.getStatusCode());

        throw exception;
    }
}
