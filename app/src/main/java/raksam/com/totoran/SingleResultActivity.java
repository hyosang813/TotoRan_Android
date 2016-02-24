package raksam.com.totoran;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleResultActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);

        //共通クラス取得
        common = (Common) getApplication();

        //前の画面から判定結果Arrayを受け取る
        Intent intent = getIntent();
        ArrayList<ArrayList<String>> hanteiStrArray = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("hanteiStringArrayData");

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_single);
        tv.setTypeface(Typeface.MONOSPACE);

        //整形データをTextViewにセット
        tv.setText(HanteiStrMake.resultHanteiStr(hanteiStrArray, common.jitenStr));
    }

    //戻るボタン押下
    public void goBack(View v) {
        //アラート表示
        BackAlertDialogFragment dialog = new BackAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message","前画面に戻ると現在のランダム抽出データが破棄されますが、よろしいですか？");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //シングル条件指定画面に戻る
    public void back(boolean yesNo) {
        //アラートでキャンセル(false)を選択された場合は戻らない
        if (!yesNo) return;

        finish();
    }

}
