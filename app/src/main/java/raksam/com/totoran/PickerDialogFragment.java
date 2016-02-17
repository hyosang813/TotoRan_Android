package raksam.com.totoran;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/16.
 * ポップアップ(ダイアログ)表示クラス
 */
public class PickerDialogFragment extends DialogFragment {

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //渡される最小値、最大値、現在値データの取得
        final ArrayList<Integer> minMaxCurValArray = getArguments().getIntegerArrayList("minMaxCurValArray");

        //ナンバーピッカーの設置
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View pickerLayout = inflater.inflate(R.layout.numberpicker_layout, null, false);
        final NumberPicker np = (NumberPicker) pickerLayout.findViewById(R.id.numberpicker);
        if (minMaxCurValArray != null) {
            np.setWrapSelectorWheel(false);
            np.setMinValue(minMaxCurValArray.get(0));
            np.setMaxValue(minMaxCurValArray.get(1));
            np.setValue(minMaxCurValArray.get(2));
        }
        //アラート作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("数字を選択してください");

        //OKボタン押下時の挙動
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //設定値をArrayに反映してダイアログを閉じてTextViewの更新
                        if (minMaxCurValArray != null) {
                            minMaxCurValArray.set(2, np.getValue());
                            Activity targetActivity = getActivity();
                            if (targetActivity instanceof SingleDetailActivity) {
                                ((SingleDetailActivity)targetActivity).okClick();
                            } else {
                                ((MultiDetailActivity)targetActivity).okClick();
                            }
                        }
                    }
                });
        builder.setView(pickerLayout);
        return builder.create();
    }
}

