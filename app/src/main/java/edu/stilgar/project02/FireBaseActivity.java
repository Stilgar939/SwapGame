package edu.stilgar.project02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FireBaseActivity extends AppCompatActivity {
    DatabaseReference db;
    ArrayList<imagenes> ali = new ArrayList<>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);
        lv = findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imagenes img = ali.get(position);

                String x = img.getValor();

                byte[] imageAsByte = Base64.decode(x, Base64.DEFAULT);

                Bitmap bm = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
                bm = Bitmap.createScaledBitmap(bm,240,240, true);

                ArrayList<Bitmap> imgs = splitBitmap(bm);
                Intent intentt = new Intent(getApplicationContext(), GamePlay.class);
                intentt.putParcelableArrayListExtra("bitmap", imgs);
                startActivity(intentt);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        db = FirebaseDatabase.getInstance().getReference().child("imagenes");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()){

                    imagenes img = shot.getValue(imagenes.class);
                    ali.add(img);
                }

            imagesAdapter imgA = new imagesAdapter(FireBaseActivity.this, ali);
            lv.setAdapter(imgA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Bitmap> splitBitmap(Bitmap picture){
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true);
        ArrayList<Bitmap> imgs = new ArrayList<>();
        imgs.add(Bitmap.createBitmap(scaledBitmap, 0, 0, 80 , 80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 80, 0, 80, 80));
        imgs.add(Bitmap.createBitmap(scaledBitmap,160, 0, 80,80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 0, 80, 80, 80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 80, 80, 80,80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 160, 80,80,80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 0, 160, 80,80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 80, 160,80,80));
        imgs.add(Bitmap.createBitmap(scaledBitmap, 160,160,80,80));
        return imgs;
    }
}
