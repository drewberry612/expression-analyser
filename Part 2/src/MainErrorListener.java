import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

public class MainErrorListener extends BaseErrorListener {
    private ArrayList<String> errors;

    public MainErrorListener() {
        errors = new ArrayList<>();
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // When an error occurs, the reason for the error is turned into a string and store for later usage in MainParser
        errors.add("Error at line: " + line + " at character: " + charPositionInLine + "\n" + msg + "\nOffending symbol: " + ((Token)offendingSymbol).getText());
    }
}
