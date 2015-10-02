package com.timeOfWitch.android.objects;

import android.content.Context;

import com.timeOfWitch.android.R;
import com.timeOfWitch.android.data.Camera;
import com.timeOfWitch.android.data.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static com.timeOfWitch.android.Constants.BYTES_PER_FLOAT;

public class Background extends Object {
    private float slide;
    private float speedOfSlide;
    private float speedOfSlideN;

    private int uSlideLocation;
    private String U_SLIDE = "u_Slide";

    public Background(Context context, float x, float y, float width, float height, Texture texture, Camera camera) {
        super(context, x, y, width, height, texture, camera);
        super.width = 1;
        super.height = 1;
        super.posXInAtlasN = 0;
        super.posYInAtlasN = 0;
    }

    private void initializeBuffer() {
        float[] vertex_data = {
                // Order of coordinates: X, Y, S, T
                // Triangle Strip
                -widthN, -heightN,   posXInAtlasN, posYInAtlasN+height,
                -widthN,  heightN,   posXInAtlasN, posYInAtlasN,
                widthN, -heightN,   posXInAtlasN+width, posYInAtlasN+height,
                widthN,  heightN,   posXInAtlasN+width, posYInAtlasN,
        };
        buffer = ByteBuffer
                .allocateDirect(vertex_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex_data);
    }


    public void attachBackground(){
        initializeBuffer();
        super.attachHUD(R.raw.background_fs, R.raw.background_vs);
        uSlideLocation = glGetUniformLocation(program, U_SLIDE);
    }

    public void draw(){
        setProgram();
        glUniform1i(uTextureUnitLocation, texture.getTextureUnit());
        glUniform1f(uSlideLocation, slide);
        if (slide==2) slide=0;
        slide+=speedOfSlideN;
        super.draw();
    }

    public void setSpeedOfSlide(float speedOfSlide){
        this.speedOfSlide = speedOfSlide;
        speedOfSlideN = this.speedOfSlide/texture.width;
    }

}
