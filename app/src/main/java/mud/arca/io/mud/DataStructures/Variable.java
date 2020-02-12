package mud.arca.io.mud.DataStructures;

enum VarType
{
    INT, FLOAT, BOOL

}

public class Variable {
    private String name;
    private VarType vartype;
    private float vardata;

    public Variable(String name, VarType vartype) {
        this.name = name;
        this.vartype = vartype;
    }

    public Variable(String name, String type) {
        this.name = name;
        switch(type) {
            case "Integer":
                this.vartype = VarType.INT;
                break;

            case "Floating Point":
                this.vartype = VarType.FLOAT;
                break;

            case "Boolean":
                this.vartype = VarType.BOOL;
                break;

            default:
                this.vartype = VarType.FLOAT;

        }


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VarType getVarType() {
        return vartype;
    }

    public String varTypeToString() {
        return getVarType().toString();
    }

    public void setVartype(VarType vartype) {
        this.vartype = vartype;
    }

    public void setVardata(float vardata) {this.vardata = vardata;}

    /**
     * setVardata override to accept boolean value
     * @param vardata boolean value to translate into workable data
     */
    public void setVardata(boolean vardata) {

        // verify that boolean matches with VarType enum

        // convert bool into assigned float number

    }


}
