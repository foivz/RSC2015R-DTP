package hr.foi.rsc.rscapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

/**
 * Created by tomo on 22.11.15..
 */
public class CommunicationFragment extends Fragment {

    GestureDetector gestureDetector;
    Person self;
    Team myTeam;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gestureDetector = new GestureDetector(this.getContext(), onGesture);
        self = SessionManager.getInstance(this.getContext()).retrieveSession("person", Person.class);
        myTeam = SessionManager.getInstance(this.getContext()).retrieveSession("team", Team.class);
        View v = inflater.inflate(R.layout.fragment_communication, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        return v;
    }

    GestureDetector.SimpleOnGestureListener onGesture = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("hr.foi.debug", "onDown HELLO");
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

            Log.i("hr.foi.debug", "onDoubleTap HELLO");
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/roger_that", HttpMethod.PUT, null));
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("hr.foi.debug", "onLongPress HELLO");
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
