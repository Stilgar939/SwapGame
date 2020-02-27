package edu.stilgar.project02;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PersonalAdapter extends BaseAdapter {

    private ArrayList<Bitmap> imatges;
    private LayoutInflater thisInflater;

    public PersonalAdapter(Context context, ArrayList<Bitmap> imatges) {
        this.thisInflater = LayoutInflater.from(context);
        this.imatges = imatges;
    }

    @Override
    public int getCount() {

        return imatges.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.arrayadapter, viewGroup, false);
            ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.petImage);
            thumbnailImage.setImageBitmap(imatges.get(position));
        }

        return convertView;
    }

}

