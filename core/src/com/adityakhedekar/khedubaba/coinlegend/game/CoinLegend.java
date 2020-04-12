package com.adityakhedekar.khedubaba.coinlegend.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class CoinLegend extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	float manY = 0;
	Rectangle manRectangle;
	Random random;

	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<>();
	ArrayList<Integer> bombYs = new ArrayList<>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	Texture bomb;
	int bombCount;

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
		random = new Random();
	}

	public void makeCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	//To put on or draw on screen
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
			bombXs.set(i, bombXs.get(i) - 8);
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

		if (pause < 8){
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

		velocity += gravity;
		manY -= velocity;
		if (manY <= 0){
			manY = 0;
		}
		batch.draw(man[manState], Gdx.graphics.getWidth() / 2f - man[manState].getWidth() / 2f, manY);

		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, (int)manY, man[manState].getWidth(),
				man[manState].getWidth());

		for (int i=0; i<coinRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))){
				Gdx.app.log("Coin: ", "collison");
			}
		}
		for (int i=0; i<bombRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))){
				Gdx.app.log("Bomb: ", "collison");
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
