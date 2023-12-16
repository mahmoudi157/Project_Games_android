package com.games;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
public class HomeActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private HashMap<String, Object> Create = new HashMap<>();
    private HashMap<String, Object> Rule = new HashMap<>();
    private HashMap<String, Object> TopUp = new HashMap<>();
    private String ShareUrl = "";
    private String AppUrl = "";
    private String Group1 = "";
    private String Group2 = "";
    private double ViewSlide = 0;
    private double number = 0;
    private double position = 0;

    private ArrayList<HashMap<String, Object>> Slide = new ArrayList<>();

    private LinearLayout PlayBg;
    private LinearLayout MenuBg;
    private ViewPager ViewPage;


    private LinearLayout FrerFireBg;
    private LinearLayout LudoBg;
    private LinearLayout linear170;
    private LinearLayout Menu3;
    private LinearLayout Menu1;

    private LinearLayout linear183;
    private LinearLayout linear14;
    private LinearLayout linear9;
    private LinearLayout linear10;
    private TimerTask Time;
    private TimerTask LogOut;
    private Intent Screen = new Intent();
    private AlertDialog.Builder Dialog;
    private Intent Website = new Intent();
    private ObjectAnimator OB = new ObjectAnimator();
    private RequestNetwork WiFi;
    private RequestNetwork.RequestListener _WiFi_request_listener;
    private AlertDialog.Builder d;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        PlayBg = findViewById(R.id.PlayBg);
        MenuBg = findViewById(R.id.MenuBg);
        ViewPage = findViewById(R.id.ViewPage);
        FrerFireBg = findViewById(R.id.FrerFireBg);
        LudoBg = findViewById(R.id.LudoBg);
        linear170 = findViewById(R.id.linear170);
        Menu3 = findViewById(R.id.Menu3);
        Menu1 = findViewById(R.id.Menu1);
        linear183 = findViewById(R.id.linear183);
        linear14 = findViewById(R.id.linear14);
        linear9 = findViewById(R.id.linear9);
        linear10 = findViewById(R.id.linear10);

        Dialog = new AlertDialog.Builder(this);
        WiFi = new RequestNetwork(this);
        d = new AlertDialog.Builder(this);

        FrerFireBg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View _view) {

                return true;
            }
        });

        FrerFireBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.putExtra("Screen", "Free Fire");
                Screen.putExtra("Image", "freefire");
                Screen.setClass(getApplicationContext(), ListActivity.class);
                startActivity(Screen);
            }
        });

        LudoBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.putExtra("Screen", "Free Fire2");
                Screen.putExtra("Image", "freefire");
                Screen.setClass(getApplicationContext(), ListActivity.class);
                startActivity(Screen);
            }
        });

        linear170.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

            }
        });

        Menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.setClass(getApplicationContext(), FavoriteActivity.class);
                startActivity(Screen);
                finish();
            }
        });

        Menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                MenuBg.setVisibility(View.GONE);
                PlayBg.setVisibility(View.VISIBLE);
                Screen.setClass(getApplicationContext(), ListActivity.class);
                startActivity(Screen);
                finish();
            }
        });

        linear183.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Screen.setClass(getApplicationContext(), MapsActivity.class);
                startActivity(Screen);
                finish();
            }
        });

        linear14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

                startActivity(Screen);
            }
        });

        linear9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                ShareUrl = "Play Games :-\n".concat(AppUrl);
                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, ShareUrl);

                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);

                startActivity(shareIntent);
            }
        });

        linear10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Dialog.setTitle("CONFIRMATION");
                Dialog.setIcon(R.drawable.logout);
                Dialog.setMessage("Are You Sure LogOut This Account ?");
                Dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _Loading(true);
                        LogOut = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Screen.setClass(getApplicationContext(), AuthActivity.class);
                                        startActivity(Screen);

                                        _Loading(false);
                                        finish();
                                    }
                                });
                            }
                        };
                        _timer.schedule(LogOut, (int)(1000));
                    }
                });
                Dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                Dialog.create().show();
            }
        });

        _WiFi_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
                final String _tag = _param1;
                final String _response = _param2;
                final HashMap<String, Object> _responseHeaders = _param3;
                LogOut = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _Loading(false);
                            }
                        });
                    }
                };
                _timer.schedule(LogOut, (int)(500));
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                _Loading(true);
            }
        };
    }

    private void initializeLogic() {
        _Loading(true);
        Time = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _Loading(false);
                        Time.cancel();
                    }
                });
            }
        };
        _timer.schedule(Time, (int)(2000));
        _Design();
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("A", "B");
            Slide.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("C", "D");
            Slide.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("E", "F");
            Slide.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("G", "H");
            Slide.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("I", "J");
            Slide.add(_item);
        }

        ViewPage.setAdapter(new ViewPageAdapter(Slide));
        ((PagerAdapter)ViewPage.getAdapter()).notifyDataSetChanged();
        position = 0;
        number = 0;
        Time = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewPage.setCurrentItem((int)number);
                        number++;
                        if (number == 4) {
                            number = 0;
                        }
                    }
                });
            }
        };
        _timer.scheduleAtFixedRate(Time, (int)(0), (int)(3000));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Time = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _Loading(true);
                        finish();
                    }
                });
            }
        };
        _timer.schedule(Time, (int) (1500));
    }
    public void _Design() {
        linear9.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFFFFFFFF));
        linear14.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFFFFFFFF));
        LudoBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF000321));
        linear10.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFFFFFFFF));
        FrerFireBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF000321));
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


    public void _SHAKIB(final View _view) {
        OB.setTarget(_view);
        OB.setPropertyName("rotation");
        OB.setFloatValues((float)(-60), (float)(0));
        OB.setDuration((int)(1000));
        OB.setInterpolator(new BounceInterpolator());
        OB.start();
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


    public void _Redius_imageView(final ImageView _view, final double _redius) {
        //Powered by: PAK Developer
        Bitmap bm = ((android.graphics.drawable.BitmapDrawable)_view.getDrawable()).getBitmap();

        _view.setImageBitmap(getRoundedCornerBitmap(bm, ((int)_redius)));

    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public class ViewPageAdapter extends PagerAdapter {

        Context _context;
        ArrayList<HashMap<String, Object>> _data;

        public ViewPageAdapter(Context _ctx, ArrayList<HashMap<String, Object>> _arr) {
            _context = _ctx;
            _data = _arr;
        }

        public ViewPageAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _context = getApplicationContext();
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public boolean isViewFromObject(View _view, Object _object) {
            return _view == _object;
        }

        @Override
        public void destroyItem(ViewGroup _container, int _position, Object _object) {
            _container.removeView((View) _object);
        }

        @Override
        public int getItemPosition(Object _object) {
            return super.getItemPosition(_object);
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            // Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method
            return "page " + String.valueOf(pos);
        }

        @Override
        public Object instantiateItem(ViewGroup _container,  final int _position) {
            View _view = LayoutInflater.from(_context).inflate(R.layout.slider, _container, false);

            final androidx.cardview.widget.CardView CardView = _view.findViewById(R.id.CardView);
            final ImageView Image = _view.findViewById(R.id.Image);

            if (_position == 0) {
                Image.setImageResource(R.drawable.img_1);
            }
            if (_position == 1) {
                Image.setImageResource(R.drawable.img_2);
            }
            if (_position == 2) {
                Image.setImageResource(R.drawable.img_3);
            }
            if (_position == 3) {
                Image.setImageResource(R.drawable.img_4);
            }
            if (_position == 4) {
                Image.setImageResource(R.drawable.img_5);
            }

            _container.addView(_view);
            return _view;
        }
    }
}