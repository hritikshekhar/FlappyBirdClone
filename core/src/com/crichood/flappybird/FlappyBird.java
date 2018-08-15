package com.crichood.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    Texture gameover;
	SpriteBatch batch;
	Texture background;
	Texture [] birds;
    int flapstate=0;
    int gamestate=0;
    float birdY=0;
    float velocity = 0;
    float gravity=2;
    Texture topTube;
    Texture bottomTube;
    float gap=450;
    float maxTubeoffset;
    int score=0;
    int scoringTube=0;
    BitmapFont font;

    Circle birdCircle;
    //ShapeRenderer shapeRenderer;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    Random randomGenerator;
    float tubeVelocity=4;
    int numberOfTubes=4;
    float[] tubeX = new float[numberOfTubes] ;
    float[] tubeoffset = new float[numberOfTubes];

    float distanceBetweenTubes;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(10);



		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");



        topTube  = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeoffset = Gdx.graphics.getHeight()/2- gap / 2- 100;

        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;

        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();



	}
	public void startGame(){

        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

        for(int i=0;i<numberOfTubes;i++){

            tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f)*( Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth()/2-topTube.getWidth()/2 +Gdx.graphics.getWidth()+ i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i]=new Rectangle();

        }


    }

	@Override
	public void render () {

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        if(gamestate==1){

            if (tubeX[scoringTube]<Gdx.graphics.getWidth()/2) {

                score++;

                if(scoringTube<numberOfTubes-1){

                    scoringTube++;
                }
                else {

                    scoringTube=0;
                }
            }

            if(Gdx.input.justTouched()){

                velocity = -30;
            }
            for (int i=0;i<numberOfTubes;i++){

                if(tubeX[i] < -topTube.getWidth()){

                        tubeX[i]+= numberOfTubes*distanceBetweenTubes;
                        tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f)*( Gdx.graphics.getHeight() - gap - 200);

                }else {

                        tubeX[i] = tubeX[i]-tubeVelocity;
                }

                batch.draw(topTube,tubeX[i], Gdx.graphics.getHeight()/2+ gap/2+tubeoffset[i]);
                batch.draw(bottomTube,tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight()+tubeoffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2+ gap/2+tubeoffset[i],topTube.getWidth(),topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight()+tubeoffset[i],bottomTube.getWidth(),bottomTube.getHeight());

            }



            if(birdY>0){

                velocity=velocity+gravity;
                birdY-=velocity;
            }else {

                gamestate=2;
            }


        }else if(gamestate==0) {

            if(Gdx.input.justTouched()){

                gamestate=1;
            }
        } else if(gamestate==2){

             batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);

            if(Gdx.input.justTouched()){

                gamestate=1;
                startGame();
                scoringTube=0;
                score=0;
                velocity=0;

            }

        }

	    if(flapstate==0){
	        flapstate=1;
        }else {

	        flapstate=0;
        }

        batch.draw(birds[flapstate],Gdx.graphics.getWidth()/2- birds[flapstate].getWidth()/2,birdY);

        font.draw(batch,String.valueOf(score),100,200);
	    batch.end();
	    birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);

	   // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    //shapeRenderer.setColor(Color.RED);
	    //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

	    for (int i=0;i<numberOfTubes;i++){
	       // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2+ gap/2+tubeoffset[i],topTube.getWidth(),topTube.getHeight());
	        //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight()+tubeoffset[i],bottomTube.getWidth(),bottomTube.getHeight());

            if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){

                gamestate=2;
            }


        }


	}
	

}
