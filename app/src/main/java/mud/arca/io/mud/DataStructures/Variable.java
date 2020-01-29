package mud.arca.io.mud.DataStructures;

public class Variable {
    private String name;
    private Measurement measurement;

    public Variable(String name, Measurement measurement) {
        this.name = name;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement m) {
        this.measurement = m;
    }
}
