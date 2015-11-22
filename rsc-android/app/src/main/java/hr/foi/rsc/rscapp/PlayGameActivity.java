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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.AlertPrompt;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Notification;
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
    Timer task;
    int gameId;
    String gameString;
    Person self;
    Team myTeam;
    ServiceParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);

        gameId = getIntent().getExtras().getInt("gameid");
        gameString = getIntent().getExtras().getString("gamecode");
        gameStatus = new Timer();
        gameStatus.schedule(getStatus, 1000, 1000);

        myTeam = SessionManager.getInstance(getApplicationContext()).retrieveSession("team", Team.class);
        self = SessionManager.getInstance(getApplicationContext()).retrieveSession("person", Person.class);
        task = new Timer();
        task.schedule(tasker, 1000, 1000);
        params = new ServiceParams("/game/isReady/" + gameId, HttpMethod.GET, null);
    }

    void cancelTimers() {
        task.cancel();
        task.purge();
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

    TimerTask tasker = new TimerTask() {
        @Override
        public void run() {
            ServiceParams params = new ServiceParams("/notification/" + self.getIdPerson()
                    + "/team/" + myTeam.getIdTeam(), HttpMethod.GET, null);
            new ServiceAsyncTask(newNotify).execute(params);
        }
    };

    Notification notification;
    void showNotification(Notification n) {
        new ServiceAsyncTask(popup).execute(new ServiceParams("/person/" + n.getIdPerson(), HttpMethod.GET, null));
        notification = n;
    }

    ServiceResponseHandler popup = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Person wat = new Gson().fromJson(response.getJsonResponse(), Person.class);
                AlertPrompt prompt = new AlertPrompt(getApplicationContext());
                prompt.prepare(wat.getName() + " " + wat.getSurname() + ": " + notification.getName(), null, 0, null, 0);
            }
            return true;
        }

        @Override
        public void onPostSend() {

        }
    };

    ServiceResponseHandler newNotify = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Type listType = new TypeToken<ArrayList<Notification>>() {
                }.getType();
                ArrayList<Notification> notifications = new Gson().fromJson(response.getJsonResponse(), listType);
                for(Notification n : notifications) {
                    if(n.getIdPerson() != self.getIdPerson()) {
                        showNotification(n);
                    }
                }
                ServiceParams params = new ServiceParams("/notification/" + self.getIdPerson(), HttpMethod.POST, notifications);
                new ServiceAsyncTask(new ServiceResponseHandler() {
                    @Override
                    public void onPreSend() {

                    }

                    @Override
                    public boolean handleResponse(ServiceResponse response) {
                        return false;
                    }

                    @Override
                    public void onPostSend() {

                    }
                }).execute(params);
            }
            return true;
        }

        @Override
        public void onPostSend() {

        }
    };

    @Override
    protected void onStop() {
        cancelTimers();
        super.onStop();
    }
}
