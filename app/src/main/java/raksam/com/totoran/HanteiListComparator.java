package raksam.com.totoran;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/19.
 * 判定済み数列データを降順ソートするクラス
 */
public class HanteiListComparator implements java.util.Comparator {
    public int compare(Object s, Object t) {
        float dispatch =  Float.valueOf(((ArrayList<String>) s).get(0)) - Float.valueOf(((ArrayList<String>) t).get(0));
        if (dispatch > 0) {
            return -1;
        } else if (dispatch < 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
