/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Compiler{

    private boolean isCompiled;
    private String title;
    private ArrayList<String>actionListeners;
    
    public Compiler(){
        isCompiled = false;
        actionListeners = new ArrayList<String>();

    }
    public void setCompiled(boolean isCompiled){
        this.isCompiled = isCompiled;
    }
    /**
     * Will turn parameter type code in java code
     * @param path 
     */
    public void compile(String path,String title,String workingDirectory) {
        path+="/"+title;
        if(isCompiled)return;
        Scanner s;
        Scanner again;
        try
        {
            /*Add */s = new Scanner(new File(path));
            again = new Scanner(new File(path));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "No config file found!");
            return;
        }
        String [] mandatory = {"title","fields","buttons"};
        Layer layer = new Layer(30);
        layer.manage(mandatory[0], 3, 1);
        layer.manage(mandatory[1], 4, 1);
        layer.manage(mandatory[2], 4, 1);
        if(!layer.parseFrom(path))
        {
            JOptionPane.showMessageDialog(null, "Invalid Syntax!");
            layer.destroyPM();
            return;
        }
        /**/ title = title.substring(0,title.length()-7);
       /*add this.*/this.title = layer.getStr(mandatory[0]);
        String fields[] = layer.getList(mandatory[1]);
        String buttons[] = layer.getList(mandatory[2]);
        layer.destroyPM();
        layer = new Layer(30);
        //Setting all to strings in the beginning then going to check the type later
        for(String r : fields)layer.manage(r,3,1);
        for(String r : buttons)layer.manage(r, 3, 1);
        if(!layer.parseFrom(path))
        {
            JOptionPane.showMessageDialog(null, "Invalid Syntax!");
            layer.destroyPM();
            return;
        }
        PrintWriter out;
        PrintWriter interfaceOut;
        try{
            out = new PrintWriter(new File(workingDirectory+"/"+title+".java"));
            interfaceOut = new PrintWriter(new File(workingDirectory+"/"+title+"FieldEdit.java"));
        }catch(FileNotFoundException fnfe){
            JOptionPane.showMessageDialog(null, "Error in compiling!");
            layer.destroyPM();
            return;
        }
        /*Printing the java code*/
        out.println("import javax.swing.*;");
        out.println("import javax.swing.text.*;");
        out.println("import java.awt.*;");
        out.println("import java.awt.event.*;");
        out.println("public class "+title+" extends JFrame implements "+title+"FieldEdit {");//main class
        //////////////////////////////*Variable declarations*////////////////////////////////////////////////////////
        out.println("    private JTextArea status;");
        out.println("    private JScrollPane scroll;");
        //STILL NEED TO ADD GET/SET METHODS
        for(String name:fields){
            String name1 = layer.getStr(name);
            if(!name1.equals("string")&&!name1.equals("integer")&&!name1.equals("float")){
                out.flush();
                JOptionPane.showMessageDialog(null, "Types specified are wrong!");
                return;
            }
            
            out.println("    private JTextField text"+name+";");
            out.println("    private JLabel lbl"+name+";");
            out.println("    public String getDC"+name+"(){");
            String n = layer.getStr(name);
            if(n.equals("integer")){
                
                    //out.println("    private int "+name+";");
                out.println("        try{");
                out.println("            Integer.parseInt(text"+name+".getText());");
                out.println("        }catch(NumberFormatException nfe){");
                out.println("            appendToStatusArea(\"Error, field does not have integer value! Returning 0.\");");
                out.println("            text"+name+".setText(\"\");");
                out.println("            return \"0\";");
                out.println("        }");
                out.println("        return text"+name+".getText();");
            }else if(n.equals("string")){

                    //out.println("    private String "+name+";");
                out.println("        return text"+name+".getText();");
            
            }else if(n.equals("float")){

                    //out.println("    private float "+name+";");
                out.println("        try{");
                out.println("            Float.parseFloat(text"+name+".getText());");
                out.println("        }catch(NumberFormatException nfe){");
                out.println("            appendToStatusArea(\"Error, field does not have float value! Returning 0.0 .\");");
                out.println("            text"+name+".setText(\"\");");
                out.println("            return \"0.0\";");
                out.println("        }");
                out.println("        return text"+name+".getText();");
            
            }
            out.println("    }");
            /*I don't know what to change yet?*/
            out.println("    public void setDC"+name+"(String message){");
            out.println("        text"+name+".setText(message);");
            out.println("    }");
        }
        out.println("    ");
        for(String name:buttons){
            actionListeners.add(layer.getStr(name));
            out.println("    private JButton btn"+name+";");
            
            //out.println("    private class "+layer.getStr(name)+" implements ActionListener {");
            //out.println("        private "+title+"FieldEdit inter;");
            //out.println("        public "+layer.getStr(name)+"("+title+"FieldEdit inter){");
            //out.println("            this.inter = inter;");
            //out.println("        }");
            //out.println("        public void actionPerformed(ActionEvent e) {");
            //out.println("            inter.appendToStatusArea(\""+name+" button pressed.\");");
            //out.println("        }");
            //out.println("    }");
        }
        ////////////////////////////////////////*End of variable declarations*//////////////////////////////////////////////////
        
        /*Append to text area*/
        out.println("    public void appendToStatusArea(String message){");
        out.println("        status.append(message+\"\\n\");");
        out.println("    }");
        /*End of method*/
        
        out.println("    public "+title+"(){");//contructor
        out.println("        super();");
        /*Add this.*/out.println("        setTitle(\""+this.title+"\");");
        out.println("        status = new JTextArea();");//body of constructor starting HEREEE
        out.println("        scroll = new JScrollPane(status);");
        out.println("        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);");
        out.println("        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);");
        out.println("        ");
        for(String r : fields){
            out.println("        lbl"+r+" = new JLabel(\""+r+"\");");
            out.println("        text"+r+" = new JTextField();");
           
        }
        for(String r: buttons){
            out.println("        btn"+r+" = new JButton(\""+r+"\");");
            out.println("        btn"+r+".addActionListener(new "+layer.getStr(r)+"(this));");
        }
        out.println("        JPanel northPanel = new JPanel(new BorderLayout());");
        out.println("        GridLayout grid1 = new GridLayout("+fields.length+",2);");
        out.println("        GridLayout grid2 = new GridLayout("+(buttons.length%4+buttons.length)/4+",4);");
        out.println("        JPanel nnPanel = new JPanel(grid1);");
        out.println("        JPanel nsPanel = new JPanel(grid2);");
        for(String r: fields){
            out.println("        nnPanel.add("+"lbl"+r+");");
            out.println("        nnPanel.add("+"text"+r+");");
        }
        for(String r: buttons){
            out.println("        nsPanel.add(btn"+r+");");
        }
        out.println("        northPanel.add(nnPanel,BorderLayout.NORTH);");
        out.println("        northPanel.add(nsPanel,BorderLayout.SOUTH);");
        out.println("        add(northPanel,BorderLayout.NORTH);");
        out.println("        JLabel label1 = new JLabel(\"Status\");");
        out.println("        label1.setHorizontalAlignment(SwingConstants.CENTER);");
        out.println("        label1.setVerticalAlignment(SwingConstants.CENTER);");
        out.println("        label1.setMaximumSize(new Dimension(5,0));");
        out.println("        JPanel southPanel = new JPanel(new BorderLayout());");
        out.println("        southPanel.add(label1,BorderLayout.NORTH);");
        out.println("        southPanel.add(scroll,BorderLayout.CENTER);");
        out.println("        add(southPanel,BorderLayout.CENTER);");
        out.println("        setSize(500,600);");
        out.println("        setVisible(true);");
        out.println("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);");
        out.println("    }");//closing brace for constructor
        out.println("    ");
        out.println("    public static void main(String [] args){");
        out.println("        new "+title+"();");
        out.println("    }");
        out.println("}");//closing brace for class
        //INTERFACE, CHANGE TO DIFFERENT TEXTFILE
        interfaceOut.println("public interface "+title+"FieldEdit{");
        interfaceOut.println("    public void appendToStatusArea(String message);");
        for(String r:fields){
            interfaceOut.println("    public String getDC"+r+"();");
            interfaceOut.println("    public void setDC"+r+"(String toSet);");
        }
        interfaceOut.println("}");
        interfaceOut.close();
        out.close();
        isCompiled = true;
        layer.destroyPM();
    }
    
    /**
     * Will compile code then run the gui
     * @param path 
     */
    public boolean compileThenRun(String realPath,String path,String name,String runTime,String arguments,boolean r){
        /*add this*/String title = name.substring(0,name.length()-7);
        if(!isCompiled){
            compile(realPath,name,path);
        }
        int wait=0;
        String s = runTime +" "+arguments+" ";
        if(r){
            s+=path+"/"+title+".java "+path+"/"+title+"FieldEdit.java";
            for(String action:actionListeners){
                File f = new File(path+"/"+action+".java");
                System.out.println(s+f.exists()+action);
                if(!f.exists()){
                    return false;
                }
                s+=" "+path+"/"+action+".java";
            }
        }else{
            s+= path+" "+title;
        }
        System.out.println(s);
        try{
            Process c;
            c = Runtime.getRuntime().exec(s);
            wait = c.waitFor();
        }catch(Exception e){
            return false;
        }
        return true;

    }
}
