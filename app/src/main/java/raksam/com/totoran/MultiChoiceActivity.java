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

    }

    //「次へ」ボタン押下時はシングル条件指定画面に画面遷移
    public void multiResultTransition(View v) {
        startActivity(new Intent(MultiChoiceActivity.this, MultiDetailActivity.class));
    }
}

