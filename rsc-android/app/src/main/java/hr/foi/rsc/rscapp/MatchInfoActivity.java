package hr.foi.rsc.rscapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.InputPrompt;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.adapters.PlayerListAdapter;
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
    Timer timer;
    Timer go;
    Person self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        self = SessionManager.getInstance(this).retrieveSession("person", Person.class);

        lvDetails = (ListView) findViewById(R.id.listViewMatchDetails);
        judge = (TextView) findViewById(R.id.textViewJudgeName);
        match = (TextView) findViewById(R.id.textView2);
        game = SessionManager.getInstance(this).retrieveSession("game", Game.class);
        judge.setText("Leon Palaić");
        match.setText(game.getName());
        myTeam = SessionManager.getInstance(this).retrieveSession("team", Team.class);

        timer = new Timer();
        timer.schedule(joinPlayer, 1000, 1000);
        go = new Timer();
        go.schedule(startMatchTimerTask, 1000, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_team_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.be_ready) {
            ServiceParams params;
            if(self.isReady() == 0) {
                item.setIcon(android.R.drawable.presence_online);
                self.setReady(1);
                SessionManager.getInstance(this).createSession(self, "person");
            } else {
                item.setIcon(android.R.drawable.presence_busy);
                self.setReady(0);
                SessionManager.getInstance(this).createSession(self, "person");
            }
            params = new ServiceParams(getString(R.string.persons_path)
                    + self.getIdPerson() + "/ready", HttpMethod.PUT, null);
            new ServiceAsyncTask(readyChange).execute(params);
            return true;
        } else if(item.getItemId() == R.id.create_qr) {
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(getIntent().getExtras().getString("code"), BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                ImageView view = new ImageView(getApplicationContext());
                view.setImageBitmap(bmp);
                InputPrompt prompt = new InputPrompt(this, view);
                prompt.prepare("Show this to your teammate or enemy", listenerClick, "OK", listenerClick, "OK");
                prompt.showPrompt();

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener listenerClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    ServiceResponseHandler readyChange = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            return true;
        }

        @Override
        public void onPostSend() {

        }
    };

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

    ServiceResponseHandler startMatch = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Log.i("hr.foi.debug", response.getJsonResponse());
                Game game = new Gson().fromJson(response.getJsonResponse(), Game.class);
                Intent play = new Intent(getApplicationContext(), PlayGameActivity.class);
                play.putExtra("gameid", game.getIdGame());
                play.putExtra("gamecode", game.getCode());
                stopTimers();
                startActivity(play);
                finish();
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

    TimerTask startMatchTimerTask = new TimerTask() {
        @Override
        public void run() {
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + "isReady/"
                    + game.getIdGame(), HttpMethod.GET, null);
            new ServiceAsyncTask(startMatch).execute(params);
        }
    };

    void stopTimers() {
        timer.cancel();
        timer.purge();
        go.cancel();
        go.purge();
    }

    @Override
    public void onBackPressed() {
        stopTimers();
        super.onBackPressed();
    }
}
