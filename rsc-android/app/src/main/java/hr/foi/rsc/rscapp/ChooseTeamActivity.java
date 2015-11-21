package hr.foi.rsc.rscapp;

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
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.handlers.RegistrationHandler;
import hr.foi.rsc.rscapp.handlers.ResponseHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class ChooseTeamActivity extends AppCompatActivity {

    String code;
    TextView red;
    TextView blue;
    Team redTeam;
    Team blueTeam;
    Person self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        self = SessionManager.getInstance(getApplicationContext()).retrieveSession("person", Person.class);

        ServiceParams params = new ServiceParams(getString(R.string.game_path) + code, HttpMethod.POST, null);
        new ServiceAsyncTask(handler).execute(params);

        ImageButton red = (ImageButton) findViewById(R.id.ibCtmRed);
        red.setEnabled(false);
        red.setOnClickListener(onRed);

        ImageButton blue = (ImageButton) findViewById(R.id.ibCtmBlue);
        blue.setEnabled(false);
        blue.setOnClickListener(onBlue);
    }

    View.OnClickListener onBlue = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + blueTeam.getIdTeam()
                    + getString(R.string.persons_path) + self.getIdPerson(), HttpMethod.POST, null);
            new ServiceAsyncTask(handlerTransit).execute(params);
        }
    };

    View.OnClickListener onRed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + redTeam.getIdTeam()
                    + getString(R.string.persons_path) + self.getIdPerson(), HttpMethod.POST, null);
            new ServiceAsyncTask(handlerTransit).execute(params);
        }
    };

    ServiceResponseHandler handler = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Type type = new TypeToken<ArrayList<Team>>() {}.getType();
                List<Team> teams = new Gson().fromJson(response.getJsonResponse(), type);
                if(teams != null && teams.size() != 0) {
                    redTeam = teams.get(0);
                    blueTeam = teams.get(1);
                    red.setText(redTeam.getName());
                    blue.setText(blueTeam.getName());
                    red.setEnabled(true);
                    blue.setEnabled(true);
                    setContentView(R.layout.activity_choose_team);
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "There is no team found for code " + code,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Failed to fetch teams for match!", Toast.LENGTH_LONG).show();
            }
            return false;
        }

        @Override
        public void onPostSend() {

        }
    };

    ResponseHandler handlerTransit = new ResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Intent intent = new Intent(getApplicationContext(), null);
                intent.putExtra("code", code);
                startActivity(intent);
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

}
