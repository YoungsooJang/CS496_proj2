package com.example.q.cs496_proj2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePaths;

    public GridViewAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            image = new ImageView(context);
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 113, this.context.getResources().getDisplayMetrics());
            final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 113, this.context.getResources().getDisplayMetrics());
            image.setLayoutParams(new GridView.LayoutParams(width, height));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            image = (ImageView) convertView;
        }
        if (imagePaths.get(position).contains("/storage/")) {
            image.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(position)));
        } else {
            Ion.with(image).load("http://52.78.101.202:3000/api/images/" + imagePaths.get(position));
        }
        return image;
    }
}
