package org.sopt.angeling.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.sopt.angeling.R;

/**
 * Created by DongHyun on 2016-01-14.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 500);

    }
}