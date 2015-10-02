package com.timeOfWitch.android.util;

import android.util.Log;

import com.timeOfWitch.android.Render;

public class TouchHelper {
    Render render;
    public TouchHelper(Render render) {
        this.render = render;
    }
    public static float[][] down = new float[2][2];
    public void down(float[][] down, int finger){
        
        for (int i=0; i<2; i++)
            for (int j=0; j<2; j++){
                this.down[i][j] = down[i][j];
//                Log.d("myLogs", down[i][j]+ " ");
            }
        render.down(finger);
    }
    public static float[][] up = new float[2][2];
    public void up(float[][] up, int finger){
//        Log.d("myLogs", finger + " finger");
       
        for (int i=0; i<2; i++)
          for (int j=0; j<2; j++){
              this.up[i][j]=up[i][j];
//              Log.d("myLogs", up[i][j]+ " ");
          }
        render.up(finger);
    }
    public static float[][] drag = new float [2][2];
    public void drag(float[][] drag, int finger){
//        Log.d("myLogs", finger + " drag");
        
        for (int i=0; i<2; i++)
          for (int j=0; j<2; j++){
              this.drag[i][j] = drag[i][j];
//              Log.d("myLogs", drag[i][j]+ " ");
          }
        render.drag(finger);
    }
    
    
    
}
