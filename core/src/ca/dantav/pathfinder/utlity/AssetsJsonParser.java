package ca.dantav.pathfinder.utlity;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.JsonValue;

import ca.dantav.pathfinder.Assets;


public class AssetsJsonParser extends JsonParser {

    private Assets assets;

    public AssetsJsonParser(Assets assets) {
        super("assets.json");
        this.assets = assets;
    }

    @Override
    public void parse(JsonValue base) {
        for(JsonValue entry = base.child; entry != null; entry = entry.next) {
            String path = entry.getString("path");
            String name = entry.getString("name");


//            if (path.startsWith("fonts/")) {
//                FileHandleResolver resolver = new InternalFileHandleResolver();
//
//                assets.getAssetManager().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//
//                assets.getAssetManager().setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
//                FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
//                parameter.fontFileName = path;
//                parameter.fontParameters.size = 48;
//                assets.getAssetManager().load(path, BitmapFont.class, parameter);
            if (path.startsWith("images/")) {
                assets.getAssetManager().load(path, Texture.class);
            } else if(path.startsWith("audio/")) {
                assets.getAssetManager().load(path, Sound.class);
            }

            assets.addToNameMap(name, path);
        }
    }


}
