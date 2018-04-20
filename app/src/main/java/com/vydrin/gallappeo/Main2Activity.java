package com.vydrin.gallappeo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.vydrin.gallappeo.answer_is_true";
    private static final String EXTRA_HELPS_LEFT =
            "com.vydrin.gallappeo.help_count";
    private static final String EXTRA_HELPED =
            "com.vydrin.gallappeo.helped";

    private boolean mAnswerIsTrue;
    private int mHelpCounter;

    private TextView mAnswerTextView,mWarningTextView,mSDKView;
    private Button mHelpButton,mCancelButton;

    //Два закрытых метода для передачи и получения данных второй активности
    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int HelpCount) {
        Intent intent = new Intent(packageContext, Main2Activity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_HELPS_LEFT, HelpCount);
        return intent;
    }
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_HELPED, false);
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
        mSDKView = (TextView) findViewById(R.id.sdk_text_view);
        mSDKView.setText(Integer.toString(Build.VERSION.SDK_INT));

        mHelpButton = (Button) findViewById(R.id.show_help_button);
        if(mHelpCounter==0) mHelpButton.setEnabled(false);
        mCancelButton = (Button) findViewById(R.id.cancel_help_button);

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                //mHelpCounter--;
                setAnswerShownResult(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mHelpButton.getWidth() / 2;
                    int cy = mHelpButton.getHeight() / 2;
                    float radius = mHelpButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mHelpButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mHelpButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mHelpButton.setVisibility(View.INVISIBLE);
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
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_HELPED, isAnswerShown);
        setResult(RESULT_OK, data);
    }

}
