package com.daveguy.matchmaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView mOutputText;

    static final int REQUEST_SHEETS_INTERFACE = 0;

    static final int REQUEST_MANAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mainactivity);

        startActivityForResult(new Intent(this, SheetsInterface.class), REQUEST_SHEETS_INTERFACE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SHEETS_INTERFACE && resultCode == RESULT_OK) {
            //setLayout((data));
            Intent intent = new Intent(this, Manage.class);
            intent.putStringArrayListExtra("playerList", data.getStringArrayListExtra("namesFromSheets"));
            startActivity(intent);
        }
    }

    //deprecated, was for testing
    void setLayout(Intent data)
    {
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(TextUtils.join("\n", data.getStringArrayListExtra("namesFromSheets")));
        activityLayout.addView(mOutputText);

        setContentView(activityLayout);
    }
}
