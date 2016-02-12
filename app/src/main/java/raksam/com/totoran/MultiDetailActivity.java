package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MultiDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_detail);
    }

    //「次へ」ボタン押下時はマルチ結果画面に画面遷移
    public void multiResultTransition(View v) {
        startActivity(new Intent(MultiDetailActivity.this, MultiResultActivity.class));
    }

    //マルチ選択画面に戻る
    public void goBack(View v) {
        finish();
    }
}
