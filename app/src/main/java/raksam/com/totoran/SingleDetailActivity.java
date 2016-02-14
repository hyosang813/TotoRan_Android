package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SingleDetailActivity extends Activity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_detail);

        //共通クラス取得
        common = (Common) getApplication();

    }

    //「次へ」ボタン押下時はシングル結果画面に画面遷移
    public void singleResultTransition(View v) {
        startActivity(new Intent(SingleDetailActivity.this, SingleResultActivity.class));
    }

    //シングル選択画面に戻る
    public void goBack(View v) {
        finish();
    }
}
