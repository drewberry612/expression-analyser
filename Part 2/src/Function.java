import java.util.ArrayList;

public class Function {
    private String name;
    private String return_type;
    private int parameters;

    public Function(String name, String return_type, int parameters) {
        this.name = name;
        this.return_type = return_type;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getReturn_type() {
        return return_type;
    }

    public int getParameters() {
        return parameters;
    }
}
