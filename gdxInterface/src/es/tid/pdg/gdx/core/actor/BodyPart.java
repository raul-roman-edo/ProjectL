package es.tid.pdg.gdx.core.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import es.tid.pdg.gdx.core.Size;
import java.util.ArrayList;

public class BodyPart {
    private Sprite sprite;
    private AnimationController controller;
    private ArrayList<String> imagesNames;
    private ColorRGB tint = new ColorRGB();
    private float xPercentage;
    private float yPercentage;
    private float animationDelta;

    public void prepare(TextureAtlas atlas, Size actorSize, Size screenSize) {
        Array<TextureRegion> regions = new Array<>();
        for (String imageName : imagesNames) {
            regions.add(atlas.findRegion(imageName));
        }
        Animation animation = new Animation(animationDelta, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        sprite = new Sprite(animation.getKeyFrame(0));
        setFullscreenIfAppropriate(actorSize, screenSize);
        controller = new AnimationController(animation);
    }

    public void reset() {
        controller.reset();
    }

    public void render(SpriteBatch batch, float actorX, float actorY, Size actorSize, float stepTimeInSeconds) {
        sprite.setRegion(controller.next(stepTimeInSeconds));
        setPosition(actorX, actorY, actorSize);
        batch.setColor(tint.getRed(), tint.getGreen(), tint.getBlue(), 1);
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(),
                sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
        batch.setColor(1, 1, 1, 1);
    }

    private void setFullscreenIfAppropriate(Size actorSize, Size screenSize) {
        if (actorSize.getWidth() == Size.FULLSCREEN && actorSize.getHeight() == Size.FULLSCREEN) {
            sprite.setScale(screenSize.getWidth() / sprite.getWidth(), screenSize.getHeight() / sprite.getHeight());
            sprite.setPosition(-screenSize.getWidth() / 2, -screenSize.getHeight() / 2);
        }
    }

    private void setPosition(float actorX, float actorY, Size actorSize) {
        if (actorSize.getWidth() == Size.FULLSCREEN && actorSize.getHeight() == Size.FULLSCREEN) return;

        float spriteX = actorX + xPercentage * actorSize.getWidth() / 2 - sprite.getWidth() / 2;
        float spriteY = actorY + yPercentage * actorSize.getHeight() / 2 - sprite.getHeight() / 2;
        sprite.setPosition(spriteX, spriteY);
    }
}
