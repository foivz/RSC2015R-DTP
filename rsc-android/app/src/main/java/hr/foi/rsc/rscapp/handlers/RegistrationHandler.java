package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * handles registration calls
 * Created by Tomislav Turek on 07.11.15..
 */
public class RegistrationHandler extends ResponseHandler {

    public RegistrationHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Credentials credentials = (Credentials) this.args[0];
        Log.i("hr.foi.debug", "RegistrationHandler -- deserialized arguments: " + credentials.toString());

        if(response.getHttpCode() == 200) {
            Log.i("hr.foi.debug", "RegistrationHandler -- successfully registered user, logging in now...");
            // login
            String loginPath = this.context.getString(hr.foi.rsc.webservice.R.string.login_path);
            TokenHandler tokenHandler = new TokenHandler(this.context);
            ServiceParams params = new ServiceParams(loginPath,
                    "POST",credentials);

            new ServiceAsyncTask(tokenHandler).execute(params);
            return true;
        } else {
            Log.w("hr.foi.debug",
                    "RegistrationHandler -- registration failed, server returned code " + response.getHttpCode());
            // show fail
            Toast.makeText(this.context,
                    "Registration failed, please try again (" + response.getHttpCode() + ")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
