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
    private ArrayList<BodyPart> members = new ArrayList<>();
    private ColorRGB tint = new ColorRGB();
    private float xPercentage;
    private float yPercentage;
    private float animationDelta;
    private boolean flip = false;
    private Size size;

    public void prepare(TextureAtlas atlas, Size actorSize, Size screenSize) {
        Array<TextureRegion> regions = new Array<>();
        for (String imageName : imagesNames) {
            regions.add(atlas.findRegion(imageName));
        }
        Animation animation = new Animation(animationDelta, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        sprite = new Sprite(animation.getKeyFrame(0));
        setFullscreenIfAppropriate(actorSize, screenSize);
        if (flip) {
            sprite.setScale(-1, 1);
        }
        controller = new AnimationController(animation);
        calculateSize();
        for (BodyPart member : members) {
            member.prepare(atlas, size, screenSize);
        }
    }

    public void reset() {
        controller.reset();
        for (BodyPart member : members) {
            member.reset();
        }
    }

    public void render(SpriteBatch batch, float actorX, float actorY, Size actorSize, float scale,
            float stepTimeInSeconds) {
        sprite.setRegion(controller.next(stepTimeInSeconds));
        setScaleIfAppropriate(scale, actorSize);
        setPosition(actorX, actorY, actorSize, scale);
        batch.setColor(tint.getRed(), tint.getGreen(), tint.getBlue(), 1);
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(),
                sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
        batch.setColor(1, 1, 1, 1);
        renderChildren(batch, stepTimeInSeconds, scale);
    }

    private void setFullscreenIfAppropriate(Size actorSize, Size screenSize) {
        if (actorSize.getWidth() == Size.FULLSCREEN && actorSize.getHeight() == Size.FULLSCREEN) {
            float xRate = screenSize.getWidth() / sprite.getWidth();
            float yRate = screenSize.getHeight() / sprite.getHeight();
            sprite.setScale(xRate, yRate);
            sprite.setPosition(-screenSize.getWidth() / (2 * xRate), -screenSize.getHeight() / (2 * yRate));
        }
    }

    private void setScaleIfAppropriate(float scale, Size actorSize) {
        if (actorSize.getWidth() != Size.FULLSCREEN && actorSize.getHeight() != Size.FULLSCREEN) {
            float xScale = scale;
            if (flip) {
                xScale *= -1;
            }
            sprite.setScale(xScale, scale);
        }
    }

    private void calculateSize() {
        size = new Size();
        size.setWidth(sprite.getWidth());
        size.setHeight(sprite.getHeight());
    }

    private void setPosition(float actorX, float actorY, Size actorSize, float scale) {
        if (actorSize.getWidth() == Size.FULLSCREEN && actorSize.getHeight() == Size.FULLSCREEN) return;

        float spriteX = actorX + xPercentage * scale * actorSize.getWidth() / 2 - sprite.getWidth() / 2;
        float spriteY = actorY + yPercentage * scale * actorSize.getHeight() / 2 - sprite.getHeight() / 2;
        sprite.setPosition(spriteX, spriteY);
    }

    private void renderChildren(SpriteBatch batch, float stepTimeInSeconds, float scale) {
        float posX = sprite.getX() + sprite.getWidth() * .5f;
        float posY = sprite.getY() + sprite.getHeight() * .5f;
        for (BodyPart member : members) {
            member.render(batch, posX, posY, size, scale, stepTimeInSeconds);
        }
    }
}
