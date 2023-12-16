package com.games;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;
public class MapsActivity extends AppCompatActivity {

    private String id = "";
    private double lat = 0;
    private double lng = 0;
    private String title = "";
    private HashMap<String, Object> map = new HashMap<>();

    private ArrayList<HashMap<String, Object>> listemaps = new ArrayList<>();
    private MapView mapview1;
    private GoogleMapController _mapview1_controller;
    private ImageView imageview1;


    private Intent screen = new Intent();
    private RequestNetwork Request;
    private RequestNetwork.RequestListener _Request_request_listener;
    private SharedPreferences loginData;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_maps);
        initialize(_savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initialize(Bundle _savedInstanceState) {
        mapview1 = findViewById(R.id.mapview1);
        mapview1.onCreate(_savedInstanceState);
        imageview1 = findViewById(R.id.imageview1);

        Request = new RequestNetwork(this);
        loginData = getSharedPreferences("loginData", Activity.MODE_PRIVATE);

        _mapview1_controller = new GoogleMapController(mapview1, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap _googleMap) {
                _mapview1_controller.setGoogleMap(_googleMap);

            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                screen.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(screen);
                finish();
            }
        });

        _Request_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
                final String _tag = _param1;
                final String _response = _param2;
                final HashMap<String, Object> _responseHeaders = _param3;
                try{
                    if (_tag.equals("listemaps")) {
                        _Loading(false);
                        listemaps = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                        for(int _repeat44 = 0; _repeat44 < (int)(listemaps.size()); _repeat44++) {
                            id = listemaps.get((int)_repeat44).get("id").toString();
                            title = listemaps.get((int)_repeat44).get("title").toString();
                            lat = Double.parseDouble(listemaps.get((int)_repeat44).get("lat").toString());
                            lng = Double.parseDouble(listemaps.get((int)_repeat44).get("lng").toString());
                            _mapview1_controller.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            _mapview1_controller.addMarker(id, lat, lng);
                            _mapview1_controller.moveCamera(Double.parseDouble(listemaps.get((int)_repeat44).get("lat").toString()), Double.parseDouble(listemaps.get((int)_repeat44).get("lng").toString()));
                            _mapview1_controller.setMarkerInfo(id, title, "");
                            _mapview1_controller.setMarkerIcon(id, R.drawable.close);
                            _mapview1_controller.setMarkerColor(id, BitmapDescriptorFactory.HUE_BLUE, 1);
                            _mapview1_controller.zoomTo(12);
                        }
                    }
                    else {

                    }
                }catch(Exception e){

                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                Toast.makeText(MapsActivity.this, _message, Toast.LENGTH_SHORT).show();
                screen.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(screen);
                finish();
            }
        };
    }

    private void initializeLogic() {
        map = new HashMap<>();
        map.put("Authorization", "Bearer ".concat(loginData.getString("token", "")));
        Request.setHeaders(map);
        Request.startRequestNetwork(RequestNetworkController.GET, "https://games-cqhi.onrender.com/api/maps", "listemaps", _Request_request_listener);
        _Loading(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        screen.setClass(getApplicationContext(), HomeActivity.class);
        startActivity(screen);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview1.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapview1.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview1.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview1.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapview1.onStop();
    }
    public void _Loading(final boolean _Loading) {
        if (_Loading) {
            if (coreprog == null){
                coreprog = new ProgressDialog(this);
                coreprog.setCancelable(false);
                coreprog.setCanceledOnTouchOutside(false);

                coreprog.requestWindowFeature(Window.FEATURE_NO_TITLE);  coreprog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));

            }
            coreprog.show();
            coreprog.setContentView(R.layout.loading);


            LinearLayout ProgBG = (LinearLayout)coreprog.findViewById(R.id.ProgBG);

            LinearLayout back = (LinearLayout)coreprog.findViewById(R.id.BG);

            android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor("#FFFFFF")); /* color */
            gd.setCornerRadius(40); /* radius */
            gd.setStroke(0, Color.WHITE); /* stroke heigth and color */
            ProgBG.setBackground(gd);
        }
        else {
            if (coreprog != null){
                coreprog.dismiss();
            }
        }
    }
    private ProgressDialog coreprog;
    {
    }

}