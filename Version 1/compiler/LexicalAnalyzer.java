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
    
    JPanel PTitle,PCode,PAnalyze,PResult;
    JLabel Title,CodeTitle,ResultTitle;
    JTextArea Code,Result;
    JButton Analyze, Parser;
    JScrollPane PScroll,SResult;
    
    private ArrayList<Identifier_Table> Table =new ArrayList<>();
    
    class Identifier_Table{
        String Name = "";
        String Type = "";
        
        public Identifier_Table(String Identifier, String Type){
            this.Name = Identifier;
            this.Type = Type;
        }
    }
    
    private int Count = 1;
    
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
        CodeTitle.setText("Enter The Code :");
        
        Code = new JTextArea();
        Code.setColumns(10);
        Code.setLineWrap(true);
        Code.setRows(6);
        Code.setBorder(new EmptyBorder(5,5,5,5));
        Code.setForeground(Color.red);
        Code.setFont(new Font("san-serif", Font.PLAIN, 14));
        
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
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        Result.setText("");
        String The_Result = "";
        Count = 1;
        
        for (int i = 0; i < Code.getText().length(); ){
            
            The_Result = identify(Code.getText().charAt(i));
            if ( The_Result.equals( "Number" ) )
                i = Number(i);
            
            else if ( The_Result.equals("identifier") )
                i = identifier(i);
            else if ( The_Result.equals("Operator" ) )
                i = Operator(i);
            
            else if ( The_Result.equals("Punctuation" ) )
                i = Punctuation(i);
            else
                i = Notidentified(i);
        }
        if (e.getActionCommand().equals("Make Parse"))
            MakeParser();
    }
    private String identify(char a){
        
        if ((int)a >= 48 && (int)a < 58 )
            return "Number";
        
        else if ((int)a >= 65 && (int)a < 91 || (int)a >= 97 && (int)a < 123 || a == '_')
            return "identifier";
        
        else if ( a == '<' || a == '>' || a == '=' || a == '!' || a == '+' || a == '-' || a == '/' || a == '*' ||
                  a == '%' || a == '&' || a == '|' )
            return "Operator";
        
        else if ( a == '"' || a == '(' || a == ')' || a == ',' || a == ';' || a == ':' || a == '{' || a == '}' ||
                  a == '[' || a == ']' || a == '.' || a == '?' || a == '\'')
            return "Punctuation";
        
        else 
            return "Not Identified";
    }

    private int Number(int i) {
        
        boolean Dot = false;
        boolean E = false;
        
        for ( int j = i+1 ; j < Code.getText().length() ; j++ ){
            
            char a = Code.getText().charAt(j);
            if ( (int)a < 48 || (int)a > 57 ){
                
                if ( a == '.' ){
                    if ( E == true ){
                        if ( Code.getText().charAt(j-1) == 'E' || Code.getText().charAt(j-1) == 'e' ){
                            Show((String) Code.getText().subSequence(i, j-1), "Number");
                            return j-1;
                        }
                        else{
                            Show((String) Code.getText().subSequence(i, j), "Number");
                            return j;
                        }
                    }
                    else {
                        if ( Dot == false )
                            Dot = true;
                        else{
                            Show((String) Code.getText().subSequence(i, j), "Number");
                            return j;
                        }
                    }
                }
                else if ( a == 'E' || a == 'e' ){
                    
                    if ( Dot == false )
                        Dot = true;
                    else{
                        if ( Code.getText().charAt(j-1) == '.' ){
                            Show((String) Code.getText().subSequence(i, j-1), "Number");
                            return j-1;
                        }
                    }
                    
                    if ( E == false )
                        E = true;
                    else{
                        if ( Code.getText().charAt(j-1) == 'E' || Code.getText().charAt(j-1) == 'e' ){
                            Show((String) Code.getText().subSequence(i, j-1), "Number");
                            return j-1;
                        }
                        else{
                            Show((String) Code.getText().subSequence(i, j), "Number");
                            return j;
                        }
                    }
                }
                else if ( Code.getText().charAt(j) == '+' || Code.getText().charAt(j) == '-' ){
                    if ( Code.getText().charAt(j-1) != 'E' && Code.getText().charAt(j-1) != 'e'){
                        if ( (int)Code.getText().charAt(j-2) > 47 && (int)Code.getText().charAt(j-2) < 58 ){
                            Show((String) Code.getText().subSequence(i, j-1), "Number");
                            return j-1;
                        }
                        else{
                            Show((String) Code.getText().subSequence(i, j-2), "Number");
                            return j-2;
                        }
                    }
                }
                else{
                    System.out.println(Code.getText().charAt(j));
                    if ( Code.getText().charAt(j-1) == '.' || Code.getText().charAt(j-1) == 'E' ||
                         Code.getText().charAt(j-1) == 'e'){
                        Show((String) Code.getText().subSequence(i, j-1), "Number");
                        return j-1;
                    }
                    Show((String) Code.getText().subSequence(i, j), "Number");
                    return j;
                }
            }
        }
        if ( Code.getText().charAt( Code.getText().length()-1 ) == '.' ||
             Code.getText().charAt( Code.getText().length()-1 ) == 'E' ||
             Code.getText().charAt( Code.getText().length()-1 ) == 'e'){
            
            Show((String) Code.getText().subSequence(i, Code.getText().length()-1), "Number");
            return Code.getText().length()-1;
        }
        Show((String) Code.getText().subSequence(i, Code.getText().length()), "Number");
        return Code.getText().length();
    }
    private int identifier(int i) {
        
        for ( int j = i+1 ; j < Code.getText().length() ; j++ ){
            char a = Code.getText().charAt(j);
            if (a == '_' || (int)a >= 65 && (int)a < 91 || (int)a >= 97 && (int)a < 123 || (int)a >= 48 && (int)a < 58 )
                ;
            else{
                Show((String) Code.getText().subSequence(i, j), "identifier");
                return j;
            }
        }
        Show((String) Code.getText().subSequence(i, Code.getText().length()), "identifier");
        return Code.getText().length();
    }
    private int Operator(int i) {
        
        char a = Code.getText().charAt(i);
        if ( a == '/' || a == '*' || a == '%'){
            Show( "" + a, "Operator");
            return i+1;
        }
        else if ( a == '+' || a == '-' ){
            if ( i+1 < Code.getText().length() ){
                if ( (int)Code.getText().charAt(i+1) > 47 && (int)Code.getText().charAt(i+1) < 58 )
                    return Number(i);
            }
            Show( "" + a, "Operator");
            return i+1;
        }
        else if ( i+1 < Code.getText().length() ){
            if ( a == '!' ){
                if ( Code.getText().charAt(i+1) == '=' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else
                    return Notidentified(i);
            }
            else if ( a == '=' ){
                if ( Code.getText().charAt(i+1) == '=' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else{
                    Show( "" + a, "Operator");
                    return i+1;
                }
            }
            else if ( a == '>' ){
                if ( Code.getText().charAt(i+1) == '=' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else{
                    Show( "" + a, "Operator");
                    return i+1;
                }
            }
            else if ( a == '<' ){
                if ( Code.getText().charAt(i+1) == '=' || Code.getText().charAt(i+1) == '>' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else{
                    Show( "" + a, "Operator");
                    return i+1;
                }
            }
            else if ( a == '&' ){
                if ( Code.getText().charAt(i+1) == '&' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else{
                    Show( "" + a, "Operator");
                    return i+1;
                }
            }
            else if ( a == '|' ){
                if ( Code.getText().charAt(i+1) == '|' ){
                    Show( "" + a + Code.getText().charAt(i+1), "Operator");
                    return i+2;
                }
                else{
                    Show( "" + a, "Operator");
                    return i+1;
                }
            }
            return Notidentified(i);
        }
        else{
            if ( a == '!' )
                return Notidentified(i);
            Show( "" + a, "Operator");
            return i+1;
        }
    }
    private int Punctuation(int i) {
        Show ( ""+Code.getText().charAt(i), "Punctuation");
        return i+1;
    }
    private int Notidentified(int i) {
        if ( (int)Code.getText().charAt(i) < 33 )
            return i+1;
        Show ( "" + Code.getText().charAt(i), "Not identified");
        return i+1;
    }

    private void Show(String The_String , String Type) {
        Result.setText( Result.getText() + Count + " <-  ( " + Type + " )  =>    " + The_String + "\n\n");
        Count++;
        Table.add(new Identifier_Table(The_String, Type));
    }
    
    
    private void MakeParser(){
        
    }
}
