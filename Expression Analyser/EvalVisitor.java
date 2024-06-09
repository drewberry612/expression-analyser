import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EvalVisitor extends ExprLangBaseVisitor<Double> {
    // this map holds all assigned variables so that their values can be used in calculations
    Map<String, Double> assignments;

    // this arraylist holds all evaluations of each line as strings to be printed later in MainParser
    ArrayList<String> strings;

    // this boolean is only false if the input file includes a reference to an undefined variable
    // for example, if x isn't defined but the input file includes the line "y=x+1", this variable would become false
    // this is then used to stop the parse tree traversal early, and output an error message
    boolean valid = true;

    public ArrayList<String> getStrings() {
        return strings;
    }

    @Override
    public Double visitStart(ExprLangParser.StartContext ctx) {
        if (!(ctx.prog() == null)) {
            assignments = new HashMap<>();
            strings = new ArrayList<>();
            return visitProg(ctx.prog());
        }
        return null;
    }

    @Override
    public Double visitProg(ExprLangParser.ProgContext ctx) {
        // prog recursion
        if (!(ctx.prog() == null)) {
            visitProg(ctx.prog());
        }

        // these counts are used when a prog has multiple assigns or expressions
        int assignCount = 0;
        int expressionCount = 0;
        // for every child this prog has
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.children.get(i) instanceof ExprLangParser.AssignContext) {
                visitAssign(ctx.assign().get(assignCount));
                if (!valid) {
                    return null;
                }
                assignCount++;
            }
            if (ctx.children.get(i) instanceof ExprLangParser.ExpressionContext) {
                double num = visitExpression(ctx.expression().get(expressionCount));
                if (!valid) {
                    return null;
                }
                // when a line is just an expression, the output is "ans =" plus the evaluation
                // where "ans" means answer
                String string = "ans = " + num;
                strings.add(string);
                expressionCount++;
            }
        }
        return null;
    }

    @Override
    public Double visitAssign(ExprLangParser.AssignContext ctx) {
        double num = visitExpression(ctx.expression());
        // when the return number is not a number (NaN) valid becomes false
        if (Double.isNaN(num)) {
            valid = false;
            return null;
        }
        assignments.put(ctx.VAR().getText(), num);
        String string = ctx.VAR().getText() + " = " + num;
        strings.add(string);
        return null;
    }

    @Override
    public Double visitExpression(ExprLangParser.ExpressionContext ctx) {
        return visitExpr(ctx.expr());
    }

    // this relays the expr context to the visit function for the subcontexts EXPR1-5
    public Double visitExpr(ExprLangParser.ExprContext ctx) {
        if (ctx instanceof ExprLangParser.EXPR1Context) {
            return visitEXPR1((ExprLangParser.EXPR1Context) ctx);
        }
        else if (ctx instanceof ExprLangParser.EXPR2Context) {
            return visitEXPR2((ExprLangParser.EXPR2Context) ctx);
        }
        else if (ctx instanceof ExprLangParser.EXPR3Context) {
            return visitEXPR3((ExprLangParser.EXPR3Context) ctx);
        }
        else if (ctx instanceof ExprLangParser.EXPR4Context) {
            return visitEXPR4((ExprLangParser.EXPR4Context) ctx);
        }
        else if (ctx instanceof ExprLangParser.EXPR5Context) {
            return visitEXPR5((ExprLangParser.EXPR5Context) ctx);
        }
        // if the ctx is not an instance of one of the 5 EXPRs, return a NaN and valid becomes false
        valid = false;
        return Double.NaN;
    }

    @Override
    public Double visitEXPR1(ExprLangParser.EXPR1Context ctx) {
        double num1 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(0));
        if (!valid) {
            return Double.NaN;
        }
        double num2 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(2));
        if (!valid) {
            return Double.NaN;
        }

        if (Objects.equals(ctx.MULT().getText(), "*")) {
            return num1 * num2;
        }
        else if (Objects.equals(ctx.MULT().getText(), "/")) {
            return num1 / num2;
        }
        // returns an error in any unseen cases
        valid = false;
        return Double.NaN;
    }

    @Override
    public Double visitEXPR2(ExprLangParser.EXPR2Context ctx) { // validate bidmas
        double num1 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(0));
        if (!valid) {
            return Double.NaN;
        }
        double num2 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(2));
        if (!valid) {
            return Double.NaN;
        }

        if (Objects.equals(ctx.ADD().getText(), "+")) {
            return num1 + num2;
        }
        else if (Objects.equals(ctx.ADD().getText(), "-")) {
            return num1 - num2;
        }
        // returns an error in any unseen cases
        valid = false;
        return Double.NaN;
    }

    @Override
    public Double visitEXPR3(ExprLangParser.EXPR3Context ctx) {
        if (ctx.getChildCount() == 1) {
            return Double.parseDouble(ctx.children.get(0).getText());
        }
        else if (ctx.getChildCount() == 3) {
            double num = Double.parseDouble(ctx.children.get(0).getText());
            num = Math.pow(num, Double.parseDouble(ctx.children.get(2).getText()));
            return num;
        }
        // returns an error in any unseen cases
        valid = false;
        return Double.NaN;
    }

    @Override
    public Double visitEXPR4(ExprLangParser.EXPR4Context ctx) {
        // checks the set of assigned variables for the current input variable
        for (String s : assignments.keySet()) {
            if (Objects.equals(s, ctx.children.get(0).getText())) {
                if (ctx.getChildCount() == 1) {
                    return assignments.get(ctx.children.get(0).getText());
                }
                else if (ctx.getChildCount() == 3) {
                    double num = assignments.get(ctx.children.get(0).getText());
                    num = Math.pow(num, Double.parseDouble(ctx.children.get(2).getText()));
                    return num;
                }
            }
        }
        // if the variable isn't already assigned, return a NaN and make valid false
        valid = false;
        return Double.NaN;
    }

    @Override
    public Double visitEXPR5(ExprLangParser.EXPR5Context ctx) {
        return visitBrackets((ExprLangParser.BracketsContext) ctx.children.get(0));
    }

    @Override
    public Double visitBrackets(ExprLangParser.BracketsContext ctx) {
        if (ctx.getChildCount() == 3) {
            return visitExpr((ExprLangParser.ExprContext) ctx.children.get(1));
        }
        else if (ctx.getChildCount() == 5) {
            double num = visitExpr((ExprLangParser.ExprContext) ctx.children.get(1));
            num = Math.pow(num, Double.parseDouble(ctx.children.get(4).getText()));
            return num;
        }
        // returns an error in any unseen cases
        valid = false;
        return Double.NaN;
    }
}
