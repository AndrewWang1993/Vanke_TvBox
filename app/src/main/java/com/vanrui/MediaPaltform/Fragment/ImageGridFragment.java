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
import com.vanrui.MediaPaltform.Player.VideoViewPlayingActivity;
import com.vanrui.MediaPaltform.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridFragment extends AbsListViewBaseFragment {

    public static final int VIDEO = 1;
    public static final int IMAGE = 2;
    public static final int WEB = 3;
    final static String TAG = "tag";
    final static String TAG_VIDEO = "video";
    final static String TAG_IMAGE = "image";
    final static String TAG_WEB = "web";
    String tag;

    String[] imageUrls = Constants.IMAGES;

    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
        listView = (GridView) rootView.findViewById(R.id.grid);
        listView.setAdapter(new ImageAdapter());
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (tag.equals(TAG_VIDEO)) {
                    String source = "http://bcs.duapp.com/hhshipin/media/18%E6%97%A5%E5%AE%98%E6%96%B910%E4%BD%B3%E7%90%83.mp4";
                    if (source == null || source.equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error URL", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getActivity().getApplicationContext(), VideoViewPlayingActivity.class);
                        intent.setData(Uri.parse(source));
                        startActivity(intent);
                    }
                } else if (tag.equals(TAG_IMAGE)) {
                    startImagePagerActivity(position);
                } else if (tag.equals(TAG_WEB)) {
                    Uri uri = Uri.parse("http://www.qq.com");
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
                if (tag.equals(TAG_VIDEO)) {
                    holder.imageicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    holder.desc.setText("万科视频 " + position);
                } else if (tag.equals(TAG_WEB)) {
                    holder.imageicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_explore));
                    holder.imageicon.setScaleX(1.8f);
                    holder.imageicon.setScaleY(1.8f);
                    holder.imageicon.setImageAlpha(200);
                    holder.desc.setText("万科网址 " + position);
                }else {
                    holder.desc.setText("万科图片 " + position);
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
}