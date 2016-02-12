package raksam.com.totoran;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SingleResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);
    }

    //シングル条件指定画面に戻る
    public void goBack(View v) {
        finish();
    }
}
