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

    /**
     *シングルランダムロジック
     */
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

    /**
     *マルチランダムロジック
     */
    public static ArrayList<ArrayList<String>> multiRandomDataMake(ArrayList<ArrayList<Boolean>> boolArray, ArrayList<ArrayList<Integer>> pickArray) {
        //返すArray
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();

        //元Arrayに影響でないように論理DEEPコピー用のArrayを用意
        ArrayList<ArrayList<Boolean>> localBoolArray = new ArrayList<>();

        //シングル、ダブル、トリプルの枠番号をそれぞれ控えておくための箱を用意
        ArrayList<ArrayList<Integer>> wakuNumArray = new ArrayList<>();
        wakuNumArray.add(new ArrayList<Integer>());
        wakuNumArray.add(new ArrayList<Integer>());
        wakuNumArray.add(new ArrayList<Integer>());

        //ノータップ枠をなくしながら枠ごとのタップ状況を検査
        int wakuNum = 0;
        for (ArrayList<Boolean> boolChild : boolArray) {
            //参照渡しにならないようにディープコピー
            ArrayList<Boolean> localBoolChild = new ArrayList<>(boolChild);

            //ノータップならランダムでどこかをタップ状態にする
            if (localBoolChild.indexOf(true) == -1)  localBoolChild.set(new Random().nextInt(3), true);

            //論理DEEPコピー先に追加
            localBoolArray.add(localBoolChild);

            //タップ状況を格納
            int tapCount = -1;
            for (Boolean wakuBool : localBoolChild) {
                if (wakuBool) tapCount++;
            }

            //しかるべき箇所に枠番号を追加
            wakuNumArray.get(tapCount).add(wakuNum);
            wakuNum++;
        }

        //トリプル昇格抽選
        for (int i = 0; i < pickArray.get(1).get(2) - pickArray.get(1).get(0); i++) {
            //シングル or ダブル枠を対象にトリプル昇格抽選
            int triplePromoteWakuCount = new Random().nextInt(13);

            //もし対象がすでにトリプル枠だったらやり直し
            if (wakuNumArray.get(2).contains(triplePromoteWakuCount)) {
                i--;
                continue;
            }

            //対象がシングルかダブルならトリプル昇格
            for (int j = 0; j < 3; j++) {
                localBoolArray.get(triplePromoteWakuCount).set(j, true);
            }

            //各枠情報も更新
            wakuNumArray.get(2).add(triplePromoteWakuCount);
            if (wakuNumArray.get(0).contains(triplePromoteWakuCount)) wakuNumArray.get(0).remove(wakuNumArray.get(0).indexOf(triplePromoteWakuCount));
            if (wakuNumArray.get(1).contains(triplePromoteWakuCount)) wakuNumArray.get(1).remove(wakuNumArray.get(1).indexOf(triplePromoteWakuCount));
        }

        //ダブル枠数は変動するから別保存
        int doubleWakuCount =  wakuNumArray.get(1).size();
        int a = pickArray.get(0).get(2);

        //ダブル昇格抽選　※ダブルがトリプルに昇格してる可能性があるから注意！！！
        for (int i = 0; i < pickArray.get(0).get(2) - doubleWakuCount; i++) {
            //シングル枠を対象に昇格抽選
            int doublePromoteWakuCount = wakuNumArray.get(0).get(new Random().nextInt(wakuNumArray.get(0).size()));

            //どこかをtrueにする
            while(true) {
                //もしその箇所がすでにtrueだったらやり直し
                int wakuTrue = new Random().nextInt(3);
                if (localBoolArray.get(doublePromoteWakuCount).get(wakuTrue)) continue;
                localBoolArray.get(doublePromoteWakuCount).set(wakuTrue, true);
                break;
            }

            //各枠情報を更新
            wakuNumArray.get(1).add(doublePromoteWakuCount);
            wakuNumArray.get(0).remove(wakuNumArray.get(0).indexOf(doublePromoteWakuCount));
        }

        //localBoolArray情報を元にランダムStringArrayに変換[1][0][2][-]
        for (ArrayList<Boolean> boolChild : localBoolArray) {
            ArrayList<String> wakuStrArray = new ArrayList<>();
            for (int i = 0; i < boolChild.size(); i++) {
                if (boolChild.get(i)) {
                    switch (i) {
                        case 0:
                            wakuStrArray.add(String.valueOf(i + 1));
                            break;
                        case 1:
                            wakuStrArray.add(String.valueOf(i - 1));
                            break;
                        default:
                            wakuStrArray.add(String.valueOf(i));
                            break;
                    }
                } else {
                    wakuStrArray.add("-");
                }
            }

            //返すArrayに追加
            returnArray.add(wakuStrArray);
        }

        //返す
        return returnArray;
    }
}
