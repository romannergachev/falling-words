package com.nergachev.roman.fallingwords.game.mechanics;

import android.widget.TextView;

import com.nergachev.roman.fallingwords.model.Dictionary;
import com.nergachev.roman.fallingwords.model.Word;

/**
 * Created by rone on 23/02/16.
 */
public class GameEngine {
    private Dictionary dictionary;
    private Word currentWord;
    private TextView textWord;
    private TextView textTranslation;
    private TextView textCorrect;
    private TextView textWrong;
    private TextView textNoAnswer;
    private boolean isTranslationCorrect;
    private int correct;
    private int wrong;
    private int noAnswer;


    public GameEngine(Dictionary dictionary, TextView textWord, TextView textTranslation,
                      TextView textCorrect, TextView textWrong, TextView textNoAnswer) {
        this.dictionary = dictionary;
        this.textWord = textWord;
        this.textTranslation = textTranslation;
        this.textCorrect = textCorrect;
        this.textWrong = textWrong;
        this.textNoAnswer = textNoAnswer;
        this.correct = 0;
        this.wrong = 0;
        this.noAnswer = 0;
    }

    public void setWord() {
        currentWord = dictionary.getRandomWord();
        textWord.setText(currentWord.getTextEng());

        isTranslationCorrect = dictionary.correctOrIncorrectDecision();
        if (isTranslationCorrect) {
            textTranslation.setText(currentWord.getTextSpa());
        } else {
            Word tempWord = dictionary.getRandomWordExceptTheWord(currentWord);
            textTranslation.setText(tempWord.getTextSpa());
        }
    }

    public boolean isTranslationCorrect() {
        return isTranslationCorrect;
    }

    public void increaseCorrectCounter() {
        correct++;
        textCorrect.setText(String.valueOf(correct));
    }

    public void increaseWrongCounter() {
        wrong++;
        textWrong.setText(String.valueOf(wrong));
    }

    public void increaseNoAnswerCounter() {
        noAnswer++;
        textNoAnswer.setText(String.valueOf(noAnswer));
    }
}
