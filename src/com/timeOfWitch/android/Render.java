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
import static android.opengl.GLES20.GL_ONE;
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
    private Sprite[] trees;
    private Sprite[] grass_roads;
    private Sprite[] villages;
    private Sprite[] forests_far;
    private Sprite[] forests_mid;
    private Sprite[] forests;
    private Sprite[] shades;
    private Sprite[] houses;
    private Texture tree;
    private Texture tree2;
    private Texture forest_close;
    private Texture forest_mid;
    private Texture forest_far;
    private Texture home;
    private Texture shade;
    private TextureHelper textureHelper;
    private TextureAtlas atlas;
    private Texture airHockey;
    private Texture desert;
    private Texture desert2;
    private Texture grass;
    private Texture grass2;
    private Texture village;
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
    private int roadTile = 0;
    private int grassTile = 0;

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        //glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        fps = new FPSCounter();
        textureHelper = new TextureHelper();
        scene1 = new Scene();

        atlas1 = new TextureAtlas(2048, 2048, 1);
        alise = new Texture(1250, 450, 0, 0, 1, 5, R.drawable.red_smaller);
        desert = new Texture(1000, 275,0,451, 1, 1, R.drawable.wild_road);
        desert2 = new Texture(1000, 275,0,727, 1, 1, R.drawable.wild_road_crossing_small);
        grass = new Texture(1000, 116, 0, 1003, 1, 1, R.drawable.grass_small);
        grass2 = new Texture(145, 116, 0, 1120, 1, 1, R.drawable.grass_r_small);
        village = new Texture(617, 186, 0, 1237, 1, 1, R.drawable.back_vill_small);
        home = new Texture(512, 505, 1251, 0, 1, 1, R.drawable.lodge_small);
        forest_close = new Texture(1000, 550, 1001, 506, 1, 1, R.drawable.forest_close_small);
        shade = new Texture(300, 150, 0, 1424, 1, 1, R.drawable.shades_small);
        atlas1.attachTexture(alise);
        atlas1.attachTexture(desert);
        atlas1.attachTexture(desert2);
        atlas1.attachTexture(grass);
        atlas1.attachTexture(grass2);
        atlas1.attachTexture(village);
        atlas1.attachTexture(home);
        atlas1.attachTexture(forest_close);
        atlas1.attachTexture(shade);
        atlas1.loadAtlas();

        atlas3 = new TextureAtlas(2048, 1500, 3);
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
            backg[i] = new Sprite(scene1, i * grass.width/2 -100, Initialization.realHeight *0.2f, grass.width/2, grass.height / 2, grass, camera);
        }

        back = new Sprite[4];
        for (int i=0; i<4; i++) {
            back[i] = new Sprite(scene1, i * desert.width/2, Initialization.realHeight *0.12f, desert.width/2, desert.height / 2, desert, camera);
        }

        houses = new Sprite[3];
        houses[0] = new Sprite(scene1, 1000, Initialization.realHeight*0.41f, home.width*0.7f,home.height*0.7f, home, camera);
        houses[1] = new Sprite(scene1, 2250, Initialization.realHeight*0.41f, home.width*0.7f,home.height*0.7f, home, camera);
        houses[2] = new Sprite(scene1, -1250, Initialization.realHeight*0.41f, home.width*0.7f,home.height*0.7f, home, camera);

        trees = new Sprite[11];
        trees[0] = new Sprite(scene1, 800, Initialization.realHeight*0.64f, tree.width * 0.9f, tree.height * 0.9f, tree, camera);
        trees[1] = new Sprite(scene1, 1000, Initialization.realHeight*0.62f, tree.width * 0.6f, tree.height * 0.6f, tree, camera);
        trees[2] = new Sprite(scene1, 1800, Initialization.realHeight*0.50f, tree.width * 0.4f, tree.height * 0.4f, tree2, camera);
        trees[3] = new Sprite(scene1, -2800, Initialization.realHeight*0.50f, tree.width * 0.4f, tree.height * 0.4f, tree2, camera);
        trees[4] = new Sprite(scene1, 3500, Initialization.realHeight*0.50f, tree.width * 0.4f, tree.height * 0.4f, tree2, camera);
        trees[5] = new Sprite(scene1, 600, Initialization.realHeight*0.50f, tree.width * 0.4f, tree.height * 0.4f, tree2, camera);
        trees[6] = new Sprite(scene1, -1500, Initialization.realHeight*0.50f, tree.width * 0.4f, tree.height * 0.4f, tree2, camera);
        trees[7] = new Sprite(scene1, 4600, Initialization.realHeight*0.62f, tree.width * 0.85f, tree.height * 0.85f, tree, camera);
        trees[8] = new Sprite(scene1, 4800, Initialization.realHeight*0.62f, tree.width * 0.9f, tree.height * 0.9f, tree, camera);
        trees[9] = new Sprite(scene1, 5300, Initialization.realHeight*0.59f, tree.width * 0.8f, tree.height * 0.8f, tree, camera);
        trees[10] = new Sprite(scene1, 5500, Initialization.realHeight*0.90f, tree.width * 1.6f, tree.height * 1.6f, tree, camera);

        grass_roads = new Sprite[2];
        grass_roads[0] = new Sprite(scene1, -950, Initialization.realHeight*0.2f, grass2.width / 2, grass2.height / 2, grass2, camera);
        grass_roads[1] = new Sprite(scene1, 1900, Initialization.realHeight*0.2f, grass2.width / 2, grass2.height / 2, grass2, camera);

        villages = new Sprite[2];
        villages[0] = new Sprite(scene1, 100, Initialization.realHeight*0.34f, village.width * 0.85f, village.height * 0.85f, village, camera);
        villages[1] = new Sprite(scene1, 800, Initialization.realHeight*0.34f, village.width * 0.85f, village.height * 0.85f, village, camera);

        forests_far = new Sprite[2];
        forests_far[0] = new Sprite(scene1, 1850, Initialization.realHeight*0.36f, forest_far.width , forest_far.height , forest_far, camera);
        forests_far[1] = new Sprite(scene1, 2900, Initialization.realHeight*0.39f, forest_far.width * 1.6f , forest_far.height * 1.6f, forest_far, camera);

        forests_mid = new Sprite[1];
        forests_mid[0] = new Sprite(scene1, 3500, Initialization.realHeight*0.53f, forest_mid.width , forest_mid.height , forest_mid, camera);
        //forests_mid[1] = new Sprite(scene1, 5500, Initialization.realHeight*0.53f, forest_mid.width , forest_mid.height , forest_mid, camera);

        forests = new Sprite[3];
        forests[0] = new Sprite(scene1, 4200, Initialization.realHeight*0.65f, forest_close.width , forest_close.height , forest_close, camera);
        forests[1] = new Sprite(scene1, 5600, Initialization.realHeight*0.65f, forest_close.width , forest_close.height , forest_close, camera);
        forests[2] = new Sprite(scene1, 4700, Initialization.realHeight*0.58f, forest_close.width*1.1f , forest_close.height*1.1f , forest_close, camera);

        shades = new Sprite[12];
        for (int i=0; i<12; i++) {
            shades[i] = new Sprite(scene1, 4000 + 200*i, Initialization.realHeight*0.1f, shade.width , shade.height , shade, camera);
        }

        aliseSprite = new AnimatedSprite(scene1, Initialization.realWidth / 2, Initialization.realHeight *0.27f, 100, 196, alise, camera);
        aliseSprite.setAnimate(new int[]{1}, new int[]{15});
        aliseSprite.rotate(0, 0, 1, 0);

        textureHelper.useTextureAtlas(atlas1);
        textureHelper.useTexture(greenBack);
        textureHelper.useTexture(sky);
        textureHelper.useTextureAtlas(atlas3);

        skySprites.setSpeedOfSlide(0.4f);
        trees[0].coeffForParalax(-0.9f);
        trees[7].coeffForParalax(-0.7f);
        trees[8].coeffForParalax(-1.0f);
        trees[9].coeffForParalax(-0.3f);
        trees[10].coeffForParalax(-5.4f);
        trees[1].coeffForParalax(0.2f);
        for (int i=2; i<7; i++) {
            trees[i].coeffForParalax(0.6f);
        }
        grass_roads[0].coeffForParalax(0.4f);
        grass_roads[1].coeffForParalax(0.4f);
        villages[0].coeffForParalax(2.3f);
        villages[1].coeffForParalax(2.3f);
        forests_far[0].coeffForParalax(2.3f);
        forests_far[1].coeffForParalax(1.9f);
        forests_mid[0].coeffForParalax(1.5f);
        //forests_mid[1].coeffForParalax(1.5f);
        forests[0].coeffForParalax(0.45f);
        forests[1].coeffForParalax(0.45f);
        forests[2].coeffForParalax(0.2f);
        for (int i=0; i<3; i++) {
            houses[i].coeffForParalax(0.4f);
        }
        for (int i=0; i<4; i++) {
            backg[i].coeffForParalax(0.4f);
        }

        skySprites.attachBackground();
        green.attachBackground();
        villages[0].attachSprite();
        villages[1].attachSprite();
        forests_far[0].attachSprite();
        forests_far[1].attachSprite();
        //---------------------------------------
        forests_mid[0].attachSprite();
        //forests_mid[1].attachSprite();
        for (int i=2; i<7; i++) {
            trees[i].attachSprite();
        }
        for (int i=0; i<4; i++) {
            backg[i].attachSprite();
        }
        grass_roads[0].attachSprite();
        grass_roads[1].attachSprite();
        //---------------------------------------
        for (int i=0; i<3; i++) {
            houses[i].attachSprite();
        }
        forests[0].attachSprite();
        forests[1].attachSprite();
        forests[2].attachSprite();
        trees[1].attachSprite();
        //---------------------------------------
        for (int i=0; i<4; i++) {
            back[i].attachSprite();
        }
        for (int i=0; i<12; i++) {
            shades[i].attachSprite();
        }
        aliseSprite.attachHUDAnimatedSprite();
        //---------------------------------------
        trees[0].attachSprite();
        trees[7].attachSprite();
        trees[8].attachSprite();
        trees[9].attachSprite();
        trees[10].attachSprite();
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
            if (Math.abs(aliseSprite.getXScreen()) < 5000+Initialization.realWidth/2) up[0][0] = aliseSprite.getX() + (up[0][0] - aliseSprite.getX())/(Initialization.realHeight/540f);
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
    private float speedX = 4;
    private int signMove;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        if (xMoveHeroEnd != 0) {
            if ((_goMove) && (Math.abs(aliseSprite.getXScreen() - xMoveHeroEnd) > speedX)) {
                if (Math.abs(aliseSprite.getXScreen()) > 5000+Initialization.realWidth/2) {
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
            camera.translate(camera.getCameraX() + speedX * signMove, 0);
        }
        setSequenceAndPositionForBack();
        scene1.draw();
    }

    private boolean setSequenceAndPositionForBack() {
        float toRight, toLeft;
        boolean t = false;
        toRight = (Initialization.realWidth - back[3].getXScreen());
        toLeft = (back[0].getXScreen());

        if (toLeft > speedX || toRight > speedX) {

            if (toLeft > speedX) {
                back[3].translate(back[0].getX() - back[0].getWidth(), back[3].getY());
                back = Geometry.shiftToRight(back, 1);
                roadTile--;
            }
            if (toRight > speedX) {
                back[0].translate(back[3].getX() + back[3].getWidth(), back[0].getY());
                back = Geometry.shiftToLeft(back, 1);
                roadTile++;
            }
            setTextureForBackWhenChanged(roadTile, back, desert, desert2);
            Log.d("myLogs", "road = " + roadTile);
        }

        toRight = (Initialization.realWidth - backg[3].getXScreen());
        toLeft = (backg[0].getXScreen());

        if (toLeft > speedX || toRight > speedX) {

            if (toLeft > speedX) {
                backg[3].translate(backg[0].getX() - backg[0].getWidth(), backg[3].getY());
                backg = Geometry.shiftToRight(backg, 1);
                grassTile--;
            }
            if (toRight > speedX) {
                backg[0].translate(backg[3].getX() + backg[3].getWidth(), backg[0].getY());
                backg = Geometry.shiftToLeft(backg, 1);
                grassTile++;
            }
            //setTextureForBackWhenChanged(grassTile, backg, grass, grass2);
            Log.d("myLogs", "grass = " + grassTile);
        }
        return t;

    }

    private void setTextureForBackWhenChanged(int tile, Sprite[] s, Texture texture, Texture texture2)
    {
        int crossNum = 2;
        int[] crossing = {-5, 1};
        if (signMove > 0) {
            for (int i = 0; i < crossNum; i ++) {
                if (tile == crossing[i]) {
                    s[3].changeTexture(texture2);
                }
                if (tile == crossing[i] + 4) {
                    s[3].changeTexture(texture);
                }
            }
        } else if (signMove < 0) {
            for (int i = 0; i < crossNum; i ++) {
                if (tile == crossing[i] + 3) {
                    s[0].changeTexture(texture2);
                }
                if (tile == crossing[i]-1) {
                    s[0].changeTexture(texture);
                }
            }
        }
    }
}