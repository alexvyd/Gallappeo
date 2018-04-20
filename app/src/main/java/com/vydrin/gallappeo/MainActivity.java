package com.vydrin.gallappeo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton,mFalseButton,mHelpButton;
    private ImageButton mNextButton,mPrevButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mAnswersArray[] = new int[mQuestionBank.length]; //Вспомогательный массив
    private int mAnswerCounter; //Счетчик отвеченных вопросов
    private int mHelpCounter = 3; //Счетчик оставшихся подсказок
    private int mCurrentIndex = 0; //Номер текущего вопроса

    //Константы для сохранения состояния при реинкарнации активности
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSW = "answ";
    private static final String KEY_COUNT = "counter";
    private static final String KEY_HELP = "help";
    private static final int REQUEST_CODE_CHEAT = 0;

    //Сохранение состояния при повороте
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        int i = 0;
        for (Question X : mQuestionBank) {
            mAnswersArray[i] = X.getAnswer();
            i++;
        }
        savedInstanceState.putIntArray(KEY_ANSW, mAnswersArray);
        savedInstanceState.putInt(KEY_COUNT, mAnswerCounter);
        savedInstanceState.putInt(KEY_HELP, mHelpCounter);
    }

    //Инициализация, в т.ч. восстановление состояния при повороте
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerCounter = savedInstanceState.getInt(KEY_COUNT, 0);
            mHelpCounter = savedInstanceState.getInt(KEY_HELP, 0);
            mAnswersArray = savedInstanceState.getIntArray(KEY_ANSW);
            int i = 0;
            for (int Y : mAnswersArray) {
                mQuestionBank[i].setAnswer(Y);
                i++;
            }
        }

        //Инициализация текста вопроса и обработчика клика по нему
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        };
        mQuestionTextView.setOnClickListener(ocl);

        //Инициализация кнопок и обработчиков кликов по ним
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(ocl);

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex==0)? mQuestionBank.length-1 : mCurrentIndex - 1;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mHelpButton = (Button)findViewById(R.id.cheat_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Main2Activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = Main2Activity.newIntent(MainActivity.this, answerIsTrue, mHelpCounter);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });



        //Вывод первого вопроса
        updateQuestion();
    }

    //Переопределение обработчиков основных состояний нашей активности для записи меток об их прохождении в лог
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //Вывод очередного вопроса и ответа по нему
    protected void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        BlockButton(mQuestionBank[mCurrentIndex].getAnswer(),mQuestionBank[mCurrentIndex].getWright());
    }

    //Обновление статуса кнопок (какой был ответ, и правильный ли)
    private void BlockButton(int butt, boolean wright) {
        switch (butt) {
            case 1:
                mTrueButton.setEnabled(false); mFalseButton.setEnabled(true);
                if(wright){mTrueButton.setTextColor(Color.GREEN);}else{mTrueButton.setTextColor(Color.RED);}
                mFalseButton.setTextColor(Color.DKGRAY);
                break;
            case 2:
                mFalseButton.setEnabled(false); mTrueButton.setEnabled(true);
                if(wright){mFalseButton.setTextColor(Color.GREEN);}else{mFalseButton.setTextColor(Color.RED);}
                mTrueButton.setTextColor(Color.DKGRAY);
                break;
            default:
                mTrueButton.setEnabled(true); mFalseButton.setEnabled(true);
                mTrueButton.setTextColor(Color.DKGRAY);
                mFalseButton.setTextColor(Color.DKGRAY);
        }
    }

    //Обработчик клика по кнопкам ответа
    private void checkAnswer(boolean userPressedTrue) {
        if (mQuestionBank[mCurrentIndex].getAnswer()==0) {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            boolean isWright = (userPressedTrue == answerIsTrue);
            int messageResId = 0;
            if (isWright) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

            if (userPressedTrue == true) {
                mQuestionBank[mCurrentIndex].setAnswer(1);
                BlockButton(1, isWright);
            } else {
                mQuestionBank[mCurrentIndex].setAnswer(2);
                BlockButton(2, isWright);
            }
            mAnswerCounter++;
        }
        //Больше нет неотвеченных вопросов
        if(mAnswerCounter==mQuestionBank.length){
            int i = 0;
            for (Question X : mQuestionBank) {
                if(X.getWright()) i++;
            }
            String message = "Викторина окончена. Правильных ответов – "+Integer.toString(i)+" из "+Integer.toString(mQuestionBank.length);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
