public class Variable {
    private String name;
    private String datatype;
    private int scope;

    public Variable(String name, String datatype, int scope) {
        this.name = name;
        this.datatype = datatype;
        this.scope = scope;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getName() {
        return name;
    }

    public String getDatatype() {
        return datatype;
    }

    public int getScope() {
        return scope;
    }

    // might add sorting here
}
