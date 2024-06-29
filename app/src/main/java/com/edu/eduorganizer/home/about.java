package com.edu.eduorganizer.home;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.edu.eduorganizer.R;

public class about extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView tvLink = (TextView) findViewById(R.id.textViewLink);
        tvLink.setText(Html.fromHtml("<a href=\"http://androidcontrol.blogspot.com/\">http://androidcontrol.blogspot.com/</a> "));
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void onDestroy() {
        super.onDestroy();
    }
}
