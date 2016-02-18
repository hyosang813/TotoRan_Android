package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
    @SuppressWarnings("unchecked") //intent.getSerializableExtraでwarningが出ちゃうの消すアノテーション。。。よくはないよね。。。
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_detail);

        //前の画面からピッカー用Arrayを受け取る
        Intent intent = getIntent();
        pickerArray = (ArrayList<ArrayList<Integer>>)intent.getSerializableExtra("pickerArrayData");

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
        //チェックに引っかかった場合はアラート表示して画面遷移中止
        if (ConsistencyCheck.doubleTripleCheck(pickerArray)) {
            OkAlertDialogFragment dialog = new OkAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "警告");
            args.putString("message", "以下の組合わせを超える事は出来ません\n※合計486口を超える事はできません\nダブル:1 トリプル:5\nダブル:2 トリプル:4\nダブル:3 トリプル:3\nダブル:4 トリプル:3\nダブル:5 トリプル:2\nダブル:6 トリプル:1\nダブル:7 トリプル:1\nダブル:8 トリプル:0");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        //マルチ条件指定画面に画面遷移
        Intent intent = new Intent(getApplication(), MultiResultActivity.class);
        intent.putExtra("pickerArrayData", pickerArray);
        startActivity(intent);
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
