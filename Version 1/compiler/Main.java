package compiler;

import java.awt.EventQueue;

public class Main {

    public static void main(String[] args) {
        System.out.print("\u00C0");
        
        EventQueue.invokeLater(() -> {
            new LexicalAnalyzer();
        });
    }    
}
