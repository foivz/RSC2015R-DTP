package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.Serializable;
import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.rscapp.JoinActivity;
import hr.foi.rsc.rscapp.UserProfileActivity;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * Created by paz on 18.11.15..
 */
public class LoginHandler extends ResponseHandler {

    public LoginHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {

        if(response.getHttpCode() == 200) {

            // convert json to token object
            Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
            // save person to session
            SessionManager manager = SessionManager.getInstance(this.getContext());
            if(manager.createSession(person, "person")) {


                Log.i("hr.foi.debug",
                        "LoginHandler -- valid user, created token: " + person.toString()
                                + ", going into ");


                Intent intent = new Intent(this.getContext(), JoinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getContext().startActivity(intent);
                return true;

            } else {
                // login failed
                Toast.makeText(this.getContext(),
                        "Internal application error, please try again", Toast.LENGTH_LONG).show();
                return false;
            }

        } else  {
            // http code different from 200 OK
            // TODO: seperate invalid credentials and cannot connect to server

            Log.i("hr.foi.debug", "TokenHandler -- invalid credentials sent");
            Toast.makeText(this.getContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

