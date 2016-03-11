package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;


public class SingleChoiceActivity extends BaseChoiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);

        //ボタン作成メソッドを呼び出し
        buttonMake("single");

    }

    @Override
    public void buttonClicked(View v) {
        super.buttonClicked(v);

        //ボタン押下時がcheckedの場合のみシングルは枠内で１つしか選択できないので排他制御が必要
        if (!((ToggleButton)v).isChecked()) return;

        //自身のIDを取得
        int vId = v.getId();

        //トグルボタンのIDの文字列を取得(最後の一文字だけ取っ払う)
        String btnIdStr = getApplicationContext().getResources().getResourceEntryName(vId).substring(0, 16);

        //自分以外の他２つを非選択状態にする
        for (int i = 1; i < 4; i++) {
            //id文字列作成
            int btnId = getResources().getIdentifier(btnIdStr + String.valueOf(i), "id", getPackageName());

            //自分はスルー
            if (btnId == vId) continue;

            //チェック状態をクリア
            ToggleButton btn = (ToggleButton) findViewById(btnId);
            btn.setChecked(false);
        }
    }

    //「次へ」ボタン押下時はシングル条件指定画面に画面遷移
    public void singleResultTransition(View v) {
        //ボタン連打制御(１秒)
        if (!Common.isClickEvent()) return;

        //ボタンのbool状態をcommonに格納する共通メソッドコール
        setBoolArray("single");

        //画面遷移
        startActivity(new Intent(SingleChoiceActivity.this, SingleDetailActivity.class));

        //アニメーション設定(進む)
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }
}
