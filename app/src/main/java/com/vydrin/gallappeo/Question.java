package com.vydrin.gallappeo;

//Класс вопросов и состояний ответов на них
public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private int mAnswer;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAnswer = 0;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setAnswer(int answer) {
        mAnswer = answer;
    }

    public int getAnswer() {
        return mAnswer;
    }

    public boolean getWright() {
        return ((mAnswerTrue & mAnswer==1) | (!mAnswerTrue & mAnswer==2));
    }
}
