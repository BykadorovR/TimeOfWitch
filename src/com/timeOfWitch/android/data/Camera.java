package com.timeOfWitch.android.data;

import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.orthoM;

import android.util.Log;

import com.timeOfWitch.android.Initialization;

public class Camera {
    float[] camera;
    private boolean needOfMovement;
    private float xCameraN;
    private float diff = 0;
    private float signOfSpeedCamera;
    private float xCamera;
    private float yCamera;
    private float cameraWidth;
    private float cameraHeight;

    public Camera(float cameraWidth, float cameraHeight, float posX, float posY) {
        camera = new float[16];
        needOfMovement = true;
        this.cameraWidth = cameraWidth;
        this.cameraHeight = cameraHeight;

        setCameraPosition(posX,posY);

    }

    //translateM - translate current x and y to diff so newX = x + diff; newY = y + diff; if diff is no positive translating will be to left
    public void translate(float xCamera, float yCamera) {
        if (needOfMovement) {
            float diffOfCameraX = xCamera - this.xCamera;
            signOfSpeedCamera = Math.signum(diffOfCameraX);
            diff += diffOfCameraX;
            setCameraPosition(xCamera, yCamera);
        }
    }

    private void setCameraPosition(float xCamera, float yCamera) {
        this.xCamera = xCamera;
        this.yCamera = yCamera;
        xCameraN = (xCamera / cameraWidth) * 2 - 1;
        setIdentityM(camera, 0);
        if (xCamera - this.xCamera > 0)
            translateM(camera, 0, xCameraN, 0, 0);
        else translateM(camera, 0, -xCameraN, 0, 0);
    }


    public float getSignOfSpeedCamera() {
        return signOfSpeedCamera;
    }
    //deviation from equilibrium (startX and startY)
    public float getCameraXMoved() {
        return diff;
    }

    public void needMove(boolean needOfMovement) {
        this.needOfMovement = needOfMovement;
    }

    public boolean needMove() {
        return needOfMovement;
    }

    public float[] getCamera() {
        return camera;
    }

    public float getCameraWidth() {
        return cameraWidth;
    }

    public float getCameraHeight() {
        return cameraHeight;
    }

    public float getCameraX() {
        return xCamera;
    }

    public float getCameraY() {
        return yCamera;
    }

    public void resetCameraDiff() {
        diff = 0;
    }
}
