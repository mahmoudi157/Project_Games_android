package com.games;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class OnegameActivity extends AppCompatActivity {

    private HashMap<String, Object> map = new HashMap<>();
    private HashMap<String, Object> game = new HashMap<>();

    private LinearLayout linear4;
    private LinearLayout linear2;
    private WebView webview1;
    private ImageView imageview1;
    private ImageView ICON;
    private TextView TITLE;
    private TextView descpt;
    private Button Required;
    private Button tutoriel;

    private SharedPreferences loginData;
    private RequestNetwork Request;
    private RequestNetwork.RequestListener _Request_request_listener;
    private Intent i = new Intent();
    private AlertDialog cosd;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_onegame);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        linear4 = findViewById(R.id.linear4);
        linear2 = findViewById(R.id.linear2);
        webview1 = findViewById(R.id.webview1);
        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setSupportZoom(true);
        imageview1 = findViewById(R.id.imageview1);
        ICON = findViewById(R.id.ICON);
        TITLE = findViewById(R.id.TITLE);
        descpt = findViewById(R.id.descpt);
        Required = findViewById(R.id.Required);
        tutoriel = findViewById(R.id.tutoriel);
        loginData = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        Request = new RequestNetwork(this);

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
                final String _url = _param2;

                super.onPageStarted(_param1, _param2, _param3);
            }

            @Override
            public void onPageFinished(WebView _param1, String _param2) {
                final String _url = _param2;

                super.onPageFinished(_param1, _param2);
            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setClass(getApplicationContext(), ListActivity.class);
                startActivity(i);
                finish();
            }
        });

        Required.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

            }
        });

        tutoriel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                linear2.setVisibility(View.GONE);
                webview1.setVisibility(View.VISIBLE);
                webview1.loadUrl(game.get("tutorial").toString());
            }
        });

        _Request_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
                final String _tag = _param1;
                final String _response = _param2;
                final HashMap<String, Object> _responseHeaders = _param3;
                if (_tag.equals("game")) {
                    game = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
                    _Loading(false);
                    if (game.containsKey("name")) {
                        TITLE.setText(game.get("name").toString());
                    }
                    if (game.containsKey("descpt")) {
                        descpt.setText(game.get("descpt").toString());
                    }
                    if (game.containsKey("imgurl")) {
                        try{
                            Glide.with(getApplicationContext()).load(Uri.parse(game.get("imgurl").toString())).into(ICON);
                        }catch(Exception e){

                        }
                    }
                    Required.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View _view){
                            final AlertDialog cosd = new AlertDialog.Builder(OnegameActivity.this).create();
                            LayoutInflater cosdLI = getLayoutInflater();
                            View cosdCV = (View) cosdLI.inflate(R.layout.costumed, null);
                            cosd.setView(cosdCV);
                            final TextView textcaract = (TextView)
                                    cosdCV.findViewById(R.id.textcaract);
                            textcaract.setText(game.get("caractur").toString());
                            final Button buttonmaps = (Button)
                                    cosdCV.findViewById(R.id.buttonmaps);
                            buttonmaps.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF2196F3));
                            buttonmaps.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View _view){
                                    i.setClass(getApplicationContext(), MapsActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            cosd.show();
                        }
                    });
                }
                else {

                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                Toast.makeText(OnegameActivity.this, _message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initializeLogic() {
        map = new HashMap<>();
        map.put("Authorization", "Bearer ".concat(loginData.getString("token", "")));
        Request.setHeaders(map);
        Request.startRequestNetwork(RequestNetworkController.GET, "https://games-cqhi.onrender.com/api/game/game/".concat(getIntent().getStringExtra("id")), "game", _Request_request_listener);
        _Loading(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webview1.getVisibility() == View.VISIBLE) {
            webview1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
        } else {
            i.setClass(getApplicationContext(), ListActivity.class);
            startActivity(i);
            finish();
        }
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