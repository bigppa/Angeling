package org.sopt.angeling.Fragment;

/**
 * Created by DongHyun on 2016-01-13.
 */

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.sopt.angeling.Activity.DetailActivity;
import org.sopt.angeling.Activity.MainActivity;
import org.sopt.angeling.Controller.CustomAdapter;
import org.sopt.angeling.Controller.GpsInfo;
import org.sopt.angeling.Controller.MarkerListener;
import org.sopt.angeling.Model.Thumbnail;
import org.sopt.angeling.NaverMap.NMapPOIflagType;
import org.sopt.angeling.NaverMap.NMapViewerResourceProvider;
import org.sopt.angeling.Network.ApplicationController;
import org.sopt.angeling.Network.NetworkService;
import org.sopt.angeling.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * LocationFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용한 예제임.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
public class LocationFragment extends Fragment implements MarkerListener {

    private NMapView mapView;
    private NMapContext mMapContext;
    private NMapController mMapController;
    private static final String LOG_TAG = "NMapViewer";
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    int markerId;
    //public int locationNum;

    public String addressparams = null;
    public String selectedCity = null;
    public String selectedCountry = null;

    public ArrayList<Thumbnail> thumbnails = null;
    ListView listView;
    public CustomAdapter adapter;

    TextView distanceFromMe;

    NetworkService networkService;
    NMapPOIdataOverlay poiDataOverlay;

