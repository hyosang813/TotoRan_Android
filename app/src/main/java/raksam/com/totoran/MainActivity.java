package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //起動後は共通クラスを初期化
        common = (Common) getApplication();
        common.init();

        //データをDBから取得
        getDatabaseData();

    }

    //「シングル」ボタン押下時はシングル選択画面に画面遷移
    public void singleChoiceTransition(View v) {
        startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
    }

    //「マルチ」ボタン押下時はマルチ選択画面に画面遷移
    public void multiChoiceTransition(View v) {
        startActivity(new Intent(MainActivity.this, MultiChoiceActivity.class));
    }

    //DBから必要情報をゲットしてcommonに格納する
    private void getDatabaseData() {
        //DB操作インスタンスの取得
        GetDatabaseData dbData = new GetDatabaseData(this);

        //現在開催中の回数を取得
        common.numberOfTimes = dbData.getOpenNumberAndEndDate().get(0);

        //現在開催中の締切日を取得
        common.deadLineTime = dbData.getOpenNumberAndEndDate().get(1);

        //データの取得年月日を取得
        common.dataGetTime = dbData.getDataGetTime(common.numberOfTimes);

        //チーム名情報を取得
        common.teamNameArray = dbData.getTeamNameArray(common.numberOfTimes);

        //toto支持率データの取得
        common.totoRateArray = dbData.getRateArray(common.numberOfTimes).get(0);

        //book支持率データの取得
        common.bookRateArray = dbData.getRateArray(common.numberOfTimes).get(1);

        //DB操作インスタンスの破棄
        dbData.dbClose();
    }


}
