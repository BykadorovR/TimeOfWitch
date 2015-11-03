package com.timeOfWitch.android.data;

import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.orthoM;

import android.util.Log;

import com.timeOfWitch.android.Initialization;

public class Camera {
    float[] camera;
    public float startX, startY;
    private boolean needOfMovement;
    private float signOfSpeedCamera = 0;
    private float xCamera;
    private float yCamera;
    private float diffOfCameraX = 0;

    public Camera(float posX, float posY) {
        camera = new float[16];
        needOfMovement = true;
        xCamera = posX;
        yCamera = posY;
    }

    //translateM - translate current x and y to diff so newX = x + diff; newY = y + diff; if diff is no positive translating will be to left
    public void translate(float xCamera, float yCamera) {
        if (needOfMovement) {
            diffOfCameraX = xCamera - this.xCamera;
            this.xCamera = xCamera;
            this.yCamera = yCamera;
            signOfSpeedCamera = Math.signum(diffOfCameraX);
        }
    }


    public float getSignOfSpeedCamera() {
        return signOfSpeedCamera;
    }
    //deviation from equilibrium (startX and startY)

    public void needMove(boolean needOfMovement) {
        this.needOfMovement = needOfMovement;
    }

    public boolean needMove() {
        return needOfMovement;
    }

    public float getCameraX() {
        return xCamera;
    }

    public float getCameraY() {
        return yCamera;
    }

    public float getCameraDiff() {return diffOfCameraX;}

}
