package es.tid.pdg.gdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import es.tid.pdg.gdx.core.actor.Actor;
import java.util.HashMap;

public class DressingRoom {
    private String atlasName;
    private HashMap<String, Actor> actors;

    public void prepare(TextureAtlas atlas, Size screenSize) {
        for (Actor actor : actors.values()) {
            actor.prepare(atlas, screenSize);
        }
    }

    public Actor obtainActor(String actorId) {
        return actors.get(actorId);
    }
}
