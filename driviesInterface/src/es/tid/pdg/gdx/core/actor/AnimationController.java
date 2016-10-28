package es.tid.pdg.gdx.core.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationController {
    private float time;
    private Animation animation;

    public AnimationController(Animation animation) {
        this.animation = animation;
    }

    public TextureRegion next(float stepInSeconds) {
        time += stepInSeconds;
        return animation.getKeyFrame(time);
    }

    public void reset() {
        time = 0;
    }
}
