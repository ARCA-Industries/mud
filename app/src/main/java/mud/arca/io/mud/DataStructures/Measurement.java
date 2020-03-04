package mud.arca.io.mud.DataStructures;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;

public class Measurement implements Serializable {
    private float value;
    private Variable variable;

    public Measurement() {
    }

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

    /**
     * Search a list of measurements for measurement with variable name varName.
     * @param measurements
     * @param varName
     * @return
     * @throws NoSuchElementException
     */
    public static Measurement searchList(Collection<Measurement> measurements, String varName) throws NoSuchElementException {
        for (Measurement m : measurements) {
            if (m.getVariable().getName().equals(varName)) {
                return m;
            }
        }
        throw new NoSuchElementException("Measurement with that name not found");
    }

    /**
     * Return a string that displays the value based on VarType.
     * e.g. If VarType=FLOAT, return "2.0"
     *      If VarType=INT, return "2"
     * @return
     */
    @Exclude
    public String getFormattedValue() {
        Variable.VarType vt = variable.getVartype();
        if (vt == Variable.VarType.FLOAT) {
            return String.format("%.3f", value);
        } else if (vt == Variable.VarType.INT) {
            return String.format("%d", Math.round(value));
        } else { // BOOL
            return String.format("%d", Math.round(value));
        }
    }

}
