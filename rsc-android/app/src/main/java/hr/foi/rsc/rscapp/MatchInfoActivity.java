/*package hr.foi.rsc.rscapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Game;
import hr.foi.rsc.rscapp.handlers.MatchInfoHandler;
import hr.foi.rsc.rscapp.handlers.TokenHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;

public class MatchInfoActivity extends AppCompatActivity {

    String code;
    private ListView lvDetails;
    private String matchName;
    private List<String> matchDetails;
    Game getMatchInfo(String code){ //radi zahtjev na server

        SessionManager manager=SessionManager.getInstance(getApplicationContext());

        String url = "/game/" + code;
        ServiceParams params = new ServiceParams(url,
                HttpMethod.GET,"", null, true);

        MatchInfoHandler matchInfoHandler = new MatchInfoHandler(MatchInfoActivity.this);
        new ServiceAsyncTask(matchInfoHandler).execute(params);

        Game game = manager.retrieveSession("game",Game.class);

    }
    void populateListView(){
        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lvDetails.setAdapter(arrayAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        lvDetails = (ListView) findViewById(R.id.listViewMatchDetails);
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        getMatchInfo(code);
        populateListView();

    }

}*/
