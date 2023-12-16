package com.games;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



public class AuthActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private boolean SHAKIB_MODE = false;
    private HashMap<String, Object> map = new HashMap<>();

    private ArrayList<HashMap<String, Object>> UsersCreate = new ArrayList<>();

    private ScrollView Scroll;
    private LinearLayout Full;
    private TextView Welcome;
    private TextView Title;
    private LinearLayout L_Name;
    private LinearLayout L_Email;
    private LinearLayout L_Password;
    private LinearLayout Button_Continue;
    private LinearLayout Button_Mode;
    private ImageView I_Name;
    private EditText T_Name;
    private ImageView I_Email;
    private EditText T_Email;
    private ImageView I_Password;
    private EditText T_Password;
    private TextView T_Continue;
    private TextView T_Or;

    private TextView T_Mode;

    private Intent Screen = new Intent();
    private RequestNetwork login;
    private RequestNetwork.RequestListener _login_request_listener;
    private SharedPreferences loginData;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_auth);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        Scroll = findViewById(R.id.Scroll);
        Full = findViewById(R.id.Full);
        Welcome = findViewById(R.id.Welcome);
        Title = findViewById(R.id.Title);
        L_Name = findViewById(R.id.L_Name);
        L_Email = findViewById(R.id.L_Email);
        L_Password = findViewById(R.id.L_Password);
        Button_Continue = findViewById(R.id.Button_Continue);
        Button_Mode = findViewById(R.id.Button_Mode);
        I_Name = findViewById(R.id.I_Name);
        T_Name = findViewById(R.id.T_Name);
        I_Email = findViewById(R.id.I_Email);
        T_Email = findViewById(R.id.T_Email);
        I_Password = findViewById(R.id.I_Password);
        T_Password = findViewById(R.id.T_Password);
        T_Continue = findViewById(R.id.T_Continue);
        T_Or = findViewById(R.id.T_Or);
        T_Mode = findViewById(R.id.T_Mode);
        login = new RequestNetwork(this);
        loginData = getSharedPreferences("loginData", Activity.MODE_PRIVATE);

        Button_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (Welcome.getText().toString().equals("Welcome Again")) {
                    if (T_Email.getText().toString().equals("")) {

                        Toast.makeText(AuthActivity.this, "Type Your Email", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (T_Password.getText().toString().equals("")) {
                            Toast.makeText(AuthActivity.this, "Type Your Password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            map = new HashMap<>();
                            map.put("username", T_Email.getText().toString());
                            map.put("password", T_Password.getText().toString());
                            login.setParams(map, RequestNetworkController.REQUEST_BODY);
                            login.startRequestNetwork(RequestNetworkController.POST, "https://games-cqhi.onrender.com/api/auth/login", "login", _login_request_listener);
                            _Loading(true);
                        }
                    }
                }
                else {
                    if (T_Name.getText().toString().equals("")) {

                        Toast.makeText(AuthActivity.this, "Type Your Name", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (T_Email.getText().toString().equals("")) {

                            Toast.makeText(AuthActivity.this, "Type Your Email", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (T_Password.getText().toString().equals("")) {

                                Toast.makeText(AuthActivity.this, "Type Password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                map = new HashMap<>();
                                map.put("username", T_Name.getText().toString());
                                map.put("email", T_Email.getText().toString());
                                map.put("password", T_Password.getText().toString());
                                login.setParams(map, RequestNetworkController.REQUEST_BODY);
                                login.startRequestNetwork(RequestNetworkController.POST, "https://games-cqhi.onrender.com/api/auth/signup", "login", _login_request_listener);
                                _Loading(true);
                            }
                        }
                    }
                }
            }
        });

        Button_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (SHAKIB_MODE) {
                    _Mode(false);
                }
                else {
                    _Mode(true);
                }
            }
        });

        _login_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
                final String _tag = _param1;
                final String _response = _param2;
                final HashMap<String, Object> _responseHeaders = _param3;
                try{
                    if (_tag.equals("test")) {
                        _Loading(false);
                    }
                    else {
                        if (_tag.equals("login")) {
                            _Loading(false);
                            map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
                            if (map.containsKey("accessToken")) {
                                loginData.edit().putString("userid", map.get("userId").toString()).commit();
                                loginData.edit().putString("token", map.get("accessToken").toString()).commit();
                                Screen.setClass(getApplicationContext(), HomeActivity.class);
                                startActivity(Screen);
                                finish();
                            }
                            else {

                                Toast.makeText(AuthActivity.this, _response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {

                        }
                    }
                }catch(Exception e){

                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;

                Toast.makeText(AuthActivity.this, _message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initializeLogic() {
        _Loading(true);
        _Design();
        login.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "test", _login_request_listener);
        if (loginData.contains("token")) {
            if (!loginData.getString("token", "").equals("")) {
                Screen.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(Screen);
                finish();
            }
        }
        else {

        }
    }

    public void _Shadow(final double _sadw, final double _cru, final String _wc, final View _widgets) {
        android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
        wd.setColor(Color.parseColor(_wc));
        wd.setCornerRadius((int)_cru);
        _widgets.setElevation((int)_sadw);
        _widgets.setBackground(wd);
    }


    public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(Color.parseColor(_focus));
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                Color.parseColor("#" + _strokeclr.replace("#", "")));
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }


    public void _TransitionManager(final View _view, final double _duration) {
        LinearLayout viewgroup =(LinearLayout) _view;

        android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
    }


    public void _ClickAnimation(final View _view) {
        _view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        ObjectAnimator scaleX = new ObjectAnimator();
                        scaleX.setTarget(_view);
                        scaleX.setPropertyName("scaleX");
                        scaleX.setFloatValues(0.9f);
                        scaleX.setDuration(100);
                        scaleX.start();

                        ObjectAnimator scaleY = new ObjectAnimator();
                        scaleY.setTarget(_view);
                        scaleY.setPropertyName("scaleY");
                        scaleY.setFloatValues(0.9f);
                        scaleY.setDuration(100);
                        scaleY.start();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{

                        ObjectAnimator scaleX = new ObjectAnimator();
                        scaleX.setTarget(_view);
                        scaleX.setPropertyName("scaleX");
                        scaleX.setFloatValues((float)1);
                        scaleX.setDuration(100);
                        scaleX.start();

                        ObjectAnimator scaleY = new ObjectAnimator();
                        scaleY.setTarget(_view);
                        scaleY.setPropertyName("scaleY");
                        scaleY.setFloatValues((float)1);
                        scaleY.setDuration(100);
                        scaleY.start();

                        break;
                    }
                }
                return false;
            }
        });
    }


    public void _Mode(final boolean _login) {
        SHAKIB_MODE = _login;
        _TransitionManager(Full, 200);
        if (_login) {
            Welcome.setText("Welcome Here");
            L_Name.setVisibility(View.VISIBLE);
            T_Continue.setText("Create Account");
            T_Password.setHint("Password");
            T_Mode.setText("Already Have An Account");
            Title.setText("Let's Create Your New Account");
        }
        else {
            L_Name.setVisibility(View.GONE);
            Welcome.setText("Welcome Again");
            L_Email.setVisibility(View.VISIBLE);
            T_Continue.setText("Login Account");
            T_Mode.setText("Create New Account");
            Title.setText("Let's Login Your Account");
            L_Password.setVisibility(View.VISIBLE);
            T_Password.setHint("Your Password");
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


    public void _Design() {
        _Mode(false);
        _ClickAnimation(Button_Mode);
        _ClickAnimation(Button_Continue);
        I_Email.setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        Scroll.setVerticalScrollBarEnabled(false);
        I_Name.setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        I_Password.setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        _Shadow(0, 50, "#EEEEEE", L_Email);
        _Shadow(0, 50, "#EEEEEE", L_Name);
        _Shadow(0, 50, "#EEEEEE", L_Password);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        Title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.NORMAL);
        T_Or.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.BOLD);
        getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
        T_Email.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.BOLD);
        T_Mode.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.BOLD);
        Welcome.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib1.ttf"), Typeface.NORMAL);
        T_Continue.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.BOLD);
        T_Password.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/shakib3.ttf"), Typeface.NORMAL);
        _rippleRoundStroke(Button_Mode, "#EEEEEE", "#BDBDBD", 50, 0, "#EEEEEE");
        _rippleRoundStroke(Button_Continue, "#00BCD4", "#0097A7", 50, 0, "#00BCD4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); }
    }

}