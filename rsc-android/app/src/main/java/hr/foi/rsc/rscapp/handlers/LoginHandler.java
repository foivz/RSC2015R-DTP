package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * handles login calls
 * Created by Tomislav Turek on 07.11.15..
 */
public class LoginHandler extends ResponseHandler {

    public LoginHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Log.i("hr.foi.debug", "LoginHandler -- Got response: " + response.toString());

        if(response.getHttpCode() == 200) {

            // convert json to person object
            Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
            // save person to session
            SessionManager manager = SessionManager.getInstance(this.context);
            if(manager.createSession(person, "person")) {

                Person sessionPerson = manager.retrieveSession("person", Person.class);
                Log.i("hr.foi.debug",
                        "LoginHandler -- valid user, created session: " + sessionPerson.toString()
                                + ", proceeding to group activity");
                // TODO: token stuff

                // TODO: start main activity

                //Intent intent = new Intent(this.context, Activity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //this.context.startActivity(intent);
                return true;

            } else {
                // login failed
                Toast.makeText(this.context,
                        "Internal application error, please try again", Toast.LENGTH_LONG).show();
                return false;
            }

        } else  {
            // http code different from 200 OK
            // TODO: seperate invalid credentials and cannot connect to server

            Log.i("hr.foi.debug", "LoginHandler -- invalid credentials sent");
            Toast.makeText(this.context, "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}