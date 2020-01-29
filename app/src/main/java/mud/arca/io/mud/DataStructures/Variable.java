package mud.arca.io.mud.DataStructures;

enum VarType
{
    INT, FLOAT, BLUE;
}

public class Variable {
    private String name;
    private VarType vartype;

    public Variable(String name, VarType vartype) {
        this.name = name;
        this.vartype = vartype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VarType getVartype() {
        return vartype;
    }

    public void setVartype(VarType vartype) {
        this.vartype = vartype;
    }
}
