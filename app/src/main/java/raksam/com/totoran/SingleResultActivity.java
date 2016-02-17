package raksam.com.totoran;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class SingleResultActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);
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
