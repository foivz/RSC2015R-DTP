package hr.foi.rsc.rscapp.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Token;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;

/**
 * handles login calls
 * Created by Tomislav Turek on 07.11.15..
 */
public class TokenHandler extends ResponseHandler {

    public TokenHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {

        if(response.getHttpCode() == 200) {

            // convert json to token object
            Token personToken = new Gson().fromJson(response.getJsonResponse(), Token.class);
            // save person to session
            SessionManager manager = SessionManager.getInstance(this.context);
            if(manager.createSession(personToken, "token")) {

                Token sessionToken = manager.retrieveSession("token", Token.class);
                Log.i("hr.foi.debug",
                        "TokenHandler -- valid user, created token: " + sessionToken.getAccessToken()
                                + ", going to make request with token");


                LoginHandler LoginHandler = new LoginHandler(TokenHandler.this.context);
                Credentials credentials=manager.retrieveSession("credentials",Credentials.class);

                Log.i("hr.foi.debug",
                        "TokenHandler -- credentials: " + credentials.getUsername()+" "+credentials.getPassword()
                                + ", going to make request with token");

                sessionToken = manager.retrieveSession("token", Token.class);

                Log.i("hr.foi.debug",
                        "TokenHandler -- token: " + sessionToken.getAccessToken()
                                + ", going to make request with token");

                ServiceParams params = new ServiceParams(this.context.getString(hr.foi.rsc.webservice.R.string.login_path_token),
                        HttpMethod.POST, credentials,sessionToken);
                new ServiceAsyncTask(LoginHandler).execute(params);



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

            Log.i("hr.foi.debug", "TokenHandler -- invalid credentials sent");
            Toast.makeText(this.context, "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
