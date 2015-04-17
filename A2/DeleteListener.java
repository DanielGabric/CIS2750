/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
public class DeleteListener implements ActionListener {
    private compiled_exampleFieldEdit inter;
    public DeleteListener(compiled_exampleFieldEdit inter){
        this.inter = inter;
    }
    public void actionPerformed(ActionEvent e) {
        inter.appendToStatusArea("Delete button pressed.");
    }
}