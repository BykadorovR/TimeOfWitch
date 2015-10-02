/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.timeOfWitch.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.timeOfWitch.android.util.TouchHelper;



public class Initialization extends Activity {
    private GLSurfaceView glSurfaceView; 
    private boolean rendererSet = false;
    public static int width;
    public static int height;
    private boolean _isFirstMove = false;
    private int actionIndexPUp=-1;
    private int actionIndexMove;
    float[][] posDown = new float[2][2];
    float[][] posUp = new float[2][2];
    float[][] posMove = new float[2][2];
    TouchHelper touchHelper;    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        width = metricsB.widthPixels;
        height = metricsB.heightPixels;
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = 
            (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
            .getDeviceConfigurationInfo();
        final boolean supportsEs2 =
            configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
                 && (Build.FINGERPRINT.startsWith("generic")
                  || Build.FINGERPRINT.startsWith("unknown")
                  || Build.MODEL.contains("google_sdk")
                  || Build.MODEL.contains("Emulator")
                  || Build.MODEL.contains("Android SDK built for x86")));
        final Render render = new Render(this);
        touchHelper = new TouchHelper(render);
        if (supportsEs2) {
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            glSurfaceView.setEGLContextClientVersion(2);            
            glSurfaceView.setRenderer(render);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                Toast.LENGTH_LONG).show();
            return;
        }
        
        glSurfaceView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                
                    int actionMask = event.getActionMasked();
                    int actionIndex = event.getActionIndex();
                    
                    final int location[] = { 0, 0 };
                    v.getLocationOnScreen(location);
                    switch (actionMask) {
                    /**/
                    case MotionEvent.ACTION_DOWN:
                        posDown[0][0] = event.getX(0) + location[0];
                        posDown[0][1] = event.getY(0) + location[1];
                        actionIndexPUp=-1;
                        touchHelper.down(new float[][]{{posDown[actionIndex][0],posDown[actionIndex][1]},{0,0}}, 0);
                        touchHelper.up(new float[][]{{0,0},{0,0}}, -1);
                        for (int i=0; i<2; i++)
                            for (int j=0; j<2; j++)//����
                                posUp[i][j]=0;//������
                    case MotionEvent.ACTION_POINTER_DOWN: // ����������� �������
                        if (event.getPointerCount()==2) { //���� 2 ������� (�� ������ ������� � ��������, ��� ��� ��� 2 �������� �� ����� 0 � 1 (������ ����)) 
                        if (actionIndex==0){ //��� (1 0 ; 1 1 ; 0 1 -popali- (1) 1) 
                        posUp[0][0]=0; //�������� up � ��������, ������� ������ ��� ��� ������������������ down
                        posUp[0][1]=0; //
                        touchHelper.up(new float[][]{{posUp[0][0], posUp[0][1]},{posUp[1][0],posUp[1][1]}}, -1); //��������� ������
                        }
                        if (actionIndex==1){ //����������
                        posUp[1][0]=0;
                        posUp[1][1]=0;
                        touchHelper.up(new float[][]{{posUp[0][0],posUp[0][1]},{posUp[1][0], posUp[1][1]}}, -1);    
                        }
                        posDown[actionIndex][0] = event.getX(actionIndex) + location[0]; //������ �������� �������� ������� � ������� �������� �������
                        posDown[actionIndex][1] = event.getY(actionIndex) + location[1];
                        if (actionIndex == 1)
                        touchHelper.down(new float[][]{{posDown[0][0], posDown[0][1]},{posDown[1][0],posDown[1][1]}}, 1);       //���������� ������
                        if (actionIndex == 0)
                            touchHelper.down(new float[][]{{posDown[0][0], posDown[0][1]},{posDown[1][0],posDown[1][1]}}, 0);       //���������� ������
                        }
                    /**/
                    break;
                    case MotionEvent.ACTION_UP: // ���������� ���������� �������
                        _isFirstMove = false;
                        touchHelper.down(new float[][]{{0,0},{0,0}}, -1);   //���� ��� ���������, �� �������� ��� down
                        touchHelper.drag(new float[][]{{0,0},{0,0}}, -1); //� drag ����
                        if (actionIndexPUp==0) touchHelper.up(new float[][]{{posUp[0][0],posUp[0][1]},{event.getX(0) + location[0], event.getY(0) + location[1]}}, 1);
                        else 
                        if (actionIndexPUp==1)  touchHelper.up(new float[][]{{event.getX(0) + location[0], event.getY(0) + location[1]},{posUp[1][0],posUp[1][1]}}, 0); 
                        else touchHelper.up(new float[][]{{event.getX(0) + location[0], event.getY(0) + location[1]},{posUp[1][0],posUp[1][1]}}, 0);
                    case MotionEvent.ACTION_POINTER_UP: // ���������� �������
                        if (event.getPointerCount()==2) {
                        
                        if (actionIndex==1){ //���� ������ ������� ����� ������ 1 - 1 0 , 1 1 , 1 (0 - index = 1) (�� ���� ����� �������� actionIndex = 1)
                            actionIndexMove = 1; //���������� action ��� ���������� ����
                            actionIndexPUp = 1;
                            touchHelper.down(new float[][]{{posDown[0][0],posDown[0][1]},{0, 0}}, -1); //�������� � down, ��� ��������� ������ ����� 
                            touchHelper.drag(new float[][]{{posMove[0][0],posMove[0][1]},{0, 0}}, -1); //�������� � drag, ��� ��������� ������ ����� 
                            posUp[1][0] = event.getX(1) + location[0]; //�������������� ������ �����
                            posUp[1][1] = event.getY(1) + location[1]; //
                        }
                        if (actionIndex==0){ //���� 1 0, 1 1 , (0 index = 0) 1
                            actionIndexMove = 0; 
                            actionIndexPUp = 0;
                            touchHelper.down(new float[][]{{0, 0},{posDown[1][0],posDown[1][1]}}, -1);
                            touchHelper.drag(new float[][]{{0, 0},{posMove[1][0],posMove[1][1]}}, -1); //�������� � drag, ��� ��������� ������ ����� 
                            posUp[0][0] = event.getX(0) + location[0];
                            posUp[0][1] = event.getY(0) + location[1];
                        }
                        touchHelper.up(new float[][]{{posUp[0][0],posUp[0][1]},{posUp[1][0],posUp[1][1]}}, actionIndex); //�������� ���������� � ������
                        }
                    break;
                    /**/
                    case MotionEvent.ACTION_MOVE: // ��������
                            if ((event.getPointerCount()==1) && (_isFirstMove==false)) { //������������ 1 ������� (move) ���� �� ������ ������ ������� ��� �� ��������� ������ �����
                                    
                                    posMove[0][0] = event.getX(0) + location[0]; //��������������
                                    posMove[0][1] = event.getY(0) + location[1];
                                    posMove[1][0] = 0;
                                    posMove[1][1] = 0;
                                    touchHelper.drag(new float[][]{{posMove[0][0], posMove[0][1]},{posMove[1][0],posMove[1][1]}}, 0); //��������
                            } 
                            if ((event.getPointerCount()==1) && (_isFirstMove==true)) { //������������ ��������� �������, ����� ���� ��� ������ � ���� ���������
                                if (actionIndexMove==1){ //���� ��������� 2 ����� (������ if (actionIndex==1) � up)
                                    posMove[0][0] = event.getX(0) + location[0]; //���������� �������� � 1 �����
                                    posMove[0][1] = event.getY(0) + location[1];
                                    touchHelper.drag(new float[][]{{posMove[0][0], posMove[0][1]},{0,0}}, 0);  //� ������ ����� ����
                                }
                                if (actionIndexMove==0){ //���� ��������� 1 �����
                                    posMove[1][0] = event.getX(0) + location[0]; //�� ���������� �� 2 �����
                                    posMove[1][1] = event.getY(0) + location[1];
                                    touchHelper.drag(new float[][]{{0,0},{posMove[1][0], posMove[1][1]}}, 1);  //� ������ ����
                                }
                            } 
                            if (event.getPointerCount()==2) { //���� ����� ����� ��������
                            _isFirstMove = true; //������ �������(move) ������ �� ��������������, ���� �� �������� ��� ������
                            posMove[0][0] = event.getX(0) + location[0];//������ �������������� ��� � ���
                            posMove[0][1] = event.getY(0) + location[1];
                            posMove[1][0] = event.getX(1) + location[0];
                            posMove[1][1] = event.getY(1) + location[1];
                            touchHelper.drag(new float[][]{{posMove[0][0], posMove[0][1]},{posMove[1][0],posMove[1][1]}}, 1);  //��������   
                            }
                    break;
                    
                    }
                    return true;                    
            }
        });
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }
}