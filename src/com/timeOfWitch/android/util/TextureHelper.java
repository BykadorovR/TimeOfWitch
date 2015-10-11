package com.timeOfWitch.android.util;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;
import android.content.Context;
import android.graphics.Bitmap;

import com.timeOfWitch.android.data.Texture;
import com.timeOfWitch.android.data.TextureAtlas;

public class TextureHelper {
    private int[] textureObjectIds;
    public int loadTexture(Bitmap bitmap) {
        textureObjectIds = new int[1];
        //the first argument is the number of ids you want to generate, the second an array of ids, the 3rd the start index in the array where the generated ids are stored
        glGenTextures(1, textureObjectIds, 0);
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
    
    //One texture unit - one image (object) is active but in memory of texture unit can be a lot of images (glBindTexture - pick necessary one
    //So we can load 8 images to units and use them simultaneously in one shader or in different
    //Also we can load image to unit, draw it, load another image, draw it and .... but it is will be slower
    public void useTextureAtlas(TextureAtlas atlas){
        glActiveTexture(GL_TEXTURE0 + atlas.getTextureUnit());
        glBindTexture(GL_TEXTURE_2D, atlas.getLoadedTexture());
    }
    public void useTexture(Texture texture) {
        glActiveTexture(GL_TEXTURE0 + texture.getTextureUnit());
        glBindTexture(GL_TEXTURE_2D, texture.getLoadedTexture());
    }

    public void deleteTexture(){
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(1, textureObjectIds, 0);
    }
}
