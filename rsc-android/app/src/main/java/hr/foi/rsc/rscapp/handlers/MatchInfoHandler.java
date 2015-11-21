package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.rscapp.UserProfileActivity;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * Created by hrvoje on 21/11/15.
 */
public class MatchInfoHandler  extends ResponseHandler {

    public MatchInfoHandler(Context context, Serializable... args) {
        super(context, args);
    }
    @Override
    public boolean handleResponse(ServiceResponse response) {

        if(response.getHttpCode() == 200) {

            // convert json to token object
            Game game = new Gson().fromJson(response.getJsonResponse(), Game.class);
            // save person to session
            SessionManager manager = SessionManager.getInstance(this.getContext());
            if(manager.createSession(game, "game")) {
                return true;
            } else {
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
