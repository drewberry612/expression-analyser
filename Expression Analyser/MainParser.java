import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainParser {

    public static void main(String[] args) throws IOException {
        // Initialisation of the lexer, parser and error listeners
        CharStream in = CharStreams.fromFileName("./src/test.cc");
        ExprLangLexer lexer = new ExprLangLexer((CharStream) in);
        lexer.removeErrorListeners();
        MainErrorListener errorListener = new MainErrorListener();
        lexer.addErrorListener(errorListener);
        CommonTokenStream tokens = new CommonTokenStream((TokenSource) lexer);
        ExprLangParser parser = new ExprLangParser((TokenStream) tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        // PrettyVisitor traverses the parse tree and determines whether there are any errors
        PrettyVisitor pretty = new PrettyVisitor();
        String result = pretty.visit(parser.start());
        // if there are no errors
        if (errorListener.getErrors().size() == 0) {
            // if the result is valid
            if (result == null) {
                ArrayList<String> prettyStrings = pretty.getStrings();

                // reset the parser for another traversal of the tree in EvalVisitor
                parser.reset();
                EvalVisitor eval = new EvalVisitor();
                eval.visit(parser.start());
                // if the input file includes a reference to an undefined variable
                if (!eval.valid) {
                    System.out.println("Input file includes a reference to an undefined variable!");
                }
                // otherwise display all the pretty and eval strings
                // where the pretty print is on the left and the evaluation of the line is on the right
                else {
                    ArrayList<String> evalStrings = eval.getStrings();

                    for (int i = 0; i < prettyStrings.size(); i++) {
                        String gap = " ".repeat(Math.max(0, 35 - prettyStrings.get(i).length()));
                        System.out.print(prettyStrings.get(i) + gap + evalStrings.get(i) + "\n");
                    }
                }
            }
            // the program will only reach here when result is 'ERROR'
            else {
                System.out.println("Input file is invalid!");
            }
        }
        // the program will only reach here if there are syntax errors
        else {
            for (String str: errorListener.getErrors()) {
                System.out.println(str);
                System.out.println();
            }
        }
    }
}
