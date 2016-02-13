package raksam.com.totoran;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class BaseChoiceActivity extends Activity {

    //クリアボタンと通常ボタンの音
    private SoundPool buttonSoundPool;
    private int buttonSoundId, clearSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 予め音声データを読み込む
        buttonSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); //この定義の仕方はLolipopで非推奨になったけどminAPIレベルがJeryBeanなのでシャーない
        buttonSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.kashan, 0);
        clearSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.erase, 0);
    }

    //クリアボタンの挙動
    public void allClear(View v) {
        //音を追加
        buttonSoundPool.play(clearSoundId, 1.0F, 1.0F, 0, 0, 1.0F);

        //全選択状態をクリア(対応配列も初期化)
    }

    //メイン画面に戻る
    public void goBack(View v) {
        finish();
    }
}
