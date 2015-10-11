package com.timeOfWitch.android.data;

import com.timeOfWitch.android.objects.Object;

import java.util.Vector;

/**
 * Created by rbykador on 09.10.2015.
 */
public class Scene {
    Vector<Object> objectsInScene;
    public Scene() {
        objectsInScene = new Vector<Object>();
    }

    public void setObject(Object object) {
        objectsInScene.add(object);
    }

    public void draw() {
        for (int i = 0; i < objectsInScene.size(); i++) {
            objectsInScene.elementAt(i).draw();
        }
    }

}
