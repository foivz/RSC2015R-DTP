package hr.foi.rsc.rscapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.AlertPrompt;
import hr.foi.rsc.model.Notification;
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
    Notification notification;

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
        Timer task = new Timer();
        task.schedule(tasker, 1000, 1000);
        return v;
    }

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
                AlertPrompt prompt = new AlertPrompt(getContext());
                prompt.prepare(wat.getName() + " " + wat.getSurname() + ": " + notification.getName(), null, 0, null, 0);
            }
            return true;
        }

        @Override
        public void onPostSend() {

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
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/" + message, HttpMethod.POST, null));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Log.i("hr.foi.debug", "onDoubleTap HELLO");
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/roger_that", HttpMethod.POST, null));
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("hr.foi.debug", "onLongPress HELLO");
            sendMessage(new ServiceParams("/notification/"
                    + myTeam.getIdTeam() + "/person/" + self.getIdPerson() + "/message/need_backup", HttpMethod.POST, null));

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
