package es.tid.pdg.gdx.core.actions;

import java.util.ArrayList;

public class ActionGroup {
    private static final float INITIAL = 0.0f;
    private ArrayList<Action> actions = new ArrayList<>();
    private float duration;
    private float current = 0.0f;

    public ArrayList<Action> getActions() {
        return actions;
    }

    public boolean isFinished() {
        if (duration == 0) {
            return false;
        }
        return current >= duration;
    }

    public boolean isInitial() {
        return current == INITIAL;
    }

    public void addProgress(float step) {
        current += step;
    }
}
