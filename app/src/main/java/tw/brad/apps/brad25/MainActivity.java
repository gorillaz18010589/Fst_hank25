package tw.brad.apps.brad25;
//撥音樂Service
//1.官方android mediaplayer
//玩硬體的東西跟狀態有關,如果mic風沒有把電源打開沒有用
//所以你處在什麼狀態,做什麼事情

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private MyReceiver receiver;
    private SeekBar seekBar;

    private SoundPool sp;
    private  int soundTest1, soundTest2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //要玩sd卡的音樂,權限要處理
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        }

        //置入音效
//        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//        soundTest1 = sp.load(this, R.raw.test1, 1);
//        soundTest2 = sp.load(this, R.raw.test2, 1);


        //當音樂跳要改變時
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {//當觸碰到音樂跳(音樂條,i進度,使用者是否操做)
                if (fromUser){ //如果有動到為true
                    Log.v("brad", "fromUser");
                    Intent intent = new Intent(MainActivity.this, MyService.class); //從這頁面連接到service頁面
                    intent.putExtra("seekto", i); //要跳去哪呢,跳到i的地方
                    intent.putExtra("act", "seekto");//設定act,裡面值為seekto
                    startService(intent);
                }
            }
            //摸著音樂條滑
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //放開音樂調
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //撥放音樂參數
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACT_LEN");//加上長度參數
        filter.addAction("ACT_NOW");//加上現在參數
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    //按按鈕啟動音樂
    public void startPlay(View view) {
        Intent intent = new Intent(this, MyService.class); //從這頁面連接到service頁面
        intent.putExtra("act", "start");//掛上srart參數
        startService(intent);
    }

    //按按鈕關掉音樂
    public void stopPlay(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);//關掉連接
    }

    //按按鈕暫停
    public void pausePlay(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("act", "pause");
        startService(intent);
    }

    //音效啟動按鈕1
    public void test1(View view) {
        sp.play(soundTest1,1,1,1,0,1); //設定音效啟動(1.左聲道2,右聲道3.)
    }
    //音效啟動按鈕2
    public void test2(View view) {
        sp.play(soundTest2,1,1,1,0,1);
    }

    //接受廣播音樂長度
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();//抓到廣播action
            if (act.equals("ACT_LEN")){//如果有包含這個ACT_LEN
                int len = intent.getIntExtra("len", -1);//抓到廣播傳來的音樂長度參數
                Log.v("brad", "len:" + len);
                seekBar.setMax(len); //音樂條的最大值(為你音樂的大小)
            }else if (act.equals("ACT_NOW")){//如果包含"ACT_NOW"時
                int now = intent.getIntExtra("now", -1);//接收到now參數
                seekBar.setProgress(now);//這個目錄條,回報到你給得now進度
            }
        }
    }

}