package com.adityakhedekar.khedubaba.coinlegend.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinLegend extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture[] man;
	private int manState = 0;
	private int pause = 0;
	private float velocity = 0;
	private float manY = 0;
	private Random random;

	private ArrayList<Integer> coinXs = new ArrayList<>();
	private ArrayList<Integer> coinYs = new ArrayList<>();
	private ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	private Texture coin;
	private int coinCount;

	private ArrayList<Integer> bombXs = new ArrayList<>();
	private ArrayList<Integer> bombYs = new ArrayList<>();
	private ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	private Texture bomb;
	private int bombCount;

	private int score;
	private int gameState = 0;
	private BitmapFont font;

	private Texture dizzy;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight() / 2f;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		dizzy = new Texture("dizzy-1.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	private void makeCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	private void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	//To put on or draw on screen
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1){
			//GAME IS LIVE
			//BOMBS
			if (bombCount < 100){
				bombCount++;
			}
			else{
				bombCount = 0;
				makeBomb();
			}
			bombRectangles.clear();
			for (int i=0; i<bombXs.size(); i++){
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 6);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getWidth()));
			}

			//COINS
			if (coinCount < 100){
				coinCount++;
			}
			else{
				coinCount = 0;
				makeCoin();
			}
			coinRectangles.clear();
			for (int i=0; i<coinXs.size(); i++){
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 4);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getWidth()));
			}

			//character
			if (Gdx.input.justTouched()){
				velocity = -10;
			}

			if (pause < 6){
				pause++;
			}
			else{
				pause = 0;
				if (manState < 3){
					manState++;
				}
				else{
					manState = 0;
				}
			}

			float gravity = 0.29f;
			velocity += gravity;
			manY -= velocity;
			if (manY <= 0){
				manY = 0;
			}
		}
		else if (gameState == 0){
			//GAME WAITING TO START
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if (gameState == 2){
			//GAME IS OVER
			if (Gdx.input.justTouched()){
				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2f;
				score = 0; velocity = 0;
				coinXs.clear(); coinYs.clear(); coinRectangles.clear(); coinCount = 0;
				bombXs.clear(); bombYs.clear(); bombRectangles.clear(); bombCount = 0;
			}
		}

		if (gameState == 2){
			//Dizzy
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2f - man[manState].getWidth() / 2f, manY);
		}
		else{
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2f - man[manState].getWidth() / 2f, manY);
		}

		Rectangle manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2f - man[manState].getWidth() / 2f, (int) manY, man[manState].getWidth(),
				man[manState].getWidth());

		for (int i=0; i<coinRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))){
				score++;
				//to stop from colliding over and over again
				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for (int i=0; i<bombRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))){
				Gdx.app.log("Bomb: ", "collison");
				gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
