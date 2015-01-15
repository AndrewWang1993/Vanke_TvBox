package com.vanrui.MediaPaltform.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vanrui.MediaPaltform.AnimView.*;
import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.Fragment.ImageGridFragment;
import com.vanrui.MediaPaltform.Parser.MediaParser;
import com.vanrui.MediaPaltform.R;
import com.vanrui.MediaPaltform.Util.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wxm on 2015/1/10 in Vanke
 */

public class BoxMainActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lancher);
		addView();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

//        if (android.os.Build.VERSION.SDK_INT > 9) {       //网络严格模式
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

	}

	private void addView() {
        LinearLayout layout_item = (LinearLayout) findViewById(R.id.layout_item);
        LancherLayout lancherView = new LancherLayout(this);
		lancherView.initListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }

        });

		lancherView.initListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.zoout, R.anim.zoin);
                switch (v.getId()) {
                    case R.id.picture:
                        Intent intent1 = new Intent(BoxMainActivity.this, SimpleImageActivity.class);
                        intent1.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.IMAGE);
                        startActivity(intent1);
                        break;
                    case R.id.video:
                        Intent intent2 = new Intent(BoxMainActivity.this, SimpleImageActivity.class);
                        intent2.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.VIDEO);
                        startActivity(intent2);
                        break;
                    case R.id.web:
                        Intent intent3 = new Intent(BoxMainActivity.this, SimpleImageActivity.class);
                        intent3.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.WEB);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        break;
                    default:
                        break;
                }
            }
        });
		layout_item.addView(lancherView);
	}



}
