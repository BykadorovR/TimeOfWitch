package com.timeOfWitch.android.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.timeOfWitch.android.util.TextureHelper;

public class Texture {
    public TextureAtlas atlas;
    
    public int width;
    public int height;
    public int posXInAtlas;
    public int posYInAtlas;
    public int row;
    public int column;
    public int resourceId;

    //can delete texture using this field
    private int texture;
    private int textureUnit;
    private TextureHelper textureHelper;
    private Bitmap textureBitmap;

    public Texture(int width, int height, int posXInAtlas, int posYInAtlas, int row, int column, int resourceId) {
        this.height = height;
        this.width = width;
        this.posXInAtlas = posXInAtlas;
        this.posYInAtlas = posYInAtlas;
        this.row = row;
        this.column = column;
        this.resourceId = resourceId;
    }

    public Texture(int width, int height, int row, int column, int resourceId) {
        this.height = height;
        this.width = width;
        this.row = row;
        this.column = column;
        this.resourceId = resourceId;
    }

    public void loadTexture(Context context, int textureUnit) {
        this.textureUnit = textureUnit;
        textureHelper = new TextureHelper();
        //Initialize empty bitmap with size as a texture
        textureBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //Binding canvas and textureBitmap
        Canvas canvas = new Canvas(textureBitmap);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        //loading picture from texture id
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        //draw on canvas (textureBitmap)
        canvas.drawBitmap(bitmap, 0, 0, null);
        texture = textureHelper.loadTexture(context, textureBitmap);
    }

    public int getLoadedTexture(){
        return texture;
    }
    public int getTextureUnit(){
        return textureUnit;
    }

}