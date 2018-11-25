package learningcurve.zachg.learningcurve;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

// Wire Cheat Activity's Prev button to return to MainActivity
// If that doesn't fix the True/False buttons, troubleshoot til they fixed
// Maximum of three cheats, keeps track
// Do not let screen rotation skip cheating

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question [] {
            new Question(R.string.question_casterlyrock, false),
            new Question(R.string.question_harrenhal, false),
            new Question(R.string.question_winterfell, true),
            new Question(R.string.question_dragonstone, true),
            new Question(R.string.question_summerhall, true)
    };

    private int mCurrentIndex = 0;
    private int mCorrectCount = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

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

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setEnabled(false);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == mQuestionBank.length -1 ) {
                    restart();
                } else {
                    mCurrentIndex = (mCurrentIndex + 1);
                    mIsCheater = false;
                    updateQuestion();
                }
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setEnabled(false);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
                if (mCurrentIndex == 0) {
                    mPrevButton.setEnabled(false);
                }
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void checkAnswer (boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (mCurrentIndex < mQuestionBank.length - 1) {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;
                    mCorrectCount++;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            } else {
                calculateScore();
            }
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mNextButton.setEnabled(true);
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mNextButton.setEnabled(false);
        if (mCurrentIndex > 0) {
            mPrevButton.setEnabled(true);
        }
    }

    private void calculateScore() {
        int score = (mCorrectCount * 100)/mQuestionBank.length;
        Toast.makeText(this, "Your score is " + score + "%!", Toast.LENGTH_SHORT).show();
        mCorrectCount = 0;
    }

    private void restart() {
        mCorrectCount = 0;
        mCurrentIndex = 0;
        int messageResId = R.string.restart_toast;
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        updateQuestion();
        mPrevButton.setEnabled(false);
        mNextButton.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceStated");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}