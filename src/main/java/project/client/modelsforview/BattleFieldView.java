package project.client.modelsforview;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import project.client.view.pooyaviewpackage.DuelView;

public class BattleFieldView extends Rectangle {
    private double upperLeftX;
    private double upperLeftY;
    private double width;
    private double height;

    public BattleFieldView() {
        super(DuelView.getStageWidth() / 4, DuelView.getStageHeight() / 8, DuelView.getStageWidth() * 2 / 3, DuelView.getStageHeight() * 3 / 4);
        this.upperLeftX = DuelView.getStageWidth() / 4;
        this.upperLeftY = DuelView.getStageHeight() / 8;
        this.width = DuelView.getStageWidth() * 2 / 3;
        this.height = DuelView.getStageHeight() * 3 / 4;
        System.out.println("upperLeftX is " + upperLeftX);
        System.out.println("upperLeftY is " + upperLeftY);
        System.out.println("width should be " + DuelView.getStageWidth() * 2 / 3);
        System.out.println("height should be " + DuelView.getStageHeight() * 3 / 4);
        System.out.println(DuelView.getStageWidth() / 4);
        System.out.println(DuelView.getStageHeight() / 8);
        this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm())));
        this.setViewOrder(1);
    }

    public double getUpperLeftX() {
        return upperLeftX;
    }

    public double getUpperLeftY() {
        return upperLeftY;
    }

    public void updateImageByThisName(String string){
        if (string.isBlank()){
            this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm())));
        } else {
            if (string.equals("Umiiruka")){
                this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_water.bmp").toExternalForm())));
            } else if (string.equals("Forest")){
                this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_mori.bmp").toExternalForm())));
            } else if (string.equals("Closed Forest")){
                this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_sougen.bmp").toExternalForm())));
            } else if (string.equals("Yami")){
                this.setFill(new ImagePattern(new Image(BattleFieldView.class.getResource("/project/battlefield/fie_yami.bmp").toExternalForm())));
            }
        }
    }
}
