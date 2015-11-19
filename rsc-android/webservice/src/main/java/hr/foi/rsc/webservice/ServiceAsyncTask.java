package hr.foi.rsc.webservice;

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
    String method;

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
        Looper.prepare();

        ServiceResponse jsonResponse = null;

        url = mainUrl+sp.getUrl();
        method = sp.getMethod();

        Log.i(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- Initiating service call to " + sp.getUrl());


        try {

                RestTemplate rest=new RestTemplate();


                rest.getMessageConverters().add(new FormHttpMessageConverter());
                rest.getMessageConverters().add(new StringHttpMessageConverter());
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                if(sp.getUrl().equals("/oauth/token")) {
                    resp = tokenRequest((Credentials) sp.getObject(), rest);
                    jsonResponse= new ServiceResponse();
                    jsonResponse.setHttpCode(resp.getStatusCode().value());
                    jsonResponse.setJsonResponse(resp.getBody());
                }
                else{

                    resp=getResult(sp.getObject(),sp.getToken(),rest,method);
                    jsonResponse=new ServiceResponse();
                    jsonResponse.setHttpCode(resp.getStatusCode().value());
                    jsonResponse.setJsonResponse(resp.getBody());

                }





        }catch(RestClientException a) {

                a.printStackTrace();
                Log.i(ServiceCaller.LOG_TAG, "Eror making request");

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

    public ResponseEntity<String> getResult(Serializable object, Serializable token, RestTemplate rest,String method){

            Token userToken =(Token) token;

            String authorization = "Bearer "+ userToken.getAccessToken();

            Log.i(ServiceCaller.LOG_TAG, "Authorization token:" + authorization);

            HttpHeaders headers=new HttpHeaders();
            headers.add("Authorization",  authorization);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity request=new HttpEntity(object,headers);

            ResponseEntity<String> result=rest.exchange(url, HttpMethod.POST, request, String.class);

            Log.i(ServiceCaller.LOG_TAG, "Response" + result.getBody().toString());
            Log.i(ServiceCaller.LOG_TAG, "Response" + result.getStatusCode().value());

            return result;

    }

    public ResponseEntity<String> tokenRequest(Credentials cred,RestTemplate rest){

        String authorization =
                new String(Base64Utils.encode("angular:davinci2015".getBytes()));

        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization", "Basic " + authorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", cred.getUsername());
        bodyMap.add("password", cred.getPassword());
        bodyMap.add("grant_type","password");
        bodyMap.add("scope", "read write");
        bodyMap.add("client_id", "angular");
        bodyMap.add("client_secret", "davinci2015");

        HttpEntity<MultiValueMap<String, String>> request=new
                            HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);

        Log.i(ServiceCaller.LOG_TAG, "Response" + request.getHeaders().toString());
        Log.i(ServiceCaller.LOG_TAG, "Response" + request.getBody().toString());

        ResponseEntity<String> response=rest.exchange(url, HttpMethod.POST, request, String.class);
        Log.i(ServiceCaller.LOG_TAG, "Response" + response.getBody().toString());
        Log.i(ServiceCaller.LOG_TAG, "Response" + response.getStatusCode().value());

        return response;
    }
}
