package com.games;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;


public class ListActivity extends AppCompatActivity {

    private FloatingActionButton _fab;
    private double C = 0;
    private double longt = 0;
    private double nomber = 0;
    private String value = "";
    private HashMap<String, Object> map = new HashMap<>();
    private HashMap<String, Object> favdto = new HashMap<>();

    private ArrayList<HashMap<String, Object>> games = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> listesear = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> listefav = new ArrayList<>();


    private ImageView imageview1;
    private EditText edittext1;
    private ListView listview1;

    private Intent Screen = new Intent();
    private ObjectAnimator OB = new ObjectAnimator();
    private RequestNetwork Request;
    private RequestNetwork.RequestListener _Request_request_listener;
    private SharedPreferences loginData;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_list);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        _fab = findViewById(R.id._fab);

        imageview1 = findViewById(R.id.imageview1);
        edittext1 = findViewById(R.id.edittext1);
        listview1 = findViewById(R.id.listview1);
        Request = new RequestNetwork(this);
        loginData = getSharedPreferences("loginData", Activity.MODE_PRIVATE);

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(Screen);
                finish();
            }
        });

        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                _search();
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                Screen.setClass(getApplicationContext(), OnegameActivity.class);
                Screen.putExtra("id", games.get((int)_position).get("id").toString());
                startActivity(Screen);
                finish();
            }
        });

        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.setClass(getApplicationContext(), FavoriteActivity.class);
                startActivity(Screen);
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
                    if (_tag.equals("listegames")) {
                        _Loading(false);
                        games = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                        listview1.setAdapter(new Listview1Adapter(games));
                        ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
                    }
                    else {
                        if (_tag.equals("addtofav")) {
                            _Loading(false);
                            Toast.makeText(ListActivity.this, "Added to favorite", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (_tag.equals("listefav")) {
                                _Loading(false);
                                listefav = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                            }
                            else {
                                if (_tag.equals("addtofav")) {
                                    _Loading(false);
                                    Toast.makeText(ListActivity.this, "Added to favorite", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                }
                            }
                        }
                    }
                }catch(Exception e){

                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                Toast.makeText(ListActivity.this, _message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initializeLogic() {
        getWindow().setStatusBarColor(Color.parseColor("#FF000321"));
        map = new HashMap<>();
        map.put("Authorization", "Bearer ".concat(loginData.getString("token", "")));
        Request.setHeaders(map);
        Request.startRequestNetwork(RequestNetworkController.GET, "https://games-cqhi.onrender.com/api/game", "listegames", _Request_request_listener);
        _Loading(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Request.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "A", _Request_request_listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Screen.setClass(getApplicationContext(), HomeActivity.class);
        startActivity(Screen);
        finish();
    }
    public void _SHAKIB(final View _view) {
        OB.setTarget(_view);
        OB.setPropertyName("rotation");
        OB.setFloatValues((float)(-60), (float)(0));
        OB.setDuration((int)(1000));
        OB.setInterpolator(new BounceInterpolator());
        OB.start();
    }


    public void _search() {
        listesear = new Gson().fromJson(new Gson().toJson(games), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        if (edittext1.getText().toString().length() > 0) {
            try{
                longt = games.size();
                nomber = longt - 1;
                for(int _repeat20 = 0; _repeat20 < (int)(longt); _repeat20++) {
                    value = games.get((int)nomber).get("name").toString();
                    if (value.toLowerCase().contains(edittext1.getText().toString().toLowerCase())) {

                    }
                    else {
                        listesear.remove((int)(nomber));
                    }
                    nomber--;
                }
                listview1.setAdapter(new Listview1Adapter(listesear));
                ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
            }catch(Exception e){

            }
        }
        else {
            listview1.setAdapter(new Listview1Adapter(games));
            ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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

    public class Listview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.costume, null);
            }

            final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
            final LinearLayout linear3 = _view.findViewById(R.id.linear3);
            final LinearLayout linear4 = _view.findViewById(R.id.linear4);
            final LinearLayout linear5 = _view.findViewById(R.id.linear5);
            final androidx.cardview.widget.CardView cardview2 = _view.findViewById(R.id.cardview2);
            final LinearLayout linear7 = _view.findViewById(R.id.linear7);
            final TextView name = _view.findViewById(R.id.name);
            final TextView desc = _view.findViewById(R.id.desc);
            final LinearLayout linear6 = _view.findViewById(R.id.linear6);
            final ImageView imageview5 = _view.findViewById(R.id.imageview5);
            final ImageView addfav = _view.findViewById(R.id.addfav);
            final ImageView imageview3 = _view.findViewById(R.id.imageview3);

            if (_data.get((int)_position).containsKey("name")) {
                name.setText(_data.get((int)_position).get("name").toString());
            }
            if (_data.get((int)_position).containsKey("descpt")) {
                desc.setText(_data.get((int)_position).get("descpt").toString());
            }
            if (_data.get((int)_position).containsKey("imgurl")) {
                try{
                    Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("imgurl").toString())).into(imageview5);
                }catch(Exception e){
                    imageview5.setVisibility(View.GONE);
                }
            }
            addfav.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View _view){
                    favdto = new HashMap<>();
                    favdto.put("iduser", loginData.getString("userid", ""));
                    favdto.put("idgame", _data.get((int)_position).get("id").toString());
                    Request.setHeaders(map);
                    Request.setParams(favdto, RequestNetworkController.REQUEST_BODY);
                    Request.startRequestNetwork(RequestNetworkController.POST, "https://games-cqhi.onrender.com/api/game/favorite/add", "addtofav", _Request_request_listener);
                }
            });

            return _view;
        }
    }
}