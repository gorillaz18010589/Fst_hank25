package tw.brad.apps.brad25;
//撥音樂Service
//1.官方android mediaplayer
//玩硬體的東西跟狀態有關,如果mic風沒有把電源打開沒有用
//所以你處在什麼狀態,做什麼事情
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //按按鈕啟動音樂
    public void startPlay(View view) {
        Intent intent = new Intent(this,MyService.class);  //從這頁面連接到service頁面
        intent.putExtra("act","start"); //掛上srart參數
        startService(intent);
    }
    //按按鈕關掉音樂
    public void stopPlay(View view) {
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
    }
    //暫停音樂
    public void pausePlay(View view) {
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("act","pause");
        startService(intent);
    }
}
