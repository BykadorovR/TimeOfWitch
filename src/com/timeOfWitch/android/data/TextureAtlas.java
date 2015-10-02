package com.timeOfWitch.android.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.timeOfWitch.android.util.TextureHelper;

public class TextureAtlas {
    public int width;
    public int height;
    
    private Bitmap textureAtlas;
    private Canvas canvas;
    private Context context;
    private TextureHelper textureHelper;
    private int texture;
    private int textureUnit;
    
    public TextureAtlas(Context context, int width, int height, int textureUnit) {
        this.context = context;
        this.width = width;
        this.height = height;
        this.textureUnit = textureUnit;
        textureHelper = new TextureHelper();
        textureAtlas = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(textureAtlas); 
    }
    

    public void attachTexture(Texture texture){
        texture.atlas = this;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture.resourceId, options);
        canvas.drawBitmap(bitmap, texture.posXInAtlas, texture.posYInAtlas, null); 
    }
    

    public void loadAtlas(){
        texture = textureHelper.loadTexture(context, textureAtlas);
    }
    

    public int getLoadedTexture(){
        return texture;
    }
    public int getTextureUnit(){
        return textureUnit;
    }
}
