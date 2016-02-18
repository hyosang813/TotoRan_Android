package raksam.com.totoran;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/18.
 */
public class DoubleTripleCheck {

    //w８t５チェック
    public static boolean dtCheck (ArrayList<ArrayList<Integer>> checkArray) {

        //doubleとtripleの現在値を取得
        int doubleCount = checkArray.get(0).get(2);
        int tripleCount = checkArray.get(1).get(2);

        if (doubleCount > 8 ) return true; //wは最高8個まで
        if (tripleCount > 5 ) return true; //tは最高5個まで
        if (doubleCount == 0 && tripleCount > 5 ) return true; //wが0の時はtは5まで
        if (doubleCount == 1 && tripleCount > 5 ) return true; //wが1の時はtは5まで
        if (doubleCount == 2 && tripleCount > 4 ) return true; //wが2の時はtは4まで
        if (doubleCount == 3 && tripleCount > 3 ) return true; //wが3の時はtは3まで
        if (doubleCount == 4 && tripleCount > 3 ) return true; //wが4の時はtは3まで
        if (doubleCount == 5 && tripleCount > 2 ) return true; //wが5の時はtは2まで
        if (doubleCount == 6 && tripleCount > 1 ) return true; //wが6の時はtは1まで
        if (doubleCount == 7 && tripleCount > 1 ) return true; //wが7の時はtは1まで
        if (doubleCount == 8 && tripleCount > 0 ) return true; //wが8の時はtは0まで

        //falseは結果OK
        return false;
    }
}
