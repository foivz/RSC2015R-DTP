package hr.foi.rsc.rscapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import hr.foi.rsc.core.Input;
import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.handlers.RegistrationHandler;
import hr.foi.rsc.rscapp.handlers.ResponseHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class ChooseTeamActivity extends Activity {

    String code;
    TextView redView;
    TextView blueView;
    Team redTeam;
    Team blueTeam;
    Person self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        self = SessionManager.getInstance(getApplicationContext()).retrieveSession("person", Person.class);

        ServiceParams params = new ServiceParams(getString(R.string.game_code_path) + code, HttpMethod.GET, null);
        new ServiceAsyncTask(handler).execute(params);
    }

    View.OnClickListener onBlue = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendRequest(blueTeam);
        }
    };

    View.OnClickListener onRed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendRequest(redTeam);
        }
    };


    void sendRequest(Team team) {
        SessionManager.getInstance(getApplicationContext()).createSession(team, "team");
        ServiceParams params = new ServiceParams(getString(R.string.team_path) + team.getIdTeam()
                + getString(R.string.persons_path) + self.getIdPerson(), HttpMethod.POST, null);
        new ServiceAsyncTask(handlerTransit).execute(params);
    }

    ServiceResponseHandler handler = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Game game = new Gson().fromJson(response.getJsonResponse(), Game.class);
                if(game != null && game.getTeam().size() != 0) {

                    setContentView(R.layout.activity_choose_team);

                    ImageButton red = (ImageButton) findViewById(R.id.ibCtmRed);
                    red.setOnClickListener(onRed);

                    ImageButton blue = (ImageButton) findViewById(R.id.ibCtmBlue);
                    blue.setOnClickListener(onBlue);

                    SessionManager.getInstance(getApplicationContext()).createSession(game, "game");
                    redTeam = game.getTeam().get(0);
                    blueTeam = game.getTeam().get(1);

                    redView = (TextView) findViewById(R.id.textViewTeamREd);
                    blueView = (TextView) findViewById(R.id.textViewTeamBlue);

                    redView.setText(redTeam.getName());
                    blueView.setText(blueTeam.getName());

                    red.setEnabled(true);
                    blue.setEnabled(true);

                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "There is no team found for code " + code,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Failed to fetch teams for match!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }

        @Override
        public void onPostSend() {

        }
    };

    ServiceResponseHandler handlerTransit = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Intent intent = new Intent(getApplicationContext(), MatchInfoActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
                finish();
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        @Override
        public void onPostSend() {

        }
    };

}
