package org.sopt.angeling.Activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.sopt.angeling.Fragment.CityFragment;
import org.sopt.angeling.Fragment.CountryFragment;
import org.sopt.angeling.Fragment.ListFragment;
import org.sopt.angeling.Fragment.LocationFragment;
import org.sopt.angeling.Fragment.MainFragment;
import org.sopt.angeling.R;


public class MainActivity extends AppCompatActivity {

    public EditText search_editText;
    public Button search_button;
    public TextView angeling_title;
    public Spinner spinner;
    public FragmentManager fm;
    public FragmentTransaction ft;
    public boolean goMain = false;
    public int stack_size = 0;

    public static final int THEME_ENV = 1;
    public static final int THEME_EDU = 2;
    public static final int THEME_LOCAL = 3;
    public static final int THEME_ALI = 4;
    public static final int THEME_LIVING = 5;
    public static final int THEME_OTHER = 6;

    public static final int FAVORITE = 7;


    LinearLayout linear_env, linear_edu, linear_local, linear_ali, linear_living, linear_other;
    LinearLayout linear_seoul, linear_busan, linear_gyeonggi, linear_region_other;

    TextView tv_Thema_title, tv_Thema_env, tv_Thema_edu, tv_Thema_local, tv_Thema_ali, tv_Thema_living, tv_Thema_other;
    TextView tv_Region_title, tv_Region_seoul, tv_Region_gyeonggi, tv_Region_busan, tv_Region_other;

    ImageView Img_Favorite;

    public static final String ip = "52.32.168.151";
    public static final int port = 5000;

    public String keyword;

    private boolean spinnerFlag =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }
    private void initFragment(){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragment_place, new MainFragment());
        ft.commit();
    }

    public void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Img_Favorite = (ImageView)findViewById(R.id.img_favorite);

        search_editText = (EditText)findViewById(R.id.editText_search);
        search_button = (Button)findViewById(R.id.button_search);
        spinner = (Spinner)findViewById(R.id.spinner_distance);
        angeling_title = (TextView)findViewById(R.id.TextView_Angeling_Title);
        angeling_title.setTypeface(font);

        linear_env = (LinearLayout) findViewById(R.id.env);
        linear_edu = (LinearLayout) findViewById(R.id.edu);
        linear_local = (LinearLayout) findViewById(R.id.local);
        linear_ali = (LinearLayout) findViewById(R.id.ali);
        linear_living = (LinearLayout) findViewById(R.id.living);
        linear_other = (LinearLayout) findViewById(R.id.other);
        linear_seoul = (LinearLayout) findViewById(R.id.seoul);
        linear_gyeonggi = (LinearLayout) findViewById(R.id.gyeonggi);
        linear_busan = (LinearLayout) findViewById(R.id.busan);
        linear_region_other = (LinearLayout) findViewById(R.id.region_other);

        tv_Thema_title = (TextView) findViewById(R.id.TextView_Thema_Title);
        tv_Thema_title.setTypeface(font);
        tv_Thema_env = (TextView) findViewById(R.id.TextView_Thema_env);
        tv_Thema_env.setTypeface(font);
        tv_Thema_edu = (TextView) findViewById(R.id.TextView_Thema_edu);
        tv_Thema_edu.setTypeface(font);
        tv_Thema_local = (TextView) findViewById(R.id.TextView_Thema_local);
        tv_Thema_local.setTypeface(font);
        tv_Thema_ali = (TextView) findViewById(R.id.TextView_Thema_ali);
        tv_Thema_ali.setTypeface(font);
        tv_Thema_living = (TextView) findViewById(R.id.TextView_Thema_living);
        tv_Thema_living.setTypeface(font);
        tv_Thema_other = (TextView) findViewById(R.id.TextView_Thema_other);
        tv_Thema_other.setTypeface(font);

        tv_Region_title = (TextView) findViewById(R.id.TextView_Region_Title);
        tv_Region_title.setTypeface(font);
        tv_Region_seoul = (TextView) findViewById(R.id.TextView_Region_seoul);
        tv_Region_seoul.setTypeface(font);
        tv_Region_gyeonggi = (TextView) findViewById(R.id.TextView_Region_gyeonggi);
        tv_Region_gyeonggi.setTypeface(font);
        tv_Region_busan = (TextView) findViewById(R.id.TextView_Region_busan);
        tv_Region_busan.setTypeface(font);
        tv_Region_other = (TextView) findViewById(R.id.TextView_Region_other);
        tv_Region_other.setTypeface(font);


        linear_env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_ENV);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_EDU);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_LOCAL);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_ALI);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_living.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_LIVING);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(THEME_OTHER);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_seoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCountryFragment("서울");
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_gyeonggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCountryFragment("경기");
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_busan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCountryFragment("부산");
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        linear_region_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setEnabled(true);
                CityFragment cityFragment = new CityFragment();
                changeFragment(cityFragment);
                goMain = true;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        Img_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(FAVORITE);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = search_editText.getText().toString();

                ListFragment fragment = new ListFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("search", true);
                bundle.putString("keyword", keyword);
                fragment.setArguments(bundle);

                changeFragment(fragment);
                search_editText.setText("");
                goMain = true;
                spinner.setEnabled(true);
            }
        });
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.distance, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setEnabled(false);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!spinnerFlag) {
                    spinnerFlag = true;
                    gpsCheck();
                }
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selected = parent.getItemAtPosition(position).toString();
                if(position == 1)position = 5;
                else if (position == 2) position = 10;
                else if (position == 3) position = 15;

                if (position == 0) {
                    Fragment currentFragment = fm.findFragmentById(R.id.fragment_place);
                    if (currentFragment instanceof ListFragment) {
                        ((ListFragment) currentFragment).adapter.setthumbnails(((ListFragment) currentFragment).thumbnails);
                    } else if (currentFragment instanceof LocationFragment) {
                        ((LocationFragment) currentFragment).adapter.setthumbnails(((LocationFragment) currentFragment).thumbnails);
                    }
                } else {
                    Fragment currentFragment = fm.findFragmentById(R.id.fragment_place);
                    if (currentFragment instanceof ListFragment) {
                        ((ListFragment) currentFragment).calcDistance(position);
                    } else if (currentFragment instanceof LocationFragment) {
                        ((LocationFragment) currentFragment).calcDistance(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(stack_size>0){
            fm.popBackStack();
            stack_size--;
        }
        else if(goMain) {
            spinner.setEnabled(false);
            changeFragment(new MainFragment());
            goMain = false;
        }else{
            super.onBackPressed();
        }
    }

    private void gotoListFragment(int themeNum){
        spinner.setEnabled(true);
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("search",false);
        bundle.putInt("themeNum", themeNum);
        listFragment.setArguments(bundle);
        changeFragment(listFragment);
        goMain = true;
    }
    private void gotoCountryFragment(String location){
        spinner.setEnabled(true);
        CountryFragment countryFragment = new CountryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedCity", location);
        countryFragment.setArguments(bundle);
        changeFragment(countryFragment);
        goMain = true;
    }

    private void changeFragment(Fragment newFragment){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_place, newFragment);
        ft.commit();
    }

    public void gpsCheck(){
        String context= Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(context);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n" + "위치 서비스 기능을 설정하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveConfigGPS();
                            spinnerFlag =false;
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            spinnerFlag =false;
                        }
                    });
            AlertDialog alert=builder.create();
            alert.show();
        }else{
            spinnerFlag =false;
        }
    }
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
}
