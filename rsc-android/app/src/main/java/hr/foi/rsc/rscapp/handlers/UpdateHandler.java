package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * handles profile updates
 * Created by Tomislav Turek on 07.11.15..
 */
public class UpdateHandler extends ResponseHandler {

    public UpdateHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Person user = (Person) this.args[0];
        Log.i("hr.foi.debug", "UpdateHandler -- deserialized arguments: " + user.toString());

        if(response.getHttpCode() == 200) {
            Log.i("hr.foi.debug", "UpdateHandler -- successfully updated user");

            // update session
            SessionManager manager= SessionManager.getInstance(this.context);
            manager.destroySession("person");
            manager.createSession(user, "person");
            Toast.makeText(this.context,
                    "Update successful", Toast.LENGTH_LONG).show();
            // TODO: finish()
            return true;
        } else {
            Log.w("hr.foi.debug",
                    "UpdateHandler -- update failed, server returned code " + response.getHttpCode());
            // show fail
            Toast.makeText(this.context,
                    "Update failed, please try again ("+response.getHttpCode()+")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
