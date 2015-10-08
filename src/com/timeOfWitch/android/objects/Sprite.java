package com.timeOfWitch.android.objects;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttrib1f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.timeOfWitch.android.Constants.BYTES_PER_FLOAT;
import static com.timeOfWitch.android.Constants.POSITION_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.STRIDE;
import static com.timeOfWitch.android.Constants.TEXTURE_COORDINATES_COMPONENT_COUNT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.util.Log;

import com.timeOfWitch.android.Initialization;
import com.timeOfWitch.android.R;
import com.timeOfWitch.android.data.Camera;
import com.timeOfWitch.android.data.Texture;
import com.timeOfWitch.android.data.TextureAtlas;
import com.timeOfWitch.android.util.ShaderHelper;
import com.timeOfWitch.android.util.TextResourceReader;

public class Sprite extends Object {

    public Sprite(Context context, float x, float y, float width, float height, Texture texture, Camera camera) {
        super(context, x, y, width, height, texture, camera);
        atlas = texture.atlas;
        super.width = (float) texture.width / (float) atlas.width; //512/2048
        super.height = (float) texture.height / (float) atlas.height; //512/2048
        super.posXInAtlasN = (float) texture.posXInAtlas / (float) atlas.width; // = 513/2048
        super.posYInAtlasN = (float) texture.posYInAtlas / (float) atlas.height; // = 0/2048

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
            .allocateDirect(vertex_data.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex_data);
    }

    public boolean drawIfVisible(){
        if (needToDisplay()) {
            setProgram();
            glUniform1i(uTextureUnitLocation, atlas.getTextureUnit());
            super.draw();
            return true;
        } else {
            return false;
        }
    }

    public void draw() {
        setProgram();
        glUniform1i(uTextureUnitLocation, atlas.getTextureUnit());
        super.draw();
    }

    public void attachSprite(){
        initializeBuffer();
        super.attach(R.raw.sprite_fs, R.raw.sprite_vs);
    }

    public void attachHUDSprite(){
        initializeBuffer();
        super.attachHUD(R.raw.sprite_fs, R.raw.sprite_vs);
    }

    public void changeTexture(Texture texture) {
        this.texture = texture;
        this.atlas = texture.atlas;
        this.posXInAtlasN = (float) texture.posXInAtlas/(float) atlas.width; // = 513/2048
        this.posYInAtlasN = (float) texture.posYInAtlas/(float) atlas.height; // = 0/2048
        initializeBuffer();
    }

    public Texture getTexture() {
        return texture;
    }
 }
