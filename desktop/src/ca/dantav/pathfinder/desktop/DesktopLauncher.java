package ca.dantav.pathfinder.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.dantav.pathfinder.Pathfinder;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 600;
		new LwjglApplication(new Pathfinder(new MouseWindowQueryDesktop()), config);
	}
}
