package raksam.com.totoran;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.nend.android.NendAdView;

import java.util.ArrayList;

public class SingleResultActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //判定結果
    String singleHanteiStr = "";

    //NEND
    private NendAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);

        //共通クラス取得
        common = (Common) getApplication();

        //前の画面から判定結果Arrayを受け取る
        Intent intent = getIntent();
        ArrayList<ArrayList<String>> hanteiStrArray = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("hanteiStringArrayData");

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_single);
        tv.setTypeface(Typeface.MONOSPACE);

        //整形データをTextViewにセット
        singleHanteiStr = HanteiStrMake.resultHanteiStr(hanteiStrArray, common.dataGetTime);
        tv.setText(singleHanteiStr);

        //NEND広告の表示
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.single_result);

        // 1 NendAdView をインスタンス化
        adView = new NendAdView(this, 3174, "c5cb8bc474345961c6e7a9778c947957ed8e1e4f"); //テスト
//        adView = new NendAdView(this, 555646, "6d5a08cb10ed4fb4359de92bb644a7dec23a20a1"); //本番

        // 2 NendAdView をレイアウトに追加
        rootLayout.addView(adView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // 3 広告の取得を開始
        adView.loadAd();
    }

    //コピーボタン押下
    public void singleCopy(View v) {
        //ランダム抽出データをクリップボードにコピー
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // コピーするデータをエディットテキストから取得する
        ClipData clip = ClipData.newPlainText("copied_text", singleHanteiStr + "\n#トトラン！");

        // クリップボードに内容をコピーする
        clipBoard.setPrimaryClip(clip);

        //コピーダイアログ表示
        copyDialogShow();
    }

    //戻るボタン押下
    public void goBack(View v) {
        //アラート表示
        BackAlertDialogFragment dialog = new BackAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message","前画面に戻ると現在のランダム抽出データが破棄されますが、よろしいですか？");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //シングル条件指定画面に戻る
    public void back(boolean yesNo) {
        //アラートでキャンセル(false)を選択された場合は戻らない
        if (!yesNo) return;

        //NENDの終了
        adView = null;

        finish();

        //アニメーション設定(戻る)
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    //コピーボタン押下時の挙動
    private void copyDialogShow() {
        OkAlertDialogFragment dialog = new OkAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "データ保存");
        args.putString("message", "クリップボードに保存しました\n" +
                "以下のような場所にご使用ください\n" +
                "・twitter\n" +
                "・FaceBook\n" +
                "・某巨大掲示板");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

}
