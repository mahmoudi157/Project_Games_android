package com.games;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends AppCompatActivity {

    private HashMap<String, Object> map = new HashMap<>();
    private HashMap<String, Object> favdto = new HashMap<>();

    private ArrayList<HashMap<String, Object>> games = new ArrayList<>();

    private ImageView imageview1;
    private ListView Macth;

    private RequestNetwork Request;
    private RequestNetwork.RequestListener _Request_request_listener;
    private SharedPreferences loginData;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        imageview1 = findViewById(R.id.imageview1);
        Macth = findViewById(R.id.Macth);
        Request = new RequestNetwork(this);
        loginData = getSharedPreferences("loginData", Activity.MODE_PRIVATE);

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setClass(getApplicationContext(), ListActivity.class);
                startActivity(i);
                finish();
            }
        });

        Macth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                i.setClass(getApplicationContext(), OnegameActivity.class);
                i.putExtra("id", games.get((int)_position).get("id").toString());
                startActivity(i);
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
                    if (_tag.equals("listefav")) {
                        _Loading(false);
                        games = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                        Macth.setAdapter(new MacthAdapter(games));
                        ((BaseAdapter)Macth.getAdapter()).notifyDataSetChanged();
                    }
                    else {
                        if (_tag.equals("deletfav")) {
                            _Loading(true);
                            Request.setHeaders(map);
                            Request.startRequestNetwork(RequestNetworkController.GET, "https://games-cqhi.onrender.com/api/game/favorite/myfavorite/".concat(loginData.getString("userid", "")), "listefav", _Request_request_listener);
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
                _Loading(false);
                Toast.makeText(FavoriteActivity.this, _message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initializeLogic() {
        map = new HashMap<>();
        map.put("Authorization", "Bearer ".concat(loginData.getString("token", "")));
        Request.setHeaders(map);
        Request.startRequestNetwork(RequestNetworkController.GET, "https://games-cqhi.onrender.com/api/game/favorite/myfavorite/".concat(loginData.getString("userid", "")), "listefav", _Request_request_listener);
        _Loading(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        i.setClass(getApplicationContext(), ListActivity.class);
        startActivity(i);
        finish();
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

    public class MacthAdapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public MacthAdapter(ArrayList<HashMap<String, Object>> _arr) {
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

            addfav.setImageResource(R.drawable.ic_bookmark_black);
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
                    Request.startRequestNetwork(RequestNetworkController.DELETE, "https://games-cqhi.onrender.com/api/game/favorite/delet", "deletfav", _Request_request_listener);
                }
            });

            return _view;
        }
    }
}