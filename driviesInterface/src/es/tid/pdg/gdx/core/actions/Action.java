package es.tid.pdg.gdx.core.actions;

public class Action {
    private static final String EMPTY = "";

    private String actorId = EMPTY;
    private float xScreenPercentage;
    private float yScreenPercentage;
    private Talk talk;
    private Translation translation;

    public String getActorId() {
        return actorId;
    }

    public float getxScreenPercentage() {
        return xScreenPercentage;
    }

    public float getyScreenPercentage() {
        return yScreenPercentage;
    }

    public Talk getTalk() {
        return talk;
    }

    public Translation getTranslation() {
        return translation;
    }
}
