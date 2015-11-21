package hr.foi.rsc.rscapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class GameFragment extends Fragment {

    Team myTeam;
    GoogleMap mMap;
    Timer t;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        myTeam = SessionManager.getInstance(this.getContext()).retrieveSession("team", Team.class);

        t = new Timer();
        t.schedule(locations, 1000, 1000);
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
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
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Person self = SessionManager.getInstance(getContext()).retrieveSession("user", Person.class);
            self.setLat(location.getLatitude());
            self.setLng(location.getLongitude());
            ServiceParams params = new ServiceParams(getString(R.string.game_path) + "/personLocation", HttpMethod.PUT, self);
            new ServiceAsyncTask(locationHandler).execute(params);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

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
}
