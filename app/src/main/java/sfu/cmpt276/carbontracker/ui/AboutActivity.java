package sfu.cmpt276.carbontracker.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import sfu.cmpt276.carbontracker.BuildConfig;
import sfu.cmpt276.carbontracker.R;
/*Activity to show app information, i.e. team information, resources used, version number*/

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FullScreencall();

        TextView versionTxt = (TextView)findViewById(R.id.app_version_txt);
        versionTxt.setText(versionTxt.getText().toString()+ " " +versionCode + "-" + versionName);

        TextView namesTxt = (TextView)findViewById(R.id.creators_txt);
        String[]names = getResources().getStringArray(R.array.creators);
        for(String n:names)
        {
            namesTxt.append(n+"\n");
        }

        TextView linksTxt = (TextView)findViewById(R.id.links_txt);
        linksTxt.setMovementMethod(LinkMovementMethod.getInstance());
        String[]links = getResources().getStringArray(R.array.links);
        for(String n:links)
        {
            linksTxt.append(Html.fromHtml(n)+"\n");
        }
        Linkify.addLinks(linksTxt, Linkify.WEB_URLS);

    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
