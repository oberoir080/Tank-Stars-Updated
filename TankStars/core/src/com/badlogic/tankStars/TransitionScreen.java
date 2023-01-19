package com.badlogic.tankStars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TransitionScreen implements Screen, TransitionScreenCreate {
    final Tank game;
    private Texture background;
    OrthographicCamera camera;
    private TextButton buttonStartGame;
    private Stage stage;

    Texture tank1,tank2,tank3,tank4;
    public static BitmapFont headingFont;
    private Skin skin = new Skin(Gdx.files.internal("skin/skin/uiskin.json"));


    public TransitionScreen(final Tank game){
        this.game=game;
        this.camera= new OrthographicCamera();
        this.stage = new Stage(new StretchViewport(1920,1080,camera));

//        background= new Texture(Gdx.files.internal("")); //Add image for this screen later on
        tank1 = new Texture("Tank1.png");
        tank2 = new Texture("Tank2.png");
        tank3 = new Texture("Tank3.png");
        camera.setToOrtho(false,1920,1080);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator HeadingGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter HeadingParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        HeadingParameter.size= 40;
        headingFont = HeadingGenerator.generateFont(HeadingParameter);
        headingFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        headingFont.setColor(Color.valueOf("d3d3d3"));

    }

    private void update(float delta){
        stage.act(delta);
    }

    private void initButtons(){
        buttonStartGame = new TextButton("Start Game",skin,"default");
        buttonStartGame.setSize(200,60);
        buttonStartGame.setPosition((1920- buttonStartGame.getWidth())/2, 1080/2-400);
        buttonStartGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ //This will check whenever the button is clicked and change the screen
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        stage.addActor(buttonStartGame);
    }

    @Override
    public void create(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        initButtons();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        GlyphLayout GameTitle = new GlyphLayout();

        GameTitle.setText(headingFont,"Select your Tank");
        game.batch.draw(tank1,Gdx.graphics.getWidth()/3+30,Gdx.graphics.getHeight()/3+200,300,100);
        game.batch.draw(tank2,Gdx.graphics.getWidth()/3+30,Gdx.graphics.getHeight()/3+50,300,100);
        game.batch.draw(tank3,Gdx.graphics.getWidth()/3+30,Gdx.graphics.getHeight()/3-100,300,100);
        headingFont.draw(game.batch, GameTitle, (1920 - GameTitle.width)/2 , 1080/2 + 420);

        update(delta);

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

    }
}
