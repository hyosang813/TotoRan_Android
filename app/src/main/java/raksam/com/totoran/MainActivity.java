package raksam.com.totoran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.setClassName("raksam.com.totoran", "raksam.com.totoran.MultiDetailActivity");
        startActivity(i);

    }
}
