import java.util.ArrayList;
import java.util.Objects;

public class PythonVisitor extends CompilerBaseVisitor<String> {
    private ArrayList<String> strings; // Holds strings that are written to a python file
    private String error; // Holds the error string for output in MainParser

    private int indent; // Measures the amount of indent a current block has
    private boolean insideFunction; // Helps to check that returns are only used in functions
    private ArrayList<Variable> variables; // holds all variables that are available in the current scope
    private ArrayList<Function> functions; // holds all defined functions
    private String currentFunct; // holds the current function for access in some visit() functions

    public ArrayList<String> getStrings() {
        return strings;
    }

    public String getError() {
        return error;
    }

    public String checkError(String string) {
        if (Objects.equals(string, "ERROR")) {
            return "ERROR";
        }
        return string;
    }

    @Override
    public String visitStart(CompilerParser.StartContext ctx) {
        if (ctx.prog() == null) {
            System.out.println("File is empty!");
            return "ERROR";
        }
        // if the file isn't empty traverse the parse tree
        else {
            strings = new ArrayList<>();
            error = "";
            functions = new ArrayList<>();
            variables = new ArrayList<>();
            indent = 0;
            insideFunction = false;
            return visitProg(ctx.prog());
        }
    }

    @Override
    public String visitProg(CompilerParser.ProgContext ctx) {
        String valid = null;
        // this is recursion of prog so that the tree is evaluated from the bottom up
        if (!(ctx.prog() == null)) {
            valid = visitProg(ctx.prog());
        }

        // these counts are used when a prog has multiple lines or function_defs
        int lineCount = 0;
        int functionDefCount = 0;
        String string;
        // for every child this prog has
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.children.get(i) instanceof CompilerParser.LineContext) {
                string = visitLine(ctx.line().get(lineCount));
                // if an error is found it is passed back to the parent recursively until visitStart() returns "ERROR"
                // this block appears in nearly all other functions
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                strings.add(string);
                lineCount++;
            }
            if (ctx.children.get(i) instanceof CompilerParser.Function_defContext) {
                string = visitFunction_def(ctx.function_def().get(functionDefCount));
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                strings.add(string);
                functionDefCount++;
            }
        }

        // return null when there are no errors
        return valid;
    }

    @Override
    public String visitFunction_def(CompilerParser.Function_defContext ctx) {
        // Checks that function is not already defined
        for (Function f: functions) {
            if (Objects.equals(f.getName(), ctx.FUNCT().getText())) {
                error = "Function is already defined";
                return "ERROR";
            }
        }
        // checks for the correct number of parameters
        int params = 0;
        StringBuilder p = new StringBuilder();
        for (int i=3; i<ctx.children.size()-4; i++) {
            if (i%2 == 1) {
                if (i != 3) {
                    p.append(",");
                }
                p.append(ctx.children.get(i).getText());
                params++;
            }
        }
        functions.add(new Function(ctx.FUNCT().getText(), ctx.RETURN_TYPE().getText(), params));
        String define = "def " + ctx.FUNCT().getText() + p + "):\n";
        indent++;
        insideFunction = true;
        String body = visitFunction_body(ctx.function_body());
        if (Objects.equals(body, "ERROR")) {
            return "ERROR";
        }
        insideFunction = false;

        // removes variables that cannot be accessed outside the function body
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return define + body;
    }

    @Override
    public String visitFunction_body(CompilerParser.Function_bodyContext ctx) {
        StringBuilder body = new StringBuilder();
        for (int i=0; i<ctx.line().size(); i++) {
            String string = visitLine(ctx.line(0));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            body.append(string);
        }
        return body.toString();
    }

    // Relays each context to the correct visit function
    @Override
    public String visitLine(CompilerParser.LineContext ctx) {
        if (ctx.children.get(0) instanceof CompilerParser.AssignContext) {
            String string = visitAssign(ctx.assign());
            return checkError(string);
        }
        else if (ctx.children.get(0) instanceof CompilerParser.PrintContext) {
            String string = visitPrint(ctx.print());
            return checkError(string);
        }
        else if (ctx.children.get(0).equals(ctx.function_call())) {
            String string = visitFunction_call(ctx.function_call());
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            // Checks that function isn't referenced before definition
            boolean valid = false;
            for (Function f: functions) {
                if (Objects.equals(f.getName(), currentFunct)) {
                    valid = true;
                    break;
                }
            }
            currentFunct = "";
            if (!valid) {
                error = "Function referenced before definition";
                return "ERROR";
            }
            return "\t".repeat(indent) + string + "\n";
        }
        else if (ctx.children.get(0) instanceof CompilerParser.IfelseContext) {
            String string = visitIfelse(ctx.ifelse());
            return checkError(string);
        }
        else if (ctx.children.get(0) instanceof CompilerParser.WhileContext) {
            String string = visitWhile(ctx.while_());
            return checkError(string);
        }
        else if (ctx.children.get(0) instanceof CompilerParser.ForContext) {
            String string = visitFor(ctx.for_());
            return checkError(string);
        }
        // only allows returns if inside a function
        else if (ctx.children.get(0).getText().equals("return") && insideFunction) {
            String string = visitExpression(ctx.expression());
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "\t".repeat(indent) + "return " + string + "\n";
        }
        return "ERROR";
    }

    @Override
    public String visitIfelse(CompilerParser.IfelseContext ctx) {
        String if_str = visitIf(ctx.if_());
        if (Objects.equals(if_str, "ERROR")) {
            return "ERROR";
        }

        if (ctx.getChildCount() == 3) {
            String else_str = visitElse(ctx.else_());
            if (Objects.equals(else_str, "ERROR")) {
                return "ERROR";
            }
            return if_str + else_str;
        }
        else if (ctx.getChildCount() > 3){
            StringBuilder elifs = new StringBuilder();
            for (int i=0; i<ctx.elif().size(); i++) {
                String string = visitElif(ctx.elif().get(i));
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                elifs.append(string);
            }
            String else_str = visitElse(ctx.else_());
            if (Objects.equals(else_str, "ERROR")) {
                return "ERROR";
            }
            return if_str + elifs + else_str;
        }
        return if_str;
    }

    @Override
    public String visitIf(CompilerParser.IfContext ctx) {
        String str = visitCondition(ctx.condition());
        StringBuilder loop = new StringBuilder("\t".repeat(indent) + "if " + str + ":\n");
        indent++;
        // checks all lines in the if
        for (int i=0; i<ctx.line().size(); i++) {
            String string = visitLine((CompilerParser.LineContext) ctx.children.get(4+i));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            loop.append(string);
        }
        // removes variables that cannot be accessed outside the if
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return loop.toString();
    }

    @Override
    public String visitElif(CompilerParser.ElifContext ctx) {
        String str = visitCondition(ctx.condition());
        StringBuilder loop = new StringBuilder("\t".repeat(indent) + "elif " + str + ":\n");
        indent++;
        // checks all lines in the elif
        for (int i = 0; i<ctx.line().size(); i++) {
            String string = visitLine((CompilerParser.LineContext) ctx.children.get(4+i));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            loop.append(string);
        }
        // removes variables that cannot be accessed outside the elif
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return loop.toString();
    }

    @Override
    public String visitElse(CompilerParser.ElseContext ctx) {
        StringBuilder loop = new StringBuilder("\t".repeat(indent) + "else:\n");
        indent++;
        // checks all lines in the else
        for (int i = 0; i<ctx.line().size(); i++) {
            String string = visitLine((CompilerParser.LineContext) ctx.children.get(2+i));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            loop.append(string);
        }
        // removes variables that cannot be accessed outside the else
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return loop.toString();
    }

    @Override
    public String visitWhile(CompilerParser.WhileContext ctx) {
        String str = visitCondition(ctx.condition());
        StringBuilder loop = new StringBuilder("\t".repeat(indent) + "while " + str + ":\n");
        indent++;
        // checks each line in the while
        for (int i = 0; i<ctx.line().size(); i++) {
            String string = visitLine((CompilerParser.LineContext) ctx.children.get(4+i));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            loop.append(string);
        }
        // removes variables that cannot be accessed outside the while
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return loop.toString();
    }

    @Override
    public String visitFor(CompilerParser.ForContext ctx) {
        // checks that the variable given in the for is not already defined
        Variable currentVar = null;
        for (Variable v: variables) {
            if (Objects.equals(v.getName(), ctx.children.get(1).getText()) && v.getScope() <= indent) {
                error = "Variable referenced outside it's scope";
                return "ERROR";
            }
            else if (ctx.VAR().size() == 2 && Objects.equals(v.getName(), ctx.children.get(3).getText()) && v.getScope() <= indent) {
                currentVar = v;
            }
        }
        if (currentVar == null && ctx.VAR().size() == 2) {
            error = "Variable referenced before definition";
            return "ERROR";
        }
        variables.add(new Variable(ctx.children.get(1).getText(), "float", indent+1));

        StringBuilder loop = new StringBuilder("\t".repeat(indent) + "for " + ctx.children.get(1).getText() + " in range(" + ctx.children.get(3).getText() + "):\n");
        indent++;
        // checks each line in the for
        for (int i = 0; i<ctx.line().size(); i++) {
            String string = visitLine((CompilerParser.LineContext) ctx.children.get(6+i));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            loop.append(string);
        }
        // removes variables that cannot be accessed outside the for
        ArrayList<Variable> temp = new ArrayList<>(variables);
        for (Variable v: temp) {
            if (v.getScope() == indent) {
                variables.remove(v);
            }
        }
        indent--;
        return loop.toString();
    }

    // Turns the cond op into a string for writing to a python file
    public String setCondOp(String str) {
        if (Objects.equals(str, "===")) {
            return "==";
        }
        else if (Objects.equals(str, "!==")) {
            return "!=";
        }
        return str;
    }

    @Override
    public String visitCondition(CompilerParser.ConditionContext ctx) {
        if (ctx.children.get(1) instanceof CompilerParser.ExpressionContext) {
            String str1 = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(1));
            if (Objects.equals(str1, "ERROR")) {
                return "ERROR";
            }
            String str2 = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(3));
            if (Objects.equals(str2, "ERROR")) {
                return "ERROR";
            }
            return "(" + str1 + " " + setCondOp(ctx.COND_OP().getText()) + " " + str2 + ")";
        }
        else {
            // checks that the var is defined already
            Variable currentVar = null;
            for (Variable v: variables) {
                if (Objects.equals(v.getName(), ctx.VAR().getText()) && v.getScope() <= indent) {
                    currentVar = v;
                }
            }
            if (currentVar == null) {
                error = "Variable isn't defined";
                return "ERROR";
            }

            if (ctx.getChildCount() == 4) {
                return "(" + ctx.VAR().getText() + " " + setCondOp(ctx.children.get(2).getText()) + " " + ctx.STRING().getText() + ")";
            }
            else if (ctx.children.get(0).getText().equals("(")) {
                return ctx.children.get(0).getText() + ctx.VAR().getText() + ")";
            }
            else if (ctx.children.get(0).getText().equals("(!")) {
                return "(not " + ctx.VAR().getText() + ")";
            }
        }
        return "ERROR";
    }

    @Override
    public String visitAssign(CompilerParser.AssignContext ctx) {
        // checks that the variable isn't already defined
        Variable currentVar = null;
        for (Variable v: variables) {
            if (Objects.equals(v.getName(), ctx.VAR().getText()) && v.getScope() <= indent) {
                currentVar = v;
            }
        }

        // When a variable isn't defined in the scope, create a new one
        if (currentVar == null && Objects.equals(ctx.ASSIGN_OP().getText(), "=")) {
            if (ctx.children.get(2) instanceof CompilerParser.ExpressionContext) {
                variables.add(new Variable(ctx.VAR().getText(), "float", indent));
                String string = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(2));
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP() + string + "\n";
            }
            else if (ctx.children.get(2).equals(ctx.function_call())) {
                String string = visitFunction_call(ctx.function_call());
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                boolean valid = false;
                String currentReturnType = "";
                for (Function f: functions) {
                    if (Objects.equals(f.getName(), currentFunct)) {
                        valid = true;
                        currentReturnType = f.getReturn_type();
                    }
                }
                currentFunct = "";
                if (!valid) {
                    error = "Function has not yet been defined";
                    return "ERROR";
                }
                else if (Objects.equals(currentReturnType, "void")) {
                    error = "Assignment can't occur with a function that returns void";
                    return "ERROR";
                }
                variables.add(new Variable(ctx.VAR().getText(), currentReturnType, indent));
                return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + string  + "\n";
            }
            else if (ctx.children.get(2).equals(ctx.STRING())) {
                variables.add(new Variable(ctx.VAR().getText(), "string", indent));
                return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.STRING().getText()  + "\n";
            }
            else if (ctx.children.get(2).equals(ctx.BOOL())) {
                variables.add(new Variable(ctx.VAR().getText(), "boolean", indent));
                return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.BOOL().getText()  + "\n";
            }
        }
        else if (currentVar != null) {
            if (Objects.equals(ctx.ASSIGN_OP().getText(), "=")) {
                if (ctx.children.get(2) instanceof CompilerParser.ExpressionContext) {
                    currentVar.setDatatype("float");
                    String string = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(2));
                    if (Objects.equals(string, "ERROR")) {
                        return "ERROR";
                    }
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP() + string + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.function_call())) {
                    String string = visitFunction_call(ctx.function_call());
                    if (Objects.equals(string, "ERROR")) {
                        return "ERROR";
                    }
                    // checks that the function is already defined and has the correct number of params
                    boolean valid = false;
                    String currentReturnType = "";
                    for (Function f: functions) {
                        if (Objects.equals(f.getName(), currentFunct)) {
                            valid = true;
                            currentReturnType = f.getReturn_type();
                        }
                    }
                    currentFunct = "";
                    if (!valid) {
                        error = "Function has not yet been defined";
                        return "ERROR";
                    }
                    else if (Objects.equals(currentReturnType, "void")) {
                        error = "Assignment can't occur with a function that returns void";
                        return "ERROR";
                    }
                    currentVar.setDatatype(currentReturnType);
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + string + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.STRING())) {
                    currentVar.setDatatype("string");
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.STRING().getText() + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.BOOL())) {
                    currentVar.setDatatype("boolean");
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.BOOL().getText() + "\n";
                }
            }
            else { // Does the calculations for lines with an assign op that isn't =
                if (ctx.children.get(2) instanceof CompilerParser.ExpressionContext && Objects.equals(currentVar.getDatatype(), "float")) {
                    String string = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(2));
                    if (Objects.equals(string, "ERROR")) {
                        return "ERROR";
                    }
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP() + string + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.function_call())) {
                    String string = visitFunction_call(ctx.function_call());
                    if (Objects.equals(string, "ERROR")) {
                        return "ERROR";
                    }
                    boolean valid = false;
                    String currentReturnType = "";
                    for (Function f: functions) {
                        if (Objects.equals(f.getName(), currentFunct)) {
                            valid = true;
                            currentReturnType = f.getReturn_type();
                        }
                    }
                    currentFunct = "";
                    if (!valid) {
                        error = "Function has not yet been defined";
                        return "ERROR";
                    }
                    else if (Objects.equals(currentReturnType, "void")) {
                        error = "Assignment can't occur with a function that returns void";
                        return "ERROR";
                    }
                    else if (Objects.equals(currentVar.getDatatype(), "bool")) {
                        // add error message
                        return "ERROR";
                    }
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + string + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.STRING()) && Objects.equals(currentVar.getDatatype(), "string")) {
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.STRING().getText() + "\n";
                }
                else if (ctx.children.get(2).equals(ctx.BOOL()) && Objects.equals(currentVar.getDatatype(), "bool")) {
                    return "\t".repeat(indent) + ctx.VAR().getText() + ctx.ASSIGN_OP().getText() + ctx.BOOL().getText() + "\n";
                }
            }
        }

        return "ERROR";
    }

    @Override
    public String visitExpression(CompilerParser.ExpressionContext ctx) {
        String string = visitExpr(ctx.expr());
        return checkError(string);
    }

    // this relays the expr context to the visit function for the subcontexts EXPR1-6
    public String visitExpr(CompilerParser.ExprContext ctx) {
        if (ctx instanceof CompilerParser.EXPR1Context) {
            String string = visitEXPR1((CompilerParser.EXPR1Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof CompilerParser.EXPR2Context) {
            String string = visitEXPR2((CompilerParser.EXPR2Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof CompilerParser.EXPR3Context) {
            String string = visitEXPR3((CompilerParser.EXPR3Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof CompilerParser.EXPR4Context) {
            String string = visitEXPR4((CompilerParser.EXPR4Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof CompilerParser.EXPR5Context) {
            String string = visitEXPR5((CompilerParser.EXPR5Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof CompilerParser.EXPR6Context) {
            String string = visitEXPR6((CompilerParser.EXPR6Context) ctx);
            return checkError(string);
        }
        // if the ctx is not an instance of one of the 5 EXPRs, return an error
        return "ERROR";
    }

    @Override
    public String visitEXPR1(CompilerParser.EXPR1Context ctx) {
        String str1 = visitExpr((CompilerParser.ExprContext) ctx.children.get(0));
        if (Objects.equals(str1, "ERROR")) {
            return "ERROR";
        }
        String str2 = visitExpr((CompilerParser.ExprContext) ctx.children.get(2));
        if (Objects.equals(str2, "ERROR")) {
            return "ERROR";
        }
        return str1 + " " + ctx.MULT().toString() + " " + str2;
    }

    @Override
    public String visitEXPR2(CompilerParser.EXPR2Context ctx) {
        String str1 = visitExpr((CompilerParser.ExprContext) ctx.children.get(0));
        if (Objects.equals(str1, "ERROR")) {
            return "ERROR";
        }
        String str2 = visitExpr((CompilerParser.ExprContext) ctx.children.get(2));
        if (Objects.equals(str2, "ERROR")) {
            return "ERROR";
        }
        return str1 + " " + ctx.ADD().toString() + " " + str2;
    }

    @Override
    public String visitEXPR3(CompilerParser.EXPR3Context ctx) {
        String str1 = visitExpr((CompilerParser.ExprContext) ctx.children.get(0));
        if (Objects.equals(str1, "ERROR")) {
            return "ERROR";
        }
        String str2 = visitExpr((CompilerParser.ExprContext) ctx.children.get(2));
        if (Objects.equals(str2, "ERROR")) {
            return "ERROR";
        }
        return str1 + " % " + str2;
    }

    @Override
    public String visitEXPR4(CompilerParser.EXPR4Context ctx) {
        if (ctx.getChildCount() == 1) {
            return ctx.children.get(0).getText();
        }
        else if (ctx.getChildCount() == 3) {
            return ctx.children.get(0).getText() + "**" + ctx.children.get(2).getText();
        }
        return "ERROR";
    }

    @Override
    public String visitEXPR5(CompilerParser.EXPR5Context ctx) {
        // checks if the variable is already defined
        Variable currentVar = null;
        for (Variable v: variables) {
            if (Objects.equals(v.getName(), ctx.VAR().getText()) && v.getScope() <= indent) {
                currentVar = v;
            }
        }
        if (currentVar == null) {
            error = ctx.VAR().getText() + " has not been defined yet";
            return "ERROR";
        }
        if (ctx.getChildCount() == 1) {
            return ctx.children.get(0).getText();
        }
        else if (ctx.getChildCount() == 3 && Objects.equals(currentVar.getDatatype(), "float")) {
            return ctx.children.get(0).getText() + "**" + ctx.children.get(2).getText();
        }
        error = "You cannot raise a string to a power";
        return "ERROR";
    }

    @Override
    public String visitEXPR6(CompilerParser.EXPR6Context ctx) {
        return visitBrackets((CompilerParser.BracketsContext) ctx.children.get(0));
    }

    @Override
    public String visitBrackets(CompilerParser.BracketsContext ctx) {
        if (ctx.getChildCount() == 3) {
            String string = visitExpr((CompilerParser.ExprContext) ctx.children.get(1));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "(" + string + ")";
        }
        else if (ctx.getChildCount() == 5) {
            String string = visitExpr((CompilerParser.ExprContext) ctx.children.get(1));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "(" + string + ")**" + ctx.children.get(4).getText();
        }
        return "ERROR";
    }

    @Override
    public String visitPrint(CompilerParser.PrintContext ctx) {
        if (ctx.children.get(1) instanceof CompilerParser.ExpressionContext) {
            String string = visitExpression((CompilerParser.ExpressionContext) ctx.children.get(1));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "\t".repeat(indent) + "print(" + string + ")\n";
        }
        else if (ctx.children.get(1).equals(ctx.STRING())) {
            return "\t".repeat(indent) + "print(" + ctx.children.get(1).getText() + ")\n";
        }
        return "ERROR";
    }

    @Override
    public String visitFunction_call(CompilerParser.Function_callContext ctx) {
        int params = 0;
        StringBuilder p = new StringBuilder();
        for (int i=0; i<ctx.children.size()-1; i++) {
            if (i%2 == 1) {
                if (i != 1) {
                    p.append(",");
                }
                p.append(ctx.children.get(i).getText());
                params++;
            }
        }

        boolean valid = false;
        for (Function f: functions) {
            if (Objects.equals(f.getName(), ctx.FUNCT().getText())) {
                if (f.getParameters() == params) {
                    valid = true;
                }
                else {
                    error = ctx.FUNCT().getText() + ") has the incorrect number of parameters";
                    return "ERROR";
                }
            }
        }
        if (!valid) {
            error = ctx.FUNCT().getText() + ") has not yet been defined";
            return "ERROR";
        }

        currentFunct = ctx.FUNCT().getText();
        return ctx.FUNCT().getText() + p + ")\n";
    }
}
