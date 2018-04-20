package com.vydrin.gallappeo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.vydrin.gallappeo.answer_is_true";
    private static final String EXTRA_HELPS_LEFT =
            "com.vydrin.gallappeo.help_count";
    private boolean mAnswerIsTrue;
    private int mHelpCounter;

    private TextView mAnswerTextView,mWarningTextView;
    private Button mHelpButton,mCancelButton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int HelpCount) {
        Intent intent = new Intent(packageContext, Main2Activity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_HELPS_LEFT, HelpCount);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mHelpCounter = getIntent().getIntExtra(EXTRA_HELPS_LEFT, 0);

        mWarningTextView = (TextView) findViewById(R.id.warning_text_view);
        String tempText = getBaseContext().getString(R.string.warning_text) + Integer.toString(mHelpCounter);
        mWarningTextView.setText(tempText);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mHelpButton = (Button) findViewById(R.id.show_help_button);
        mCancelButton = (Button) findViewById(R.id.cancel_help_button);

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
