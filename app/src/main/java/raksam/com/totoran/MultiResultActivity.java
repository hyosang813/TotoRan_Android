package raksam.com.totoran;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiResultActivity extends FragmentActivity {

    private static final int BASE_COUNT = 13;

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

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
         * そこまでコストのかかる処理じゃないかなー
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

        //プチ削減表示ポップアップWindow
        PopupWindow popReduceWindow = new PopupWindow(MultiResultActivity.this);

        // レイアウト設定
        final View popupView = getLayoutInflater().inflate(R.layout.pop_reduce_result, null);

        //ドローの選択チェックボックス
        int arrayCounter = 1;
        for (int i = Integer.valueOf(drawGroupArray.get(0).get(0).toString()); i <= Integer.valueOf(drawGroupArray.get(drawGroupArray.size() - 1).get(0).toString()); i++) {
            /**
             * 応急処置的にドロー10以上は対象外にする
             * 恒久処置になるかも？？？
             */
            if (i == 10) break;
            
            //対象のチェックボックスを取得
            String idStr = "draw_checkbox_" + String.valueOf(i);

            int cbId = getResources().getIdentifier(idStr, "id", getPackageName());
            CheckBox cb = (CheckBox)popupView.findViewById(cbId);
            cb.setVisibility(View.VISIBLE);

            //最初の数値はチェックボックスTrueでグレーアウト
            if (i == Integer.valueOf(drawGroupArray.get(0).get(0).toString())) {
                cb.setChecked(true);
                cb.setEnabled(false);
                continue;
            }

            //ドロー数の最低数の次の要素からはArrayの状況を設定
            cb.setChecked(Boolean.valueOf(drawGroupArray.get(arrayCounter).get(1).toString()));

            //ドロー数チェックボックスのクリックリスナー
            checkArraySynchronism(cb, drawGroupArray);

            arrayCounter++;
        }

        //異動
        secondForFirstMove(popupView, R.id.draw_checkboxes_five_for_nine, R.id.draw_checkboxes_zero_for_four);

        //大文字、小文字判定チェックボックス
        ArrayList<String> bigOrSmall = new ArrayList<>();
        bigOrSmall.add("upper");
        bigOrSmall.add("lower");

        for (String targetStr : bigOrSmall) {
            //判定結果次第で表示するチェックボックスは異なる
            ArrayList<ArrayList<Object>> targetHanteiStrArray = targetStr.equals("upper") ? hanteiStrUpperArray : hanteiStrLowerArray;
            //とりあえず対象のチェックボックスを表示させてチェック状態も反映
            for (ArrayList<Object> hanteiStrAndBool : targetHanteiStrArray) {
                String idStr = targetStr + "_checkbox_" + String.valueOf(hanteiStrAndBool.get(0)).toLowerCase();
                int cbId = getResources().getIdentifier(idStr, "id", getPackageName());
                CheckBox cb = (CheckBox)popupView.findViewById(cbId);
                cb.setVisibility(View.VISIBLE);
                cb.setChecked(Boolean.valueOf(String.valueOf(hanteiStrAndBool.get(1))));

                //チェックボックスのクリックリスナー
                checkArraySynchronism(cb, targetHanteiStrArray);
            }

            //異動先と異動元のidを取得
            int idouMotoId = getResources().getIdentifier(targetStr + "_checkboxes_e_for_f", "id", getPackageName());
            int idouSakiId = getResources().getIdentifier(targetStr + "_checkboxes_s_for_d", "id", getPackageName());

            //異動
            secondForFirstMove(popupView, idouMotoId, idouSakiId);
        }

        //全選択ボタン押下時の挙動
        popupView.findViewById(R.id.pop_reduce_alltap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //表示されているチェックボックスの全て選択仕直し
                int sectionIdArray[] = {R.id.draw_checkboxes_zero_for_four,
                                        R.id.draw_checkboxes_five_for_nine,
                                        R.id.upper_checkboxes_s_for_d,
                                        R.id.upper_checkboxes_e_for_f,
                                        R.id.lower_checkboxes_s_for_d,
                                        R.id.lower_checkboxes_e_for_f};

                for (int sectionId : sectionIdArray) {
                    LinearLayout sectionLinearLayout = (LinearLayout)popupView.findViewById(sectionId);
                    for (int i = 0; i < sectionLinearLayout.getChildCount(); i++) {
                        if (sectionLinearLayout.getChildAt(i).getVisibility() == View.VISIBLE) ((CheckBox)sectionLinearLayout.getChildAt(i)).setChecked(true);
                    }
                }

                //同時に対応BoolArrayの値もちゃんとね！！！！！！！！！
                ArrayList<ArrayList<ArrayList<Object>>> threeArrays = new ArrayList<>();
                threeArrays.add(hanteiStrLowerArray);
                threeArrays.add(hanteiStrUpperArray);
                threeArrays.add(drawGroupArray);
                for (ArrayList<ArrayList<Object>> grandArray : threeArrays) {
                    for (ArrayList<Object> parentArray : grandArray) {
                        parentArray.set(1, true);
                    }
                }
            }
        });

        popReduceWindow.setContentView(popupView);

        // タップ時に他のViewでキャッチされないための設定
        popReduceWindow.setOutsideTouchable(true);
        popReduceWindow.setFocusable(true);

        //画面サイズをゲット
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);

        //画面サイズの８割サイズでポップアップ　※たてサイズは６割
        popReduceWindow.setWidth((int) (p.x * 0.8));
