package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

public class SingleDetailActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //ピッカー用のデータ取得
    ArrayList<ArrayList<Integer>> pickerArray;

    //対象TextView全部
    int[] targatTextViews = {R.id.home_unit, R.id.draw_unit, R.id.away_unit, R.id.unit_unit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_detail);

        //共通クラス取得
        common = (Common) getApplication();

        //ピッカーで使用するデータロジッククラス
        DetailUseDataMaker dataMaker = new DetailUseDataMaker(common.singleBoolArray);

        //ピッカー用のデータ取得
        pickerArray = dataMaker.singlePickDataMake();

        //ホーム、ドロー、アウェイの数の初期値をTextViewにセット
        for (int i = 0; i < targatTextViews.length - 1; i++) {
            setValue(targatTextViews[i], pickerArray.get(i).get(2));
        }

        //合計数が「13」だったら「以上」を非表示
        ijouCheck();
    }

    //TextViewの値設定
    private void setValue(int partId, int initValue) {
        TextView tv = (TextView)findViewById(partId);
        tv.setText(String.valueOf(initValue));
    }

    //「次へ」ボタン押下時はシングル結果画面に画面遷移
    public void singleResultTransition(View v) {
        //ボタン連打制御(１秒)
        if (!Common.isClickEvent()) return;

        //整合性チェック
        int checkResult = ConsistencyCheck.singleConsistencyCheck(pickerArray);

        if (checkResult == 0) {
            //シングルランダムロジックをかます
            ArrayList<ArrayList<String>> singleRandomResultArray = RandomLogic.singleRandomDataMake(common.singleBoolArray, pickerArray);

            //判定ロジックをかます
            ArrayList<ArrayList<String>> singleHanteiResultArray = HanteiLogic.hanteiDataMake(singleRandomResultArray, common.totoRateArray, common.bookRateArray);

            //シングル結果表示画面に画面遷移
            Intent intent = new Intent(getApplication(), SingleResultActivity.class);
            intent.putExtra("hanteiStringArrayData", singleHanteiResultArray); //判定ロジックをかましたString二次元Array
            startActivity(intent);

            //アニメーション設定(進む)
            overridePendingTransition(R.anim.in_right, R.anim.out_left);

        } else {
            String warnMessage = "";
            switch (checkResult) {
                case 1:
                    warnMessage = "選択された合計数が「１３試合」を超えてます";
                    break;
                case 2:
                    warnMessage = "選択された合計数が「１３試合」の場合の口数は１口までです";
                    break;
                case 3:
                    warnMessage = "選択された合計数が「１２試合」の場合の口数は３口までです";
                    break;
                case 4:
                    warnMessage = "選択された合計数が「１１試合」の場合の口数は９口までです";
                    break;
            }
            //警告メッセージ表示
            OkAlertDialogFragment dialog = new OkAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "警告");
            args.putString("message", warnMessage);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    //シングル選択画面に戻る
    public void goBack(View v) {
        finish();

        //アニメーション設定(戻る)
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    //各TextViewがtapされたらここが呼ばれてpickerDialogを表示
    public void showPicker(View v) {
        int targetId = 3;
        switch (v.getId()) {
            case R.id.home_unit:
                targetId = 0;
                break;
            case R.id.draw_unit:
                targetId = 1;
                break;
            case R.id.away_unit:
                targetId = 2;
            default:break;
        }

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

        //合計数が「13」だったら「以上」を非表示
        ijouCheck();
    }

    //「以上」ラベル非表示判断
    private void ijouCheck() {
        int ijouBoolVal = ((pickerArray.get(0).get(2) + pickerArray.get(1).get(2) + pickerArray.get(2).get(2)) >= 13) ? View.INVISIBLE : View.VISIBLE;
        int[] ijouViews = {R.id.home_unit_label, R.id.draw_unit_label, R.id.away_unit_label};
        for (int ijouLabel : ijouViews) {
            findViewById(ijouLabel).setVisibility(ijouBoolVal);
        }
    }

}
