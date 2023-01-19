package com.badlogic.tankStars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class MainMenu implements Screen, create {

    final Tank game;
    private final TextureRegion backgroundTexture;
    OrthographicCamera camera;
    public static BitmapFont headingFont;
    public static BitmapFont bodyFont;
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("skin/skin/uiskin.json"));
    private TextButton buttonPlay;
    private TextButton buttonQuit;
    private TextButton buttonResume;



    public MainMenu(final Tank game) {
        this.game = game;
        this.camera= new OrthographicCamera();
        this.stage = new Stage(new StretchViewport(1920,1080,camera));

        camera.setToOrtho(false,1920,1080);
        Gdx.input.setInputProcessor(stage);

        Texture backgroundImage = new Texture(Gdx.files.internal("Broken_Buildings.png"));

        backgroundTexture = new TextureRegion(backgroundImage, 0, 0, 1920, 1080);

        FreeTypeFontGenerator HeadingGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter HeadingParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        HeadingParameter.size= 40;
        headingFont = HeadingGenerator.generateFont(HeadingParameter);
        headingFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        headingFont.setColor(Color.valueOf("d3d3d3"));


        FreeTypeFontGenerator BodyGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter BodyParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BodyParameter.size= 24;
        bodyFont = BodyGenerator.generateFont(BodyParameter);
        bodyFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bodyFont.setColor(Color.valueOf("d3d3d3"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        initButtons();

    }

    private void update(float delta){
        stage.act(delta);
    }

    private void initButtons(){
        buttonPlay = new TextButton("Play",skin,"default");
        buttonPlay.setSize(200,60);
        buttonPlay.setPosition((1920-buttonPlay.getWidth())/2, 1080/2);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ //This will check whenever the button is clicked and change the screen
                game.setScreen(new TransitionScreen(game));
                dispose();
            }
        });

        //Create Resume Screen!!!!
        buttonResume = new TextButton("Resume",skin,"default");
        buttonResume.setSize(200,60);
        buttonResume.setPosition((1920-buttonResume.getWidth())/2, 1080/2-100);
        buttonResume.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ //This will check whenever the button is clicked and change the screen
                game.setScreen(new TransitionScreen(game));
                dispose();
            }
        });

        buttonQuit = new TextButton("Quit",skin,"default");
        buttonQuit.setSize(200,60);
        buttonQuit.setPosition((1920-buttonPlay.getWidth())/2, 1080/2-200);
        buttonQuit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){  //This will check whenever the button is clicked and exit the game
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonQuit);
        stage.addActor(buttonResume);

    }

    @Override
    public void create(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(backgroundTexture, 0,0, 1920, 1080);

        GlyphLayout GameTitle = new GlyphLayout();
        GameTitle.setText(headingFont,"Tank Stars");
        headingFont.draw(game.batch, GameTitle, (1920 - GameTitle.width)/2 , 1080/2 + 360);

        update(delta);

//        GlyphLayout Play = new GlyphLayout();
//        Play.setText(bodyFont,"Play");
//        bodyFont.draw(game.batch, Play, (1920-Play.width)/2, 1080/2);
//
//        GlyphLayout ResumeGame = new GlyphLayout();
//        ResumeGame.setText(bodyFont,"Resume Game");
//        bodyFont.draw(game.batch, ResumeGame, (1920-ResumeGame.width)/2, 1080/2-100);
//
//        GlyphLayout Exit = new GlyphLayout();
//        Exit.setText(bodyFont,"Exit");
//        bodyFont.draw(game.batch, Exit, (1920-Exit.width)/2, 1080/2-200);

//        This is text not actual buttons, buttons are implemented in the initbuttons() method

        stage.draw();
        stage.act();

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        headingFont.dispose();
        bodyFont.dispose();

    }

}
