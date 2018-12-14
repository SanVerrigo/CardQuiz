package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String QUIZ_MODE = "quizMode";
    private static final String PACK_NAME = "packName";
    private static final String CURRENT_INDEX = "currentCardIndex";
    private static final String CURRENT_LOOP = "currentLoop";

    private Intent intent;
    private String packName;
    private String mode;
    private CardQuizDBHelper dbHelper;
    private String answer;
    private int currentCardIndex;
    private int currentLoop;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        mode = intent.getStringExtra(QUIZ_MODE);
//        Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
        dbHelper = new CardQuizDBHelper(this);
        cardList = dbHelper.getCardListByPackName(packName);
        switch (mode){
            case MainActivity.NORMAL_MODE:
                break;
            case MainActivity.CHAOTIC_MODE:
                Collections.shuffle(cardList);
                break;
        }
        int cardListSize = cardList.size();
        if (savedInstanceState != null) {
            currentCardIndex = savedInstanceState.getInt(CURRENT_INDEX);
            currentLoop = savedInstanceState.getInt(CURRENT_LOOP);
        } else {
            currentCardIndex = 0;
            currentLoop = 0;
        }

        for (int i = 0; i < cardListSize; i++) {
            if (!cardList.get(i).isShown()) {
                cardList.remove(i);
                i--;
            }
            cardListSize = cardList.size();
        }

        final TextView questionTextView = findViewById(R.id.activity_quiz_question);
        final TextView answerTextView = findViewById(R.id.activity_quiz_answer);

        getSupportActionBar().setTitle(packName);
        questionTextView.setText(cardList.get(currentCardIndex).getQuestion());
        answer = cardList.get(currentCardIndex).getAnswer();

        final TextView loopCounter = findViewById(R.id.activity_quiz_loop_counter);
        final TextView currentCardCounter = findViewById(R.id.activity_quiz_current_card_counter);
        loopCounter.setText(loopCounterString(currentLoop));
        currentCardCounter.setText(currentCardCounterString(currentCardIndex, cardList.size()));
        ImageButton next = findViewById(R.id.activity_quiz_go_to_next);
        ImageButton previous = findViewById(R.id.activity_quiz_return_to_previous);
        FloatingActionButton fab = findViewById(R.id.activity_quiz_fab);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCardIndex++;
                if (currentCardIndex >= cardList.size()) {
                    currentLoop++;
                    if (mode.equals(MainActivity.CHAOTIC_MODE)) {
                        Collections.shuffle(cardList);
                    }
                    loopCounter.setText(loopCounterString(currentLoop));
                }
                currentCardIndex = (currentCardIndex >= cardList.size() ? 0 : currentCardIndex++);
                currentCardCounter.setText(currentCardCounterString(currentCardIndex, cardList.size()));
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
                currentCardCounter.setText(currentCardCounterString(currentCardIndex, cardList.size()));
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_INDEX, currentCardIndex);
        outState.putInt(CURRENT_LOOP, currentLoop);
        super.onSaveInstanceState(outState);
    }

    public String loopCounterString(int currentLoop) {
        return String.format("Кругов: %d", currentLoop);
    }

    public String currentCardCounterString(int currentCardIndex, int amountOfCards) {
        return String.format("№%d/%d", currentCardIndex + 1, amountOfCards);
    }

    public Intent createIntent(Context context, String packName, String mode) {
        return new Intent(context, QuizActivity.class)
                .putExtra(PACK_NAME, packName)
                .putExtra(QUIZ_MODE, mode);

    }
}
