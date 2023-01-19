package com.badlogic.tankStars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;

import static com.badlogic.tankStars.MycontactListener.flag1;
import static com.badlogic.tankStars.MycontactListener.flag2;

class MycontactListener implements ContactListener{ //Strategy used here and in every class where there is implements
    public static int hitCountTank1;
    public static int hitCountTank2;
    public static int flag1=0;
    public static int flag2=0;
    @Override
    public void beginContact(Contact contact) {
//        Body a=contact.getFixtureA().getBody();
//        Body b=contact.getFixtureB().getBody();
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
//        if(a.getUserData()=="bullet" ) {
//            System.out.println("a");
////            world.destroyBody(a.getBody());
//        }
//        else if(b.getUserData()=="ammo"){
////            GameScreen.deleted=b;
////            b.getBody().destroyFixture(b);
//        }

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        if(fa.getUserData().equals("bullet") && fb.getUserData().equals("tank2Creation")) {
            System.out.println("bullet hit Tank2");
//            GameScreen.
//            fb.getBody().destroyFixture(fa);
            hitCountTank2+=1;
            flag2=1;
        }
        if(fb.getUserData().equals("bullet") && fa.getUserData().equals("tank2Creation")) {
            System.out.println("bullet hit Tank2");
//            fb.getBody().destroyFixture(fb);
            hitCountTank2+=1;
            flag2=1;
        }
        if(fa.getUserData().equals("bullet2") && fb.getUserData().equals("tank1Creation")){
            System.out.println("bullet hit Tank1");
//            fb.getBody().destroyFixture(fa);
            hitCountTank1+=1;
            flag1=1;
        }
        if(fb.getUserData().equals("bullet2") && fa.getUserData().equals("tank1Creation")){
            System.out.println("bullet hit Tank1");
//            fb.getBody().destroyFixture(fb);
            hitCountTank1+=1;
            flag1=1;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
public class GameScreen implements Screen {
    final Tank game;
    Texture tank1Image;
    private boolean paused;
    private final Texture terrain;
    private final TextureRegion terrainTexture;
    public static BitmapFont bodyFont;

    private Stage stage;

    private final Texture backgroundImage;
    private final TextureRegion backgroundTexture,healthBackground,healthForegroundimg, gasImg,gasImg2;
    Music Music;
    OrthographicCamera camera;
    private final Skin skin = new Skin(Gdx.files.internal("skin/skin/uiskin.json"));
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body tank1Creation, tank2Creation;
    public CircleShape bulletShape,bulletShape2;
    private final float TIMESTEP=1/30f;
    private final int VELOCITYITERATIONS=5, POSITIONITERATIONS=2;
    private int tank1angle = 100;
    private int tank2angle = 100;
    private Texture healthBackgroundimg, healthBar, gas,gas2;
    public static int currHealth1;
    public static int currHealth2;



    //trying to make texture
//    private SpriteBatch batch;//jere
    private Texture tex;//jere
    private Body bullet,bullet2;
    private static int fuelChancetank1=1;
    private static int fuelChancetank2=1;
    private static int moveSpeed=40;
    private static int moveSpeed2=40;
    private boolean turn1=true,turn2=true;
    private boolean nextTurn1=false, nextTurn2=false;
    private boolean flag1Boom = false,flag2Boom=false;

    public GameScreen(final Tank game) {

        this.game = game;

        // load the images for the droplet and the tank1, 64x64 pixels each
        tank1Image = new Texture(Gdx.files.internal("Tank1.png"));
        terrain = new Texture(Gdx.files.internal("Terrain.png"));
        terrainTexture = new TextureRegion(terrain,0,0,1920,1080);
        backgroundImage = new Texture(Gdx.files.internal("Broken_Buildings.png"));
        backgroundTexture = new TextureRegion(backgroundImage, 0, 0, 1920, 1080);
        healthBackgroundimg= new Texture((Gdx.files.internal("healthBarBackground.png")));
        healthBackground = new TextureRegion(healthBackgroundimg, 30,980,250,30);
        healthBar = new Texture((Gdx.files.internal("healthbar.png")));
        healthForegroundimg = new TextureRegion(healthBar, 30, 980,175,30);
        gas = new Texture(Gdx.files.internal("gas.png"));
        gasImg= new TextureRegion(gas,30,950,50,50);
        gas2 = new Texture(Gdx.files.internal("gas.png"));
        gasImg2= new TextureRegion(gas2,30,950,50,50);
        Music = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
        Music.setLooping(true);

        FreeTypeFontGenerator BodyGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter BodyParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BodyParameter.size= 24;
        bodyFont = BodyGenerator.generateFont(BodyParameter);
        bodyFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bodyFont.setColor(Color.valueOf("d3d3d3"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void render(float delta) {
        int hitcount2=MycontactListener.hitCountTank2;
        int hitcount1=MycontactListener.hitCountTank1;
        int gasSize=50;
        int gasSize2=50;
        if(flag2==1){
            world.destroyBody(bullet);
            flag2=0;
        }
        if(flag1==1){
            world.destroyBody(bullet2);
            flag1=0;
        }
        if(fuelChancetank1<=0){
            moveSpeed=0;
            gasSize=0;
            nextTurn1=true;

        }
        if(fuelChancetank2<=0){
            moveSpeed2=0;
            gasSize2=0;
            nextTurn2=true;
        }
        if(turn1==true && turn2==true){
                moveSpeed=40;
                moveSpeed2=40;
                gasSize=50;
                gasSize2=50;
        }



        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the tank
        game.batch.begin();
//        batch.begin();//jere

        currHealth1=(175-hitcount1*35);
        currHealth2=(175-hitcount2*35);

        game.batch.draw(backgroundTexture, 0,0, 1920, 1080);
        game.batch.draw(terrainTexture,0,0,1920,1080);
        game.batch.draw(healthBar, 1920-267,1000,currHealth2,25);
        game.batch.draw(healthBackgroundimg, 1920-310,980,250,80);
        game.batch.draw(healthBar, 75,1000,currHealth1,25);
        game.batch.draw(healthBackgroundimg, 30,980,250,80);
        game.batch.draw(gas,30,950,gasSize,50);
        game.batch.draw(gas2,1920-250,950,gasSize2,50);

        if(currHealth1==0){
            flag1Boom=true;
            GlyphLayout tank2Win = new GlyphLayout();
            tank2Win.setText(bodyFont,"Tank 2 Wins!");
            bodyFont.draw(game.batch,tank2Win,(1920- tank2Win.width)/2, 1080/2 + 360);
            if(flag1Boom==true){
//                world.destroyBody(tank1Creation);
                flag1Boom=false;
                currHealth1=-1;
            }

        }
        if(currHealth2==0){
            flag2Boom=true;
            GlyphLayout tank1Win = new GlyphLayout();
            tank1Win.setText(bodyFont,"Tank 1 Wins!");
            bodyFont.draw(game.batch,tank1Win,(1920- tank1Win.width)/2, 1080/2 + 360);
            if(flag2Boom==true){
//                world.destroyBody(tank2Creation);
                flag2Boom=false;
                currHealth2=-1;
            }
        }



//        game.batch.draw(tank1Image, tank1.x, tank1.y, tank1.width, tank1.height);
        game.batch.draw(tex,tank1Creation.getPosition().x-20,tank1Creation.getPosition().y-17,70,50);//jere
        if(paused){
            initButtons();
            GlyphLayout GamePaused = new GlyphLayout();
            GamePaused.setText(bodyFont,"Game Paused!");
            bodyFont.draw(game.batch,GamePaused,(1920- GamePaused.width)/2, 1080/2 + 360);
        }

        update(delta);
//        batch.end();//jere
        game.batch.end();


        debugRenderer.render(world, camera.combined);
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
        bulletShape.setPosition(new Vector2(tank1Creation.getPosition().x+25,tank1Creation.getPosition().y+5));
        bulletShape2.setPosition(new Vector2(tank2Creation.getPosition().x-25,tank2Creation.getPosition().y+5));


        if(paused){
            initButtons();

            if (Gdx.input.isKeyPressed((Keys.ESCAPE))){
                paused=false;
                try{
                    Thread.sleep(150);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        else{
            UpdateGame();
        }


    }
    public void UpdateGame(){
        if (Gdx.input.isKeyPressed((Keys.ESCAPE))) {
            paused = true;
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    public void tank1Hitbox(){

        BodyDef bodyDef_tank1 = new BodyDef();
        bodyDef_tank1.type = BodyDef.BodyType.DynamicBody;
        bodyDef_tank1.position.set(40,340);
        PolygonShape tank1Shape = new PolygonShape();
        FixtureDef tank1Fixture = new FixtureDef();

        tank1Shape.setAsBox(20,10);

        tank1Fixture.shape = tank1Shape;
        tank1Fixture.density= 100;
        tank1Fixture.friction= 0.3f;
        tank1Fixture.restitution= 0;

        tank1Creation = world.createBody(bodyDef_tank1);
        tank1Creation.createFixture(tank1Fixture).setUserData("tank1Creation");//Jere added setUserData part

        tank1Shape.dispose();

    }

    public void tank2Hitbox(){
        BodyDef bodyDef_tank2 = new BodyDef();
        bodyDef_tank2.type = BodyDef.BodyType.DynamicBody;
        bodyDef_tank2.position.set(1080,140);
        PolygonShape tank2Shape = new PolygonShape();
        FixtureDef tank2Fixture = new FixtureDef();

        tank2Shape.setAsBox(20,10);

        tank2Fixture.shape = tank2Shape;
        tank2Fixture.density= 100;
        tank2Fixture.friction= 0.3f;
        tank2Fixture.restitution= 0;

        tank2Creation = world.createBody(bodyDef_tank2);
        tank2Creation.createFixture(tank2Fixture).setUserData("tank2Creation");//Jere added setUserData part

        tank2Shape.dispose();

    }

    @Override
    public void show() {

        Result result= JUnitCore.runClasses(MyTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(new MycontactListener());
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/20);
//        batch = new SpriteBatch();        //jere
        tex = new Texture("Tank1.png");
        tank1Hitbox();
        tank2Hitbox();

        Gdx.input.setInputProcessor(stage);
        initButtons();
        // start the playback of the background music
        // when the screen is shown
        Music.play();

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        //BULLET

        final BodyDef bulletProjectile = new BodyDef();
        bulletProjectile.type = BodyDef.BodyType.DynamicBody;
        bulletShape = new CircleShape();
        bulletShape.setRadius(2.5f);
        final FixtureDef bulletFixture = new FixtureDef();
        bulletFixture.shape=bulletShape;
        bulletFixture.density=5;
        bulletFixture.restitution=1;

        final BodyDef bulletProjectile2 = new BodyDef();
        bulletProjectile2.type = BodyDef.BodyType.DynamicBody;
        bulletShape2 = new CircleShape();
        bulletShape2.setRadius(2.5f);
        final FixtureDef bulletFixture2 = new FixtureDef();
        bulletFixture2.shape=bulletShape2;
        bulletFixture2.density=5;
        bulletFixture2.restitution=1;

        //TANK


        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                if(turn1==true) {
                    if (keycode == Keys.A) {
                        tank1Creation.setLinearVelocity(-moveSpeed, 0);
                        fuelChancetank1 -= 1;
                    } else if (keycode == Keys.D) {
                        tank1Creation.setLinearVelocity(moveSpeed, 0);
                        fuelChancetank1 -= 1;
                    } else if (keycode == Keys.W) {
                        tank1angle += 100;
                    } else if (keycode == Keys.S) {
                        if (tank1angle - 100 > 100) {
                            tank1angle -= 100;
                        }
                    }
                    if (keycode == Keys.SPACE) {
                        bullet = world.createBody(bulletProjectile);
                        bullet.createFixture(bulletFixture).setUserData("bullet");//jere changes the fixture
                        bullet.setLinearVelocity(new Vector2(200, tank1angle));
                        turn1=false;

                    }
                }
                if(turn2==true) {
                    if (keycode == Keys.LEFT) {
                        tank2Creation.setLinearVelocity(-moveSpeed2, 0);
                        fuelChancetank2 -= 1;
                    } else if (keycode == Keys.RIGHT) {
                        tank2Creation.setLinearVelocity(moveSpeed2, 0);
                        fuelChancetank2 -= 1;
                    } else if (keycode == Keys.UP) {
                        tank2angle += 100;
                    } else if (keycode == Keys.DOWN) {
                        if (tank2angle - 100 > 100) {
                            tank2angle -= 100;
                        }
                    }
                    if (keycode == Keys.NUMPAD_ENTER) {
                        bullet2 = world.createBody(bulletProjectile2);
                        bullet2.createFixture(bulletFixture2).setUserData("bullet2"); //jere creates the setuserdata
                        bullet2.setLinearVelocity(new Vector2(-200, tank2angle));
                        turn2=false;
                    }
                }
                if(turn1==false && turn2==false){
                    turn1=true;
                    turn2=true;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode){
                if(keycode == Keys.A){
                    tank1Creation.setLinearVelocity(0,0);
                }
                else if (keycode==Keys.D) {
                    tank1Creation.setLinearVelocity(0,0);
                }
                else if (keycode==Keys.LEFT) {
                    tank2Creation.setLinearVelocity(0,0);
                }
                else if (keycode==Keys.RIGHT) {
                    tank2Creation.setLinearVelocity(0,0);
                }
                return true;
            }
        });


        //GROUND

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        ChainShape groundShape = new ChainShape();

        groundShape.createChain(new Vector2[] {
                new Vector2(0,320),
                new Vector2(480,320),
                new Vector2(960,120),
                new Vector2(1920,120)  });


        fixtureDef.shape =groundShape;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0;

        world.createBody(bodyDef).createFixture(fixtureDef);

        groundShape.dispose();
    }

    private void initButtons(){
        TextButton buttonSave;
        TextButton buttonQuit;
        buttonSave = new TextButton("Play",skin,"default");
        buttonSave.setSize(200,60);
        buttonSave.setPosition((1920-buttonSave.getWidth())/2, 1080/2);
        buttonSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ //This will check whenever the button is clicked and change the screen
                Gdx.app.exit();
            }
        });

        buttonQuit = new TextButton("Quit",skin,"default");
        buttonQuit.setSize(200,60);
        buttonQuit.setPosition((1920-buttonQuit.getWidth())/2, 1080/2-100);
        buttonQuit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){  //This will check whenever the button is clicked and exit the game
                Gdx.app.exit();
            }
        });

        this.stage = new Stage(new StretchViewport(1920,1080,camera));

        camera.setToOrtho(false,1920,1080);
        Gdx.input.setInputProcessor(stage);

        stage.addActor(buttonSave);
        stage.addActor(buttonQuit);

        stage.draw();
        stage.act();
    }

    private void update(float delta){
        this.stage = new Stage(new StretchViewport(1920,1080,camera));
        stage.act(delta);
//        batch.setProjectionMatrix(camera.combined);//jere

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        Music.dispose();
        tank1Image.dispose();
        terrain.dispose();
        MainMenu.bodyFont.dispose(); //Facade used here
    }

}
