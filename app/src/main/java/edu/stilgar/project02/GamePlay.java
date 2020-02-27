package edu.stilgar.project02;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class GamePlay extends AppCompatActivity {

    ArrayList<Bitmap> imageSplited = new ArrayList<>();
    ArrayList<Bitmap> imageSplitedcheck = new ArrayList<>();
    int transparente = 8, counter = 0;
    PersonalAdapter ia;
    TextView t1;
    MediaPlayer mp1;
    ImageView title;
    private boolean isReproduint= true;
    private Intent intentMusic;
    private AudioManager am;
    GridView gvS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Intent getData = getIntent();
        imageSplited = getData.getParcelableArrayListExtra("bitmap");
        imageSplitedcheck = (ArrayList<Bitmap>) imageSplited.clone();
        t1 = (TextView) findViewById(R.id.contador);
        gvS = (GridView) findViewById(R.id.GridMap);
        title = findViewById(R.id.title);
        Bitmap bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        imageSplitedcheck.set(8, bitmap);
        Collections.shuffle(imageSplited);
        imageSplited.set(8, bitmap);
        mp1 = MediaPlayer.create(this, R.raw.move);
        ia = new PersonalAdapter(getApplicationContext(), imageSplited);
        gvS.setAdapter(ia);
        Animation rotate = AnimationUtils.loadAnimation(this , R.anim.rotator);
        title.startAnimation(rotate);
        gvS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if (checkCasilla(imageSplited,position)) {
                    if (mp1.isPlaying()){
                        mp1.pause();
                        mp1.seekTo(0);
                    }
                    mp1.start();
                    //counter++;
                    LlistaTask task= new LlistaTask();
                    task.execute();
                    Collections.swap(imageSplited, position, transparente);
                    transparente = position;
                    ia.notifyDataSetChanged();
                    gvS.setAdapter(ia);
                    //t1.setText(String.valueOf(counter));

                    if (imageSplitedcheck.equals(imageSplited)){
                        uploadDB();
                    }}}
        });
        intentMusic= new Intent(this, AudioHelper.class);
        intentMusic.putExtra("operacio", "inici");
        startService(intentMusic);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_pause);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;

                if (isReproduint) {
                    text = "Pausant Audio";
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    isReproduint = false;
                    intentMusic.putExtra("operacio", "pausa");
                    startService(intentMusic);
                } else {
                    text = "Reproduint Audio";
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    isReproduint = true;
                    intentMusic.putExtra("operacio", "inici");
                    startService(intentMusic);
                }

                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int requestResult = am.requestAudioFocus(
                mAudioFocusListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            intentMusic.putExtra("operacio", "inici");
            startService(intentMusic);



        } else if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            intentMusic.putExtra("operacio", "pausa");
            startService(intentMusic);

        }
    }

    private static boolean checkCasilla(ArrayList<Bitmap> imageSplited, int position){
        ArrayList<Integer> checkposition = new ArrayList<>();
        checkposition.add(position + 1);
        checkposition.add(position + 3);
        checkposition.add(position - 1);
        checkposition.add(position - 3);
        boolean yolo = false;
        for (int i = 0; i < checkposition.size(); i++){
            if (checkposition.get(i) < 0 || checkposition.get(i) > 8){
                continue;
            }
            Integer color = getDominantColor(imageSplited.get(checkposition.get(i)));
            if (color.equals(Color.TRANSPARENT)){
                yolo = true;
                break;
            }else{
                yolo = false;
            }
        }
        return yolo;
    }

    private static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    intentMusic.putExtra("operacio", "loss");
                    startService(intentMusic);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    intentMusic.putExtra("operacio", "lossMid");
                    startService(intentMusic);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    intentMusic.putExtra("operacio", "trans");
                    startService(intentMusic);

                case AudioManager.AUDIOFOCUS_GAIN:
                    intentMusic.putExtra("operacio", "gain");
                    startService(intentMusic);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            uploadDB();
        }else if (id == R.id.action_exit){
            System.exit(0);
        }
        imageSplited = imageSplitedcheck;
        return super.onOptionsItemSelected(item);
    }

    private void uploadDB(){
        Toast toast = Toast.makeText(this, "GANASTE! Tu puntuación quedará registrada", Toast.LENGTH_SHORT);
        toast.show();
        DatabaseReference DBArtistes = FirebaseDatabase.getInstance().getReference("puntuaciones");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        DBArtistes.child(currentDateandTime).setValue(counter);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private class LlistaTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return String.valueOf(counter++);
        }

        @Override
        protected void onPostExecute(final String resultat) {
            t1.setText(String.valueOf(counter));
        }
    }

}
