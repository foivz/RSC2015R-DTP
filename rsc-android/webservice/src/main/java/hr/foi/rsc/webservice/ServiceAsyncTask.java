package hr.foi.rsc.webservice;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceAsyncTask extends AsyncTask<ServiceParams, Void, ServiceResponse> {

    ServiceParams sp;
    // TODO: change url
    static final String mainUrl = "http://teamup-puding.rhcloud.com";
    ServiceResponseHandler handler;

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

        Log.i(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- Initiating service call to " + sp.getUrl());
        try {
            URL url = new URL(mainUrl+sp.getUrl());
            String method = sp.getMethod();
            Serializable object = sp.getObject();
            jsonResponse = ServiceCaller.call(url, method, object);
        } catch (MalformedURLException e) {
            // TODO: catch impl
            Log.e(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- failed to create URL from string " + sp.getUrl());
        } catch (IOException e) {
            // TODO: catch impl
            Log.e(ServiceCaller.LOG_TAG, "ServiceAsyncTask -- cannot open connection to " + sp.getUrl());
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