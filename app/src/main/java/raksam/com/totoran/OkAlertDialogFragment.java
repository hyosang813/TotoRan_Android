package raksam.com.totoran;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;


/**
 * Created by hyosang813 on 16/02/17.
 * ポップアップ(ダイアログ)単純にOkすれば消えるアラート表示クラス
 */
public class OkAlertDialogFragment extends DialogFragment {

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //メッセージ中央揃えのためにわざわざ専用TextViewを用意してcenter指定
        TextView tV = new TextView(getActivity());
        tV.setGravity(Gravity.CENTER);
        tV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        tV.setText(getArguments().getString("message"));

        //アラート作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"));
        builder.setView(tV);

        //OKボタン押下時の挙動
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //アラートを閉じる
                        dismiss();
                    }
                });
        return builder.create();
    }
}

