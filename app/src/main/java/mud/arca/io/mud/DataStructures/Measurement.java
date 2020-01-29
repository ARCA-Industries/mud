package mud.arca.io.mud.DataStructures;

public class Measurement {
    private float value;
    private String unit;
    private Variable variable;

    public Measurement(float value, String unit, Variable variable) {
        this.value = value;
        this.unit = unit;
        this.variable = variable;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
