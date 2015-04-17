/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
public class AddListener implements ActionListener {
    private compiled_exampleFieldEdit inter;
    public AddListener(compiled_exampleFieldEdit inter){
        this.inter = inter;
    }
    public void actionPerformed(ActionEvent e) {
        inter.appendToStatusArea("Add button pressed.");
    }
}