package es.tid.pdg.gdx.core.actions;

public class Message {
    private static final String EMPTY = "";
    private String text = EMPTY;
    private float duration;
    private float current;

    public void reset() {
        current = 0;
    }

    public boolean isFinished() {
        if (current == 0) return false;
        return current > duration;
    }

    public String getText(float timeDelta) {
        if (duration > 0) {
            current += timeDelta;
        }
        return text;
    }
}
