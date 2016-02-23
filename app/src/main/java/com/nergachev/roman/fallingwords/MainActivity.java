package com.nergachev.roman.fallingwords;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nergachev.roman.fallingwords.game.animation.GameAnimatorListener;
import com.nergachev.roman.fallingwords.game.mechanics.GameEngine;
import com.nergachev.roman.fallingwords.model.Dictionary;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MAIN_ACTIVITY";
    private static int START_DURATION = 10000;
    private static int DURATION_STEP = 50;
    private static int SMALLEST_DURATION = 500;

    private Dictionary dictionary;
    private TextView textWord;
    private TextView textTranslation;
    private ImageView topBorder;
    private ImageView bottomBorder;
    private View container;
    private ImageButton buttonRight;
    private ImageButton buttonWrong;
    private TextView textCorrect;
    private TextView textWrong;
    private TextView textNoAnswer;
    private GameEngine gameEngine;
    private GameAnimatorListener gameAnimatorListener;
    private int duration;
    private int bottomCoordinate;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObjects();
        loadWords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnClickListeners();
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] locations = new int[2];
                topBorder.getLocationOnScreen(locations);
                int y1 = locations[1];
                int[] locations2 = new int[2];
                bottomBorder.getLocationOnScreen(locations2);
                int y2 = locations2[1];
                bottomCoordinate = y2 - bottomBorder.getHeight() - textTranslation.getHeight() - y1;
                startAnimator(duration);
            }
        });
    }

    private String readJsonFromFile() throws IOException {
        StringBuilder fileData = new StringBuilder();
        Resources res = getResources();
        InputStream inputStream = res.openRawResource(R.raw.json);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null) {
            fileData.append(line);
            line = reader.readLine();
        }
        return fileData.toString();
    }

    public void initObjects() {
        textTranslation = (TextView) findViewById(R.id.text_translation);
        textWord = (TextView) findViewById(R.id.text_word);
        buttonRight = (ImageButton) findViewById(R.id.button_right);
        buttonWrong = (ImageButton) findViewById(R.id.button_wrong);
        textCorrect = (TextView) findViewById(R.id.text_correct);
        textWrong = (TextView) findViewById(R.id.text_wrong);
        textNoAnswer = (TextView) findViewById(R.id.text_noanswer);
        topBorder = (ImageView) findViewById(R.id.top_border);
        bottomBorder = (ImageView) findViewById(R.id.bottom_border);
        container = findViewById(R.id.container);
        duration = START_DURATION;

    }

    private void setOnClickListeners() {
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameEngine.isTranslationCorrect()) {
                    gameEngine.increaseCorrectCounter();
                    duration -= DURATION_STEP;
                    if (duration < SMALLEST_DURATION) {
                        duration = SMALLEST_DURATION;
                    }
                } else {
                    gameEngine.increaseWrongCounter();
                }
                restartAnimator(duration);
            }
        });

        buttonWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameEngine.isTranslationCorrect()) {
                    gameEngine.increaseCorrectCounter();
                    duration -= DURATION_STEP;
                    if (duration < SMALLEST_DURATION) {
                        duration = SMALLEST_DURATION;
                    }
                } else {
                    gameEngine.increaseWrongCounter();
                }
                restartAnimator(duration);
            }
        });
    }

    private void startAnimator(int duration) {
        gameEngine.setWord();
        textTranslation.clearAnimation();
        objectAnimator = ObjectAnimator.ofFloat(textTranslation, "translationY", 0f, bottomCoordinate);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addListener(gameAnimatorListener);
        objectAnimator.start();
    }

    private void restartAnimator(int duration) {
        gameEngine.setWord();
        objectAnimator.setDuration(duration);
        objectAnimator.cancel();
        objectAnimator.start();
    }

    private void loadWords() {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Dictionary> jsonAdapter = moshi.adapter(Dictionary.class);

        try {
            String json = readJsonFromFile();
            dictionary = jsonAdapter.fromJson(json);
            gameEngine = new GameEngine(dictionary, textWord, textTranslation, textCorrect, textWrong, textNoAnswer);
            gameAnimatorListener = new GameAnimatorListener(gameEngine);
        } catch (IOException e) {
            Log.d(TAG, "Error reading json file data");
        }
    }
}
