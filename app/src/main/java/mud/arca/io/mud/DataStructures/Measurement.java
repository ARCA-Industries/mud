package mud.arca.io.mud.DataStructures;

import java.util.Collection;
import java.util.NoSuchElementException;

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

    // Search a list of measurements for measurement with variable name varName
    public static Measurement searchList(Collection<Measurement> measurements, String varName) throws NoSuchElementException {
        for (Measurement m : measurements) {
            if (m.getVariable().getName().equals(varName)) {
                return m;
            }
        }
        //return null;
        throw new NoSuchElementException("Measurement with that name not found");
    }


}
