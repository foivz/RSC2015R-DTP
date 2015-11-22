package hr.foi.rsc.rscapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.rscapp.R;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;
import hr.foi.rsc.webservice.ServiceResponseHandler;

/**
 * Created by hrvoje on 21/11/15.
 */
public class GameFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Team myTeam;
    GoogleMap mMap;
    Timer t;
    LocationManager locationManager;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        myTeam = SessionManager.getInstance(this.getContext()).retrieveSession("team", Team.class);

        t = new Timer();
        t.schedule(locations, 1000, 1000);
        buildGoogleApiClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mMapFragment = (com.google.android.gms.maps.MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Person self = SessionManager.getInstance(this.getContext()).retrieveSession("person", Person.class);
        self.setLat(location.getLatitude());
        self.setLng(location.getLongitude());
        ServiceParams params = new ServiceParams(getString(R.string.game_path) + "personLocation", HttpMethod.PUT, self);
        new ServiceAsyncTask(dummy).execute(params);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    ServiceResponseHandler dummy = new ServiceResponseHandler() {
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
    };

    ServiceResponseHandler locationHandler = new ServiceResponseHandler() {
        @Override
        public void onPreSend() {

        }

        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Type listType = new TypeToken<ArrayList<Person>>() {
                }.getType();
                ArrayList<Person> teamMembers = new Gson().fromJson(response.getJsonResponse(), listType);
                for(Person p: teamMembers) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLat(),
                            p.getLng())).title(p.getName() + " " + p.getSurname()));
                }
            }
            return true;
        }

        @Override
        public void onPostSend() {

        }
    };

    TimerTask locations = new TimerTask() {
        @Override
        public void run() {
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + 0
                    + getString(R.string.team_path) + myTeam.getIdTeam(), HttpMethod.GET, null);
            new ServiceAsyncTask(locationHandler).execute(params);
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
