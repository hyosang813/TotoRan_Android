package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SingleChoiceActivity extends BaseChoiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);

        //ボタン作成メソッドを呼び出し
        buttonMake("single");

    }

    //「次へ」ボタン押下時はシングル条件指定画面に画面遷移
    public void singleResultTransition(View v) {
        startActivity(new Intent(SingleChoiceActivity.this, SingleDetailActivity.class));
    }
}
