// Generated from java-escape by ANTLR 4.11.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CompilerParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CompilerVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CompilerParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(CompilerParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(CompilerParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#function_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_def(CompilerParser.Function_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_body(CompilerParser.Function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(CompilerParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#ifelse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfelse(CompilerParser.IfelseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(CompilerParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#elif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElif(CompilerParser.ElifContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#else}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse(CompilerParser.ElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#while}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(CompilerParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor(CompilerParser.ForContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(CompilerParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(CompilerParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CompilerParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR6}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR6(CompilerParser.EXPR6Context ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR5}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR5(CompilerParser.EXPR5Context ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR2}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR2(CompilerParser.EXPR2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR1}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR1(CompilerParser.EXPR1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR4}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR4(CompilerParser.EXPR4Context ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR3}
	 * labeled alternative in {@link CompilerParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR3(CompilerParser.EXPR3Context ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#brackets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrackets(CompilerParser.BracketsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(CompilerParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link CompilerParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(CompilerParser.Function_callContext ctx);
}