package com.nergachev.roman.fallingwords.model;

import com.nergachev.roman.fallingwords.model.Word;

import java.util.List;
import java.util.Random;

/**
 * Created by rone on 22/02/16.
 */
public class Dictionary {
    public List<Word> dictionary;

    public Word getRandomWord() {
        Random r = new Random();
        int nextInt = r.nextInt(dictionary.size());
        return dictionary.get(nextInt);
    }

    public Word getRandomWordExceptTheWord(Word word) {
        Random r = new Random();
        int nextInt = r.nextInt(dictionary.size());
        if (dictionary.get(nextInt).equals(word)) {
            if (nextInt > 1) {
                nextInt--;
            } else {
                nextInt++;
            }
        }
        return dictionary.get(nextInt);
    }

    public boolean correctOrIncorrectDecision() {
        Random r = new Random();
        int nextInt = r.nextInt(2);
        if (nextInt == 0) {
            return true;
        } else {
            return false;
        }
    }
}
