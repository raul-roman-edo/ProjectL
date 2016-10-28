package es.tid.pdg.gdx.core.actions;

public class Translation {
    private float moveFromX = Float.MIN_VALUE;
    private float moveFromY = Float.MIN_VALUE;
    private float moveToX;
    private float moveToY;
    private float duration;
    private float step;

    public void reset() {
        moveFromX = Float.MIN_VALUE;
        moveFromY = Float.MIN_VALUE;
        step = 0;
    }

    public float[] move(float currentX, float currentY, float stepInSeconds) {
        moveFromX = normalizeComponent(moveFromX, currentX);
        moveFromY = normalizeComponent(moveFromY, currentY);
        float progress = calculateProgress(stepInSeconds);
        float[] newPosition = calculateCurrentPosition(progress);

        return newPosition;
    }

    private float normalizeComponent(float oldComponent, float currentComponent) {
        float normalized = oldComponent;
        if (normalized == Float.MIN_VALUE) {
            normalized = currentComponent;
        }

        return normalized;
    }

    private float calculateProgress(float stepInSeconds) {
        step += stepInSeconds;
        boolean isAnimationFinished = step > duration;
        if (isAnimationFinished) {
            step = duration;
        }
        float progress = step / duration;

        return progress;
    }

    private float[] calculateCurrentPosition(float progress) {
        float[] newPosition = new float[2];
        newPosition[0] = moveFromX + (moveToX - moveFromX) * progress;
        newPosition[1] = moveFromY + (moveToY - moveFromY) * progress;
        return newPosition;
    }
}
