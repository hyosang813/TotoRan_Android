package raksam.com.totoran;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiResultActivity extends FragmentActivity {

    private static final int BASE_COUNT = 13;

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //判定結果表示ポップアップWindow
    private PopupWindow popHanteiWindow;

    //ランダムロジックをかました文字列二次元Array
    private ArrayList<ArrayList<String>> randomStrArray;

    //判定対象数列
    private ArrayList<ArrayList<String>> targetStrArray;

    //判定文字列削減有無(大文字)
    public ArrayList<ArrayList<Object>> hanteiStrUpperArray;

    //判定文字列削減有無(小文字)
    public ArrayList<ArrayList<Object>> hanteiStrLowerArray;

    //ドロー削減有無
    public ArrayList<ArrayList<Object>> drawGroupArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result);

        //共通クラス取得
        common = (Common) getApplication();

        //前の画面からランダム結果Arrayを受け取る
        Intent intent = getIntent();
        randomStrArray = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("randomStringArrayData");

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_multi);
        tv.setTypeface(Typeface.MONOSPACE);

        //最終表示用変数用意
        String displayText = "";

        //チーム情報とランダム情報をがっちゃんこして表示
        for (int i = 0; i < BASE_COUNT; i++) {
            //「01 ホーム - アウェイ [102]」の形式
            displayText += String.format("%02d", i + 1) +
                    " " +
                    common.teamNameArray.get(i).get(0) +
                    " - " +
                    common.teamNameArray.get(i).get(1) +
                    " [" +
                    randomStrArray.get(i).get(0) +
                    randomStrArray.get(i).get(1) +
                    randomStrArray.get(i).get(2) +
                    "]\n";
        }
        tv.setText(displayText);

        //判定用の数列生成と削減対象有無(大文字、小文字、ドロー数)の生成
        /**
         * 判定の数列はリターンさせるが、削減対象有無は入れ物だけ一緒に投げて参照する
         * 上の画面表示の裏で動かすように別スレッドにすべきかな？？？
         */
        hanteiStrUpperArray = new ArrayList<>();
        hanteiStrLowerArray = new ArrayList<>();
        drawGroupArray = new ArrayList<>();
        targetStrArray = MultiToSingleLogic.multiToSingleDataMake(randomStrArray, hanteiStrUpperArray, hanteiStrLowerArray, drawGroupArray, common.totoRateArray, common.bookRateArray);
    }

    //戻るボタン押下
    public void goBack(View v) {
        //アラート表示
        BackAlertDialogFragment dialog = new BackAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", "前画面に戻ると現在のランダム抽出データが破棄されますが、よろしいですか？");
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    //マルチ条件指定画面に戻る
    public void back(boolean yesNo) {
        //アラートでキャンセル(false)を選択された場合は戻らない
        if (!yesNo) return;

        finish();
    }

    //プチ削減ボタン押下で削減条件指定画面ポップアップ
    public void popSakugen(View v) {

    }

    //コピーボタン押下でコピーダイアログ表示
    public void popCopy(View v) {

        //ランダム抽出データをクリップボードにコピー




        //コピーダイアログ表示
        copyDialogShow();
    }

    //判定ボタン押下で判定結果表示画面ポップアップ
    public void popHantei(View v) {

        //popupwindowを生成
        popHanteiWindow = new PopupWindow(MultiResultActivity.this);

        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.pop_hantei_result, null);

        //コピーボタン押下時の挙動
        popupView.findViewById(R.id.pop_hantei_copy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判定データをクリップボードにコピー




                //コピーダイアログ表示
                copyDialogShow();
            }
        });
        popHanteiWindow.setContentView(popupView);

        // タップ時に他のViewでキャッチされないための設定
        popHanteiWindow.setOutsideTouchable(true);
        popHanteiWindow.setFocusable(true);

        //画面サイズをゲット
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);

        //画面サイズの８割サイズでポップアップ　※たてサイズは６割
        popHanteiWindow.setWidth((int) (p.x * 0.8));
        popHanteiWindow.setHeight((int) (p.y * 0.6));
//        popHanteiWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        popHanteiWindow.showAtLocation(findViewById(R.id.result_text_view_multi), Gravity.CENTER, 0, 0);
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
