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

public class AnimatedSprite extends Object {


    private float adjustX;
    private float adjustY;
    private int currentAnimateTile;
    private float widthTile;
    private float heightTile;
    private float[] tileX;
    private float[] tileY;
    private int[] latency;
    private int[] latencyInit;
    private int countOfAnimateTiles;
    private boolean _setAnimate;


    private int uAdjustX;
    private int uAdjustY;
    private String U_ADJUST_X = "u_AdjustX";
    private String U_ADJUST_Y = "u_AdjustY";


    public AnimatedSprite(Context context, float x, float y, float width, float height, Texture texture, Camera camera) {
        super(context, x, y, width, height, texture, camera);
        atlas = texture.atlas;
        super.width = (float) texture.width / (float) atlas.width; //512/2048
        super.height = (float) texture.height / (float) atlas.height; //512/2048
        super.posXInAtlasN = (float) texture.posXInAtlas / (float) atlas.width; // = 513/2048
        super.posYInAtlasN = (float) texture.posYInAtlas / (float) atlas.height; // = 0/2048
        this.widthTile = super.width / (float) texture.column;
        this.heightTile = super.height / (float) texture.row;
    }

    private void initializeBuffer() {
        float[] vertex_data = {
                // Order of coordinates: X, Y, S, T
                // Triangle Strip
                -widthN, -heightN, posXInAtlasN, posYInAtlasN + heightTile,
                -widthN, heightN, posXInAtlasN, posYInAtlasN,
                widthN, -heightN, posXInAtlasN + widthTile, posYInAtlasN + heightTile,
                widthN, heightN, posXInAtlasN + widthTile, posYInAtlasN};
        buffer = ByteBuffer.allocateDirect(vertex_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertex_data);
    }

    public void attachAnimatedSprite() {
        initializeBuffer();
        super.attach(R.raw.animated_sprite_fs, R.raw.animated_sprite_vs);
        uAdjustX = glGetUniformLocation(program, U_ADJUST_X);
        uAdjustY = glGetUniformLocation(program, U_ADJUST_Y);

    }

    public void attachHUDAnimatedSprite() {
        initializeBuffer();
        super.attachHUD(R.raw.animated_sprite_fs, R.raw.animated_sprite_vs);
        uAdjustX = glGetUniformLocation(program, U_ADJUST_X);
        uAdjustY = glGetUniformLocation(program, U_ADJUST_Y);
    }


    public void draw() {
        setProgram();
        glUniform1i(uTextureUnitLocation, atlas.getTextureUnit());
        if ((latency[currentAnimateTile] != latencyInit[currentAnimateTile]) && (latency[currentAnimateTile] != 0)) {
            latency[currentAnimateTile]--;
        }

        if (latency[currentAnimateTile] == 0) {
            currentAnimateTile++;
            adjustX = 0;
            adjustY = 0;

        }

        if (currentAnimateTile == countOfAnimateTiles) {
            currentAnimateTile = 0;
            for (int i = 0; i < latency.length; i++)
                latency[i] = latencyInit[i];
        }
//java.lang.ArrayIndexOutOfBoundsException: length=1; index=1 at com.timeOfWitch.android.objects.AnimatedSprite.draw(AnimatedSprite.java:109)
        if (latency[currentAnimateTile] == latencyInit[currentAnimateTile]) {
            adjustX += tileX[currentAnimateTile];
            adjustY += tileY[currentAnimateTile];
            latency[currentAnimateTile]--;
        }

        glUniform1f(uAdjustX, adjustX);
        glUniform1f(uAdjustY, adjustY);

        super.draw();
    }


    public void setAnimate(int[] tiles, int[] latency) {
        _setAnimate = true;
        int row;
        row = 0;
        int column;
        adjustX = 0;
        adjustY = 0;
        currentAnimateTile = 0;
        countOfAnimateTiles = tiles.length;
        tileX = new float[tiles.length];
        tileY = new float[tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] <= textureColumn) {
                row = 0;
                column = tiles[i] - 1;
            } else {
                row = (int) Math.ceil((double) tiles[i] / textureColumn) - 1;
                column = tiles[i] % textureColumn - 1;
            }
            tileX[i] = column * widthTile;
            tileY[i] = row * heightTile;
        }
        this.latency = new int[latency.length];
        this.latencyInit = new int[latency.length];
        for (int i = 0; i < latency.length; i++) {
            this.latency[i] = latency[i];
            this.latencyInit[i] = latency[i];
        }
    }

    public int getCurrentAnimateTile() {
        return currentAnimateTile;
    }


}
