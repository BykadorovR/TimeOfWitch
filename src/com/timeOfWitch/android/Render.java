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
    private Texture tree;
    private Texture tree2;
    private Texture forest_far;
    private Texture forest_mid;
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
    private Sprite[] trees;
    private Background skySprites;
    private Background green;
    private Sprite grassSprite;
    private Scene scene;

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        fps = new FPSCounter();
        textureHelper = new TextureHelper();
        Scene scene1 = new Scene();
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
        tree = new Texture(600, 916, 0, 0, 1, 1, R.drawable.tree_d_small);
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


        camera = new Camera(Initialization.width, Initialization.height, Initialization.width/2, Initialization.height / 2);
        camera.needMove(false);


        back = new Sprite[4];
        for (int i=0; i<4; i++) {
            back[i] = new Sprite(scene1, (i-1) * desert.width/2, Initialization.height *0.12f, desert.width/2, desert.height / 2, desert, camera);
            back[i].attachSprite();
        }
        backg = new Sprite[4];
        for (int i=0; i<4; i++) {
            backg[i] = new Sprite(scene1, (i-1) * grass.width/2, Initialization.height *0.19f, grass.width/2, grass.height / 2, grass, camera);
            backg[i].attachSprite();
        }

        aliseSprite = new AnimatedSprite(scene1, Initialization.width/2, Initialization.height *0.27f, 100, 196, alise, camera);
        aliseSprite.translateGlobal(Initialization.width / 2, Initialization.height * 0.27f);
        aliseSprite.setAnimate(new int[]{1}, new int[]{15});
        aliseSprite.attachHUDAnimatedSprite();
        aliseSprite.rotate(0, 0, 1, 0);

        green = new Background(scene1, Initialization.width / 2, Initialization.height *0.34f, Initialization.width, greenBack.height, greenBack, camera);
        green.attachBackground();


        skySprites = new Background(scene1, Initialization.width / 2, Initialization.height / 2 + Initialization.height / 8, Initialization.width, Initialization.height*3/4f, sky, camera);
        skySprites.unscale();
        skySprites.setSpeedOfSlide(getSpeedForParallax(skySprites, 1));
        skySprites.attachBackground();

        homeSprite = new Sprite(scene1, 1000, Initialization.height*0.41f, home.width * 0.7f,home.height * 0.7f, home, camera);
        homeSprite.attachSprite();

        trees = new Sprite[4];
        trees[0] = new Sprite(scene1, 830, Initialization.height*0.53f, tree.width/1.5f, tree.height/1.5f, tree, camera);
        trees[1] = new Sprite(scene1, 1000, Initialization.height*0.52f, tree.width/2, tree.height/2, tree, camera);
        trees[2] = new Sprite(scene1, 700, Initialization.height*0.48f, tree.width/3, tree.height/3, tree2, camera);
        trees[3] = new Sprite(scene1, 1200, Initialization.height*0.51f, tree.width/2.5f, tree.height/2.5f, tree, camera);


        trees[0].attachSprite();
        trees[1].attachSprite();
        trees[2].attachSprite();
        trees[3].attachSprite();

        textureHelper.useTextureAtlas(atlas1);
        textureHelper.useTexture(greenBack);
        textureHelper.useTexture(sky);
        textureHelper.useTextureAtlas(atlas3);
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
            if (xMoveHeroEnd == 0) camera.needMove(true);
            xMoveHeroEnd = up[0][0] + camera.getCameraXMoved();
            if ((Math.abs(aliseSprite.getXWithCamera() - xMoveHeroEnd) < aliseSprite.getWidth() / 3)) {
                xMoveHeroEnd = xMoveHeroChk;
            }
            xMoveHeroChk = up[0][0] + camera.getCameraXMoved();
            signMove = Integer.signum((int) (xMoveHeroEnd - aliseSprite.getXWithCamera()));

            trees[0].coeffForParalax(getSpeedForParallax(trees[0]));
            trees[1].coeffForParalax(getSpeedForParallax(trees[1]));
            trees[2].coeffForParalax(getSpeedForParallax(trees[2]));
            trees[3].coeffForParalax(getSpeedForParallax(backg[0]));
            homeSprite.coeffForParalax(getSpeedForParallax(backg[0], 1));
            for (int i=0; i<4; i++) {
                backg[i].coeffForParalax(getSpeedForParallax(backg[0], 1));
            }

            if ((Math.abs(aliseSprite.getXWithCamera() - xMoveHeroEnd) > aliseSprite.getWidth() / 3)) {
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
            if ((_goMove) && (Math.abs(aliseSprite.getXWithCamera() - xMoveHeroEnd) > speedX)) {
                if (Math.abs(aliseSprite.getXWithCamera()) > 4500) {
                    green.setSpeedOfSlide(0);
                    aliseSprite.translateGlobal(aliseSprite.getX() + speedX * signMove, aliseSprite.getY());
                    if (camera.needMove() == true)
                        camera.needMove(false);
                } else {
                    green.setSpeedOfSlide(getSpeedForParallax(green,signMove));
                    if (camera.needMove() == false)
                        camera.needMove(true);
                    aliseSprite.translateGlobal(Initialization.width / 2, aliseSprite.getY());

                }
            } else if ((_goMove) && (Math.abs(aliseSprite.getXWithCamera() - xMoveHeroEnd) <= speedX)) {
                _goMove = false;
                green.setSpeedOfSlide(0);
                if (camera.needMove() == true)
                    camera.needMove(false);
;
                _setAnimateMove = false;
                aliseSprite.setAnimate(new int[]{1}, new int[]{15});
            }
        }
        Log.d("myLogs", "CAm X=" + camera.getCameraXShifted() + "init width =" + Initialization.width  + "init height =" + Initialization.height);
        //-------------------------------
        skySprites.draw();
        green.draw();
        trees[2].draw();
        //-------------------------------

        for (int i=0; i<4; i++) {
            backg[i].draw();
        }
        homeSprite.draw();
        trees[3].draw();
        trees[1].draw();
        //-------------------------------
        for (int i=0; i<4; i++) {
            back[i].draw();
        }

        if (setSequenceAndPositionForBack()) {
            setTextureForBackWhenChanged(1, desert2);
        }

        aliseSprite.draw();
        //-------------------------------
        trees[0].draw();


    }

    private boolean setSequenceAndPositionForBack() {
        float toRight, toLeft;
        boolean t = false;
        toRight = Math.abs (camera.getCameraXShifted() - back[3].getX() + back[3].getWidth());
        toLeft = Math.abs (camera.getCameraXShifted() - back[0].getX() - back[0].getWidth()/2);
        //Log.d("myLogs", " cam X " + camera.getCameraX() + " bak0 X " + back[0].getX() + " bak3 X " + back[3].getX());
        if (toLeft < speedX || toRight < speedX) {

            if (toLeft < 2*speedX) {
                back[3].translate(back[0].getX() - back[0].getWidth(), back[3].getY());
                back = Geometry.shiftToRight(back, 1);
            }
            if (toRight < 2*speedX) {
                back[0].translate(back[3].getX() + back[3].getWidth(), back[0].getY());
                back = Geometry.shiftToLeft(back, 1);
            }
            t = true;
        }

        toRight = Math.abs (camera.getCameraXShifted() - backg[3].getX() + backg[3].getWidth());
        toLeft = Math.abs (camera.getCameraXShifted() - backg[0].getX() - backg[0].getWidth()/2);

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

    public float getSpeedForParallax(Object object) {

        return (-aliseSprite.getY()*540/Initialization.height+aliseSprite.getHeight()/2 +
                object.getY()*540/Initialization.height-object.getHeight()/2)/100f;
    }
    public float getSpeedForParallax(Object object, int direction) {

        return direction*(-aliseSprite.getY()*540/Initialization.height+aliseSprite.getHeight()/2 +
                object.getY()*540/Initialization.height-object.getHeight()/2)/100f;
    }




}