package es.tid.pdg.gdx.core.actions;

import java.util.ArrayList;

public class Talk {
    private static final int INITIAL_POSITION = -1;
    private static final String EMPTY = "";
    private ArrayList<Message> messages = new ArrayList<>();
    private float actorMarginPercentage;
    private int currentStep = INITIAL_POSITION;

    public void reset() {
        currentStep = -1;
    }

    public String getMessage(float timeDelta) {
        int step = Math.max(currentStep, 0);
        String text = obtainCurrentMessage(step, timeDelta);

        return text;
    }

    public float getActorMarginPercentage() {
        return actorMarginPercentage;
    }

    private String obtainCurrentMessage(int step, float timeDelta) {
        boolean isNew = step != currentStep;
        currentStep = step;
        if (step >= messages.size()) return EMPTY;
        Message message = messages.get(step);
        if (isNew) {
            message.reset();
        }
        if (message.isFinished()) {
            return obtainCurrentMessage(step+1, timeDelta);
        }
        return message.getText(timeDelta);
    }
}
