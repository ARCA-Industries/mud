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

    /**
     * Return a string that displays the value based on VarType.
     * e.g. If VarType=FLOAT, return "2.0"
     *      If VarType=INT, return "2"
     * @return
     */
    public String getFormattedValue() {
        VarType vt = variable.getVartype();
        if (vt == VarType.FLOAT) {
            return String.format("%.3f", value);
        } else if (vt == VarType.INT) {
            return String.format("%d", Math.round(value));
        }
        return "TODO";
    }

}
