/**
 * Created by hyosang813 on 16/02/07.
 * Activity間で情報共有
 */
package raksam.com.totoran;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

public class Common extends Application {

    //ホーム、アウェイのチーム名２次元Array
    List teamNameArray = Arrays.asList(
            Arrays.asList("広　島","川　崎"),
            Arrays.asList("鳥　栖","福　岡"),
            Arrays.asList("　柏　","浦　和"),
            Arrays.asList("湘　南","新　潟"),
            Arrays.asList("神　戸","甲　府"),
            Arrays.asList("磐　田","名古屋"),
            Arrays.asList("横浜Ｆ","仙　台"),
            Arrays.asList("東　京","大　宮"),
            Arrays.asList("Ｇ大阪","鹿　島"),
            Arrays.asList("千　葉","徳　島"),
            Arrays.asList("熊　本","山　雅"),
            Arrays.asList("町　田","Ｃ大阪"),
            Arrays.asList("京　都","水　戸")
            ); //テストデータだよーん！！！！！！！！！！！
        



    //初期化
    public void init(){

    }

}
