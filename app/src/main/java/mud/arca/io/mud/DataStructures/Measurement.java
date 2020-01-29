package mud.arca.io.mud.DataStructures;

public class Measurement<T extends Number> {
    private T value;
    private String unit;

    public Measurement(T value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
