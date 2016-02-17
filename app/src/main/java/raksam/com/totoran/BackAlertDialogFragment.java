package raksam.com.totoran;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


/**
 * Created by hyosang813 on 16/02/17.
 * ポップアップ(ダイアログ)アラート表示クラス
 */
public class BackAlertDialogFragment extends DialogFragment {

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //渡されるメッセージデータ
        final String message = getArguments().getString("message");

        //アラート作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("注意");
        builder.setMessage(message);
        //OKボタン押下時の挙動
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPearentActivityMethod(true);
                    }
                });
        //キャンセルボタン押下時の挙動
        builder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPearentActivityMethod(false);
                    }
                });
        return builder.create();
    }

    //OKとキャンセルで同じメソッドを呼んで引数をBOOL判断できるかな？
    private void callPearentActivityMethod(boolean yesNo) {

        //呼びもとActivityをゲット
        Activity targetActivity = getActivity();

        //instanceofでどのActivityなのか判断
        if (targetActivity instanceof SingleChoiceActivity) {
            ((SingleChoiceActivity)targetActivity).back(yesNo);
        } else if (targetActivity instanceof MultiChoiceActivity) {
            ((MultiChoiceActivity)targetActivity).back(yesNo);
        }

    }
}

