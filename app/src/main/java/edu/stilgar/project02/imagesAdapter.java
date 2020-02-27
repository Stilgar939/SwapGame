package edu.stilgar.project02;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class imagesAdapter extends ArrayAdapter<imagenes> {

    private ArrayList<imagenes> imatges;
    private LayoutInflater thisInflater;
    private Activity context;


    public imagesAdapter(Activity context, ArrayList<imagenes> imatges) {
        super(context, R.layout.imagesadapter, imatges);
        this.thisInflater = LayoutInflater.from(context);
        this.imatges = imatges;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View lsV = inflater.inflate(R.layout.imagesadapter, null, true);
        ImageView valor = lsV.findViewById(R.id.valor);

        imagenes img = imatges.get(position);

        String x = img.getValor();

        byte[] imageAsByte = Base64.decode(x, Base64.DEFAULT);

        Bitmap bm = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
        bm = Bitmap.createScaledBitmap(bm,240,240, true);

        valor.setImageBitmap(bm);

        return lsV;
    }

}

