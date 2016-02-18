package raksam.com.totoran;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by hyosang813 on 16/02/18.
 * ランダムロジッククラス
 */
public class RandomLogic {

    private static final int BASE_COUNT = 13;

    //シングルランダムロジック
    public static ArrayList<ArrayList<String>> singleRandomDataMake(ArrayList<ArrayList<Boolean>> boolArray, ArrayList<ArrayList<Integer>> pickArray) {
        //返すArray
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();

        //ベースとなる文字列Arrayを作る
        //Home:"1" Draw:"0" Away:"2" Other:"-1"
        ArrayList<String> baseStringSequenceArray = new ArrayList<>();
        int nontapCount = 0;
        for (ArrayList<Boolean> boolChild : boolArray) {
            //参照渡しにならないようにディープコピー
            ArrayList<Boolean> localBoolChild = new ArrayList<>(boolChild);

            //要素の0と1を入れ替え
            Collections.swap(localBoolChild, 0, 1);

            //ベースとなる文字列Arrayに数列を格納
            baseStringSequenceArray.add(String.valueOf(localBoolChild.indexOf(true)));

            //ついでにタップされてない枠数も取得しとこうかね
            if (localBoolChild.indexOf(true) == -1) nontapCount++;
        }

        //参照渡しにならないようにディープコピー
        ArrayList<ArrayList<Integer>> localPickArray = new ArrayList<>(pickArray);

        //要素の0と1を入れ替え
        Collections.swap(localPickArray, 0, 1);

        //ランダム値を格納する一時Array
        ArrayList<String> tempSetTargetStringArray = new ArrayList<>();

        //ピッカーで選択された差分分のArrayを作成
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < (localPickArray.get(i).get(2) - localPickArray.get(i).get(0)); j++) {
                tempSetTargetStringArray.add(String.valueOf(i));
                nontapCount--; //ランダム生成が必要な回数を減らす
            }
        }

        //口数分の数列を生成
        for (int i = 0; i < pickArray.get(3).get(2); i++) {
            //差分Arrayをコピー
            ArrayList<String> setTargetStringArray = new ArrayList<>(tempSetTargetStringArray);

            //ランダム値生成
            for (int j = 0; j < nontapCount; j++) {
                setTargetStringArray.add(String.valueOf(new Random().nextInt(3)));
            }

            //ランダムArrayをシャッフル
            Collections.shuffle(setTargetStringArray);

            //最終数列Arrayにコピー
            ArrayList<String> finalStringSequenceArray = new ArrayList<>(baseStringSequenceArray);

            //nontap枠をランダム値に置き換え
            int replaceCount = 0;
            for (int k = 0; k < finalStringSequenceArray.size(); k++) {
                if (finalStringSequenceArray.get(k).equals("-1")) {
                    finalStringSequenceArray.set(k, setTargetStringArray.get(replaceCount));
                    replaceCount++;
                }
            }

            //返すArrayに追加
            returnArray.add(finalStringSequenceArray);
        }

        //返す
        return returnArray;
    }


}
