// Generated from java-escape by ANTLR 4.11.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CompilerParser}.
 */
public interface CompilerListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CompilerParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(CompilerParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(CompilerParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(CompilerParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(CompilerParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#function_def}.
	 * @param ctx the parse tree
	 */
	void enterFunction_def(CompilerParser.Function_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#function_def}.
	 * @param ctx the parse tree
	 */
	void exitFunction_def(CompilerParser.Function_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#function_body}.
	 * @param ctx the parse tree
	 */
	void enterFunction_body(CompilerParser.Function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#function_body}.
	 * @param ctx the parse tree
	 */
	void exitFunction_body(CompilerParser.Function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(CompilerParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(CompilerParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#ifelse}.
	 * @param ctx the parse tree
	 */
	void enterIfelse(CompilerParser.IfelseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#ifelse}.
	 * @param ctx the parse tree
	 */
	void exitIfelse(CompilerParser.IfelseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#if}.
	 * @param ctx the parse tree
	 */
	void enterIf(CompilerParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#if}.
	 * @param ctx the parse tree
	 */
	void exitIf(CompilerParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#elif}.
	 * @param ctx the parse tree
	 */
	void enterElif(CompilerParser.ElifContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#elif}.
	 * @param ctx the parse tree
	 */
	void exitElif(CompilerParser.ElifContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#else}.
	 * @param ctx the parse tree
	 */
	void enterElse(CompilerParser.ElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#else}.
	 * @param ctx the parse tree
	 */
	void exitElse(CompilerParser.ElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#while}.
	 * @param ctx the parse tree
	 */
	void enterWhile(CompilerParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#while}.
	 * @param ctx the parse tree
	 */
	void exitWhile(CompilerParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#for}.
	 * @param ctx the parse tree
	 */
	void enterFor(CompilerParser.ForContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#for}.
	 * @param ctx the parse tree
	 */
	void exitFor(CompilerParser.ForContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(CompilerParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(CompilerParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#assign}.
	 * @param ctx the parse tree
	 */
	void enterAssign(CompilerParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#assign}.
	 * @param ctx the parse tree
	 */
	void exitAssign(CompilerParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CompilerParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CompilerParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR6}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR6(CompilerParser.EXPR6Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR6}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR6(CompilerParser.EXPR6Context ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR5}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR5(CompilerParser.EXPR5Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR5}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR5(CompilerParser.EXPR5Context ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR2}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR2(CompilerParser.EXPR2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR2}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR2(CompilerParser.EXPR2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR1}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR1(CompilerParser.EXPR1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR1}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR1(CompilerParser.EXPR1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR4}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR4(CompilerParser.EXPR4Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR4}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR4(CompilerParser.EXPR4Context ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR3}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR3(CompilerParser.EXPR3Context ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR3}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR3(CompilerParser.EXPR3Context ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#brackets}.
	 * @param ctx the parse tree
	 */
	void enterBrackets(CompilerParser.BracketsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#brackets}.
	 * @param ctx the parse tree
	 */
	void exitBrackets(CompilerParser.BracketsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(CompilerParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(CompilerParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link CompilerParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(CompilerParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link CompilerParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(CompilerParser.Function_callContext ctx);
}