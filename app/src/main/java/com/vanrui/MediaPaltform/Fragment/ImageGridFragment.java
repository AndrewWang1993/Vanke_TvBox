/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
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
package com.vanrui.MediaPaltform.Fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.vanrui.MediaPaltform.Activity.SimpleImageActivity;
import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.Parser.MediaParser;
import com.vanrui.MediaPaltform.Player.VideoViewPlayingActivity;
import com.vanrui.MediaPaltform.R;
import com.vanrui.MediaPaltform.Util.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridFragment extends AbsListViewBaseFragment {

    public static final int VIDEO = 1;
    public static final int IMAGE = 2;
    public static final int WEB = 3;
    public Thread th;
    String tag;
    /**
     * 列表图片URL
     */
    String[] imageUrls;
    /**
     * 列表信息描述
     */
    String[] descString;
    /**
     * 二级列表图片
     */
    String[] picsURL;
    /**
     * 网址或者视频URL
     */
    String contentURL;

    ArrayList<HashMap<String, String[]>> mLishHash;
    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(Constants.Media_Type.TAG);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        parserWhitThread();

        synchronized (th) {
            try {
                th.wait();
            } catch (Exception e) {
                Log.v("Thread Exception", e.toString());
            }
        }


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
        listView = (GridView) rootView.findViewById(R.id.grid);
        listView.setAdapter(new ImageAdapter());
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (tag.equals(Constants.Media_Type.VIDEO)) {
                    String source;
                    contentURL = util.getUrls(mLishHash, tag, position + 1);
                    source = contentURL;
                    if (source == null || source.equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error URL", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getActivity().getApplicationContext(), VideoViewPlayingActivity.class);
                        intent.setData(Uri.parse(source));
                        startActivity(intent);
                    }
                } else if (tag.equals(Constants.Media_Type.PICTURE)) {
                    picsURL = util.getFlipPics(mLishHash, position + 1);
                    startImagePagerActivity(picsURL);
                } else if (tag.equals(Constants.Media_Type.WEB)) {
                    contentURL = util.getUrls(mLishHash, tag, position + 1);
                    Uri uri = Uri.parse(contentURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                holder.desc = (TextView) view.findViewById(R.id.desc);
                holder.imageicon = (ImageView) view.findViewById(R.id.icon);
                holder.desc.setText(descString[position]);
                if (tag.equals(Constants.Media_Type.VIDEO)) {
                    holder.imageicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                } else if (tag.equals(Constants.Media_Type.WEB)) {
                    holder.imageicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_explore));
                    holder.imageicon.setScaleX(1.8f);
                    holder.imageicon.setScaleY(1.8f);
                    holder.imageicon.setImageAlpha(200);
                } else {
                }
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageLoader imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            }
            imageLoader.displayImage(imageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressBar.setProgress(0);
                    holder.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    holder.progressBar.setProgress(Math.round(100.0f * current / total));
                }
            });

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView desc;
        ImageView imageicon;
    }



    public void parserWhitThread() {
        th = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        XmlPullParser xmlPullParser = null;
                        try {
                            InputStream is = util.getInput(Constants.XMLURL);
                            XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
                            xmlPullParser = pullFactory.newPullParser();
                            xmlPullParser.setInput(is, "UTF-8");
                        } catch (Exception e) {
                            Log.v("TAG", e.toString());
                        }
                        ArrayList<Object> arrarys = MediaParser.ParseXml(xmlPullParser);
                        mLishHash = util.process(arrarys);
                        imageUrls = util.getGridDescOrPic(mLishHash, tag, false);
                        descString = util.getGridDescOrPic(mLishHash, tag, true);
                        this.notify();
                    }
                } catch (Exception e) {
                    Log.v("Thread Exception", e.toString());
                }

            }
        };
        th.start();
    }


}