package project.model.modelsforview;

import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.General.RowOfCardLocation;

public class ViewLittleInformation {
    private RowOfCardLocation rowOfCardLocation;
    private boolean isFirstPlayerChoosing;

    public ViewLittleInformation(RowOfCardLocation rowOfCardLocation, boolean isFirstPlayerChoosing) {
        this.rowOfCardLocation = rowOfCardLocation;
        this.isFirstPlayerChoosing = isFirstPlayerChoosing;
    }

    public RowOfCardLocation getRowOfCardLocation() {
        return rowOfCardLocation;
    }

    public boolean isFirstPlayerChoosing() {
        return isFirstPlayerChoosing;
    }

    public void setRowOfCardLocation(RowOfCardLocation rowOfCardLocation) {
        this.rowOfCardLocation = rowOfCardLocation;
    }

    public void setFirstPlayerChoosing(boolean firstPlayerChoosing) {
        isFirstPlayerChoosing = firstPlayerChoosing;
    }
}
