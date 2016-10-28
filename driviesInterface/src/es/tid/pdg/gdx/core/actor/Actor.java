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

    public void renderBody(SpriteBatch batch, float x, float y, float stepTimeInSeconds) {
        for (BodyPart member : members) {
            member.render(batch, x, y, size, stepTimeInSeconds);
        }
    }

    public void talk(SpriteBatch batch, BitmapFont font, float actorPositionX, float actorPositionY, Size screenSize,
            Talk talk, float stepTimeInSeconds) {
        if (talk == null) return;
        String message = talk.getMessage(stepTimeInSeconds);
        if (message.isEmpty()) return;
        font.setColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), ALPHA_FULL);
        Point2D position = calculatePosition(font, actorPositionX, actorPositionY, screenSize, talk, message);
        font.drawMultiLine(batch, message, position.getX(), position.getY());
    }

    private Point2D calculatePosition(BitmapFont font, float actorPositionX, float actorPositionY, Size screenSize,
            Talk talk, String message) {
        Point2D position = new Point2D();
        BitmapFont.TextBounds bounds = font.getMultiLineBounds(message);
        position.setX(actorPositionX - bounds.width * .5f);
        position.setX(normalizeMessagePosition(position.getX(), bounds.width, screenSize.getWidth()));
        position.setY(actorPositionY + talk.getActorMarginPercentage() * size.getHeight() * .5f + bounds.height);
        position.setY(normalizeMessagePosition(position.getY(), bounds.height, screenSize.getHeight()));
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
}
