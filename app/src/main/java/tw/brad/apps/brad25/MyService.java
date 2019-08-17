package tw.brad.apps.brad25;
//撥音樂伺服器
//撥放專案中的音樂檔,共有三招(1.連接網際網路,2.連接自己sd卡的音樂,3.撥放專案中的音樂檔)
//音樂檔放在res=>androdr rosue =>選擇raw
//今天1.專案搭音樂
//2.sd卡音樂播放
//3.音效使用

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private File sdroot;

    public MyService(){
        sdroot = Environment.getExternalStorageDirectory();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    //剛開始創造時
    @Override
    public void onCreate() {
        super.onCreate();


        //創造一個時間序
        timer = new Timer();
        timer.schedule(new MyTask(), 0, 200);//美0.2秒回報一次

        //使用專案中的音樂檔,所以不需要prepare直接撥放
        mediaPlayer = MediaPlayer.create(this, R.raw.a);//音樂產生檔("自己的頁面","音樂檔資源區")


        //使用sd卡裡的音樂
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.setDataSource(sdroot.getAbsolutePath() + "/faded.mp3");
//            mediaPlayer.prepare();
//        }catch (Exception e){
//            Log.v("brad", e.toString());
//        }


        int len = mediaPlayer.getDuration();//傳回這整首歌的長度,回傳int
        sendBroadcast(new Intent("ACT_LEN").putExtra("len",len));//傳送廣播(音樂長度)
    }

    //你每按一次持續發生時
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String act = intent.getStringExtra("act");//抓到你activity的參數
        if (act != null){//如果有抓到參數資料的話
            if (act.equals("start")){ //如果裡面包含start的字
                mediaPlayer.start();//開啟音樂
            }else if (act.equals("pause") && mediaPlayer!=null && mediaPlayer.isPlaying()){//如果包含暫停,而且音樂不是空,而且音樂正在撥放時
                mediaPlayer.pause();//音樂暫停
            }else if (act.equals("seekto") && mediaPlayer != null){//如果有包含seekto參數而且不為空的話
                int seekto = intent.getIntExtra("seekto", -1); //抓到參數
                if (seekto >= 0) {//如果音樂條大於等於0
                    mediaPlayer.seekTo(seekto); //音樂跳移動到你指定的位置
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //執行緒音樂now
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()){  //如果有音樂,而且正在撥放音樂時
                int now = mediaPlayer.getCurrentPosition();//查詢到再撥到哪裡,存到now
                sendBroadcast(new Intent("ACT_NOW").putExtra("now",now));//廣播送出去(現在播放時間now)
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null){ //如果結束時沒有關調
            if (mediaPlayer.isPlaying()){//如果音樂正在播放
                mediaPlayer.stop();//關掉音樂
            }
            mediaPlayer.release();
        }
        if (timer != null){//如果執行緒沒有空時
            timer.cancel();//關掉執行緒
            timer.purge();
            timer = null;
        }

        super.onDestroy();
    }
}