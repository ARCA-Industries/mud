package mud.arca.io.mud.DataStructures;

enum VarType
{
    INT, FLOAT, BLUE;
}

public class Variable {
    private String name;
    private Measurement measurement;
    private VarType vartype;

    public Variable(String name, Measurement measurement, VarType vartype) {
        this.name = name;
        this.measurement = measurement;
        this.vartype = vartype;
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

    public VarType getVartype() {
        return vartype;
    }

    public void setVartype(VarType vartype) {
        this.vartype = vartype;
    }
}
