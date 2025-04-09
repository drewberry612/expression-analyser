# 🖥️ Expression Analyzer and Compiler Project

In this project, I created an **expression analyser** based on a grammar that I defined, which I then expanded into a **compiler** for that grammar, all implemented in Java. The project leverages **ANTLR** (Another Tool for Language Recognition) to facilitate the parsing of the grammar and expression analysis.

## ⚙️ Key Features

- **Expression Analyzer**: I designed a custom grammar to analyse mathematical and logical expressions. This analyser evaluates expressions, checking for syntax correctness and performing initial parsing.
  
- **Compiler**: Using the analyser, I developed a compiler that translates the expressions into a lower-level format for execution. This involves syntax tree construction, semantic analysis, and code generation.

- **ANTLR Integration**: The grammar was formalised using **ANTLR**, which allowed me to easily generate the parser and lexer for the expressions. ANTLR simplifies the process of language recognition and ensures the compiler is efficient and maintainable.

- **Java Implementation**: The entire project was built in **Java**, making use of object-oriented programming concepts to create modular components like the lexer, parser, semantic analyzer, and code generator.

## 🧑‍💻 Project Flow

1. **Define Grammar**: The first step was defining the grammar that the analyser and compiler would use. This included mathematical operations, conditional expressions, and control structures.
  
2. **Parse Expressions**: Using ANTLR, I generated the parser and lexer from the defined grammar to parse user input into an abstract syntax tree (AST).
  
3. **Semantic Analysis and Code Generation**: The parsed expressions were checked for correctness through semantic analysis, and then translated into lower-level code that could be executed or processed further.
