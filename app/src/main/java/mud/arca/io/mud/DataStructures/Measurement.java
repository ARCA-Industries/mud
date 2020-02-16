package mud.arca.io.mud.DataStructures;

import java.util.Collection;
import java.util.NoSuchElementException;

public class Measurement {
    private float value;
    private Variable variable;

    public Measurement(float value, Variable variable) {
        this.value = value;
        this.variable = variable;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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
