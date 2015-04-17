/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Compiler{

    private boolean isCompiled;
    private String title;
    private ArrayList<String>actionListeners;
    private boolean isError;
    
    public Compiler(){
        isError = false;
        isCompiled = false;
        actionListeners = new ArrayList<String>();

    }
    public void setCompiled(boolean isCompiled){
        this.isCompiled = isCompiled;
    }
    /**
     * Will turn parameter type code in java code
     * PRE: Valid paths to workingdirectory and config directory and config title
     * POST: compile "language" to java and output to file
     * @param path 
     */
    public void compile(String path,String title,String workingDirectory) {
        if(isError)return;
        path+="/"+title;
        title = title.substring(0,title.length()-7);
        File file = new File(title);
        file.mkdir();
        if(isCompiled)return;
        Scanner s;
        Scanner again;
        try{
            /*Add */s = new Scanner(new File(path));
            again = new Scanner(new File(path));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No config file found!");
            return;
        }
        String [] mandatory = {"title","fields","buttons"};
        Layer layer = new Layer(30);
        layer.manage(mandatory[0], 3, 1);
        layer.manage(mandatory[1], 4, 1);
        layer.manage(mandatory[2], 4, 1);
        if(!layer.parseFrom(path)){
            isError = true;
            JOptionPane.showMessageDialog(null, "Invalid Syntax!");
            layer.destroyPM();
            return;
        }
        /**/ 
       /*add this.*/this.title = layer.getStr(mandatory[0]);
        String fields[] = layer.getList(mandatory[1]);
        String buttons[] = layer.getList(mandatory[2]);
        layer.destroyPM();
        layer = new Layer(30);
        //Setting all to strings in the beginning then going to check the type later
        for(String r : fields)layer.manage(r,3,1);
        for(String r : buttons)layer.manage(r, 3, 1);
        if(!layer.parseFrom(path)){
            isError = true;
            JOptionPane.showMessageDialog(null, "Invalid Syntax!");
            layer.destroyPM();
            return;
        }
        PrintWriter out;
        PrintWriter interfaceOut;
        PrintWriter illegal;
        try{
            //System.out.println(workingDirectory+"/"+title+"/"+title+".java");
            out = new PrintWriter(new File(workingDirectory+"/"+title+"/"+title+".java"));
            interfaceOut = new PrintWriter(new File(workingDirectory+"/"+title+"/"+title+"FieldEdit.java"));
            illegal = new PrintWriter(new File(workingDirectory+"/"+title+"/IllegalFieldValueException.java"));
        }catch(FileNotFoundException fnfe){
            isError = true;
            JOptionPane.showMessageDialog(null, "Error in compiling!");
            layer.destroyPM();
            return;
        }
        /*Printing the java code*/
        out.printf("import javax.swing.*;\n");
        out.printf("import javax.swing.text.*;\n");
        out.printf("import java.awt.*;\n");
        out.printf("import java.awt.event.*;\n");
        out.printf("public class %s extends JFrame implements %sFieldEdit {\n",title,title);//main class
        //////////////////////////////*Variable declarations*////////////////////////////////////////////////////////
        out.printf("    private JTextArea status;\n");
        out.printf("    private JScrollPane scroll;\n");
        //STILL NEED TO ADD GET/SET METHODS
        for(String name:fields){
            String name1 = layer.getStr(name);
            if(!name1.equals("string")&&!name1.equals("integer")&&!name1.equals("float")){
                out.flush();
                isError=true;
                JOptionPane.showMessageDialog(null, "Types specified are wrong!");
                return;
            }
            
            out.printf("    private JTextField text%s;\n",name);
            out.printf("    private JLabel lbl%s;\n",name);
            out.printf("    public String getDC%s() throws IllegalFieldValueException{\n",name);
            String n = layer.getStr(name);
            if(n.equals("integer")){
                
                    //out.printf("    private int "+name+";");
                out.printf("        try{\n");
                out.printf("            Integer.parseInt(text%s.getText());\n",name);
                out.printf("        }catch(NumberFormatException nfe){\n");
                out.printf("            appendToStatusArea(\"Error, field %s does not have integer value!\");\n",name);
                out.printf("            text%s.setText(\"ERROR\");\n",name);
                out.printf("            throw new IllegalFieldValueException(\"Error, field %s does not have float value!\");\n",name);
                out.printf("        }\n");
                out.printf("        return text%s.getText();\n",name);
            }else if(n.equals("string")){

                    //out.printf("    private String "+name+";");
                out.printf("        return text%s.getText();\n",name);
            
            }else if(n.equals("float")){

                    //out.printf("    private float "+name+";");
                out.printf("        try{\n");
                out.printf("            Float.parseFloat(text%s.getText());\n",name);
                out.printf("        }catch(NumberFormatException nfe){\n");
                out.printf("            appendToStatusArea(\"Error, field %s does not have float value!\");\n",name);
                out.printf("            text%s.setText(\"ERROR\");\n",name);
                out.printf("            throw new IllegalFieldValueException(\"Error, field %s does not have float value!\");\n",name);
                out.printf("        }\n");
                out.printf("        return text%s.getText();\n",name);
            
            }
            out.printf("    }\n");
            /*I don't know what to change yet?*/
            out.printf("    public void setDC%s(String message){\n",name);
            out.printf("        text%s.setText(message);\n",name);
            out.printf("    }\n");
        }
        out.printf("    \n\n");
        for(String name:buttons){
            actionListeners.add(layer.getStr(name));
            out.printf("    private JButton btn%s;\n",name);
            
            //out.printf("    private class "+layer.getStr(name)+" implements ActionListener {");
            //out.printf("        private "+title+"FieldEdit inter;");
            //out.printf("        public "+layer.getStr(name)+"("+title+"FieldEdit inter){");
            //out.printf("            this.inter = inter;");
            //out.printf("        }");
            //out.printf("        public void actionPerformed(ActionEvent e) {");
            //out.printf("            inter.appendToStatusArea(\""+name+" button pressed.\");");
            //out.printf("        }");
            //out.printf("    }");
        }
        ////////////////////////////////////////*End of variable declarations*//////////////////////////////////////////////////
        
        /*Append to text area*/
        out.printf("    public void appendToStatusArea(String message){\n");
        out.printf("        status.append(message+\"\\n\");\n");
        out.printf("    }\n");
        /*End of method*/
        
        out.printf("    public %s(){\n",title);//contructor
        out.printf("        super();\n");
        /*Add this.*/out.printf("        setTitle(\"%s\");\n",this.title);
        out.printf("        status = new JTextArea();\n");//body of constructor starting HEREEE
        out.printf("        scroll = new JScrollPane(status);\n");
        out.printf("        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n");
        out.printf("        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);\n\n");
        out.printf("        \n");
        for(String r : fields){
            out.printf("        lbl%s = new JLabel(\"%s\");\n",r,r);
            out.printf("        text%s = new JTextField();\n",r);
           
        }
        for(String r: buttons){
            out.printf("        btn%s = new JButton(\"%s\");",r,r);
            out.printf("        btn%s.addActionListener(new %s(this));\n",r,layer.getStr(r));
        }
        out.printf("        JPanel northPanel = new JPanel(new BorderLayout());\n");
        out.printf("        GridLayout grid1 = new GridLayout(%d,2);\n",fields.length);
        out.printf("        GridLayout grid2 = new GridLayout(%d,4);\n",(buttons.length%4+buttons.length)/4);
        out.printf("        JPanel nnPanel = new JPanel(grid1);\n");
        out.printf("        JPanel nsPanel = new JPanel(grid2);\n");
        for(String r: fields){
            out.printf("        nnPanel.add(lbl%s);\n",r);
            out.printf("        nnPanel.add(text%s);\n",r);
        }
        for(String r: buttons){
            out.printf("        nsPanel.add(btn%s);\n",r);
        }
        out.printf("        northPanel.add(nnPanel,BorderLayout.NORTH);\n");
        out.printf("        northPanel.add(nsPanel,BorderLayout.SOUTH);\n");
        out.printf("        add(northPanel,BorderLayout.NORTH);\n");
        out.printf("        JLabel label1 = new JLabel(\"Status\");\n");
        out.printf("        label1.setHorizontalAlignment(SwingConstants.CENTER);\n");
        out.printf("        label1.setVerticalAlignment(SwingConstants.CENTER);\n");
        out.printf("        label1.setMaximumSize(new Dimension(5,0));\n");
        out.printf("        JPanel southPanel = new JPanel(new BorderLayout());\n");
        out.printf("        southPanel.add(label1,BorderLayout.NORTH);\n");
        out.printf("        southPanel.add(scroll,BorderLayout.CENTER);\n");
        out.printf("        add(southPanel,BorderLayout.CENTER);\n");
        out.printf("        setSize(500,600);\n");
        out.printf("        setVisible(true);\n");
        out.printf("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        out.printf("    }\n\n\n");//closing brace for constructor
        out.printf("    ");
        out.printf("    public static void main(String [] args){\n");
        out.printf("        new %s();\n",title);
        out.printf("    }\n");
        out.printf("}\n");//closing brace for class
        //INTERFACE, CHANGE TO DIFFERENT TEXTFILE
        interfaceOut.printf("public interface %sFieldEdit{\n",title);
        interfaceOut.printf("    public void appendToStatusArea(String message);\n");
        for(String r:fields){
            interfaceOut.printf("    public String getDC%s()throws IllegalFieldValueException; \n",r);
            interfaceOut.printf("    public void setDC%s(String toSet);\n",r);
        }
        interfaceOut.printf("}\n");
        illegal.printf("public class IllegalFieldValueException extends Exception{\n");
        illegal.printf("    public IllegalFieldValueException(String str){\n");
        illegal.printf("        super(str);\n");
        illegal.printf("    }\n");
        illegal.printf("}\n");
        interfaceOut.close();
        illegal.close();
        out.close();
        isCompiled = true;
        isError = false;
        JOptionPane.showMessageDialog(null,"Compiled Successfully!");
        layer.destroyPM();
    }
    
    /**
     * Will compile code then run the gui
     * PRE: Valid workingdirectory/config directory path
     * POST: compile java code and run it
     * @param path 
     */
    public boolean compileThenRun(String realPath,String path,String name,String runTime,String arguments,boolean r,boolean isYacc){
        if(isError)return false;
        /*add this*/String title = name.substring(0,name.length()-7);
        File file = new File(title);
        file.mkdir();
        
        if(!isCompiled&&!isYacc){
            compile(realPath,name,path);
        }else if(!isCompiled&&isYacc){
            compileWithYacc(realPath,path,name);
        }
        path+="/"+title;
        int wait=0;
        String s = runTime +" "+arguments+" ";
        if(r){
            s+=path+"/"+title+".java "+path+"/"+title+"FieldEdit.java "+path+"/IllegalFieldValueException.java";
            
            for(String action:actionListeners){
                File f = new File(path+"/"+action+".java");
                //System.out.printf(s+f.exists()+action);
                if(!f.exists()){
                    return false;
                }
                
                s+=" "+path+"/"+action+".java";
            }
        }else{
            s+= path+" "+title;
        }
        //System.out.println(s);
        try{
            if(!isError){
                Process c;
                c = Runtime.getRuntime().exec(s);
                wait = c.waitFor();
            }
        }catch(Exception e){
            return false;
        }
        return true;

    }

    /**
     * Compile with lex/yacc
     * PRE: Valid workingdirectory/config directory path and config file name
     * POST: java code from "language" in IDE
     */
    public boolean compileWithYacc(String path,String realPath,String name){
        if(isError)return false;
        String title = name.substring(0,name.length()-7);
        File file = new File(title);
        file.mkdir();
       
        String firstPath = realPath+"/"+title+"/"+title+".java";
        String secondPath = realPath +"/"+title+"/"+title+"FieldEdit.java";
        String thirdPath = realPath +"/"+title+"/IllegalFieldValueException.java";
        String fourthPath = path+"/error.log";
        String fifthPath = path+"/listeners.txt";
        String sixthPath = path+"/"+name;
        try{
            (new File(firstPath)).createNewFile();
            (new File(secondPath)).createNewFile();
            (new File(thirdPath)).createNewFile();
            (new File(fourthPath)).createNewFile();
            File f =(new File(fifthPath));
            f.createNewFile();
            PrintWriter s = new PrintWriter(f);
            s.flush();
            s.close();
        }catch(IOException e){
            return false;
        }

        String toSend = "./yadc "+firstPath+" "+secondPath+" "+thirdPath+" "+fourthPath+" "+fifthPath+" "+sixthPath+" "+title/*+" < "+sixthPath*/;
       
        //System.out.println(toSend);
        try{
            Process c;
            c = Runtime.getRuntime().exec(toSend);
            int wait = c.waitFor();
            
        }catch(Exception e){
            return false;
        }
        /*out = new PrintWriter(new File(workingDirectory+"/"+title+"/"+title+".java"));
            interfaceOut = new PrintWriter(new File(workingDirectory+"/"+title+"/"+title+"FieldEdit.java"));
            illegal = new PrintWriter(new File(workingDirectory+"/"+title+"/IllegalFieldValueException.java"));*/

        /*DO COMPILING HERE*/
        Scanner s,s1;
        try{
            s = new Scanner(new File(fourthPath));
            s1 = new Scanner(new File(fifthPath));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error log/Listener file cannot be opened!");
            return false;
        }
        String str = "";
        if(s.hasNext()){    
            while(s.hasNext()){
                str = s.nextLine();
                isError = true;
                isCompiled = false;
                JOptionPane.showMessageDialog(null, str);
                return false;
                
            }   
        }
        while(s1.hasNext()){
            String toAdd =s1.nextLine();
            
            if(!actionListeners.contains(toAdd))
                actionListeners.add(toAdd);
            
        }
        //System.out.println(actionListeners);
        JOptionPane.showMessageDialog(null,"Compiled Successfully!");
        isCompiled = true;
        isError = false;
        return true;

    }
}