    /**
     * Fragment에 포함된 NMapView 객체를 반환함
     */
    private NMapView findMapView(View v) {

        if (!(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup)v;
        if (vg instanceof NMapView) {
            return (NMapView)vg;
        }

        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                continue;
            }

            NMapView mapView = findMapView(child);
            if (mapView != null) {
                return mapView;
            }
        }
        return null;
    }

	/* Fragment 라이프사이클에 따라서 NMapContext의 해당 API를 호출함 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
        markerId = NMapPOIflagType.PIN;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.location_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.location_listView);
        distanceFromMe = (TextView)view.findViewById(R.id.loc_distance_from_me2);

        Bundle extra = getArguments();
        selectedCity = extra.getString("selectedCity");
        selectedCountry = extra.getString("selectedCountry");
        Log.i(selectedCity,selectedCountry);

        initModel();
        initView();
        initService();
        makeList();

        //throw new IllegalArgumentException("onCreateView should be implemented in the subclass of LocationFragment.");
        return view;
    }

    private void initService() {
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService(MainActivity.ip, MainActivity.port);
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Fragment에 포함된 NMapView 객체 찾기
        mapView = findMapView(super.getView());
        if (mapView == null) {
            throw new IllegalArgumentException("LocationFragment dose not have an instance of NMapView.");
        }

        mapView.setApiKey("bbefec93419e98cf9417c9256e7ee700"); //donghyun key

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mMapContext.setupMapView(mapView);
        mMapController = mapView.getMapController();
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true, null);

        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(super.getActivity());
        mOverlayManager = new NMapOverlayManager(super.getActivity(), mapView, mMapViewerResourceProvider);


    }

    @Override
    public void onStart(){
        super.onStart();

        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapContext.onPause();
    }

    @Override
    public void onStop() {

        mMapContext.onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        super.onDestroy();
    }

    private void initModel() {
        thumbnails = new ArrayList<Thumbnail>();
    }

    private void initView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Thumbnail thumbnail_t = (Thumbnail) adapter.getItem(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                Log.i("MyTag", "thumbnail_t.progrmRegistNo : " + thumbnail_t.progrmRegistNo);
                Bundle args =new Bundle();
                args.putLong("progid", thumbnail_t.progrmRegistNo);
                intent.putExtra("regid", args);
                getActivity().startActivity(intent);
            }
        });
    }

    private void makeList() {
        adapter = new CustomAdapter(thumbnails, getActivity().getApplicationContext(), this);
        listView.setAdapter(adapter);
        get_thumbnail_from_server();
    }

    private void get_thumbnail_from_server() {
        selectedCity = alter_city_name(selectedCity);
        addressparams = selectedCity + " " + selectedCountry;


        Call<List<Thumbnail>> listCall = networkService.getAddressItem(addressparams);
        listCall.enqueue(new Callback<List<Thumbnail>>() {
            @Override
            public void onResponse(Response<List<Thumbnail>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
                    Log.i("MyTag", jsonString);

                    thumbnails = (ArrayList<Thumbnail>) response.body();
                    adapter.setthumbnails(thumbnails);
                    adapter.notifyDataSetChanged();

                    firstMarker();
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
    private String alter_city_name(String selectedCity) {
        switch(selectedCity){
            case "서울":
                selectedCity = "서울특별시";
                break;
            case "대구":
                selectedCity = "대구광역시";
                break;
            case "광주":
                selectedCity = "광주광역시";
                break;
            case "울산":
                selectedCity = "울산광역시";
                break;
            case "강원":
                selectedCity = "강원도";
                break;
            case "경남":
                selectedCity = "경상남도";
                break;
            case "전남":
                selectedCity = "전라남도";
                break;
            case "제주":
                selectedCity = "제주도";
                break;
            case "부산":
                selectedCity = "부산광역시";
                break;
            case "인천":
                selectedCity = "인천광역시";
                break;
            case "대전":
                selectedCity = "대전광역시";
                break;
            case "세종":
                selectedCity = "세종시";
                break;
            case "경기":
                selectedCity = "경기도";
                break;
            case "경북":
                selectedCity = "경상북도";
                break;
            case "전북":
                selectedCity = "전라북도";
                break;
            case "충남":
                selectedCity = "충청남도";
                break;
            default:
                break;
        }
        return selectedCity;
    }

    private void firstMarker(){
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        if (adapter.getItem(0) != null) {
            Thumbnail thumbnail_temp = (Thumbnail) adapter.getItem(0);

            Location location = new Location("aa");

            location.setLatitude(thumbnail_temp.y);
            location.setLongitude(thumbnail_temp.x);

            poiData.beginPOIdata(1);
            poiData.addPOIitem(location.getLongitude(), location.getLatitude(), thumbnail_temp.progrmSj, markerId, 0);
            poiData.endPOIdata();

            mMapController.setMapCenter(new NGeoPoint(location.getLongitude(), location.getLatitude()), 12);
            poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);


        }
    }

    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            if (errorInfo == null) { // success
                //mMapController.setMapCenter(new NGeoPoint(127.0630205, 37.5091300), 13);
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
    @Override
    public void pickMarker(Thumbnail thumbnail) {
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);

        double x = thumbnail.x;
        double y = thumbnail.y;

        Location location = new Location("a");
        location.setLongitude(x);
        location.setLatitude(y);

        poiData.beginPOIdata(1);
        poiData.addPOIitem(location.getLongitude(), location.getLatitude(), thumbnail.progrmSj, markerId, 0);
        poiData.endPOIdata();

        mMapController.setMapCenter(new NGeoPoint(location.getLongitude(), location.getLatitude()), 12);
        poiDataOverlay.removeAllPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        initModel();
    }

    @Override
    public void calcDistance(int position) {
        try {
            Log.i("Thread.sleep(3000)","start");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GpsInfo gps = new GpsInfo(getActivity().getApplicationContext());
        Location myLocation = gps.getLocation();

        Log.i(myLocation.getLatitude()+"",myLocation.getLongitude()+"");

        if(myLocation != null){
            Log.i("myLocation", "not null");
            for(int i=0;i<thumbnails.size();i++){
                Location loc = new Location(i+"");

                Log.i(thumbnails.get(i).x + "", thumbnails.get(i).y + "");
                loc.setLatitude(thumbnails.get(i).y);
                loc.setLongitude(thumbnails.get(i).x);

                double distance = loc.distanceTo(myLocation) / 1000;
                Log.i("distance",distance+"");

                thumbnails.get(i).distance = distance;
            }
            Log.i("before sort", thumbnails.get(0).distance + "");
            Collections.sort(thumbnails, new Comparator<Thumbnail>() {
                @Override
                public int compare(Thumbnail lhs, Thumbnail rhs) {
                    return (lhs.distance < rhs.distance) ? -1 : (lhs.distance > rhs.distance) ? 1 : 0;
                }
            });
            Log.i("after sort", thumbnails.get(0).distance + "");
            ArrayList<Thumbnail> newArr = new ArrayList<>();
            Log.i(thumbnails.size()+"","thumnails.size()");



            for(int i=0; i<thumbnails.size();i++){
                Log.i((int)(thumbnails.get(i).distance)+"",position+"");

                if((int)(thumbnails.get(i).distance) < position){
                    Log.i("aaavvvvv",i+"");
                    newArr.add(thumbnails.get(i));
                    Log.i("aaa",i+"");
                }
            }
            Log.i("setthumbnails", "success");
            adapter.setthumbnails(newArr);
        }
    }
}
