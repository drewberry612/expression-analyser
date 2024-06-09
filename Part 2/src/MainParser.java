import org.antlr.v4.runtime.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainParser {

    public static void main(String[] args) throws IOException {
        // The following are the files that are written in my language
        // Uncomment the one that needs to be tested
        //String filename = "test1"; // shows how functions cannot be referenced before definition
        //String filename = "test2"; // shows a working version of test1 where the function is defined before calling
        //String filename = "fibonacci";
        //String filename = "factorial";
        String filename = "odd_even";

        // Initialisation of the lexer, parser and error listeners
        CharStream in = CharStreams.fromFileName("./src/" + filename + ".cc");
        CompilerLexer lexer = new CompilerLexer((CharStream) in);
        lexer.removeErrorListeners();
        MainErrorListener errorListener = new MainErrorListener();
        lexer.addErrorListener(errorListener);
        CommonTokenStream tokens = new CommonTokenStream((TokenSource) lexer);
        CompilerParser parser = new CompilerParser((TokenStream) tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        // PrettyVisitor traverses the parse tree and determines whether there are any errors
        PythonVisitor visitor = new PythonVisitor();
        String result = visitor.visit(parser.start());
        // if there are no errors
        if (errorListener.getErrors().size() == 0) {
            // if the result is valid
            if (result == null) {
                ArrayList<String> strings = visitor.getStrings();

                // Output to a python source code file
                BufferedWriter writer = new BufferedWriter(new FileWriter("./src/" + filename + ".py"));

                for (String s: strings) {
                    writer.write(s);
                }

                System.out.println("File created with the name: " + filename + ".py");
                writer.close();
            }
            // the program will only reach here when result is 'ERROR'
            else {
                System.out.println(visitor.getError());
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
