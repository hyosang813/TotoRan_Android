package raksam.com.totoran;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class MultiResultActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result);

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_multi);
        tv.setTypeface(Typeface.MONOSPACE);
    }

    //戻るボタン押下
    public void goBack(View v) {
        //アラート表示
        BackAlertDialogFragment dialog = new BackAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", "前画面に戻ると現在のランダム抽出データが破棄されますが、よろしいですか？");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //マルチ条件指定画面に戻る
    public void back(boolean yesNo) {
        //アラートでキャンセル(false)を選択された場合は戻らない
        if (!yesNo) return;

        finish();
    }
}
