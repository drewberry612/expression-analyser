import java.util.ArrayList;
import java.util.Objects;

public class PrettyVisitor extends ExprLangBaseVisitor<String> {
    // this arraylist holds all lines as strings to be printed later in MainParser
    private ArrayList<String> strings;

    public ArrayList<String> getStrings() {
        return strings;
    }

    public String checkError(String string) {
        if (Objects.equals(string, "ERROR")) {
            return "ERROR";
        }
        return string;
    }

    @Override
    public String visitStart(ExprLangParser.StartContext ctx) {
        if (ctx.prog() == null) {
            System.out.println("File is empty!");
            return "ERROR";
        }
        // if the file isn't empty traverse the parse tree
        else {
            strings = new ArrayList<>();
            return visitProg(ctx.prog());
        }
    }


    @Override
    public String visitProg(ExprLangParser.ProgContext ctx) {
        // this is recursion of prog so that the tree is evaluated from the bottom up
        if (!(ctx.prog() == null)) {
            visitProg(ctx.prog());
        }

        // these counts are used when a prog has multiple assigns or expressions
        // they get the current assign or expression to visit, refer to line 47 and 57
        int assignCount = 0;
        int expressionCount = 0;
        String string;
        // for every child this prog has
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.children.get(i) instanceof ExprLangParser.AssignContext) {
                string = visitAssign(ctx.assign().get(assignCount));
                // if an error is found it is passed back to the parent recursively until visitStart() returns "ERROR"
                // this block appears in nearly all other functions
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                // adds the valid string to a list for display in MainParser
                strings.add(string);
                assignCount++;
            }
            if (ctx.children.get(i) instanceof ExprLangParser.ExpressionContext) {
                string = visitExpression(ctx.expression().get(expressionCount));
                if (Objects.equals(string, "ERROR")) {
                    return "ERROR";
                }
                // adds the valid string to a list for display in MainParser
                strings.add(string);
                expressionCount++;
            }
        }

        // return null when there are no errors
        return null;
    }

    @Override
    public String visitAssign(ExprLangParser.AssignContext ctx) {
        String string = visitExpression(ctx.expression());
        if (Objects.equals(string, "ERROR")) {
            return "ERROR";
        }
        return ctx.VAR() + " = " + string;
    }

    @Override
    public String visitExpression(ExprLangParser.ExpressionContext ctx) {
        String string = visitExpr(ctx.expr());
        return checkError(string);
    }

    // this relays the expr context to the visit function for the subcontexts EXPR1-5
    public String visitExpr(ExprLangParser.ExprContext ctx) {
        if (ctx instanceof ExprLangParser.EXPR1Context) {
            String string = visitEXPR1((ExprLangParser.EXPR1Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof ExprLangParser.EXPR2Context) {
            String string = visitEXPR2((ExprLangParser.EXPR2Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof ExprLangParser.EXPR3Context) {
            String string = visitEXPR3((ExprLangParser.EXPR3Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof ExprLangParser.EXPR4Context) {
            String string = visitEXPR4((ExprLangParser.EXPR4Context) ctx);
            return checkError(string);
        }
        else if (ctx instanceof ExprLangParser.EXPR5Context) {
            String string = visitEXPR5((ExprLangParser.EXPR5Context) ctx);
            return checkError(string);
        }
        // if the ctx is not an instance of one of the 5 EXPRs, return an error
        return "ERROR";
    }

    @Override
    public String visitEXPR1(ExprLangParser.EXPR1Context ctx) {
        String str1 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(0));
        if (Objects.equals(str1, "ERROR")) {
            return "ERROR";
        }
        String str2 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(2));
        if (Objects.equals(str2, "ERROR")) {
            return "ERROR";
        }
        return str1 + " " + ctx.MULT().toString() + " " + str2;
    }

    @Override
    public String visitEXPR2(ExprLangParser.EXPR2Context ctx) {
        String str1 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(0));
        if (Objects.equals(str1, "ERROR")) {
            return "ERROR";
        }
        String str2 = visitExpr((ExprLangParser.ExprContext) ctx.children.get(2));
        if (Objects.equals(str2, "ERROR")) {
            return "ERROR";
        }
        return str1 + " " + ctx.ADD().toString() + " " + str2;
    }

    // this is used to display indices
    // the u00b codes are unicode for the superscripts of numbers
    public String superscript(String str) {
        str = str.replaceAll("0", "\u00b0");
        str = str.replaceAll("1", "\u00b1");
        str = str.replaceAll("2", "\u00b2");
        str = str.replaceAll("3", "\u00b3");
        str = str.replaceAll("4", "\u00b4");
        str = str.replaceAll("5", "\u00b5");
        str = str.replaceAll("6", "\u00b6");
        str = str.replaceAll("7", "\u00b7");
        str = str.replaceAll("8", "\u00b8");
        str = str.replaceAll("9", "\u00b9");

        return str;
    }

    @Override
    public String visitEXPR3(ExprLangParser.EXPR3Context ctx) {
        if (ctx.getChildCount() == 1) {
            return ctx.children.get(0).getText();
        }
        else if (ctx.getChildCount() == 3) {
            return ctx.children.get(0).getText() + superscript(ctx.children.get(2).getText());
        }
        return "ERROR";
    }

    @Override
    public String visitEXPR4(ExprLangParser.EXPR4Context ctx) {
        if (ctx.getChildCount() == 1) {
            return ctx.children.get(0).getText();
        }
        else if (ctx.getChildCount() == 3) {
            return ctx.children.get(0).getText() + superscript(ctx.children.get(2).getText());
        }
        return "ERROR";
    }

    @Override
    public String visitEXPR5(ExprLangParser.EXPR5Context ctx) {
        return visitBrackets((ExprLangParser.BracketsContext) ctx.children.get(0));
    }

    @Override
    public String visitBrackets(ExprLangParser.BracketsContext ctx) {
        if (ctx.getChildCount() == 3) {
            String string = visitExpr((ExprLangParser.ExprContext) ctx.children.get(1));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "(" + string + ")";
        }
        else if (ctx.getChildCount() == 5) {
            String string = visitExpr((ExprLangParser.ExprContext) ctx.children.get(1));
            if (Objects.equals(string, "ERROR")) {
                return "ERROR";
            }
            return "(" + string + ")" + superscript(ctx.children.get(4).getText());
        }
        return "ERROR";
    }
}
