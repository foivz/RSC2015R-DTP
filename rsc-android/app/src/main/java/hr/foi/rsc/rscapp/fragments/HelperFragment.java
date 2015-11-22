package hr.foi.rsc.rscapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;

import hr.foi.rsc.rscapp.GameOverActivity;
import hr.foi.rsc.rscapp.KilledActivity;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.rscapp.RegistrationActivity;

/**
 * Created by hrvoje on 21/11/15.
 */

public class HelperFragment extends Fragment {

    TextView seconds;
    TextView minutes;
    ImageButton dead;
    CountDownTimer m;
    void ajmo(){

        m = new CountDownTimer(300000, 1000) {
            boolean vibrating=false;
            public void onTick(long millisUntilFinished) {
                int secondsLong = (int) (millisUntilFinished / 1000) % 60 ;
                int minutesLong = (int) ((millisUntilFinished / (1000*60)) % 60);
                seconds.setText(String.format("%d", secondsLong));
                minutes.setText(String.format("%d",minutesLong));
                if(millisUntilFinished/1000 < 20){
                    Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if(vibrating==false){
                        vibrating=true;
                        v.vibrate(20000);
                    }
                }
            }

            public void onFinish() {
                Intent intent = new Intent(getActivity().getApplicationContext(), GameOverActivity.class);
                startActivity(intent);
            }
        }.start();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(savedInstanceState == null) {
            ajmo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_helper, container, false);
        seconds=(TextView) v.findViewById(R.id.txtViewCtrSeconds);
        minutes=(TextView) v.findViewById(R.id.textViewCtrMinutes);
        dead = (ImageButton) v.findViewById(R.id.imgBtnGrobar);
        dead.setOnClickListener(onGroblje);

        return v;
    }
    View.OnClickListener onGroblje = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("http://46.101.173.23:8080/game/2/team/3");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
    };

    @Override
    public void onStop() {
        m.cancel();
        super.onStop();
    }
}