/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
public class UpdateListener implements ActionListener {
    private testcaseFieldEdit inter;
    public UpdateListener(testcaseFieldEdit inter){
        this.inter = inter;
    }
    public void actionPerformed(ActionEvent e) {
        inter.appendToStatusArea("Update button pressed.");
    }
}