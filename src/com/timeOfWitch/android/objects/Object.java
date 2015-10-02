package com.timeOfWitch.android.objects;

import android.content.Context;

import com.timeOfWitch.android.Initialization;
import com.timeOfWitch.android.data.Camera;
import com.timeOfWitch.android.data.Texture;
import com.timeOfWitch.android.data.TextureAtlas;
import com.timeOfWitch.android.util.ShaderHelper;
import com.timeOfWitch.android.util.TextResourceReader;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;
import static com.timeOfWitch.android.Constants.POSITION_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.STRIDE;
import static com.timeOfWitch.android.Constants.TEXTURE_COORDINATES_COMPONENT_COUNT;

public class Object {


    protected float widthN, heightN;
    protected float width, height;
    protected float transparency;
    protected boolean visibility;
    protected float scaleX, scaleY;
    protected float x, y;
    protected boolean _isHUD;
    protected int angle;
    protected int textureColumn;
    protected Texture texture;
    protected float parallax;


    protected FloatBuffer buffer;
    protected Context context;
    protected int program;
    protected int uMatrixLocation;
    protected int uTextureUnitLocation;
    protected int aPositionLocation;
    protected int aTextureCoordinatesLocation;
    protected int uTransparency;
    protected String TRANSPARENCY = "u_Transparency";
    protected String U_MATRIX = "u_Matrix";
    protected String U_TEXTURE_UNIT = "u_TextureUnit";
    protected String A_POSITION = "a_Position";
    protected String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected TextureAtlas atlas;
    protected float posXInAtlasN;
    protected float posYInAtlasN;
    protected Camera camera;

    protected float[] matrix = new float[16];
    protected float[] scaleMatrix = new float[16];
    protected float[] translateMatrix = new float[16];
    protected float[] rotateMatrix = new float[16];
    protected float[] parallaxMatrix = new float[16];


    protected Object(Context context, float x, float y, float width, float height, Texture texture, Camera camera) {
        this.x = x;
        this.y = y;
        this.parallax = 0;
        this.context = context;
        this.textureColumn = texture.column;
        this.camera = camera;
        this.transparency = 1f;
        this.visibility = true;
        this.texture = texture;
        this.heightN = height / Initialization.height;
        this.widthN = width / Initialization.width;
        setIdentityM(translateMatrix,0);
        setIdentityM(scaleMatrix, 0);
        setIdentityM(parallaxMatrix, 0);
        setIdentityM(rotateMatrix, 0);


        setStartPosition(x, y);
    }

    public void attach(int fragmentShader, int vertexShader) {
        int vertexShaderTexture = ShaderHelper.compileVertexShader(TextResourceReader.readTextFileFromResource(
                context, vertexShader));
        int fragmentShaderTexture = ShaderHelper.compileFragmentShader(TextResourceReader.readTextFileFromResource(
                context, fragmentShader));
        program = ShaderHelper.linkProgram(vertexShaderTexture, fragmentShaderTexture);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uTransparency = glGetUniformLocation(program, TRANSPARENCY);
    }

    public void setProgram() {
        glUseProgram(program);
    }

    public void draw() {
        setIdentityM(matrix, 0);
        if (!_isHUD) {
            doParallax();
            multiplyMM(matrix, 0, translateMatrix, 0, rotateMatrix, 0);
            multiplyMM(matrix, 0, scaleMatrix, 0, matrix, 0);
            multiplyMM(matrix, 0, camera.getCamera(), 0, matrix, 0);
            glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        } else {
            multiplyMM(matrix, 0, translateMatrix, 0, rotateMatrix, 0);
            multiplyMM(matrix, 0, scaleMatrix, 0, matrix, 0);
            glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        }
        glUniform1f(uTransparency, transparency);

        buffer.position(0);
        glVertexAttribPointer(aPositionLocation,  POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, buffer);
        glEnableVertexAttribArray(aPositionLocation);
        buffer.position(0);
        buffer.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, buffer);
        glEnableVertexAttribArray(aTextureCoordinatesLocation);
        buffer.position(0);
        if (visibility)
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    public void attachHUD(int fragmentShader, int vertexShader) {
        _isHUD = true;
        attach(fragmentShader, vertexShader);
    }

    public void setStartPosition(float posX, float posY) {
        translate(posX, posY);
    }

    public void translate(float posX, float posY) {

        x = posX;
        y = posY;
        float xN = posX / Initialization.width * 2 - 1;
        float yN = posY / Initialization.height * 2 - 1;
        setIdentityM(translateMatrix, 0);
        translateM(translateMatrix, 0, xN, yN, 0);

    }


    public void setScale(float x, float y) {
        scaleX = x;
        scaleY = y;
        setIdentityM(scaleMatrix, 0);
        scaleM(scaleMatrix, 0, scaleX, scaleY, 1);
    }

    public void rotate(int angle, float x, float y, float z) {
        this.angle = angle;
        setIdentityM(rotateMatrix, 0);
        setRotateM(rotateMatrix, 0, angle, x, y, z);
    }

    private  void doParallax() {
        if (parallax != 0) {
            //Log.d("myLogs", x + " x");
            if (camera.needMove()) {
                //Log.d("myLogs", x + " x");
                x = x + parallax * camera.getSignOfSpeedCamera();
                translate(x, y);
                float xN = x / Initialization.width * 2 - 1;

                setIdentityM(parallaxMatrix, 0);
                translateM(parallaxMatrix, 0, xN, 0, 0);

            }
            multiplyMM(translateMatrix, 0, matrix, 0,translateMatrix, 0);

        }
    }
    public boolean needToDisplay() {
        return Math.abs(camera.getCameraX() - x) <= camera.getCameraWidth() / 2 + getWidth() / 2;

    }

    public int getAngle() {
        return angle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return widthN * Initialization.width;
    }

    public float getHeight() {
        return heightN * Initialization.height;
    }

    public void setWidth(float width){
        this.widthN = width/Initialization.width;
    }
    public void setHeight(float height){
        this.heightN = height/Initialization.height;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public float getTransparency() {
        return transparency;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public float getXWithCamera() {
        return x + camera.getCameraXMoved();
    }

    public float getYWithCamera() {
        return y;
    }

    public void coeffForParalax(float parallax) {
        this.parallax = parallax;
    }


}
