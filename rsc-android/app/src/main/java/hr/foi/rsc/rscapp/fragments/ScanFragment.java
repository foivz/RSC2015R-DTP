package hr.foi.rsc.rscapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.ChooseTeamActivity;
import hr.foi.rsc.rscapp.GameOverActivity;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.rscapp.ScanActivity;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

/**
 * Created by tomo on 22.11.15..
 */
public class ScanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scan, container, false);
        Button start = (Button) v.findViewById(R.id.start_scan);
        final Context context = getContext();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(IntentIntegrator.createScanIntent(context), IntentIntegrator.REQUEST_CODE);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if scan succeeded there will be an intent
        if (data != null) {
            Log.i("hr.foi.debug", "Scan successful");
            String contents;
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            // on successful result decode the contents
            if (result != null) {
                contents = result.getContents();
                if (contents != null) {

                    // end match
                    // TODO url
                    Game game = SessionManager.getInstance(this.getContext()).retrieveSession("game", Game.class);
                    Team myTeam = SessionManager.getInstance(this.getContext()).retrieveSession("team", Team.class);
                    Person self = SessionManager.getInstance(this.getContext()).retrieveSession("person", Person.class);
                    ServiceParams params = new ServiceParams("/game/" + game.getIdGame() + "/end/"
                            + myTeam.getIdTeam() + "/user/" + self.getIdPerson(), HttpMethod.POST, null);
                    new ServiceAsyncTask(handler).execute(params);

                } else {
                    Log.w("hr.foi.debug", "Scan returned no result");
                    Toast.makeText(getContext(), getString(R.string.decoding_fail), Toast.LENGTH_LONG).show();
                }
            }
        } else {
        }
    }

    ServiceResponseHandler handler = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            Intent intent = new Intent(getActivity().getApplicationContext(), GameOverActivity.class);
            startActivity(intent);
            return false;
        }

        @Override
        public void onPostSend() {

        }
    };
}
