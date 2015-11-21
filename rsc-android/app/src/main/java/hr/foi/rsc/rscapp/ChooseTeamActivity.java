package hr.foi.rsc.rscapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.Input;
import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.rscapp.handlers.RegistrationHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;

public class ChooseTeamActivity extends AppCompatActivity {

    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        ImageButton red = (ImageButton) findViewById(R.id.ibCtmRed);
        red.setOnClickListener(onSubmit);
        ImageButton blue = (ImageButton) findViewById(R.id.ibCtmBlue);
        blue.setOnClickListener(onSubmit);
    }

    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //dalje intentovi
        }
    };

}
