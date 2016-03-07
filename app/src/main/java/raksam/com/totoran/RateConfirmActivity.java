package raksam.com.totoran;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RateConfirmActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_confirm);

        //共通クラス取得
        common = (Common) getApplication();

        //取得年月日を表示
        ((TextView)findViewById(R.id.rate_confirm_get_time)).setText(common.dataGetTime);

        //チーム名はどっちにしろ固定だからTextViewに設定
        for (int i = 0; i < common.teamNameArray.size(); i++) {

            //ホームとアウェイのid文字列それぞれ作成
            String idStrHome = "home_team_name_" + String.format("%02d", i + 1);
            String idStrAway = "away_team_name_" + String.format("%02d", i + 1);
            int homeId = getResources().getIdentifier(idStrHome, "id", getPackageName());
            int awayId = getResources().getIdentifier(idStrAway, "id", getPackageName());

            //チーム名をセット
            ((TextView)findViewById(homeId)).setText(common.teamNameArray.get(i).get(0));
            ((TextView)findViewById(awayId)).setText(common.teamNameArray.get(i).get(1));
        }


        //セグメントコントロール
        final RadioGroup segGroup = (RadioGroup)findViewById(R.id.rate_seg);

        //最初はtotoに固定
        segGroup.check(R.id.rate_seg_toto);
        rateGraphDraw(common.totoRateArray);

        //リスナー登録
        segGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //チェックリスナー
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rate_seg_toto:
                        rateGraphDraw(common.totoRateArray);
                        break;
                    case R.id.rate_seg_book:
                        if (common.bookRateArray.get(0).get(0) != null) {
                            rateGraphDraw(common.bookRateArray);
                        } else {
                            //アラート表示してtoto表示に戻す
                            segGroup.check(R.id.rate_seg_toto);
                            OkAlertDialogFragment dialog = new OkAlertDialogFragment();
                            Bundle args = new Bundle();
                            args.putString("title", "注意");
                            args.putString("message", "現在bODDSはまだありません。");
                            dialog.setArguments(args);
                            dialog.show(getSupportFragmentManager(), "dialog");
                        }
                        break;
                }
            }
        });

    }

    //支持率グラフ再描画
    private void rateGraphDraw(ArrayList<ArrayList<String>> rateArray) {

        for (int i = 0; i < rateArray.size(); i++) {
            //フォーマット指定
            DecimalFormat df = new DecimalFormat("00.00");

            //表示整形するために一回Floatにしてそれを再度Stringに戻す
            String rateStrHome = df.format(Float.valueOf(rateArray.get(i).get(1)));
            String rateStrDraw = df.format(Float.valueOf(rateArray.get(i).get(0)));
            String rateStrAway = df.format(Float.valueOf(rateArray.get(i).get(2)));

            //まずは支持率%のsetText
            String rateStr = rateStrHome + "%          " + rateStrDraw + "%          " + rateStrAway + "%";
            String rateTextView = "rate_text_" + String.format("%02d", i + 1);
            int rateTextId = getResources().getIdentifier(rateTextView, "id", getPackageName());
            ((TextView)findViewById(rateTextId)).setText(rateStr);

            //次はグラフのウエイト設定
            String homeGraph = "home_rate_graph_" + String.format("%02d", i + 1);
            String drawGraph = "draw_rate_graph_" + String.format("%02d", i + 1);
            String awayGraph = "away_rate_graph_" + String.format("%02d", i + 1);
            int homeGraphId = getResources().getIdentifier(homeGraph, "id", getPackageName());
            int drawGraphId = getResources().getIdentifier(drawGraph, "id", getPackageName());
            int awayGraphId = getResources().getIdentifier(awayGraph, "id", getPackageName());

            final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
            findViewById(homeGraphId).setLayoutParams(new LinearLayout.LayoutParams(WC, WC, Float.valueOf(rateStrHome) / 100));
            findViewById(drawGraphId).setLayoutParams(new LinearLayout.LayoutParams(WC, WC, Float.valueOf(rateStrDraw) / 100));
            findViewById(awayGraphId).setLayoutParams(new LinearLayout.LayoutParams(WC, WC, Float.valueOf(rateStrAway) / 100));
        }
    }

    //前画面に戻る
    public void goBack(View v) {
        finish();
    }
}
