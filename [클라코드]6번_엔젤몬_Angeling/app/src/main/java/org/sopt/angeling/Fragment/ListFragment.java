package org.sopt.angeling.Fragment;

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

import org.sopt.angeling.Activity.DetailActivity;
import org.sopt.angeling.Activity.MainActivity;
import org.sopt.angeling.Controller.Distance_Interface;
import org.sopt.angeling.Controller.GpsInfo;
import org.sopt.angeling.Controller.InnerDB;
import org.sopt.angeling.Controller.LCustomAdapter;
import org.sopt.angeling.Model.Thumbnail;
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
 * Created by DongHyun on 2016-01-13.
 */
public class ListFragment extends Fragment implements Distance_Interface{

    public ArrayList<Thumbnail> thumbnails = null;
    ListView listView;
    public LCustomAdapter adapter;

    NetworkService networkService;
    public String keyword = null;
    public int themeNum = 0;
    public String themetype = null;
    TextView distanceFromMe;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.list_listView);


        Bundle extra = getArguments();
        if(extra.getBoolean("search")) {
            keyword = extra.getString("keyword");
        }else{
            themeNum = extra.getInt("themeNum");
            init_type(themeNum);
        }
        Log.i("themeNum",themeNum+"");

        initModel();
        initView();
        initService();
        makeList();

        return view;

    }

    private void init_type(int themeNum) {
        switch (themeNum) {
            case 1 :
                themetype = "env";
                break;
            case 2 :
                themetype = "edu";
                break;
            case 3 :
                themetype = "local";
                break;
            case 4 :
                themetype = "ali";
                break;
            case 5 :
                themetype = "living";
                break;
            case 6 :
                themetype = "other";
                break;
            case 7 :
                themetype = "favorite";
        }
    }

    private void initService() {
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService(MainActivity.ip, MainActivity.port);
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void initModel() {
        thumbnails = new ArrayList<>();
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
        adapter = new LCustomAdapter(thumbnails, getActivity().getApplicationContext());
        listView.setAdapter(adapter);
        get_thumbnail_from_server();
    }
    private void get_thumbnail_from_server() {

        Call<List<Thumbnail>> listCall;
        if(keyword != null){ // keyword Search
            listCall = networkService.getSearchItem(((MainActivity)getActivity()).keyword);
            keyword = null;
        }
        else if(themeNum == MainActivity.FAVORITE) { //Theme Search
            List<InnerDB> list = InnerDB.listAll(InnerDB.class);

            for (int i = 0; i < list.size(); i++) {
                Thumbnail thumbnail = new Thumbnail(list.get(i).progrmRegistNo, list.get(i).progrmSj,
                        list.get(i).postAdres, list.get(i).astelno, list.get(i).x, list.get(i).y);

                thumbnails.add(thumbnail);
            }
            adapter.setthumbnails(thumbnails);
            return;
        }else{
            listCall = networkService.getThemaItem(themetype);
            themetype = null;
        }
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

    @Override
    public void onResume(){
        Log.i("onResume", "aaa");
        if(themeNum == MainActivity.FAVORITE) { //Theme Search
            List<InnerDB> list = InnerDB.listAll(InnerDB.class);
            thumbnails.clear();
            for (int i = 0; i < list.size(); i++) {
                Thumbnail thumbnail = new Thumbnail(list.get(i).progrmRegistNo, list.get(i).progrmSj,
                        list.get(i).postAdres, list.get(i).astelno, list.get(i).x, list.get(i).y);

                thumbnails.add(thumbnail);
            }
            adapter.notifyDataSetChanged();
        }

        super.onResume();
    }


}
