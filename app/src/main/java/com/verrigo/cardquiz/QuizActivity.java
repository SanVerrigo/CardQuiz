package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private String PACK_NAME = "packName";

    private Intent intent;
    private String packName;
    private CardQuizDBHelper dbHelper;
    private String answer;
    private int currentCardIndex = 0;
    private int currentLoop = 0;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        dbHelper = new CardQuizDBHelper(this);
        cardList = dbHelper.getCardListByPackName(packName);
        final TextView questionTextView = findViewById(R.id.activity_quiz_question);
        final TextView answerTextView = findViewById(R.id.activity_quiz_answer);

        getSupportActionBar().setTitle(packName);
        questionTextView.setText(cardList.get(currentCardIndex).getQuestion());
        answer = cardList.get(currentCardIndex).getAnswer();

        final TextView loopCounter = findViewById(R.id.activity_quiz_loop_counter);
        loopCounter.setText(loopCounterString(currentLoop));
        ImageButton next = findViewById(R.id.activity_quiz_go_to_next);
        ImageButton previous = findViewById(R.id.activity_quiz_return_to_previous);
        FloatingActionButton fab = findViewById(R.id.activity_quiz_fab);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCardIndex++;
                if (currentCardIndex >= cardList.size()) {
                    currentLoop++;
                    loopCounter.setText(loopCounterString(currentLoop));
                }
                currentCardIndex = (currentCardIndex >= cardList.size() ? 0 : currentCardIndex++);
                questionTextView.setText(cardList.get(currentCardIndex).getQuestion());
                answer = cardList.get(currentCardIndex).getAnswer();
                answerTextView.setText("...");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCardIndex <= 0) {
                    currentCardIndex = 0;
                } else {
                    currentCardIndex--;
                }
                questionTextView.setText(cardList.get(currentCardIndex).getQuestion());
                answer = cardList.get(currentCardIndex).getAnswer();
                answerTextView.setText("...");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerTextView.setText(answer);
            }
        });
    }

    public String loopCounterString(int currentLoop) {
        return String.format("Количество кругов: %d", currentLoop);
    }

    public Intent createIntent(Context context, String packName) {
        return new Intent(context, QuizActivity.class)
                .putExtra(PACK_NAME, packName);
    }
}
