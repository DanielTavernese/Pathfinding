package ca.dantav.pathfinder.utlity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public abstract class JsonParser {

    private String fileName;

    public JsonParser(String fileName) {
        this.fileName = fileName;
    }

    public void execute() {
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("json/" + fileName));
        parse(base);
    }

    public abstract void parse(JsonValue base);

    public String getFileName() {
        return fileName;
    }
}
