package hr.foi.rsc.rscapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class MatchInfoActivity extends AppCompatActivity {

    String code;
    private ListView lvDetails;
    private String matchName;
    private List<String> matchDetails;
    TextView judge;
    TextView match;
    Team myTeam;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        lvDetails = (ListView) findViewById(R.id.listViewMatchDetails);
        judge = (TextView) findViewById(R.id.textViewJudgeName);
        match = (TextView) findViewById(R.id.textView2);
        game = SessionManager.getInstance(this).retrieveSession("game", Game.class);
        judge.setText("Leon Palaić");
        match.setText(game.getName());
        myTeam = SessionManager.getInstance(this).retrieveSession("team", Team.class);

        Timer timer = new Timer();
        timer.schedule(joinPlayer, 1000, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_team_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.be_ready) {
            if(item.getIcon() == getResources().getDrawable(android.R.drawable.presence_busy)) {
                item.setIcon(android.R.drawable.presence_online);
            } else {
                item.setIcon(android.R.drawable.presence_busy);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ServiceResponseHandler playerStatus = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {

            if(response.getHttpCode() == 200) {
                Type listType = new TypeToken<ArrayList<Person>>() {
                }.getType();
                ArrayList<Person> teamMembers = new Gson().fromJson(response.getJsonResponse(), listType);
                if(teamMembers != null && teamMembers.size() != 0) {
                    lvDetails.setAdapter(new PlayerListAdapter(getApplicationContext(), 0, teamMembers));
                }
            } else {

            }
            return true;
        }

        @Override
        public void onPostSend() {

        }
    };


    TimerTask joinPlayer = new TimerTask() {
        @Override
        public void run() {
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + game.getIdGame()
                    + getString(R.string.team_path) + myTeam.getIdTeam(), HttpMethod.GET, null);
            new ServiceAsyncTask(playerStatus).execute(params);
        }
    };

}
