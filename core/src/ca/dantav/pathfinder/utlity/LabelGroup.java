package ca.dantav.pathfinder.utlity;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

//@author JaneDoe
//Alows to rotate labels
public class LabelGroup extends Group {

    public LabelGroup(Label myLabel) {
        this.setWidth(myLabel.getWidth());
        this.setHeight(myLabel.getHeight());
        this.setOrigin(myLabel.getWidth()/2,myLabel.getHeight()/2);
        this.addActor(myLabel);
    }
}