package edu.stilgar.project02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Camara extends AppCompatActivity {

    private static final int PERMIS_CAPTURA_IMATGE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PERMIS_CAPTURA_IMATGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMIS_CAPTURA_IMATGE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ArrayList<Bitmap> imgs = splitBitmap(imageBitmap);
                Intent intentt = new Intent(getApplicationContext(), GamePlay.class);
                intentt.putParcelableArrayListExtra("bitmap", imgs);
                startActivity(intentt);
            }
        }
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
