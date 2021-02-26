package ca.dantav.pathfinder;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ca.dantav.pathfinder.Pathfinder;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Pathfinder(new MouseWindowQuery() {
			@Override
			public boolean isMouseInsideWindow() {
				return true;
			}
		}), config);
	}
}
