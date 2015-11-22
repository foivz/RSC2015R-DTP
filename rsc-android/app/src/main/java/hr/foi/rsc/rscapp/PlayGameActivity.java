package hr.foi.rsc.rscapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.zxing.integration.android.IntentIntegrator;

import org.springframework.http.HttpMethod;

import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.AlertPrompt;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.rscapp.adapters.PageAdapter;
import hr.foi.rsc.rscapp.fragments.GameFragment;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class PlayGameActivity extends AppCompatActivity {

    GestureDetector gestureDetector;
    Timer gameStatus;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);

        SessionManager.getInstance(this).retrieveSession("game", Game.class);
        gameStatus = new Timer();
        gameStatus.schedule(getStatus, 1000, 1000);
    }

    void cancelTimers() {
        gameStatus.cancel();
        gameStatus.purge();
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener signOutListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelTimers();
                PlayGameActivity.super.onBackPressed();
            }
        };
        AlertPrompt signOutPrompt = new AlertPrompt(this);
        signOutPrompt.prepare(R.string.leave_match, signOutListener,
                R.string.leave, null, R.string.cancel);
        signOutPrompt.showPrompt();
    }

    TimerTask getStatus = new TimerTask() {
        @Override
        public void run() {
            ServiceParams params = new ServiceParams("/game/isReady/" + game.getIdGame(), HttpMethod.GET, null);
            new ServiceAsyncTask(new ServiceResponseHandler() {
                @Override
                public void onPreSend() {

                }

                @Override
                public boolean handleResponse(ServiceResponse response) {
                    if(response.getHttpCode() == 404) {
                        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public void onPostSend() {

                }
            }).execute(params);
        }
    };
}
