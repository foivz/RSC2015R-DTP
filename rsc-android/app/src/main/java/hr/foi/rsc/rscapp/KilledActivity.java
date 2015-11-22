package hr.foi.rsc.rscapp;

import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.handlers.KillRequestHandler;
import hr.foi.rsc.rscapp.handlers.UpdateHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;

public class KilledActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //request za killed
        KillRequestHandler killRequestHandler = new KillRequestHandler(KilledActivity.this);
        SessionManager manager = SessionManager.getInstance(getApplicationContext());
        Person self = SessionManager.getInstance(this).retrieveSession("person", Person.class);
        Team  tim = SessionManager.getInstance(this).retrieveSession("team", Team.class);
        new ServiceAsyncTask(killRequestHandler)
                .execute(new ServiceParams("game/"
                        + self.getIdPerson()+"/person/"+tim.getIdTeam(), HttpMethod.GET, self.getIdPerson(), tim.getIdTeam()));

        WebView webview = new WebView(this);
        Log.i("webview URLA JE: ",webview.getOriginalUrl());
        setContentView(webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webview.loadUrl("http://46.101.173.23/dist/#/");


    }
}
