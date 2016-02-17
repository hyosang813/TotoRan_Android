package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiDetailActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //ピッカー用のデータ取得
    ArrayList<ArrayList<Integer>> pickerArray;

    //対象TextView全部
    int[] targatTextViews = {R.id.double_unit, R.id.triple_unit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_detail);

        //共通クラス取得
        common = (Common) getApplication();

        //ピッカーで使用するデータロジッククラス
        DetailUseDataMaker dataMaker = new DetailUseDataMaker(common.multiBoolArray);

        //ピッカー用のデータ取得
        pickerArray = dataMaker.multiPickDataMake();

        //ホーム、ドロー、アウェイの数の初期値をTextViewにセット
        for (int i = 0; i < targatTextViews.length; i++) {
            setValue(targatTextViews[i], pickerArray.get(i).get(2));
        }

    }

    //TextViewの値設定
    private void setValue(int partId, int initValue) {
        TextView tv = (TextView)findViewById(partId);
        tv.setText(String.valueOf(initValue));
    }

    //「次へ」ボタン押下時はマルチ結果画面に画面遷移
    public void multiResultTransition(View v) {
        startActivity(new Intent(MultiDetailActivity.this, MultiResultActivity.class));
    }

    //マルチ選択画面に戻る
    public void goBack(View v) {
        finish();
    }

    //各TextViewがtapされたらここが呼ばれてpickerDialogを表示
    public void showPicker(View v) {
        int targetId = v.getId() == R.id.double_unit ? 0 : 1;
        PickerDialogFragment dialog = new PickerDialogFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList("minMaxCurValArray", pickerArray.get(targetId));
        args.putString("Single_or_Multi","Multi");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //pickerDialogで「OK」ボタンを押されたら選択された値を受け取って該当するTextViewに表示
    public void okClick() {
        for (int i = 0; i < targatTextViews.length; i++) {
            setValue(targatTextViews[i], pickerArray.get(i).get(2));
        }
    }
}
