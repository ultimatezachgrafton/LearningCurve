package learningcurve.zachg.learningcurve;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mWasQuestionAnswered = false;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
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

    public boolean isAnswered(boolean mWasQuestionAnswered) {
        return mWasQuestionAnswered;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
