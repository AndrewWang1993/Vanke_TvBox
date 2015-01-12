/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.vanrui.MediaPaltform.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.Fragment.ImageGridFragment;
import com.vanrui.MediaPaltform.Fragment.ImagePagerFragment;
import com.vanrui.MediaPaltform.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class SimpleImageActivity extends FragmentActivity {
    final static String TAG="tag";
    final static String TAG_VIDEO="video";
    final static String TAG_IMAGE="image";
    final static String TAG_WEB="web";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
        Fragment fr;
        String tag;
        switch (frIndex) {
            default:
            case ImageGridFragment.VIDEO:
                tag = ImageGridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    ImageGridFragment imageGridFragmentVideo=new ImageGridFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString(TAG,TAG_VIDEO);
                    imageGridFragmentVideo.setArguments(bundle);
                    fr = imageGridFragmentVideo;
                }
                break;
            case ImageGridFragment.IMAGE:
                tag = ImageGridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    ImageGridFragment imageGridFragmentVideo=new ImageGridFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString(TAG,TAG_IMAGE);
                    imageGridFragmentVideo.setArguments(bundle);
                    fr = imageGridFragmentVideo;
                }
                break;
            case ImageGridFragment.WEB:
                tag = ImageGridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    ImageGridFragment imageGridFragmentVideo=new ImageGridFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString(TAG,TAG_WEB);
                    imageGridFragmentVideo.setArguments(bundle);
                    fr = imageGridFragmentVideo;
                }
                break;
            case ImagePagerFragment.INDEX:
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImagePagerFragment();
                    fr.setArguments(getIntent().getExtras());
                }
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
    }
}