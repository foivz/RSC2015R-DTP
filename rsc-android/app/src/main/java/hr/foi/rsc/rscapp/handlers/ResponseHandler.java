package hr.foi.rsc.rscapp.handlers;

import android.content.Context;

import java.io.Serializable;

import hr.foi.rsc.core.prompts.LoadingPrompt;
import hr.foi.rsc.webservice.ServiceResponseHandler;

/**
 *
 * Created by Tomislav Turek on 07.11.15..
 */
public abstract class ResponseHandler implements ServiceResponseHandler {

    Context context;
    Object[] args;
    LoadingPrompt loadingPrompt;

    public ResponseHandler(Serializable... args) {
        this.args = args;
    }

    public ResponseHandler(Context context, Serializable... args) {
        this.context = context;
        this.args = args;
        this.loadingPrompt = new LoadingPrompt(this.context);
    }

    @Override
    public void onPreSend() {
        loadingPrompt.showPrompt();
    }

    @Override
    public void onPostSend() {
        loadingPrompt.hidePrompt();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public LoadingPrompt getLoadingPrompt() {
        return loadingPrompt;
    }

    public void setLoadingPrompt(LoadingPrompt loadingPrompt) {
        this.loadingPrompt = loadingPrompt;
    }
}
