package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultiChoiceActivity extends BaseChoiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice);

        //ボタン作成メソッドを呼び出し
        buttonMake("multi");

    }

    //「次へ」ボタン押下時はシングル条件指定画面に画面遷移
    public void multiResultTransition(View v) {
        //ボタンのbool状態をcommonに格納する共通メソッドコール
        setBoolArray("multi");

        //画面遷移
        startActivity(new Intent(MultiChoiceActivity.this, MultiDetailActivity.class));
    }
}

