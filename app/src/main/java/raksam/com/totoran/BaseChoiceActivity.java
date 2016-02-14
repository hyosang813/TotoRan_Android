package raksam.com.totoran;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class BaseChoiceActivity extends Activity {

    //クリアボタンと通常ボタンの音
    private SoundPool buttonSoundPool;
    private int buttonSoundId, clearSoundId;

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //共通クラス取得して初期化
        common = (Common) getApplication();

        // 予め音声データを読み込む
        buttonSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); //この定義の仕方はLolipopで非推奨になったけどminAPIレベルがJeryBeanなのでシャーない
        buttonSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.kashan, 0); //各ボタンのサウンド
        clearSoundId = buttonSoundPool.load(getApplicationContext(), R.raw.erase, 0); //クリアボタンのサウンド

    }

    //ボタンにチーム名設定
    protected void buttonMake(String idPrefix) {
        for (int i = 0; i < 13; i++) {
            //2次元ArrayからStringArrayを取り出す（ホームチーム名とアウェイチーム名）
            ArrayList<String> teamName = common.teamNameArray.get(i);

            for (int j = 0; j < 3; j++) {
                //id文字列作成
                String idStr = idPrefix + "_button" + String.format("%02d", i + 1) + "_" + String.valueOf(j + 1);
                int btnId = getResources().getIdentifier(idStr, "id", getPackageName());

                //ボタンを取得
                ToggleButton btn = (ToggleButton)findViewById(btnId);

                //ボタンのテキスト設定　※DRAWは定義済み
                if ( j == 0) {
                    btn.setText(teamName.get(0));
                    btn.setTextOn(teamName.get(0));
                    btn.setTextOff(teamName.get(0));
                } else if ( j == 2) {
                    btn.setText(teamName.get(1));
                    btn.setTextOn(teamName.get(1));
                    btn.setTextOff(teamName.get(1));
                }
            }
        }
    }

    //ボタン押下時の挙動
    public void buttonClicked(View v) {
        //既存のエフェクト音を決して音をならす
        playSounds(buttonSoundId);
    }

    //クリアボタンの挙動
    public void allClear(View v) {
        //既存のエフェクト音を決して音をならす
        playSounds(clearSoundId);

        //全選択状態をクリア
        //対象がシングルかマルチかを分別
        String idPrefix = v.getId() == R.id.clear_button_single ? "single" : "multi";

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 3; j++) {
                //id文字列作成
                String idStr = idPrefix + "_button" + String.format("%02d", i + 1) + "_" + String.valueOf(j + 1);
                int btnId = getResources().getIdentifier(idStr, "id", getPackageName());

                //ボタンを取得
                ToggleButton btn = (ToggleButton) findViewById(btnId);

                //ボタンのチェック状態をクリア
                btn.setChecked(false);
            }
        }
    }

    //ボタンの選択状態をboolArrayにセット
    protected void setBoolArray(String part) {
        //対象のリストを判断
        ArrayList<ArrayList<Boolean>> parentBoolArray = part.equals("single") ? common.singleBoolArray : common.multiBoolArray;

        //boolArrayを一度空にする
        parentBoolArray.clear();

        //トグルボタンの選択状態をboolArrayにセット
        for (int i = 0; i < 13; i++) {
            //二次元目のBoolArray作成
            ArrayList<Boolean> childBoolArray = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                //id文字列作成
                String idStr = part + "_button" + String.format("%02d", i + 1) + "_" + String.valueOf(j + 1);
                int btnId = getResources().getIdentifier(idStr, "id", getPackageName());

                //ボタンを取得して選択状態を格納
                ToggleButton btn = (ToggleButton) findViewById(btnId);
                childBoolArray.add(Boolean.valueOf(btn.isChecked()));
            }
            //親のArrayにセット
            parentBoolArray.add(childBoolArray);
        }
    }

    //ボタンの音をならす
    private void playSounds(int part) {
        buttonSoundPool.play(part, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //メイン画面に戻る
    public void goBack(View v) {
        /**
         * 選択状態がクリアになるアラートメッセージ表示
         * ボタンの選択状態は勝手にクリアになるけど、対応ArrayはここでAll Falseにする必要あり
         */

        //戻る
        finish();
    }
}
