package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //commonインスタンスがなければ作る(シングルトン)
        if (common == null) {
            //共通クラス取得して初期化
            common = (Common) getApplication();
            common.init();
        }
        
    }

    //「シングル」ボタン押下時はシングル選択画面に画面遷移
    public void singleChoiceTransition(View v) {
        startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
    }

    //「マルチ」ボタン押下時はマルチ選択画面に画面遷移
    public void multiChoiceTransition(View v) {
        startActivity(new Intent(MainActivity.this, MultiChoiceActivity.class));
    }




}
