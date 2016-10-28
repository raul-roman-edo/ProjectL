package es.tid.pdg.gdx.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import es.tid.pdg.gdx.core.actions.Action;
import es.tid.pdg.gdx.core.actions.ActionGroup;
import es.tid.pdg.gdx.core.actor.Actor;
import es.tid.pdg.gdx.core.DressingRoom;
import es.tid.pdg.gdx.core.Size;
import es.tid.pdg.gdx.core.actions.Talk;
import es.tid.pdg.gdx.core.actions.Translation;
import java.util.ArrayList;

public class Coordinator {
    private MainGDXView view;
    private DressingRoom dressingRoom;
    private ArrayList<ActionGroup> actions;
    private int step = 0;
    private boolean isReady = false;
    private boolean stop = false;

    public void prepare(MainGDXView view, TextureAtlas atlas, Size screenSize) {
        this.view = view;
        dressingRoom.prepare(atlas, screenSize);
        isReady = true;
    }

    public void render(SpriteBatch batch, BitmapFont font, Size screenSize, float deltaTime) {
        if (stop) return;
        int lastGroupPosition = actions.size() - 1;
        boolean isFinished = step > lastGroupPosition;
        if (isFinished) {
            doFinishedFlow();
        } else {
            doNormalFlow(batch, font, screenSize, deltaTime);
        }
    }

    public boolean isReady() {
        return isReady;
    }

    private void doFinishedFlow() {
        stop = true;
        view.close();
    }

    private void doNormalFlow(SpriteBatch batch, BitmapFont font, Size screenSize, float deltaTime) {
        ActionGroup actionGroup = actions.get(step);
        if (actionGroup.isFinished()) {
            playNextGroup(batch, font, screenSize, deltaTime);
        } else {
            playActions(batch, font, screenSize, deltaTime, actionGroup);
        }
    }

    private void playNextGroup(SpriteBatch batch, BitmapFont font, Size screenSize, float deltaTime) {
        step++;
        render(batch, font, screenSize, deltaTime);
    }

    private void playActions(SpriteBatch batch, BitmapFont font, Size screenSize, float deltaTime,
            ActionGroup actionGroup) {
        boolean isInitial = actionGroup.isInitial();
        actionGroup.addProgress(deltaTime);
        for (Action action : actionGroup.getActions()) {
            performAction(batch, font, screenSize, deltaTime, isInitial, action);
        }
    }

    private void performAction(SpriteBatch batch, BitmapFont font, Size screenSize, float deltaTime, boolean isInitial,
            Action action) {
        float[] position = calculateCurrentPosition(deltaTime, isInitial, action);
        Actor actor = dressingRoom.obtainActor(action.getActorId());
        drawActor(batch, font, actor, position, screenSize, isInitial, deltaTime, action.getTalk());
    }

    private float[] calculateCurrentPosition(float deltaTime, boolean isInitial, Action action) {
        Translation translation = action.getTranslation();
        float[] position = new float[] {
                action.getxScreenPercentage(), action.getyScreenPercentage()
        };
        if (translation == null) return position;
        if (isInitial) {
            translation.reset();
        }
        position = translation.move(position[0], position[1], deltaTime);
        return position;
    }

    private void drawActor(SpriteBatch batch, BitmapFont font, Actor actor, float[] position, Size screenSize,
            boolean isInitial, float deltaTime, Talk talk) {
        if (isInitial) {
            actor.reset();
            if (talk != null) {
                talk.reset();
            }
        }
        float actorPositionX = position[0] * screenSize.getWidth() / 2;
        float actorPositionY = position[1] * screenSize.getHeight() / 2;
        actor.renderBody(batch, actorPositionX, actorPositionY, deltaTime);
        actor.talk(batch, font, actorPositionX, actorPositionY, screenSize, talk, deltaTime);
    }
}
