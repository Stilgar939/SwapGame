package edu.stilgar.project02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean isReproduint= true;
    private Intent intentMusic;
    private AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1,b2;
        b1 = findViewById(R.id.juegaCamera);
        b2 = findViewById(R.id.juegaBiblio);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
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

    @Override
    public void onClick(View arg0) {
        Intent intent;
        switch (arg0.getId()){
            case R.id.juegaCamera:
                final int PERMIS_CAMARA = 200;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMIS_CAMARA);
                    return;
                }
                intent = new Intent(this, Camara.class);
                startActivity(intent);
                break;
            case R.id.juegaBiblio:
                intent = new Intent(this, FireBaseActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
}
