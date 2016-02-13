package raksam.com.totoran;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class BaseChoiceActivity extends Activity {

    //クリアボタンと通常ボタンの音
    private SoundPool buttonSoundPool;
    private int buttonSoundId, clearSoundId;

    //共通クラスの取得
    private Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //commonインスタンスがなければ作る(シングルトン)
        if (common == null) {
            //共通クラス取得
            common = (Common) getApplication();
        }

        // 予め音声データを読み込む
        buttonSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); //この定義の仕方はLolipopで非推奨になったけどminAPIレベルがJeryBeanなのでシャーない
        buttonSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.kashan, 0); //各ボタンのサウンド
        clearSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.erase, 0); //クリアボタンのサウンド

    }

    //ボタンにチーム名設定
    protected void buttonMake(String idPrefix) {
        for (int i = 0; i < 13; i++) {
            //2次元ArrayからStringArrayを取り出す（ホームチーム名とアウェイチーム名）
            List<String> teamName = (List<String>)common.teamNameArray.get(i);

            for (int j = 0; j < 3; j++) {
                //id文字列作成
                String idStr = idPrefix + "_button" + String.valueOf(i + 1) + "_" + String.valueOf(j + 1);
                int btnId = getResources().getIdentifier(idStr, "id", getPackageName());

                //ボタンを取得
                Button btn = (Button)findViewById(btnId);

                //ボタンのテキスト設定　※DRAWは定義済み
                if ( j == 0) {
                    btn.setText(teamName.get(0));
                } else if ( j == 2) {
                    btn.setText(teamName.get(1));
                }
            }
        }
    }

    //ボタン押下時の挙動
    public void buttonClicked(View v) {
        //idのプレフィックスをキーにsingleなのかmultiなのか判断
        //判断の内容は以下
        // 色
        // 対応するcommonのboolean２次元配列はどれ？
        // 枠内でマルチ選択可能かどうか？
        // ？


        //既存のエフェクト音を決して音をならす
        playSounds(v, buttonSoundId);
    }

    //クリアボタンの挙動
    public void allClear(View v) {
        //既存のエフェクト音を決して音をならす
        playSounds(v, clearSoundId);

        //全選択状態をクリア(対応配列も初期化)
    }

    //ボタンの音をならす
    private void playSounds(View v, int part) {
        buttonSoundPool.play(part, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //メイン画面に戻る
    public void goBack(View v) {
        finish();
    }
}