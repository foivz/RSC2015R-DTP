package hr.foi.rsc.rscapp;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.rscapp.adapters.PageAdapter;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class PlayGameActivity extends AppCompatActivity {

    GestureDetector gestureDetector;
    Person self;
    Team myTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        gestureDetector = new GestureDetector(this, onGesture);

        self = SessionManager.getInstance(this).retrieveSession("person", Person.class);
        myTeam = SessionManager.getInstance(this).retrieveSession("team", Team.class);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener onGesture = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            int fingers = e.getPointerCount();
            String message;
            if(fingers == 2) {
                message = "enemy_down";
            } else {
                message = "enemy_spotted";
            }
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/" + message, HttpMethod.PUT, null));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/roger_that", HttpMethod.PUT, null));
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/need_backup", HttpMethod.PUT, null));

            super.onLongPress(e);
        }

        void sendMessage(ServiceParams params) {
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
    };

}
