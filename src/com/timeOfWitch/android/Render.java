package com.timeOfWitch.android;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.timeOfWitch.android.data.Camera;
import com.timeOfWitch.android.data.Scene;
import com.timeOfWitch.android.data.Texture;
import com.timeOfWitch.android.data.TextureAtlas;
import com.timeOfWitch.android.objects.AnimatedSprite;
import com.timeOfWitch.android.objects.Background;
import com.timeOfWitch.android.objects.Sprite;
import com.timeOfWitch.android.objects.Object;
import com.timeOfWitch.android.util.FPSCounter;

import com.timeOfWitch.android.util.Geometry;
import com.timeOfWitch.android.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_GENERATE_MIPMAP_HINT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static com.timeOfWitch.android.util.TouchHelper.up;


//Center of coordinates is center of sprites
//translate - set sprite to specify position (no move to some digital from current position but set x and y
//0,0 - left down corner; x - right, y - top (it is true for initialize sprite coord's in world space
//Therefore, screen of phone contains (0,0) - (width, height) coords, if you set above or below it will
//be located under screen (all this suggesuion is true for global coord's)
//some texture can contain some space around himself
//
public class Render implements Renderer {
    public static Context context;

    public Render(Context context) {
        this.context = context;
    }

    private int CAMERA_WIDTH = 1280;
    private int CAMERA_HEIGHT = 1024;
    private FPSCounter fps;
    private Background sprite;
    private Sprite anim;
    private Sprite treeS;
    private Sprite treeS2;
    private Texture tree;
    private Texture tree2;
    private Texture forest_mid;
    private Texture forest_far;
    private Texture home;
    private Sprite homeSprite;
    private TextureHelper textureHelper;
    private TextureAtlas atlas;
    private Texture airHockey;
    private Texture desert;
    private Texture desert2;
    private Texture grass;
    private Texture grass2;
    private Texture protoman;
    private TextureAtlas atlas1;
    private Texture textureatlas1;
    private Texture greenBack;
    private Camera camera;
    private Texture alise;
    private AnimatedSprite aliseSprite;
    private TextureAtlas atlas2;
    private Texture sky;
    private Sprite skySprite;
    private TextureAtlas atlas3;
    private Sprite back1;
    private Sprite back[];
    private Sprite backg[];
    private Background skySprites;
    private Background green;
    private Sprite grassSprite;
    private Scene scene1;

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        fps = new FPSCounter();
        textureHelper = new TextureHelper();
        scene1 = new Scene();

        atlas1 = new TextureAtlas(2048, 2048, 1);
        alise = new Texture(1250, 450, 0, 0, 1, 5, R.drawable.red_smaller);
        desert = new Texture(1000, 275,0,451, 1, 1, R.drawable.wild_road);
        desert2 = new Texture(1000, 275,0,727, 1, 1, R.drawable.wild_road_crossing_small);
        grass = new Texture(1000, 116, 0, 1003, 1, 1, R.drawable.grass_small);
        grass2 = new Texture(1000, 116, 0, 1120, 1, 1, R.drawable.grass_road_small);
        home = new Texture(512, 505, 1251, 0, 1, 1, R.drawable.lodge_small);
        atlas1.attachTexture(alise);
        atlas1.attachTexture(desert);
        atlas1.attachTexture(desert2);
        atlas1.attachTexture(grass);
        atlas1.attachTexture(grass2);
        atlas1.attachTexture(home);
        atlas1.loadAtlas();

        atlas3 = new TextureAtlas(2048, 1024, 3);
        tree = new Texture(600, 916, 0, 0, 1, 1, R.drawable.tree);
        tree2 = new Texture(600, 916, 601, 0, 1, 1, R.drawable.tree_small);
        forest_far = new Texture(950, 150, 0, 917, 1, 1, R.drawable.forest_far_small);
        forest_mid = new Texture(973, 370, 0, 1068, 1, 1, R.drawable.forest_mid_small);
        atlas3.attachTexture(tree);
        atlas3.attachTexture(tree2);
        atlas3.attachTexture(forest_far);
        atlas3.attachTexture(forest_mid);
        atlas3.loadAtlas();

        sky = new Texture(1000, 455, 1, 1, R.drawable.sky_small);
        greenBack = new Texture(1000, 185, 1, 1, R.drawable.back_small);
        sky.loadTexture(0);
        greenBack.loadTexture(2);

