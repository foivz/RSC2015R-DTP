package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * Created by hrvoje on 22/11/15.
 */
public class KillRequestHandler  extends ResponseHandler {

    public KillRequestHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {

        //Log.i("hr.foi.debug", "UpdateHandler -- deserialized arguments: " + user.toString());

        if(response.getHttpCode() == 200) {
            Log.i("hr.foi.debug", "Kill request -- successfully removed user");

            // update session

            Toast.makeText(this.getContext(),
                    "Update successful", Toast.LENGTH_LONG).show();
            // TODO: finish()
            return true;
        } else {
            Log.w("hr.foi.debug",
                    "UpdateHandler -- update failed, server returned code " + response.getHttpCode());
            // show fail
            Toast.makeText(this.getContext(),
                    "Update failed, please try again ("+response.getHttpCode()+")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
