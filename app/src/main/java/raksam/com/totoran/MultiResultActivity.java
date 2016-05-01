package raksam.com.totoran;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.nend.android.NendAdView;

import java.util.ArrayList;

public class MultiResultActivity extends FragmentActivity {

    private static final int BASE_COUNT = 13;

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //判定対象数列
    private ArrayList<ArrayList<String>> targetStrArray;

    //判定文字列削減有無(大文字)
    public ArrayList<ArrayList<Object>> hanteiStrUpperArray;

    //判定文字列削減有無(小文字)
    public ArrayList<ArrayList<Object>> hanteiStrLowerArray;

    //ホーム削減有無
    public ArrayList<ArrayList<Object>> homeGroupArray; // 20160501追加

    //ドロー削減有無
    public ArrayList<ArrayList<Object>> drawGroupArray;

    //アウェイ削減有無
    public ArrayList<ArrayList<Object>> awayGroupArray; // 20160501追加

    //最終表示用変数用意
    private String displayText = "";

    //NEND
    private NendAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result);

        //共通クラス取得
        common = (Common) getApplication();

        //前の画面からランダム結果Arrayを受け取る
        Intent intent = getIntent();
        ArrayList<ArrayList<String>> randomStrArray = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("randomStringArrayData");

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_multi);
        tv.setTypeface(Typeface.MONOSPACE);

        //チーム情報とランダム情報をがっちゃんこして表示
        for (int i = 0; i < BASE_COUNT; i++) {
            //「01 ホーム - アウェイ [102]」の形式
            displayText += String.format("%02d", i + 1) +
                    " " +
                    common.teamNameArray.get(i).get(0) +
                    " ー " +
                    common.teamNameArray.get(i).get(1) +
                    " [" +
                    randomStrArray.get(i).get(0) +
                    randomStrArray.get(i).get(1) +
                    randomStrArray.get(i).get(2) +
                    "]\n";
        }
        tv.setText(displayText);

        //NEND広告の表示
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.multi_result);

        // 1 NendAdView をインスタンス化
