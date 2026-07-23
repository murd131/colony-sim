package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraHandler extends InputAdapter {
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SelectionHandler selectionHandler;

    private final Vector3 lastMousePos = new Vector3();
    private boolean dragging = false;

    private float minZoom = 0.01f;
    private float maxZoom = 10f;
    private final float zoomSpeed = 0.05f;

    private float worldWidth = -1;
    private float worldHeight = -1;

    public CameraHandler(OrthographicCamera camera, Viewport viewport, SelectionHandler selectionHandler) {
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.selectionHandler = selectionHandler;
    }
    public void resize(int width, int height){
        viewport.update(width,height);
    }
    public void update(){
        camera.update();
    }
    public void setWorldBounds(float worldWidth, float worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }
    public void setZoomLimit(){
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("CameraController", "touchDown button=" + button);
        if (button == Input.Buttons.RIGHT || button == Input.Buttons.MIDDLE) {
            lastMousePos.set(screenX, screenY, 0);
            dragging = true;
            return true;
        }
        if (button == Input.Buttons.LEFT) {
            selectionHandler.onClick(screenX, screenY);
            Gdx.app.log("CameraController", "Selected cell:" +selectionHandler.getSelectedTile());

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        dragging = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;

        float deltaX = (screenX - lastMousePos.x) * camera.zoom;
        float deltaY = (screenY - lastMousePos.y) * camera.zoom;

        camera.position.add(-deltaX, deltaY, 0);
        //clampToWorldBounds();

        lastMousePos.set(screenX, screenY, 0);
        return true;
    }
    public float getZoom(){
        return camera.zoom;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * zoomSpeed;
        camera.zoom = MathUtils.clamp(camera.zoom, minZoom, maxZoom);
        //clampToWorldBounds();
        return true;
    }

    private void clampToWorldBounds() {
        if (worldWidth < 0 || worldHeight < 0) return;

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        float minX = effectiveViewportWidth / 2f;
        float maxX = worldWidth - effectiveViewportWidth / 2f;
        float minY = effectiveViewportHeight / 2f;
        float maxY = worldHeight - effectiveViewportHeight / 2f;

        if (maxX < minX) {
            camera.position.x = worldWidth / 2f;
        } else {
            camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        }

        if (maxY < minY) {
            camera.position.y = worldHeight / 2f;
        } else {
            camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        }
    }
}

