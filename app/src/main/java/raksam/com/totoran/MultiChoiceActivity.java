package raksam.com.totoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MultiChoiceActivity extends BaseChoiceActivity {

    //次画面に渡すピッカー用Array
    ArrayList<ArrayList<Integer>> wtCheckArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice);

        //ボタン作成メソッドを呼び出し
        buttonMake("multi");

    }

    //「次へ」ボタン押下時
    public void multiResultTransition(View v) {
        //ボタンのbool状態をcommonに格納する共通メソッドコール
        setBoolArray("multi");

        //w8t5チェック用データ取得
        DetailUseDataMaker dataMaker = new DetailUseDataMaker(common.multiBoolArray);
        ArrayList<ArrayList<Integer>> wtCheckArray = dataMaker.multiPickDataMake();

        //チェックに引っかかった場合はアラート表示して画面遷移中止
        if (ConsistencyCheck.doubleTripleCheck(wtCheckArray)) {
            OkAlertDialogFragment dialog = new OkAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "警告");
            args.putString("message", "以下の組合わせを超える事は出来ません\n※合計486口を超える事はできません\nダブル:1 トリプル:5\nダブル:2 トリプル:4\nダブル:3 トリプル:3\nダブル:4 トリプル:3\nダブル:5 トリプル:2\nダブル:6 トリプル:1\nダブル:7 トリプル:1\nダブル:8 トリプル:0");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        //マルチ条件指定画面に画面遷移
        Intent intent = new Intent(getApplication(), MultiDetailActivity.class);
        intent.putExtra("pickerArrayData", wtCheckArray);
        startActivity(intent);
    }

}