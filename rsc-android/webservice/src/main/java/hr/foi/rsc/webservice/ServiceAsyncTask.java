package hr.foi.rsc.webservice;

import android.content.res.Resources;
import android.media.session.MediaSessionManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;


import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Token;

import static org.springframework.http.HttpMethod.*;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceAsyncTask extends AsyncTask<ServiceParams, Void, ServiceResponse> {

    ServiceParams sp;
    ResponseEntity<String> resp;

    static final String mainUrl = "http://46.101.173.23:8080";
    ServiceResponseHandler handler;
    String url;
    HttpMethod method;

    public ServiceAsyncTask(ServiceResponseHandler handler) {
        this.handler = handler;
    }

    /**
     * starts the progress dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        handler.onPreSend();
    }

    /**
     * initiates calles to web service
     * @param params parameters to use in service call
     * @return service response (http code + json)
     */
    @Override
    protected ServiceResponse doInBackground(ServiceParams... params) {

        sp = params[0];

        ServiceResponse jsonResponse = new ServiceResponse();

        url = mainUrl+sp.getUrl();
        method = sp.getMethod();


        Log.i(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- Initiating service call to " + sp.getUrl());

        RestTemplate rest=new RestTemplate();

        rest.getMessageConverters().add(new FormHttpMessageConverter());
        rest.getMessageConverters().add(new StringHttpMessageConverter());
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        rest.setErrorHandler(new RestResponseErrorHandler());

        try {

            if (sp.isTokenRequest()) {
                resp = ServiceCaller.tokenRequest(url, (Credentials) sp.getObject(), rest);
            }
            else{
                resp = sp.getToken() == null ?
                          ServiceCaller.getResult(url, sp.getObject(), rest, method)
                        : ServiceCaller.getResult(url, sp.getObject(), sp.getToken(), rest, method);
            }
            jsonResponse.setHttpCode(resp.getStatusCode().value());
            jsonResponse.setJsonResponse(resp.getBody());

        } catch (RestException ex) {

            Log.i(ServiceCaller.LOG_TAG, "tusam");
            jsonResponse.setJsonResponse(ex.getBody().toString());
            jsonResponse.setHttpCode(ex.getStatusCode());

        }

        return jsonResponse;
    }

    /**
     * calls handler sent through service parameters and stops the progress dialog
     * @param s service response (http code + json)
     */
    @Override
    protected void onPostExecute(ServiceResponse s) {
        if(sp != null) {
            Log.i(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- Calling service response handler");
            handler.handleResponse(s);
        } else {
            Log.w(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- Could not call service response handler");
        }
        handler.onPostSend();
    }
}