        camera = new Camera(0, Initialization.realHeight / 2);
        camera.needMove(false);

        skySprites = new Background(scene1, Initialization.realWidth / 2, Initialization.realHeight / 2 + Initialization.realHeight / 8, Initialization.realWidth, Initialization.realHeight*3/4f, sky, camera);
        green = new Background(scene1, Initialization.realWidth / 2, Initialization.realHeight *0.34f, Initialization.realWidth , Initialization.realHeight*0.3f, greenBack, camera);

        backg = new Sprite[4];
        for (int i=0; i<4; i++) {
            backg[i] = new Sprite(scene1, i * grass.width/2, Initialization.realHeight *0.2f, grass.width/2, grass.height / 2, grass, camera);
        }

        back = new Sprite[4];
        for (int i=0; i<4; i++) {
            back[i] = new Sprite(scene1, i * desert.width/2, Initialization.realHeight *0.12f, desert.width/2, desert.height / 2, desert, camera);
        }

        homeSprite = new Sprite(scene1, 1000, Initialization.realHeight*0.41f, 512*0.7f,505*0.7f, home, camera);

        treeS = new Sprite(scene1, 800, Initialization.realHeight*0.51f, tree.width/1.5f, tree.height/1.5f, tree, camera);
        treeS2 = new Sprite(scene1, 1000, Initialization.realHeight*0.53f, tree.width / 2, tree.height / 2, tree, camera);

        aliseSprite = new AnimatedSprite(scene1, Initialization.realWidth / 2, Initialization.realHeight *0.27f, 100, 196, alise, camera);
        aliseSprite.setAnimate(new int[]{1}, new int[]{15});
        aliseSprite.rotate(0, 0, 1, 0);

        textureHelper.useTextureAtlas(atlas1);
        textureHelper.useTexture(greenBack);
        textureHelper.useTexture(sky);
        textureHelper.useTextureAtlas(atlas3);

        skySprites.setSpeedOfSlide(0.4f);
        treeS.coeffForParalax(-0.7f);
        treeS2.coeffForParalax(0.2f);
        homeSprite.coeffForParalax(0.4f);
        for (int i=0; i<4; i++) {
            backg[i].coeffForParalax(0.4f);
        }

