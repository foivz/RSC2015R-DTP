package hr.foi.rsc.webservice;

import android.util.Log;

import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Token;

import static org.springframework.http.HttpMethod.POST;

/**
 * makes HTTP calls to web service
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceCaller {

    public static final String LOG_TAG = "hr.foi.debug";

    /**
     * calls to url with desired method, use object param to send to url
     * @param url url to call
     * @param method method to use
     * @param object object to send
     * @return response in json format
     * @throws IOException when connection cannot open
     */
    public static ServiceResponse call(URL url, String method, Serializable object) throws IOException {
        Log.i(LOG_TAG, "ServiceCaller -- initiating");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // connection.setRequestProperty("Authorization", token);

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(method);
        connection.connect();

        Log.i(LOG_TAG, "ServiceCaller -- successfully connected to service: " + url.toString());
        if(object != null) {
            // write
            Log.i("hr.foi.teamup.debug", "ServiceCaller -- sending object:"
                    + object.toString() + " to " + url.toString());
            OutputStream os = connection.getOutputStream();
            os.write(new Gson().toJson(object).getBytes());
            os.close();
        }

        Log.i(LOG_TAG, "ServiceCaller -- receiving response from service " + url.toString());
        // read
        int code = connection.getResponseCode();
        StringBuilder json = new StringBuilder();
        if(code == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        connection.disconnect();

        Log.i(LOG_TAG, "ServiceCaller -- received response: "
                + json.toString() + " from " + url.toString());
        return new ServiceResponse(code, json.toString());
    }

    public static ResponseEntity<String> getResult(String url, Serializable object, RestTemplate rest,HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity request = new HttpEntity(object, headers);

        ResponseEntity<String> result = null;

        result = rest.exchange(url, method, request, String.class);

        if (result.getBody() != null)
            Log.i(ServiceCaller.LOG_TAG, "Response" + result.getBody().toString());

        Log.i(ServiceCaller.LOG_TAG, "Response" + result.getStatusCode().value());

        return result;
    }

        /**
         * sending object and token to webservice
         */
    public static ResponseEntity<String> getResult(String url, Serializable object, Serializable token, RestTemplate rest,HttpMethod method){

        Token userToken =(Token) token;

        String authorization = "Bearer "+ userToken.getAccessToken();

        Log.i(ServiceCaller.LOG_TAG, "Authorization token:" + authorization);

        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization", authorization);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity request=new HttpEntity(object,headers);

        ResponseEntity<String> result = null;

        result = rest.exchange(url, method, request, String.class);

        if(result.getBody()!=null)
            Log.i(ServiceCaller.LOG_TAG, "Response" + result.getBody().toString());

        Log.i(ServiceCaller.LOG_TAG, "Response" + result.getStatusCode().value());

        return result;
    }

    /**
     * Sending req to server for token
     * @param cred user credentials
     * @return s serviceresponse (json details of token)
     */
    public static ResponseEntity<String> tokenRequest(String url, Credentials cred, RestTemplate rest){

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


        ResponseEntity<String> response=rest.exchange(url, POST, request, String.class);
        Log.i(ServiceCaller.LOG_TAG, "Response" + response.getBody().toString());
        Log.i(ServiceCaller.LOG_TAG, "Response" + response.getStatusCode().value());

        return response;
    }

}
