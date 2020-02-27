package edu.stilgar.project02;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class AudioHelper extends IntentService {

    private MediaPlayer mp = new MediaPlayer();

    public AudioHelper() {
        super("serveiAudio");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.megalovonia);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            String operacio = intent.getStringExtra("operacio");
            switch (operacio){
                case "inici" : mp.start();
                    break;
                case "pausa" : mp.pause();
                    break;
                case "loss" :
                    mp.stop();
                    mp.release();
                    mp = null;
                    break;
                case "lossMid" :
                    if (mp.isPlaying())
                        mp.pause();
                    break;
                case "trans" :
                    mp.setVolume(0.5f, 0.5f);
                    break;
                case "gain" :
                    mp.start();
                    mp.setVolume(1.0f, 1.0f);
                    break;
                default:
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {}
}
