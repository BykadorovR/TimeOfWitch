package com.timeOfWitch.android;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.timeOfWitch.android.data.Camera;
import com.timeOfWitch.android.data.Texture;
import com.timeOfWitch.android.data.TextureAtlas;
import com.timeOfWitch.android.objects.AnimatedSprite;
import com.timeOfWitch.android.objects.Background;
import com.timeOfWitch.android.objects.Object;
import com.timeOfWitch.android.objects.Sprite;
import com.timeOfWitch.android.util.FPSCounter;
import com.timeOfWitch.android.util.Geometry;
import com.timeOfWitch.android.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
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
    private final Context context;

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
    private TextureHelper textureHelper;
    private TextureAtlas atlas;
    private Texture airHockey;
    private Texture desert;
    private Texture desert2;
    private Texture protoman;
    private TextureAtlas atlas1;
    private Texture textureatlas1;
    private Camera camera;
    private Texture alise;
    private AnimatedSprite aliseSprite;
    private TextureAtlas atlas2;
    private Texture sky;
    private Sprite skySprite;
    private TextureAtlas atlas3;
    private Sprite back1;
    private Sprite back[];

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        fps = new FPSCounter();
        textureHelper = new TextureHelper();

        atlas1 = new TextureAtlas(context, 2048, 1024, 1);


        alise = new Texture(1250, 450, 0, 0, 1, 5, R.drawable.red_smaller);
        atlas1.attachTexture(alise);


        desert = new Texture(1000, 275,0,451, 1, 1, R.drawable.wild_road);
        desert2 = new Texture(1000, 275,0,727, 1, 1, R.drawable.wild_road_crossing_small);
        atlas1.attachTexture(desert);
        atlas1.attachTexture(desert2);
        atlas1.loadAtlas();

        atlas3 = new TextureAtlas(context, 2048, 1024, 3);
        sky = new Texture(1280, 800, 0, 0, 1, 1, R.drawable.background);
        tree = new Texture(250, 250, 1281, 0, 1, 1, R.drawable.tree3);
        tree2 = new Texture(234, 250, 1600, 0, 1, 1, R.drawable.tree1);

        atlas3.attachTexture(sky);
        atlas3.attachTexture(tree);
        atlas3.attachTexture(tree2);
        atlas3.loadAtlas();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        camera = new Camera(Initialization.width, Initialization.height, Initialization.width / 2, Initialization.height / 2);
        camera.needMove(false);
        back = new Sprite[4];
        for (int i=0; i<=3; i++) {
            back[i] = new Sprite(context, i * Initialization.width/2, 0, Initialization.width / 2, desert.height / 2, desert, camera);
            back[i].translate(back[i].getX(), back[i].getY() + back[i].getHeight() / 2);
            back[i].attachSprite();

        }
        skySprite = new Sprite(context, Initialization.width / 2, Initialization.height / 2, Initialization.width, Initialization.height, sky, camera);
        skySprite.attachHUDSprite();

        aliseSprite = new AnimatedSprite(context, Initialization.width / 2, back[0].getY() + back[0].getHeight() / 2, 100, 196, alise, camera);
        aliseSprite.setAnimate(new int[]{1}, new int[]{15});
        aliseSprite.attachHUDAnimatedSprite();
        aliseSprite.rotate(0, 0, 1, 0);

        treeS = new Sprite(context, Initialization.width / 3, back[0].getY() - back[0].getHeight() / 2, 350, 350, tree, camera);


        treeS2 = new Sprite(context, Initialization.width, back[0].getY(), 350, 350, tree, camera);


        treeS.translate(treeS.getX(), treeS.getY() + treeS.getHeight() / 2);
        treeS2.translate(treeS2.getX(), treeS2.getY() + treeS2.getHeight() / 2);
        treeS.coeffForParalax(getSpeedForParallax(treeS));
        treeS2.coeffForParalax(getSpeedForParallax(treeS2));
        treeS.attachSprite();
        treeS2.attachSprite();


        textureHelper.useTextureAtlas(atlas1);

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
                    aliseSprite.translate(aliseSprite.getX() + speedX * signMove, aliseSprite.getY());
                    if (camera.needMove() == true)
                        camera.needMove(false);
                } else {
                    if (camera.needMove() == false)
                        camera.needMove(true);
//                CHANGED
                    aliseSprite.translate(Initialization.width / 2, aliseSprite.getY());

                }
            } else if ((_goMove) && (Math.abs(aliseSprite.getXWithCamera() - xMoveHeroEnd) <= speedX)) {
                _goMove = false;
                if (camera.needMove() == true)
                    camera.needMove(false);
                _setAnimateMove = false;
                aliseSprite.setAnimate(new int[]{1}, new int[]{15});
            }
        }
        skySprite.draw();
        treeS2.drawIfVisible();
        for (int i=0; i<4; i++) {
            back[i].drawIfVisible();
        }

        setSequenceAndPositionForBack();

        aliseSprite.draw();
        treeS.drawIfVisible();


    }

    private int countOfTextures = 0;
    private void setSequenceAndPositionForBack() {
        Log.d("myLogs", countOfTextures + "");
        if (Math.abs (camera.getCameraX()  - back[0].getX() - back[0].getWidth()/2) < speedX) {
            countOfTextures--;
            if (countOfTextures == 2) {
                back[3].changeTexture(desert2);
            } else {
                if (back[3].getTexture().resourceId == desert2.resourceId) {
                    back[3].changeTexture(desert);
                }
            }

            back[3].translate(back[0].getX() - back[0].getWidth(), back[3].getY());
            back = Geometry.shiftToRight(back, 1);
        }
        if (Math.abs (camera.getCameraX()  - back[3].getX() + back[3].getWidth()/2) < speedX) {
            countOfTextures++;
            if (countOfTextures == 2) {
                back[0].changeTexture(desert2);
            } else {
                if (back[0].getTexture().resourceId == desert2.resourceId) {
                    back[0].changeTexture(desert);
                }
            }

            back[0].translate(back[3].getX() + back[3].getWidth(), back[0].getY());
            back = Geometry.shiftToLeft(back, 1);
        }

    }

    public float getSpeedForParallax(Object object) {

        return 2*(-aliseSprite.getY()+aliseSprite.getHeight()/2 + object.getY()-object.getHeight()/2) / Initialization.height;
    }




}