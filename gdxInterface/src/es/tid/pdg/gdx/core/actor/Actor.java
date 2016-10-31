package es.tid.pdg.gdx.core.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import es.tid.pdg.gdx.core.Point2D;
import es.tid.pdg.gdx.core.Size;
import es.tid.pdg.gdx.core.actions.Talk;
import java.util.ArrayList;

public class Actor {
    private static final float TEXT_MARGIN_PIXELS = 2 * Gdx.graphics.getDensity();
    private static final float TEXT_VERTICAL_MARGIN_PIXELS = 12 * Gdx.graphics.getDensity();
    private static final int ALPHA_FULL = 1;
    private ArrayList<BodyPart> members = new ArrayList<>();
    private ColorRGB textColor = new ColorRGB();
    private Size size = new Size();

    public void prepare(TextureAtlas atlas, Size screenSize) {
        for (BodyPart member : members) {
            member.prepare(atlas, size, screenSize);
        }
    }

    public void reset() {
        for (BodyPart member : members) {
            member.reset();
        }
    }

    public void renderBody(SpriteBatch batch, float x, float y, float scale, float stepTimeInSeconds) {
        for (BodyPart member : members) {
            member.render(batch, x, y, size, scale, stepTimeInSeconds);
        }
    }

    public void talk(SpriteBatch batch, BitmapFont font, float actorPositionX, float actorPositionY, Size screenSize,
            Talk talk, float scale, float stepTimeInSeconds) {
        if (talk == null) return;
        String message = talk.getMessage(stepTimeInSeconds);
        if (message.isEmpty()) return;
        font.setColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), ALPHA_FULL);
        Point2D position = calculatePosition(font, actorPositionX, actorPositionY, screenSize, scale, talk, message);
        font.drawMultiLine(batch, message, position.getX(), position.getY());
    }

    private Point2D calculatePosition(BitmapFont font, float actorPositionX, float actorPositionY, Size screenSize,
            float scale, Talk talk, String message) {
        Point2D position = new Point2D();
        BitmapFont.TextBounds bounds = font.getMultiLineBounds(message);
        position.setX(actorPositionX - bounds.width * .5f);
        position.setX(normalizeMessagePosition(position.getX(), bounds.width, screenSize.getWidth()));
        float height = size.getHeight();
        if (height == Size.FULLSCREEN) {
            height = screenSize.getHeight();
        } else {
            height *= scale;
        }
        float yPos = actorPositionY + talk.getActorMarginPercentage() * height * .5f;
        if (talk.getActorMarginPercentage() > 0) {
            yPos += bounds.height;
        }
        position.setY(yPos);
        position.setY(normalizeVerticalMessagePosition(position.getY(), bounds.height, screenSize.getHeight()));
        return position;
    }

    private float normalizeMessagePosition(float messagePosition, float bound, float screenBound) {
        float halfScreenBound = screenBound * .5f;
        boolean outFromLeftBound = messagePosition < -halfScreenBound;
        boolean outFromRightBound = messagePosition + bound > halfScreenBound;
        if (outFromLeftBound) {
            messagePosition = -halfScreenBound + TEXT_MARGIN_PIXELS;
        } else if (outFromRightBound) {
            messagePosition = halfScreenBound - bound - TEXT_MARGIN_PIXELS;
        }
        return messagePosition;
    }

    private float normalizeVerticalMessagePosition(float messagePosition, float bound, float screenBound) {
        float halfScreenBound = screenBound * .5f;
        boolean outFromBottomBound = messagePosition - bound < -halfScreenBound;
        boolean outFromTopBound = messagePosition + bound > halfScreenBound;
        if (outFromBottomBound) {
            messagePosition = -halfScreenBound + TEXT_VERTICAL_MARGIN_PIXELS + bound;
        } else if (outFromTopBound) {
            messagePosition = halfScreenBound - bound - TEXT_VERTICAL_MARGIN_PIXELS;
        }
        return messagePosition;
    }
}
