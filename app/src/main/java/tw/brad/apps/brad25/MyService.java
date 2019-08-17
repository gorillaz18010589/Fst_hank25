package tw.brad.apps.brad25;
//撥音樂伺服器
//撥放專案中的音樂檔,共有三招(1.連接網際網路,2.連接自己sd卡的音樂,3.撥放專案中的音樂檔)
//音樂檔放在res=>androdr rosue =>選擇raw
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {
  private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    //剛開始創造時
    @Override
    public void onCreate() {
        super.onCreate();

        //使用專案中的音樂檔,所以不需要prepare直接撥放
        mediaPlayer = MediaPlayer.create(this,R.raw.brad); //音樂產生檔("自己的頁面","音樂檔資源區")
//        mediaPlayer.start();
    }
    //你每按一次持續發生時
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String act = intent.getStringExtra("act"); //抓到你activity的參數
        if(act != null){ //如果有抓到參數資料的話
            if(act.equals("start")){ //如果裡面包含start的字
                mediaPlayer.start(); //開啟音樂
            }else if(act.equals("pause") && mediaPlayer != null && mediaPlayer.isPlaying()){ //如果包含暫停,而且音樂不是空,而且音樂正在撥放時
                mediaPlayer.pause();//音樂暫停
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    //關掉時
    @Override
    public void onDestroy() {
        if(mediaPlayer != null){ //如果結束時沒有關調
            if(mediaPlayer.isPlaying()){//如果音樂正在播放
                mediaPlayer.stop();//關掉音樂
            }
            mediaPlayer.release();
        }

        super.onDestroy();
    }
}
