package hr.foi.rsc.rscapp;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.springframework.http.HttpMethod;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.handlers.KillRequestHandler;
import hr.foi.rsc.rscapp.handlers.UpdateHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

public class KilledActivity extends AppCompatActivity {

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_killed);
        //request za killed
        SessionManager manager = SessionManager.getInstance(getApplicationContext());
        Person self = SessionManager.getInstance(this).retrieveSession("person", Person.class);
        Team  tim = SessionManager.getInstance(this).retrieveSession("team", Team.class);
        new ServiceAsyncTask(new ServiceResponseHandler() {
            @Override
            public void onPreSend() {

            }

            @Override
            public boolean handleResponse(ServiceResponse response) {
                Toast.makeText(getApplicationContext(),"asdasdas", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onPostSend() {

            }
        }).execute(new ServiceParams("game/"
                + self.getIdPerson() + "/person/" + tim.getIdTeam() + "/team", HttpMethod.GET, self.getIdPerson(), tim.getIdTeam()));




        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://46.101.173.23/dist/#/");

    }
}