//        adView = new NendAdView(this, 3174, "c5cb8bc474345961c6e7a9778c947957ed8e1e4f"); //テスト
        adView = new NendAdView(this, 555646, "6d5a08cb10ed4fb4359de92bb644a7dec23a20a1"); //本番

        // 2 NendAdView をレイアウトに追加
        rootLayout.addView(adView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // 3 広告の取得を開始
        adView.loadAd();

        //判定用の数列生成と削減対象有無(大文字、小文字、ドロー数)の生成
        /**
         * 判定の数列はリターンさせるが、削減対象有無は入れ物だけ一緒に投げて参照する
         * 上の画面表示の裏で動かすように別スレッドにすべきかな？？？
         * そこまでコストのかかる処理じゃないかなー
         */
        hanteiStrUpperArray = new ArrayList<>();
        hanteiStrLowerArray = new ArrayList<>();
        homeGroupArray = new ArrayList<>();
        drawGroupArray = new ArrayList<>();
        awayGroupArray = new ArrayList<>();
        targetStrArray = MultiToSingleLogic.multiToSingleDataMake(randomStrArray, hanteiStrUpperArray, hanteiStrLowerArray, homeGroupArray, drawGroupArray, awayGroupArray, common.totoRateArray, common.bookRateArray);
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

        //NENDの終了
        adView = null;

        finish();

        //アニメーション設定(戻る)
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    //プチ削減のホーム、ドロー、アウェイのチェックボックス生成
    void checkBoxMake(ArrayList<ArrayList<Object>> groupArray, View popupView, String partStr) {

        int arrayCounter = 1;
        for (int i = Integer.valueOf(groupArray.get(0).get(0).toString()); i <= Integer.valueOf(groupArray.get(groupArray.size() - 1).get(0).toString()); i++) {
            /**
             * 応急処置的にドロー10以上は対象外にする
             * 恒久処置になるかも？？？
             */
            if (i >= 10) break;

            //対象のチェックボックスを取得
            String idStr = partStr + "_checkbox_" + String.valueOf(i);

            int cbId = getResources().getIdentifier(idStr, "id", getPackageName());
            CheckBox cb = (CheckBox)popupView.findViewById(cbId);
            cb.setVisibility(View.VISIBLE);
            cb.setTypeface(Typeface.MONOSPACE);

            //最初の数値はチェックボックスTrueでグレーアウト
            if (i == Integer.valueOf(groupArray.get(0).get(0).toString())) {
                cb.setChecked(true);
                cb.setEnabled(false);
                continue;
            }

            //ホーム、ドロー、アウェイ数の最低数の次の要素からはArrayの状況を設定
            cb.setChecked(Boolean.valueOf(groupArray.get(arrayCounter).get(1).toString()));

            //ホーム、ドロー、アウェイ数チェックボックスのクリックリスナー
            checkArraySynchronism(cb, groupArray);

            arrayCounter++;
        }

        //異動
        String idStrFN = partStr + "_checkboxes_five_for_nine";
        String idStrZF = partStr + "_checkboxes_zero_for_four";
        secondForFirstMove(popupView, getResources().getIdentifier(idStrFN, "id", getPackageName()), getResources().getIdentifier(idStrZF, "id", getPackageName()));
    }

    //プチ削減ボタン押下で削減条件指定画面ポップアップ
    public void popSakugen(View v) {
        //ボタン連打制御(１秒)
        if (!Common.isClickEvent()) return;

        //プチ削減表示ポップアップWindow
        PopupWindow popReduceWindow = new PopupWindow(MultiResultActivity.this);

        // レイアウト設定
        final View popupView = getLayoutInflater().inflate(R.layout.pop_reduce_result, (ViewGroup)findViewById(R.id.pop_reduce_root_layout), false);

        //ホーム、ドロー、アウェイの選択チェックボックス
        checkBoxMake(homeGroupArray, popupView, "home");
        checkBoxMake(drawGroupArray, popupView, "draw");
        checkBoxMake(awayGroupArray, popupView, "away");

        //大文字、小文字判定チェックボックス
        ArrayList<String> bigOrSmall = new ArrayList<>();
        bigOrSmall.add("upper");
        if (hanteiStrLowerArray.size() > 0)  {
            //ODDSはない可能性あり
            bigOrSmall.add("lower");
            popupView.findViewById(R.id.lower_checkbox_title).setVisibility(View.VISIBLE);
        }

        for (String targetStr : bigOrSmall) {
            //判定結果次第で表示するチェックボックスは異なる
            ArrayList<ArrayList<Object>> targetHanteiStrArray = targetStr.equals("upper") ? hanteiStrUpperArray : hanteiStrLowerArray;
            //とりあえず対象のチェックボックスを表示させてチェック状態も反映
            for (ArrayList<Object> hanteiStrAndBool : targetHanteiStrArray) {
                String idStr = targetStr + "_checkbox_" + String.valueOf(hanteiStrAndBool.get(0)).toLowerCase();
                int cbId = getResources().getIdentifier(idStr, "id", getPackageName());
                CheckBox cb = (CheckBox)popupView.findViewById(cbId);
                cb.setVisibility(View.VISIBLE);
                cb.setTypeface(Typeface.MONOSPACE);
                cb.setChecked(Boolean.valueOf(String.valueOf(hanteiStrAndBool.get(1))));

                //対象が１つしかなかったらenabredをfalseに
                if (targetHanteiStrArray.size() == 1) cb.setEnabled(false);

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
                int sectionIdArray[] = {R.id.home_checkboxes_zero_for_four,
                                        R.id.home_checkboxes_five_for_nine,
                                        R.id.draw_checkboxes_zero_for_four,
                                        R.id.draw_checkboxes_five_for_nine,
                                        R.id.away_checkboxes_zero_for_four,
                                        R.id.away_checkboxes_five_for_nine,
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
                threeArrays.add(homeGroupArray);
                threeArrays.add(drawGroupArray);
                threeArrays.add(awayGroupArray);
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

        //画面サイズの８割サイズでポップアップ ※縦サイズは動的
        popReduceWindow.setWidth((int) (p.x * 0.8));
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
        //ボタン連打制御(１秒)
        if (!Common.isClickEvent()) return;

        //判定結果表示ポップアップWindow
        PopupWindow popHanteiWindow = new PopupWindow(MultiResultActivity.this);

        // レイアウト設定
//        View popupView = getLayoutInflater().inflate(R.layout.pop_hantei_result, null);
        View popupView = getLayoutInflater().inflate(R.layout.pop_hantei_result, (ViewGroup)findViewById(R.id.pop_hantei_root_layout), false);

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)popupView.findViewById(R.id.pop_hantei_result_view);
        tv.setTypeface(Typeface.MONOSPACE);

        //プチ削減
        ArrayList<ArrayList<String>> reduceTargetStrArray = new ArrayList<>();
        for (ArrayList<String> targetStr : targetStrArray) {
            //ホーム、ドロー、アウェイ数が対象かどうかチェック
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(targetStr.size() - 3)), homeGroupArray)) continue;
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(targetStr.size() - 2)), drawGroupArray)) continue;
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(targetStr.size() - 1)), awayGroupArray)) continue;

            //大文字判定対象かどうかチェック
            if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(13)), hanteiStrUpperArray)) continue;

            //bODDSはない可能性あり
            if (hanteiStrLowerArray.size() > 0) {
                //小さい文字判定対象かどうかチェック
                if (!reduceTargetYesOrNo(String.valueOf(targetStr.get(15)), hanteiStrLowerArray)) continue;
            }
            //対象だったら追加
            reduceTargetStrArray.add(targetStr);
        }

        //削減の結果で処理変更
        final String reduceText = reduceTargetStrArray.size() == 0 ? "結果なし" : HanteiStrMake.resultHanteiStr(reduceTargetStrArray); //, common.dataGetTime);
        final String reduceCount = reduceTargetStrArray.size() == 0 ? "" : "\n削減前:" + String.valueOf(targetStrArray.size()) + "口\n削減後:" + String.valueOf(reduceTargetStrArray.size()) + "口\n\n" + common.dataGetTime + "\n";

        //結果がなかったらコピーボタン非表示
        if (reduceTargetStrArray.size() == 0) popupView.findViewById(R.id.pop_hantei_copy_button).setVisibility(View.GONE);

        //整形データをTextViewにセット
        tv.setText(reduceText + reduceCount);

        //コピーボタン押下時の挙動
        popupView.findViewById(R.id.pop_hantei_copy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ランダム抽出データをクリップボードにコピー
                ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // コピーするデータをエディットテキストから取得する
                ClipData clip = ClipData.newPlainText("copied_text", reduceText  + reduceCount + "\n#トトラン！");

                // クリップボードに内容をコピーする
                clipBoard.setPrimaryClip(clip);

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

        //画面サイズの８割サイズでポップアップ ※縦サイズは２０行まで動的でそれ以上になるとマックス６割
        popHanteiWindow.setWidth((int) (p.x * 0.8));

        if (reduceTargetStrArray.size() >= 20) {
            popHanteiWindow.setHeight((int) (p.y * 0.6));
        } else {
            popHanteiWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        }

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
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // コピーするデータをエディットテキストから取得する
        ClipData clip = ClipData.newPlainText("copied_text", displayText + "\n#トトラン！");

        // クリップボードに内容をコピーする
        clipBoard.setPrimaryClip(clip);

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
