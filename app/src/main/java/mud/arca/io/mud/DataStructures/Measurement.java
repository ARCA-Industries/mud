package mud.arca.io.mud.DataStructures;

public class Measurement<T extends Number> {

    public T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
