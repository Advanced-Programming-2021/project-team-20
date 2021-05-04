package CardData.General;

public class CardLocation {
    private RowOfCardLocation rowOfCardLocation;
    private int index;

    public CardLocation(RowOfCardLocation rowOfCardLocations, int index) {
        this.rowOfCardLocation = rowOfCardLocations;
        this.index = index;
    }

    public RowOfCardLocation getRowOfCardLocation() {
        return rowOfCardLocation;
    }

    public int getIndex() {
        return index;
    }
}