//        popReduceWindow.setHeight((int) (p.y * 0.6));
        popReduceWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        popReduceWindow.showAtLocation(findViewById(R.id.result_text_view_multi), Gravity.CENTER, 0, 0);

    }

    //チェックボックスの前(上)詰めメソッド
    private void secondForFirstMove(View popupView, int idouMoto, int idouSaki) {

        //異動先と異動元を準備
        LinearLayout idouSakiLinearLayout = (LinearLayout)popupView.findViewById(idouSaki);

        //１段目でGONEのチェックボックスの数を数える
        int firstGoneCount = 0;
        for (int i = 0; i < idouSakiLinearLayout.getChildCount(); i++) {
            if (idouSakiLinearLayout.getChildAt(i).getVisibility() == View.GONE) firstGoneCount++;
        }

        //１段目の非表示数分２段目の前から順に異動
        for (int i = 0; i < firstGoneCount; i++) {
            //異動もとの親
            LinearLayout idouMotoLinearLayout = (LinearLayout)popupView.findViewById(idouMoto);

            //２段目の一番最初のViewをゲット
            CheckBox cb = (CheckBox)idouMotoLinearLayout.getChildAt(0);

            //もう２段目にViewが残ってなかったら終了
            if (cb == null) break;
            ViewGroup cbParent = (ViewGroup)cb.getParent();

            //異動元から削除して異動先に追加
            cbParent.removeView(cb);
            idouSakiLinearLayout.addView(cb);
        }
    }

    //チェックボックスのチェック状況とBoolArrayの同期
    private void checkArraySynchronism(CheckBox cb, final ArrayList<ArrayList<Object>> groupArray) {
        //チェックボックスのクリックリスナー
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox innerCb = (CheckBox)v;
                String CbNum = innerCb.getText().toString();
                for (ArrayList<Object> drawNumBoolArray : groupArray) {
                    if (drawNumBoolArray.get(0).equals(CbNum)) {
                        drawNumBoolArray.set(1, innerCb.isChecked()); //対応するBool値をチェックボックスのチェック状況と同期
                    }
                }
            }
        });
    }

    //判定ボタン押下で判定結果表示画面ポップアップ
    public void popHantei(View v) {

        //判定結果表示ポップアップWindow
        PopupWindow popHanteiWindow = new PopupWindow(MultiResultActivity.this);

        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.pop_hantei_result, null);

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)popupView.findViewById(R.id.pop_hantei_result_view);
        tv.setTypeface(Typeface.MONOSPACE);

        //プチ削減
        ArrayList<ArrayList<String>> reduceTargetStrArray = new ArrayList<>();
        for (ArrayList<String> targetStr : targetStrArray) {
            //ドロー数が対象かどうかチェック
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(targetStr.size() - 1)), drawGroupArray)) continue;
            //大文字判定対象かどうかチェック
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(13)), hanteiStrUpperArray)) continue;
            //小さい文字判定対象かどうかチェック
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(15)), hanteiStrLowerArray)) continue;
            //対象だったら追加
            reduceTargetStrArray.add(targetStr);
        }

        //削減の結果で処理変更
        String reduceText = reduceTargetStrArray.size() == 0 ? "結果なし" : HanteiStrMake.resultHanteiStr(reduceTargetStrArray, common.jitenStr);

        //整形データをTextViewにセット
        tv.setText(reduceText);

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
//        popHanteiWindow.setHeight((int) (p.y * 0.6));
        popHanteiWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        popHanteiWindow.showAtLocation(findViewById(R.id.result_text_view_multi), Gravity.CENTER, 0, 0);
    }

    //削減対象可否判定メソッド
    private boolean reduceTargetYesOrNo(String targetStr, ArrayList<ArrayList<Object>> targetArray) {
        for (ArrayList<Object> numBoolArray : targetArray) {
            if (numBoolArray.get(0).equals(targetStr)) {
                return Boolean.valueOf(numBoolArray.get(1).toString());
            }
        }
        //無いってことは設計上ありえないけど一応falseを返す
        return false;
    }

    //コピーボタン押下でコピーダイアログ表示
    public void popCopy(View v) {

        //ランダム抽出データをクリップボードにコピー




        //コピーダイアログ表示
        copyDialogShow();
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
