package raksam.com.totoran;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


/**
 * Created by hyosang813 on 16/02/17.
 * ポップアップ(ダイアログ)単純にOkすれば消えるアラート表示クラス
 */
public class OkAlertDialogFragment extends DialogFragment {

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //アラート作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"));
        builder.setMessage(getArguments().getString("message"));
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

