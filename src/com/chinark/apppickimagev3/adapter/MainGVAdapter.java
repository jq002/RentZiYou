package com.chinark.apppickimagev3.adapter;

import android.content.Context;
//import android.support.v7.appcompat.R;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

//import com.chinark.apppickimagev3.R;
import com.chinark.apppickimagev3.utils.SDCardImageLoader;
import com.chinark.apppickimagev3.utils.ScreenUtils;
import com.example.rentziyou.R;


import java.util.ArrayList;

/**
 * 涓婚〉闈腑GridView鐨勯�閰嶅櫒
 *
 * @author hanj
 */

public class MainGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;

    private SDCardImageLoader loader;

    public MainGVAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_gridview_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setTag(filePath);
        loader.loadImage(3, filePath, holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }

}
