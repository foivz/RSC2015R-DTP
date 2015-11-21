package hr.foi.rsc.rscapp.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import hr.foi.rsc.rscapp.R;

/**
 * Created by hrvoje on 21/11/15.
 */

public class HelperFragment extends Fragment {

    TextView seconds;
    TextView minutes;

    void ajmo(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                seconds.setText(String.format("%d", millisUntilFinished / 1000));
                minutes.setText(String.format("%d", millisUntilFinished / 1000/60));
            }

            public void onFinish() {
                //big boom!
            }
        }.start();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        View v = inflater.inflate(R.layout.fragment_helper, container, false);
        seconds=(TextView) v.findViewById(R.id.txtViewCtrSeconds);
        minutes=(TextView) v.findViewById(R.id.textViewCtrMinutes);
        ajmo();
        return v;
    }


}