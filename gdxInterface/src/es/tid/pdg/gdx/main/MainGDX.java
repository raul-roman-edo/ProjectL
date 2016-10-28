package es.tid.pdg.gdx.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import es.tid.pdg.gdx.core.Size;

public class MainGDX extends ApplicationAdapter implements MainGDXView {
    private static final String JSON_TEMPLATE = "{\"dressingRoom\":%1$s,\"actions\":%2$s}";
    private static final String TAG = MainGDX.class.getName();
    private static final String NAME = MainGDX.class.getSimpleName();
    private static final float BACKGROUND_RED = 0.0f;
    private static final float BACKGROUND_GREEN = 0.0f;
    private static final float BACKGROUND_BLUE = 0.0f;
    private static final int BACKGROUND_ALPHA = 1;
    private Coordinator coordinator = null;
    private Size screenSize;
    private SpriteBatch batch;
    private BitmapFont font;
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private final Object lock = new Object();
    private OnFinishedActions finishedListener = null;

    public void configure(String rawActorsJson, String rawActionsJson, OnFinishedActions listener) {
        this.finishedListener = listener;
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Gdx.app.error(TAG, NAME, e);
            }
        }
        coordinator =
                new Json().fromJson(Coordinator.class, String.format(JSON_TEMPLATE, rawActorsJson, rawActionsJson));
        initialize();
    }

    @Override
    public void create() {
        screenSize = new Size();
        screenSize.setWidth(Gdx.graphics.getWidth());
        screenSize.setHeight(Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setScale(Gdx.graphics.getDensity());
        atlas = new TextureAtlas(Gdx.files.internal("characters.atlas"));
        camera = new OrthographicCamera(screenSize.getWidth(), screenSize.getHeight());
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
        camera.update();
        paintBackgroundBase();
        draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }

    @Override
    public void close() {
        if (finishedListener == null) return;
        finishedListener.onFinished();
    }

    private void initialize() {
        coordinator.prepare(this, atlas, screenSize);
    }

    private void paintBackgroundBase() {
        Gdx.gl.glClearColor(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.combined);
        if (coordinator == null || !coordinator.isReady()) return;
        batch.begin();
        coordinator.render(batch, font, screenSize, Gdx.graphics.getDeltaTime());
        batch.end();
    }
}
