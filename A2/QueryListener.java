/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
public class QueryListener implements ActionListener {
    private compiled_exampleFieldEdit inter;
    public QueryListener(compiled_exampleFieldEdit inter){
        this.inter = inter;
    }
    public void actionPerformed(ActionEvent e) {
        inter.appendToStatusArea("Query button pressed.");
    }
}