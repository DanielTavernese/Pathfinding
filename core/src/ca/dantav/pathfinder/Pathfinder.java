package ca.dantav.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ca.dantav.pathfinder.screen.GameScreen;
import ca.dantav.pathfinder.screen.MapScreen;
import ca.dantav.pathfinder.utlity.AssetsJsonParser;

public class Pathfinder extends ApplicationAdapter {

	private GameScreen currentScreen;

	private MouseWindowQuery mouseWindowQuery;

	private EntityManager entityManager;

	private ShapeRenderer shapeRenderer;

	private Assets assets;

	public Pathfinder(MouseWindowQuery mouseWindowQuery) {
		this.mouseWindowQuery = mouseWindowQuery;
	}

	@Override
	public void create () {
		assets = new Assets();
		shapeRenderer = new ShapeRenderer();
		entityManager = new EntityManager();

		new AssetsJsonParser(assets).execute();

		assets.getAssetManager().finishLoading();

		this.currentScreen = new MapScreen(this);

		currentScreen.start();
	}

	public void switchScreen(GameScreen screen) {
		if(currentScreen != null) {
			currentScreen.dispose();
		}
		this.currentScreen = screen;
		currentScreen.start();
	}

	@Override
	public void render () {
		//Logic
		update();

		//Drawing
		draw();
	}

	private void update() {

		if(currentScreen != null) {
			currentScreen.update();
		}

		for(Entity e : getEntityManager().getEntityList()) {
			if(e != null) {
				e.update();
			}
		}
	}

	private void draw() {

		if(currentScreen != null) {
			currentScreen.render(shapeRenderer);
		}

		for(Entity e : getEntityManager().getEntityList()) {
			if(e != null) {
				e.draw(shapeRenderer, currentScreen.getStage().getCamera());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		if(currentScreen == null) {
			return;
		}
		currentScreen.getStage().getViewport().update(width, height, true);
		currentScreen.getStage().getViewport().apply(true);
		currentScreen.resize();
	}

	public void dispose() {
		shapeRenderer.dispose();
		if(currentScreen != null) {
			currentScreen.dispose();
		}
		assets.getAssetManager().dispose();
	}

	public Assets getAssets() {
		return assets;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public MouseWindowQuery getMouseWindowQuery() {
		return mouseWindowQuery;
	}
}