        skySprites.attachBackground();
        green.attachBackground();
        //---------------------------------------
        for (int i=0; i<4; i++) {
            backg[i].attachSprite();
        }
        //---------------------------------------
        homeSprite.attachSprite();
        treeS2.attachSprite();
        //---------------------------------------
        for (int i=0; i<4; i++) {
            back[i].attachSprite();
        }
        aliseSprite.attachHUDAnimatedSprite();
        //---------------------------------------
        treeS.attachSprite();
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);

    }


    public void drag(int finger) {
    }

    public void down(int finger) {

    }

    float xMoveHeroEnd, xMoveHeroChk;

    public void up(int finger) {


        if ((up[0][0] != 0) && (finger == 0)) {
            if (Math.abs(aliseSprite.getXScreen()) < 4500) up[0][0] = aliseSprite.getX() + (up[0][0] - aliseSprite.getX())/(Initialization.realHeight/540f);
            if (xMoveHeroEnd == 0) camera.needMove(true);
            xMoveHeroEnd = up[0][0] + camera.getCameraX();
            if ((Math.abs(aliseSprite.getXScreen() - xMoveHeroEnd) < aliseSprite.getWidth() / 3)) {
                xMoveHeroEnd = xMoveHeroChk;
            }
            xMoveHeroChk = up[0][0] + camera.getCameraX();
            signMove = Integer.signum((int) (xMoveHeroEnd - aliseSprite.getXScreen()));

            if ((Math.abs(aliseSprite.getXScreen() - xMoveHeroEnd) > aliseSprite.getWidth() / 3)) {
                _goMove = true;
                if (signMove < 0)
                    aliseSprite.rotate(180, 0, 1, 0);
                if (signMove > 0)
                    aliseSprite.rotate(0, 0, 1, 0);
                if (!_setAnimateMove) {
                    aliseSprite.setAnimate(new int[]{2, 3, 4, 5}, new int[]{15, 15, 15, 15});
                    _setAnimateMove = true;
                }
            }
        }

    }


    private boolean _goMove;
    private boolean _setAnimateMove;
    private float speedX = 3;
    private int signMove;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        if (xMoveHeroEnd != 0) {
            camera.translate(camera.getCameraX() + speedX * signMove, 0);
            if ((_goMove) && (Math.abs(aliseSprite.getXScreen() - xMoveHeroEnd) > speedX)) {
                if (Math.abs(aliseSprite.getXScreen()) > 4500) {
                    green.setSpeedOfSlide(0);
                    aliseSprite.translate(aliseSprite.getX() + speedX * signMove, aliseSprite.getY());
                    if (camera.needMove() == true)
                        camera.needMove(false);
                } else {
                    green.setSpeedOfSlide(0.6f*signMove);
                    if (camera.needMove() == false)
                        camera.needMove(true);
//                CHANGED
                    aliseSprite.translate(Initialization.realWidth / 2, aliseSprite.getY());

                }
            } else if ((_goMove) && (Math.abs(aliseSprite.getXScreen() - xMoveHeroEnd) <= speedX)) {
                _goMove = false;
                green.setSpeedOfSlide(0);
                if (camera.needMove() == true)
                    camera.needMove(false);
;
                _setAnimateMove = false;
                aliseSprite.setAnimate(new int[]{1}, new int[]{15});
            }
        }
        setSequenceAndPositionForBack();
        scene1.draw();

    }

    private boolean setSequenceAndPositionForBack() {
        float toRight, toLeft;
        boolean t = false;
        toRight = Math.abs (Initialization.realWidth - back[3].getXScreen());
        toLeft = Math.abs (back[0].getXScreen());

        if (toLeft < speedX || toRight < speedX) {

            if (toLeft < speedX) {
                back[3].translate(back[0].getX() - back[0].getWidth(), back[3].getY());
                back = Geometry.shiftToRight(back, 1);
            }
            if (toRight < speedX) {
                back[0].translate(back[3].getX() + back[3].getWidth(), back[0].getY());
                back = Geometry.shiftToLeft(back, 1);
            }
            t = true;
        }

        toRight = Math.abs (Initialization.realWidth - backg[3].getXScreen());
        toLeft = Math.abs (backg[0].getXScreen());

        if (toLeft < speedX || toRight < speedX) {

            if (toLeft < speedX) {
                backg[3].translate(backg[0].getX() - backg[0].getWidth(), backg[3].getY());
                backg = Geometry.shiftToRight(backg, 1);
            }
            if (toRight < speedX) {
                backg[0].translate(backg[3].getX() + backg[3].getWidth(), backg[0].getY());
                backg = Geometry.shiftToLeft(backg, 1);
            }
        }
        return t;

    }

    private void setTextureForBackWhenChanged(int numberOfChanging, Texture texture) {
        float startForBackChanged, endForBackChanged;
        if (numberOfChanging > 0){
            if (signMove > 0) {
                startForBackChanged = camera.startX + back[0].getWidth() / 2 + back[0].getWidth() * numberOfChanging;
                endForBackChanged = camera.startX + back[0].getWidth() / 2 + back[0].getWidth() * numberOfChanging + speedX;
                //Use back[3] because back[0] translated and current back[0] no a our back[0]
                if (camera.getCameraX() >= startForBackChanged
                        && camera.getCameraX() <= endForBackChanged) {

                    back[3].changeTexture(texture);
                } else {
                    if (back[3].getTexture().resourceId != desert.resourceId) {
                        back[3].changeTexture(desert);
                    }
                }
            } else if (signMove < 0) {
                startForBackChanged = camera.startX + back[3].getWidth() / 2 + back[3].getWidth() * (numberOfChanging + 3) - speedX;
                endForBackChanged = camera.startX + back[3].getWidth() / 2 + back[3].getWidth() * (numberOfChanging + 3);
                if (camera.getCameraX() >= startForBackChanged
                        && camera.getCameraX() <= endForBackChanged) {
                    Log.d("myLogs", "POPALI BOT");
                    back[0].changeTexture(texture);
                } else {
                    if (back[0].getTexture().resourceId != desert.resourceId) {
                        back[0].changeTexture(desert);
                    }
                }
            }
        }
    }
}