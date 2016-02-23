package com.nergachev.roman.fallingwords.game.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.nergachev.roman.fallingwords.game.mechanics.GameEngine;

/**
 * Created by rone on 23/02/16.
 */
public class GameAnimatorListener extends AnimatorListenerAdapter {
    private GameEngine gameEngine;

    public GameAnimatorListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        super.onAnimationRepeat(animation);
        gameEngine.increaseNoAnswerCounter();
        gameEngine.setWord();
    }
}
