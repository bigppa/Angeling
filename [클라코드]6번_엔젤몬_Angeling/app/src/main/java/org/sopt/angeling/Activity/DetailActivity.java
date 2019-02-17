package org.sopt.angeling.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.sopt.angeling.Controller.InnerDB;
import org.sopt.angeling.Model.Thumbnail;
import org.sopt.angeling.Model.Voluntary;
import org.sopt.angeling.NaverMap.NMapPOIflagType;
import org.sopt.angeling.NaverMap.NMapViewerResourceProvider;
import org.sopt.angeling.Network.ApplicationController;
import org.sopt.angeling.Network.NetworkService;
import org.sopt.angeling.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by DongHyun on 2016-01-13.
 */public class DetailActivity extends NMapActivity {

    TextView tv_Title;
    TextView tv_Subject;
    TextView tv_Startdate;
    TextView tv_Enddate;
    TextView tv_Starttime;
    TextView tv_Endtime;
    TextView tv_StartRecruitdate;
    TextView tv_EndRecruitdate;
    TextView tv_Peoplenum;
    TextView tv_Part;
    TextView tv_Enrollplace;
    TextView tv_Actplace;
    TextView tv_Manager;
    TextView tv_Telephone;
    TextView tv_Email;
    TextView tv_Institutuelocation;
    TextView tv_Actdetail;
    ImageView btn_Call;
    ImageView btn_Favorite;

    TextView titlee;
    TextView duringg;
    TextView ripple1;
    TextView timee;
    TextView recruitdatee;
    TextView ripple2;
    TextView peoplenumm;
    TextView myung;
    TextView partt;
    TextView enrollplacee;
    TextView volunteerplacee;
    TextView managerr;
    TextView telephonee;
    TextView emaill;
    TextView addresss;
    TextView detaill;
    TextView clock1;
    TextView clock2;

    public NMapContext mMapContext;
    public InnerDB innerDB;
    public Thumbnail thumbnail;
    public Voluntary voluntary_temp;

    private NMapView mMapView;
    private NMapController mMapController;
    private static final String LOG_TAG = "NMapViewer";
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    int markerId;

    public String ip = "52.32.168.151";
    public int port = 5000;
    NetworkService networkService;

    String tel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        mMapView = (NMapView)findViewById(R.id.mapView);
        markerId = NMapPOIflagType.PIN;
        mMapView.setApiKey("bbefec93419e98cf9417c9256e7ee700");
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(true, null);
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapController = mMapView.getMapController();
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);



        Intent intent = getIntent();
        long registno=0;

        Bundle bundle = intent.getBundleExtra("regid");
        registno = bundle.getLong("progid");
        Log.i("MyTag", "받은값 : thumbnail_t.progrmRegistNo : " + registno);
        initView();
        initService();
        get_data_from_server(registno);

        btn_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCheck();
            }
        });
        btn_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innerDB = new InnerDB(thumbnail.progrmRegistNo,thumbnail.progrmSj,thumbnail.postAdres,thumbnail.astelno,thumbnail.x,thumbnail.y);
                List<InnerDB> list = InnerDB.listAll(InnerDB.class);

                for(int i=0; i<list.size();i++){
                    if((thumbnail.progrmRegistNo) == list.get(i).progrmRegistNo){
                        list.get(i).delete();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                innerDB.save();
                Toast.makeText(getApplicationContext(), "즐겨찾기에 추가됐습니다.", Toast.LENGTH_LONG).show();
                }
        });
    }

    private void callCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("전화를 거시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tel));
                        startActivity(intent_call);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert=builder.create();
        alert.show();
    }


    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            if (errorInfo == null) { // success
                //mMapController.setMapCenter(new NGeoPoint(126.97, 37.56), 13);
            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());
            }
        }

        @Override
        public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
        }

        @Override
        public void onMapCenterChangeFine(NMapView nMapView) {

        }

        @Override
        public void onZoomLevelChange(NMapView nMapView, int i) {

        }

        @Override
        public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

        }
    };

    private void get_data_from_server(long registno) {
        Call<Voluntary> DetailCall = networkService.getDetail(registno);
        DetailCall.enqueue(new Callback<Voluntary>() {
            @Override
            public void onResponse(Response<Voluntary> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
                    Log.i("MyTag", jsonString);

                    voluntary_temp = response.body();
                    thumbnail = new Thumbnail(voluntary_temp.progrmRegistNo,voluntary_temp.progrmSj,voluntary_temp.postAdres,voluntary_temp.astelno,voluntary_temp.x,voluntary_temp.y);
                    tel = voluntary_temp.astelno;

                    set_detail(voluntary_temp);
                    setMap();

                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "에러 상태 코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", t.getMessage());
            }
        });
    }

    private void set_detail(Voluntary voluntary_temp) {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");

        tv_Title.setTypeface(font);
        tv_Subject.setText(voluntary_temp.progrmSj);
        tv_Subject.setTypeface(font);
        tv_Startdate.setText(voluntary_temp.progrmBgnde);
        tv_Startdate.setTypeface(font);
        tv_Enddate.setText(voluntary_temp.progrmEndde);
        tv_Enddate.setTypeface(font);
        tv_Starttime.setText(Integer.toString(voluntary_temp.actBeginTm));
        tv_Starttime.setTypeface(font);
        tv_Endtime.setText(Integer.toString(voluntary_temp.actEndTm));
        tv_Endtime.setTypeface(font);
        tv_StartRecruitdate.setText(voluntary_temp.noticeBgnde);
        tv_StartRecruitdate.setTypeface(font);
        tv_EndRecruitdate.setText(voluntary_temp.noticeEndde);
        tv_EndRecruitdate.setTypeface(font);
        tv_Peoplenum.setText(Integer.toString(voluntary_temp.rcritNmpr));
        tv_Peoplenum.setTypeface(font);
        tv_Part.setText(voluntary_temp.srvcClCode);
        tv_Part.setTypeface(font);
        tv_Enrollplace.setText(voluntary_temp.nanmmbyNm);
        tv_Enrollplace.setTypeface(font);
        tv_Actplace.setText(voluntary_temp.actPlace);
        tv_Actplace.setTypeface(font);
        tv_Manager.setText(voluntary_temp.nanmmbyNmAdmn);
        tv_Manager.setTypeface(font);
        tv_Telephone.setText(voluntary_temp.astelno);
        tv_Telephone.setTypeface(font);
        tv_Email.setText(voluntary_temp.email);
        tv_Email.setTypeface(font);
        tv_Institutuelocation.setText(voluntary_temp.postAdres);
        tv_Institutuelocation.setTypeface(font);
        tv_Actdetail.setText(voluntary_temp.progrmCn);
        tv_Actdetail.setTypeface(font);
        titlee.setTypeface(font);
        duringg.setTypeface(font);
        ripple1.setTypeface(font);
        timee.setTypeface(font);
        recruitdatee.setTypeface(font);
        ripple2.setTypeface(font);
        peoplenumm.setTypeface(font);
        myung.setTypeface(font);
        partt.setTypeface(font);
        enrollplacee.setTypeface(font);
        volunteerplacee.setTypeface(font);
        managerr.setTypeface(font);
        telephonee.setTypeface(font);
        emaill.setTypeface(font);
        addresss.setTypeface(font);
        detaill.setTypeface(font);
        clock1.setTypeface(font);
        clock2.setTypeface(font);
    }

    private void setMap() {


        Location loc = new Location("qq");
        loc.setLongitude(voluntary_temp.x);
        loc.setLatitude(voluntary_temp.y);

        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);

        poiData.beginPOIdata(1);
        poiData.addPOIitem(loc.getLongitude(), loc.getLatitude(), tv_Enrollplace.getText().toString(), markerId, 0);
        poiData.endPOIdata();

        mMapController.setMapCenter(new NGeoPoint(loc.getLongitude(), loc.getLatitude()), 12);
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);


    }

    private void initView() {
        tv_Title = (TextView) findViewById(R.id.TextView_Detail_Title);
        tv_Subject = (TextView) findViewById(R.id.TextView_Subject);
        tv_Startdate = (TextView) findViewById(R.id.TextView_Startdate);
        tv_Enddate = (TextView) findViewById(R.id.TextView_Enddate);
        tv_Starttime = (TextView) findViewById(R.id.TextView_StartTime);
        tv_Endtime = (TextView) findViewById(R.id.TextView_EndTime);
        tv_StartRecruitdate = (TextView) findViewById(R.id.TextView_StartRecruitdate);
        tv_EndRecruitdate = (TextView) findViewById(R.id.TextView_EndRecruitdate);
        tv_Peoplenum = (TextView) findViewById(R.id.TextView_PeopleNum);
        tv_Part = (TextView) findViewById(R.id.TextView_part);
        tv_Enrollplace = (TextView) findViewById(R.id.TextView_EnrollPlace);
        tv_Actplace = (TextView) findViewById(R.id.TextView_ActPlace);
        tv_Manager = (TextView) findViewById(R.id.TextView_manager);
        tv_Telephone = (TextView) findViewById(R.id.TextView_Telephone);
        tv_Email = (TextView) findViewById(R.id.TextView_Email);
        tv_Institutuelocation = (TextView) findViewById(R.id.TextView_InstituteLocation);
        tv_Actdetail = (TextView) findViewById(R.id.TextView_ActDetail);

        btn_Call = (ImageView) findViewById(R.id.button_Call);
        btn_Favorite = (ImageView) findViewById(R.id.button_Favorite);

        titlee= (TextView) findViewById(R.id.subjecttt);
        duringg= (TextView) findViewById(R.id.duringg);
        ripple1= (TextView) findViewById(R.id.ripple1);
        timee= (TextView) findViewById(R.id.timeee);
        recruitdatee= (TextView) findViewById(R.id.mojip);
        ripple2= (TextView) findViewById(R.id.ripple);
        peoplenumm= (TextView) findViewById(R.id.headnum);
        myung= (TextView) findViewById(R.id.myung);
        partt= (TextView) findViewById(R.id.themeee);
        enrollplacee= (TextView) findViewById(R.id.enrollPlaceee);
        volunteerplacee= (TextView) findViewById(R.id.volunteerPlace);
        managerr= (TextView) findViewById(R.id.managerName);
        telephonee= (TextView) findViewById(R.id.phoneNummmm);
        emaill= (TextView) findViewById(R.id.emailllll);
        addresss= (TextView) findViewById(R.id.adresssss);
        detaill= (TextView) findViewById(R.id.detaillll);
        clock1= (TextView) findViewById(R.id.clockkk);
        clock2= (TextView) findViewById(R.id.clockkkkk);



    }

    private void initService() {
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService(ip, port);
        networkService = ApplicationController.getInstance().getNetworkService();
    }

}