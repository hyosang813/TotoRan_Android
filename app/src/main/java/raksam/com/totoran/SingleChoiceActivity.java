package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SingleChoiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);
    }

    //「次へ」ボタン押下時はシングル条件指定画面に画面遷移
    public void singleResultTransition(View v) {
        startActivity(new Intent(SingleChoiceActivity.this, SingleDetailActivity.class));
    }

    //メイン画面に戻る
    public void goBack(View v) {
        finish();
    }
}
