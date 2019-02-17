package org.sopt.angeling.Network;

/**
 * Created by DongHyun on 2016-01-13.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApplicationController extends com.orm.SugarApp {

    private static ApplicationController instance;
    public static ApplicationController getInstance(){return instance;}
    // TODO: ApplicationController 인스턴스 생성 및 getter 설정


    private String baseUrl; //이번 세미나에서 baseUrl은 서버파트원들의 ip와 port에 따라 다릅니다.

    // TODO: baseUrl 설정

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
        // TODO: 인스턴스 가져오고 서비스 실행
    }


    private NetworkService networkService;
    public NetworkService getNetworkService() {return networkService;}
    // TODO: NetworkService 인스턴스 생성 및 getter 설정

    public void buildNetworkService(String ip, int port) {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                Log.i("MyTag", "1114");
                baseUrl = String.format("http://%s:%d", ip, port);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson); //서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해서 사용합니다.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory) //GsonConverterFactory를 통해 JSON Array를 객체배열로 바꿔줌.
                        .build();//retrofit을 반환

                networkService = retrofit.create(NetworkService.class);
                // TODO: 4. Retrofit.Builder()를 이용해 Retrofit 객체를 생성한 후 이를 이용해 networkService 정의
            }
        }
    }
}
