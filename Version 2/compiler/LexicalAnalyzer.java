package compiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class LexicalAnalyzer extends JFrame implements ActionListener{
    
    private JPanel PTitle,PCode,PAnalyze,PResult;
    private JLabel Title,CodeTitle,ResultTitle;
    private JTextArea Code,Result;
    private JButton Analyze, Parser, Grammer;
    private JScrollPane PScroll,SResult;
    
    private int Count = 1, LINE_COUNTER = 1,Current_Token = 0;
    private final ArrayList<Identifier_Table> Table =new ArrayList<>();
    
    private class Identifier_Table{
        String Name = "";
        String Type = "";
        String RESRVED_NAME = "";
        int LINE = 0;
        
        public Identifier_Table(String Identifier, String Type, int LINE){
            this.Name = Identifier;
            this.Type = Type;
            this.LINE = LINE;
            switch (Type) {
                case "identifier":
                    this.RESRVED_NAME = GET_RESERVED_NAMES(Identifier);
                    break;
                case "Operator":
                    this.RESRVED_NAME = GET_RESERVED_OPERATOR_NAMES(Identifier);
                    break;
                case "Punctuation":
                    this.RESRVED_NAME = GET_RESERVED_PUNCTUATION_NAMES(Identifier);
                    break;
                case "Number":
                    this.RESRVED_NAME = "NUMBER";
                    break;
            }
        }
        
        private String GET_RESERVED_NAMES(String Identifier){
            switch (Identifier) {
                case "\u0635\u062D\u064A\u062D":
                    return "int";
                case "\u0645\u0646\u0637\u0642\u0649":
                    return "real";
                case "\u0646\u0635\u0649":
                    return "text";
                case "\u0627\u062F\u062E\u0644":
                    return "input";
                case "\u0627\u0637\u0628\u0639":
                    return "output";
                case "\u0627\u0630\u0627":
                    return "if";
                case "\u0646\u0641\u0630":
                    return "do";
                case "\u0645\u0646":
                    return "from";
                case "\u062D\u062A\u0649":
                    return "to";
                case "\u062E\u0637\u0648\u0629":
                    return "step";
                default:
                    return "Variable";
            }
        }
        
        private String GET_RESERVED_OPERATOR_NAMES(String OP){
            switch (OP) {
                case "+":
                    return "PLUS";
                case "-":
                    return "MIN";
                case "*":
                    return "MUL";
                case "/":
                    return "DIF";
                case "<":
                    return "GREATER";
                case ">":
                    return "SMALLER";
                case "=":
                    return "EQUALL";
                case "=<":
                    return "GREATER_OR_EQUALL";
                case "=>":
                    return "SMALLER_OR_EQUALL";
                default:
                    return "";
            }
        }
        
        private String GET_RESERVED_PUNCTUATION_NAMES(String OP){
            if ( OP.equals("(") ) return "OPEN";
            else if ( OP.equals(")") ) return "CLOSE";
            return "";
        }
    }
    
    public LexicalAnalyzer(){
        SetFrameAndPanels();
        SetComponents();
        AddComponentsToPanels();
    }

    private void SetFrameAndPanels() {
    
        this.setTitle("Compiler");
        this.setSize( 500, 650 );
        this.setResizable(false);
        this.setLocation( 460 , 50 );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        
        PTitle = new JPanel();
        
        PCode = new JPanel();
        PCode.setBorder(new EmptyBorder(10,10,10,10));
        PCode.setLayout(new BorderLayout());
        
        PAnalyze = new JPanel();
        PAnalyze.setBorder(new EmptyBorder(5,5,5,5));
        
        PResult = new JPanel();
        PResult.setBorder(new EmptyBorder(10,10,10,10));
        PResult.setLayout(new BorderLayout());
    }
    private void SetComponents() {
        Title = new JLabel();
        Title.setText("Lexical Analyzer");
        
        CodeTitle = new JLabel();
        CodeTitle.setText("Enter The Code in Arabic:");
        
        Code = new JTextArea();
        Code.setColumns(10);
        Code.setLineWrap(true);
        Code.setRows(6);
        Code.setBorder(new EmptyBorder(5,5,5,5));
        Code.setForeground(Color.red);
        Code.setFont(new Font("san-serif", Font.PLAIN, 14));
        
        Grammer = new JButton();
        Grammer.setText("See Grammer");
        Grammer.addActionListener(this);
        
        Analyze = new JButton();
        Analyze.setText("Analyze");
        Analyze.addActionListener(this);
        
        Parser = new JButton();
        Parser.setText("Make Parse");
        Parser.addActionListener(this);
        
        ResultTitle = new JLabel();
        ResultTitle.setText("The Analyzer Result :");
        
        Result = new JTextArea();
        Result.setColumns(10);
        Result.setLineWrap(true);
        Result.setRows(16);
        Result.setBorder(new EmptyBorder(5,5,5,5));
        Result.setForeground(Color.red);
        Result.setEditable(false);
        Result.setBackground(Color.LIGHT_GRAY);
        Result.setFont(new Font("san-serif", Font.PLAIN, 14));
    }
    private void AddComponentsToPanels() {
        
        PTitle.add(Title);
        
        PAnalyze.add(Grammer);
        PAnalyze.add(Analyze);
        PAnalyze.add(Parser);
        
        PScroll = new JScrollPane(Code);
        
        PCode.add(CodeTitle,BorderLayout.NORTH);
        PCode.add(PScroll,BorderLayout.CENTER);
        PCode.add(PAnalyze,BorderLayout.SOUTH);
        
        SResult = new JScrollPane(Result);
        
        PResult.add(ResultTitle,BorderLayout.NORTH);
        PResult.add(SResult,BorderLayout.SOUTH);
        
        this.add(PTitle,BorderLayout.NORTH);
        this.add(PCode,BorderLayout.CENTER);
        this.add(PResult,BorderLayout.SOUTH);
    }
    
    
    private void ResetData(){
        Table.clear();
        Current_Token = 0;
        LINE_COUNTER = 1;
        Count = 1;
        Result.setText("");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("See Grammer")){
            Show_Grammer();
            return ;
        }
        
        ResetData();
        
        for (int i = 0; i < Code.getText().length(); ){
            
            String The_Result = identify(Code.getText().charAt(i));
            switch (The_Result) {
                case "Number":
                    i = Number(i);
                    break;
                case "identifier":
                    i = identifier(i);
                    break;
                case "Operator":
                    i = Operator(i);
                    break;
                case "Punctuation":
                    i = Punctuation(i);
                    break;
                default:
                    i = Notidentified(i);
                    break;
            }
        }
        if (e.getActionCommand().equals("Make Parse"))
            MakeParser();
    }
    private void Show_Grammer(){
        Result.setText("<program> ==> <dif> | <Sentences>\n" +
"<dif> ==> <type> <var>\n" +
"<type> ==> int | real | text\n" +
"\n" +
"   <var> ==> <char> ( <char> | <number> )*\n" +
"       ..\n" +
"   <var> ==> <char> <var_dash>\n" +
"   <var_dash> ==> <char> <var_dash> | <number> <var_dash> | epselon\n" +
"\n" +
"<char> ==> A - Z\n" +
"<number>	==> 0 - 9\n" +
"\n" +
"   <numbers> ==> ( <number> )*\n" +
"       ..\n" +
"   <numbers> ==> <number> <numbers> | epselon\n" +
"\n" +
"\n" +
"<Sentense> ==> <condition> | <loop> | <Assigne> | <input> | <output>\n" +
"\n" +
"   <Sentences> ==> ( <Sentence> )*\n" +
"   <Sentences> ==> <Sentence> | <Sentence> <Sentences>\n" +
"   <Sentences> ==> <Sentence> { <Sentence> }\n" +
"           ..\n" +
"   <Sentences> ==> <Sentence> <Sentences> | epselon\n" +
"\n" +
"<input> ==> input <var>\n" +
"<output> ==> output <var>\n" +
"<Assigne> ==> <var> = <var>\n" +
"<condition> ==> if ( <exp> ) do <Sentences>\n" +
"<exp> ==> <operator1> <op1>\n" +
"<op1> ==> ( < | =< | > | => ) <operator1> <op1> | epselon\n" +
"<operator1> ==> <operator2> <op2>\n" +
"<op2> ==> ( - | + ) <operator2> <op2> | epselon\n" +
"<operator2> ==> <operator3> <op3>\n" +
"<op3> ==> ( * | / ) <operator3> <op3> | epselon\n" +
"<operator3> ==> <var> | <numbers>\n" +
"<loop> ==> from <var> = <exp> to <exp> step <numbers> do <Sentences>");
    }
    
    private String identify(char a){
        
        switch (a) {
            case '\u0030':
            case '\u0031':
            case '\u0032':
            case '\u0033':
            case '\u0034':
            case '\u0035':
            case '\u0036':
            case '\u0037':
            case '\u0038':
            case '\u0039':
                return "Number";
            case '\u0627':
            case '\u0628':
            case '\u062A':
            case '\u062B':
            case '\u062C':
            case '\u062D':
            case '\u062E':
            case '\u062F':
            case '\u0630':
            case '\u0631':
            case '\u0632':
            case '\u0633':
            case '\u0634':
            case '\u0635':
            case '\u0636':
            case '\u0637':
            case '\u0639':
            case '\u063A':
            case '\u0641':
            case '\u0642':
            case '\u0643':
            case '\u0644':
            case '\u0645':
            case '\u0646':
            case '\u0647':
            case '\u0648':
            case '\u0649':
            case '\u064A':
            case '\u0629':
                return "identifier";
            case '<':
            case '>':
            case '=':
            case '+':
            case '-':
            case '/':
            case '*':
                return "Operator";
            case '(':
            case ')':
                return "Punctuation";
            default:
                return "Not Identified";
        }
    }

    private int Number(int i) {
        
        for ( int j = i+1 ; j < Code.getText().length() ; j++ )
            if ( !identify( Code.getText().charAt(j) ).equals("Number") )
                return Show(i, j, "Number");
        return Show(i, Code.getText().length(), "Number");
    }
    private int identifier(int i) {
        
        for ( int j = i+1 ; j < Code.getText().length() ; j++ )
            if ( !identify( Code.getText().charAt(j) ).equals("Number") && 
                    !identify( Code.getText().charAt(j) ).equals("identifier") )
                return Show(i, j, "identifier");
        return Show(i,  Code.getText().length(), "identifier");
    }
    private int Operator(int i) {
        
        char a = Code.getText().charAt(i);
        if ( a == '/' || a == '*' || a == '+' || a == '-' || a == '<' || a == '>' )
            return Show(i, i+1, "Operator");
        else if ( a == '=' ){
                if ( Code.getText().charAt(i+1) == '>' || Code.getText().charAt(i+1) == '<' )
                    return Show(i, i+2, "Operator");
                return Show(i, i+1, "Operator");
        }
        return Notidentified(i);
    }
    private int Punctuation(int i) {
        return Show(i, i+1, "Punctuation");
    }
    private int Notidentified(int i) {
        return Show(i, i+1, "Not identified");
    }

    private int Show(int Begin, int Final, String Type) {
        String Token = (String) Code.getText().substring(Begin, Final);
        
        if ( Token.charAt(0) == ' ' || Token.charAt(0) == '\n' ){
            if ( Token.charAt(0) == '\n' )
                LINE_COUNTER ++;
            return Final;
        }
        Result.setText( Result.getText() + Count + " <-  ( " + Type + " )  =>    " + Token + "\n\n");
        Count++;
        Table.add(new Identifier_Table(Token, Type, LINE_COUNTER));
        return Final;
    }
    
    // PARSER 
    private boolean Match(String RESERVED_NAME){
        if ( RESERVED_NAME.equals(Table.get(Current_Token).RESRVED_NAME) ){
            Current_Token++;
            return true;
        }
        return false;
    }
    
    private boolean Syntax_Error(Identifier_Table TOKEN){
        Result.setText( "The Token " + TOKEN.Name + " is not excepected in Line : " + TOKEN.LINE);
        return false;
    }
    
    private void MakeParser(){
        Programe();
        if (Current_Token != Table.size() ){
            Result.setText("Error in Syntax The :" + Table.get(Current_Token).Name + " is not expected");
            return ;
        }
        Result.setText("The Syntax is Right");
    }
    
    private boolean Programe(){
        if ( Dif() ) return true;
        Result.setText("");
        return Sentences();
    }
    
    private boolean Dif(){
        if ( !Type() ) return false;
        return Var();
    }
    
    private boolean Sentences(){
        return Sentence();
    }
    
    private boolean Type(){
        if ( Match("int")  | Match("real") | Match("text") ) 
            return true;
        return Syntax_Error(Table.get(Current_Token));
    }
    
    private boolean Var(){
        if ( Match("Variable"))
            return true;
         return Syntax_Error(Table.get(Current_Token));
    }
    
    private boolean Numbers(){
        if ( Match("NUMBER"))
            return true;
         return Syntax_Error(Table.get(Current_Token));
    }
    
    private boolean Sentence(){
        if ( Input() ) return true;
        if ( Output() ) return true;
        if ( Assign() ) return true;
        if ( Condition() ) return true;
        return Loop();
    }
    
    private boolean Input(){
        if ( !Match("input") )
            return Syntax_Error(Table.get(Current_Token));
        return Var();
    }
    
    private boolean Output(){
        if ( !Match("output") )
            return Syntax_Error(Table.get(Current_Token));
        return Var();
    }
    
    private boolean Assign(){
        if ( !Var() ) return false;
        if ( !Match("EQUALL") ) return Syntax_Error(Table.get(Current_Token));
        return Var();
    }
    
    private boolean Condition(){
        if ( !Match("if") ) return Syntax_Error(Table.get(Current_Token));
        if ( !Match("OPEN") ) return Syntax_Error(Table.get(Current_Token));
        if ( !exp() ) return false;
        if ( !Match("CLOSE") ) return Syntax_Error(Table.get(Current_Token));
        if ( !Match("do") ) return Syntax_Error(Table.get(Current_Token));
        return Sentences();
    }
    
    private boolean exp(){
        if ( Operator1() )
            return Op1();
        return false;
    }
    
    private boolean Op1(){
        if ( Match("GREATER") | Match("SMALLER") | Match("GREATER_OR_EQUALL") | 
                Match("SMALLER_OR_EQUALL") ){
            if ( Operator1() )
                return Op1();
            return Syntax_Error(Table.get(Current_Token));
        }
        return true;
    }
    
    private boolean Operator1(){
        if ( Operator2() )
            return Op2();
        return false;
    }
    
    private boolean Op2(){
        if ( Match("PLUS") | Match("MIN") ){
            if ( Operator2() )
                return Op2();
            return Syntax_Error(Table.get(Current_Token));
        }
        return true;
    }
    
    private boolean Operator2(){
        if ( Operator3() )
            return Op3();
        return false;
    }
    
    private boolean Op3(){
        if ( Match("MUL") | Match("DIF") ){
            if ( Operator3() )
                return Op3();
            return Syntax_Error(Table.get(Current_Token));
        }
        return true;
    }
    
    private boolean Operator3(){
        if ( Var() )
            return true;
        else if ( Numbers() )
            return true;
        return false;
    }
    
    private boolean Loop(){
        if ( !Match("from") )  return Syntax_Error(Table.get(Current_Token));
        if ( !Var() )  return false;
        if ( !Match("EQUALL") )  return Syntax_Error(Table.get(Current_Token));
        if ( !exp() )  return false;
        if ( !Match("to") )  return Syntax_Error(Table.get(Current_Token));
        if ( !exp() )  return false;
        if ( !Match("step") )  return Syntax_Error(Table.get(Current_Token));
        if ( !Numbers() )  return false;
        if ( !Match("do") )  return Syntax_Error(Table.get(Current_Token));
        return Sentences();
    }
}
