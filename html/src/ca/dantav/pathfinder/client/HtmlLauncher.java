package ca.dantav.pathfinder.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import ca.dantav.pathfinder.MouseWindowQuery;
import ca.dantav.pathfinder.Pathfinder;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                //return new GwtApplicationConfiguration(true);
                // Fixed size application:
                return new GwtApplicationConfiguration(960, 600);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Pathfinder(new MouseWindowQuery() {
                        @Override
                        public boolean isMouseInsideWindow() {
                                return true;
                        }
                });
        }
}