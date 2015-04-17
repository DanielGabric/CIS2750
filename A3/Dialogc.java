/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.*;

public class Dialogc extends JFrame implements KeyListener {

    private JTextArea textArea;
    //private JFileChooser files;
    boolean hasBeenChanged = false;
    boolean isSaved = false;
    private FileNameExtensionFilter filterConfigs = new FileNameExtensionFilter("Config files", "config");
    private String title;
    private JLabel status;
    private JLabel name;
    private String init = "Current Project: ";
    private String filePath = "";
    private String workingDirectory;
    private String argsJVM = "-classpath";
    private String argsComp = "";
    private String runJVM = "java";
    private String runComp = "javac";
    private boolean isYacc = true;

    
    ////////////////////////////CHECKS MODIFIED/////////////////////////////////////////
    public void keyReleased(KeyEvent k) {

        if (k.getKeyCode() == KeyEvent.VK_CONTROL && k.getKeyCode() == KeyEvent.VK_X || k.getKeyCode() == KeyEvent.VK_CONTROL && k.getKeyCode() == KeyEvent.VK_V) {

            if (!hasBeenChanged) {
                appendToStatus("[modified]");
            }
            hasBeenChanged = true;

        }

    }
    public void keyPressed(KeyEvent k) {}
    public void keyTyped(KeyEvent k) {

        if (k.getKeyCode() != KeyEvent.VK_ALT && k.getKeyCode() != KeyEvent.VK_CONTROL) {
            if (!hasBeenChanged) {
                appendToStatus("[modified]");
            }
            hasBeenChanged = true;
        }
    }
    ////////////////////////////END CHECKS MODIFIED/////////////////////////////////////////

    /*When you exit the window this checks if you have saved*/
    private class ThisWindowListener implements WindowListener{
        public void windowDeactivated(WindowEvent w){}
        public void windowActivated(WindowEvent w){}
        public void windowDeiconified(WindowEvent w){}
        public void windowIconified(WindowEvent w){}
        public void windowClosed(WindowEvent w){}
        public void windowClosing(WindowEvent w){
            if (hasBeenChanged) {
                saveAs(false,true,false,false,false,false,false);
            }else{
                System.exit(0);
            }
        }
        public void windowOpening(WindowEvent w){}
        public void windowOpened(WindowEvent w){}
    }

    /*Constructor that sets up the GUI*/
    public Dialogc() {
        super();
        setUpMenuBar();

        setUpToolBar();

        status = new JLabel();
        name = new JLabel(" -");
        setStatusBar(" -");
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        workingDirectory = System.getProperty("user.dir");
        status.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(status, BorderLayout.SOUTH);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setVerticalAlignment(SwingConstants.CENTER);
        JPanel withStatus = new JPanel(new BorderLayout());

        withStatus.add(name, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        withStatus.add(scrollPane, BorderLayout.CENTER);
        add(withStatus, BorderLayout.CENTER);
        //add(scrollPane,BorderLayout.CENTER);
        this.setVisible(true);
        this.setSize(600, 600);
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ThisWindowListener());
    }
    
    /*In spec*/
    public void appendToStatus(String message) {
        String s = status.getText();
        status.setText(s + " " + message);
    }

    /*In spec*/
    public void setStatusBar(String message) {
        status.setText(init + message);
    }

    /*In spec*/
    public void changeTitle(String message) {
        name.setText(message);
    }

    /*In spec*/
    public boolean isNewPage(){
        return name.getText().equals(" -");
    }

    /*Listener for when the new button has been pressed*/
    private class NewListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            saveAs(false,false,false,false,true,false,false);
            if(!hasBeenChanged){
                textArea.setText("");
                setStatusBar(" -");
                changeTitle("-");
                title = "";
                
                isSaved = false;
                hasBeenChanged = false;
            }
        }
    }

    /*Takes care of all opening files, scans in from file and appends to textArea*/
    public void open() {
        Scanner s;
        try {
            s = new Scanner(new File(filePath + "/" + title));
        } catch (FileNotFoundException fnfe) {
            return;
        }
        changeTitle(title);
        textArea.setText("");
        setStatusBar(title.substring(0, title.length() - 7));
        while (s.hasNext()) {
            textArea.append(s.nextLine());
            textArea.append("\n");
        }

        hasBeenChanged = false;
        isSaved = true;
    }

    /*For when the open button is pressed*/
    private class OpenListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!saveAs(false,false,true,false,false,false,false)) {
                FileChooser f = new FileChooser(true,false,true,false,false,false);
            }
        }
    }

    /*For when the save button is pressed*/
    private class SaveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            saveAs(true,false,false,false,false,false,false);

        }
    }

    /*For when the save as button is pressed*/
    private class SaveAsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            saveAs(false,false,false,true,false,false,false);
        }
    }

    /*FileChooser for all buttons except from the config menu*/
    private class FileChooser extends JFrame implements ActionListener {

        private JFileChooser c;
        private boolean isWhich;
        private boolean isQuit;
        private boolean isOpen;
        private boolean isNew;
        private boolean isCompile;
        private boolean isRun;

        /*Sets up everything according to which button was pressed*/
        public FileChooser(boolean isWhich,boolean isQuit,boolean isOpen,boolean isNew, boolean isCompile,boolean isRun) {
            super();
            this.isQuit = isQuit;
            this.isOpen = isOpen;
            this.isNew = isNew;
            this.isRun = isRun;
            this.isCompile = isCompile;
            setSize(600, 360);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            c = new JFileChooser();
            c.setCurrentDirectory(new File(System.getProperty("user.dir")));
            c.addActionListener(this);
            c.setFileFilter(filterConfigs);
            this.add(c, BorderLayout.NORTH);
            this.isWhich = isWhich;
            if (isWhich) {
                c.setApproveButtonText("Open");
            } else {
                c.setApproveButtonText("Save As");
                if(isSaved){
                    c.setSelectedFile(new File(title));
                }
            }
            c.setVisible(true);
            this.setVisible(true);

        }

        /*When open/save/close buttons are pressed this is fired up, it just decides what to do based on the state you're in*/
        public void actionPerformed(ActionEvent e) {
            String a = e.getActionCommand();
            if (a.equals("ApproveSelection")) {
                filePath = c.getCurrentDirectory() + "";
                title = c.getSelectedFile().getName();
                //System.out.println(".config".substring(".config".length()-7).equals(".config"));
                if ((title.length() > 7 && !title.substring(title.length() - 7).equals(".config")) || title.length() <= 7) {
                    title += ".config";
                }

                if (isWhich) {

                    open();
                } else {
                    int whichIs=0;
                    
                    if(isSaved&&new File(filePath+"/"+c.getSelectedFile().getName()).exists()){
                        whichIs = JOptionPane.showOptionDialog(this, "There exists a file with this name already, overwrite?","Caution",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                    }
                    if(whichIs == 0){
                        saveAsHelper();
                        if(isNew){
                            textArea.setText("");
                            setStatusBar(" -");
                            changeTitle(" -");
                            title = "";
                            
                            isSaved = false;
                            hasBeenChanged = false;
                        }

                    }
                    
                    if(isOpen){
                        isWhich = true;
                        FileChooser f = new FileChooser(isWhich,isQuit,isOpen,false,isCompile,isRun);
                        if(whichIs==0){
                            dispose();
                        }
                    }

                }
                dispose();
                Compiler compile=new Compiler();
                if(isCompile&&!isNewPage()){
                    /*Add title.length*/
                    if(!isYacc)
                        compile.compile(filePath , title,workingDirectory);
                    else
                        compile.compileWithYacc(filePath,workingDirectory,title);
                }
                if(isRun&&!isNewPage()){
                    boolean canBeRan = false;
                    if(!isYacc)
                        canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runComp,argsComp,true,false);
                    else
                        canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runComp,argsComp,true,true);
                    if(canBeRan && !isYacc)
                        compile.compileThenRun(filePath,workingDirectory,title,runJVM,argsJVM,false,false);
                    else if(!canBeRan && !isYacc)
                        JOptionPane.showMessageDialog(null,"Error! Action Listeners for buttons have not been implemented yet!");
                    if(canBeRan && isYacc)
                        canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runComp,argsComp,false,true);
                    else if(!canBeRan && isYacc)
                        JOptionPane.showMessageDialog(null,"Error! Action Listeners for buttons have not been implemented yet!");
                }
                
            } else if (a.equals("CancelSelection")) {
                if(isOpen&&!isWhich){
                    isWhich = true;
                    FileChooser f = new FileChooser(isWhich,isQuit,isOpen,false,isCompile,isRun);
                }
                dispose();
            }
            if(isQuit){
                System.exit(0);
            }
        }
    }

    /*if compile and run button is pressed this is invoked*/
    private class CompileAndRunListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            if(!hasBeenChanged&&!isNewPage()){
                Compiler compile = new Compiler();
                boolean canBeRan = false;
                if(!isYacc)
                    canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runComp,argsComp,true,false);
                else
                    canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runComp,argsComp,true,true);
                if(canBeRan && !isYacc)
                    compile.compileThenRun(filePath,workingDirectory,title,runJVM,argsJVM,false,false);
                else if(!canBeRan && !isYacc)
                    JOptionPane.showMessageDialog(null,"Error! Action Listeners for buttons have not been implemented yet!");
                if(canBeRan && isYacc)
                    canBeRan = compile.compileThenRun(filePath,workingDirectory,title,runJVM,argsJVM,false,true);
                else if(!canBeRan && isYacc)
                    JOptionPane.showMessageDialog(null,"Error! Action Listeners for buttons have not been implemented yet!");
                return;
            }

            boolean flag = saveAs(false,false,false,false,false,true,true);

            if (filePath.isEmpty()) {
                return;
            }
            
        }
    }

    /*after saveAs has fired an event, when you press save as, this is ran*/
    private void saveAsHelper() {
        if (filePath.isEmpty()) {
            return;
        }
        PrintWriter out;
        String s = textArea.getText();
        try {

            out = new PrintWriter(new File(filePath + "/" + title));
            out.println(s);
        } catch (FileNotFoundException fnfe) {
            return;
        }

        out.close();
        setStatusBar(title.substring(0, title.length() - 7));
        changeTitle(title);
        isSaved = true;
        hasBeenChanged = false;
        
    }

    /*saveAs which tells you whether you want to save/save as/do nothing depending on your state*/
    public boolean saveAs(boolean isSave, boolean isQuit,boolean isOpen, boolean isSaveAs,boolean isNew,boolean isCompile,boolean isRun) {
        //Still one more case where its saved, check for that
        
        if (!hasBeenChanged && !isSave&&!isSaveAs) {
            return false;
        }
        if (isSaved && isSave&&!isSaveAs) {
            save();
            return false;
        }
        FileChooser f = new FileChooser(false, isQuit,isOpen,isNew,isCompile,isRun);
        return true;

    }

    /*saves to a filepath*/
    public void save() {
        if (!hasBeenChanged) {
            return;
        }
        PrintWriter out;
        String s = textArea.getText();
        //System.out.println(s);
        try {
            out = new PrintWriter(new File(filePath + "/" + title));
            out.println(s);
            out.close();
            hasBeenChanged = false;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error in trying to save the config file! ABORTING");
        }
        setStatusBar(title.substring(0, title.length() - 7));
    }

    /*Handles when compile button is pressed */
    private class CompileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if(!hasBeenChanged&&!isNewPage()){
                Compiler compile = new Compiler();
                if(!isYacc)
                    compile.compile(filePath , title,workingDirectory);
                else
                    compile.compileWithYacc(filePath,workingDirectory,title);
                return;
            }
            saveAs(false,false,false,false,false,true,false);
            if (!isSaved) {
                return;
            }
            
        }
    }
    /*Handles quits*/

    private class QuitListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            /*Check if unsaved*/
            if (hasBeenChanged) {
                saveAs(false,true,false,false,false,false,false);
            }else{
                System.exit(0);
            }
            

        }
    }

    /*FileChooser for all config menu type things*/
    private class ConfigFileChooser extends JFrame implements ActionListener{
        private boolean isWork;
        private boolean isComp;
        private boolean isRun;
        private JFileChooser c;

        public ConfigFileChooser(boolean isWork,boolean isComp,boolean isRun){
            super();
            this.isWork = isWork;
            this.isComp = isComp;
            this.isRun = isRun;
            setSize(600, 360);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            c = new JFileChooser();
            if(isWork){
                c.setCurrentDirectory(new File(System.getProperty("user.dir")));
                c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                c.setSelectedFile(new File(workingDirectory));
            }else{
                c.setCurrentDirectory(new File("/usr/bin"));
                
                if(isComp){
                    c.setSelectedFile(new File(runComp));
                }else{
                    c.setSelectedFile(new File(runJVM));
                }
            }

            c.addActionListener(this);
            
            this.add(c, BorderLayout.NORTH);

            c.setVisible(true);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }

        /*Sets up directories and jvm st00fs*/
        public void actionPerformed(ActionEvent e){
            String a = e.getActionCommand();
            if (a.equals("ApproveSelection")) {
                if(isWork){
                    workingDirectory = c.getSelectedFile()+"";

                }else if(isComp){
                    runComp = c.getSelectedFile()+"";
                }else if(isRun){
                    runJVM = c.getSelectedFile()+"";
                }
                dispose();
            }else if(a.equals("CancelSelection")){
                dispose();
            }
            
        }
    }


    /*Sets up a frame with two radiobuttons and two buttons and select
      either yacc or JNI compiler*/
    private class LexYacc extends JFrame implements ActionListener{
        ButtonGroup g = new ButtonGroup();
        JRadioButton yacc = new JRadioButton("Lex/Yacc Compiler");
        JRadioButton javaComp = new JRadioButton("Java Compiler");
        public LexYacc(){
            super();
            setSize(300, 100);
                        
            g.add(yacc);
            g.add(javaComp);
            if(isYacc)
                yacc.setSelected(true);
            else
                javaComp.setSelected(true);
            setLayout(new GridLayout(0,2));
            setLocationRelativeTo(null);
            add(yacc);
            add(javaComp);
            JButton confirm = new JButton("Confirm");
            JButton close = new JButton("Close");
            add(confirm);
            add(close);
            confirm.addActionListener(this);
            close.addActionListener(this);

            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        public void actionPerformed(ActionEvent e1){
            String a =e1.getActionCommand();
            if(a.equals("Confirm")){
                if(yacc.isSelected())isYacc=true;
                else isYacc = false;
            }
            dispose();
        }
    }
    /*fires up the old filechooser when certain config buttons are pressed*/
    private class ConfigListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String m = e.getActionCommand();

             /*Compiler command, init: javac*/
            if(m.equals("Java Compiler")){
                ConfigFileChooser comp = new ConfigFileChooser(false,true,false);
            /*Compiler arguments*/
            }else if(m.equals("Compile options")){
                argsComp = (String)JOptionPane.showInputDialog(null,"Please enter some compile flags!","",JOptionPane.PLAIN_MESSAGE,null,null,argsComp);
            /*Java run, init: java*/
            }else if(m.equals("Java Run-time")){
                ConfigFileChooser comp = new ConfigFileChooser(false,false,true);
            /*Java run arguments*/
            }else if(m.equals("Run-time options")){
                argsJVM = (String)JOptionPane.showInputDialog(null,"Please enter some run-time flags!","",JOptionPane.PLAIN_MESSAGE,null,null,argsJVM);
            /*JFileChooser to working directory for compiled programs*/
            }else if(m.equals("Working Directory")){
                ConfigFileChooser comp = new ConfigFileChooser(true,false,false);
            }else if(m.equals("Compile Mode")){
                
                
                new LexYacc();

            }

        }
    }

    /*Handles help/about buttons*/
    private class HelpListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String m = e.getActionCommand();

            if (m.equals("Help")) {
                String readme="";
                Scanner s;
                try{
                    s=new Scanner(new File("README.txt"));
                }catch(FileNotFoundException fnfe){
                    JOptionPane.showMessageDialog(null,"Read me not found!");
                    return;
                }
                while(s.hasNext()){
                    readme+=s.nextLine()+"\n";
                }
                JTextArea area = new JTextArea(30,50);
                area.setText(readme);
                JScrollPane pane1 = new JScrollPane(area);
                pane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                pane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                JOptionPane.showMessageDialog(null,pane1);
            } else if (m.equals("About")) {
                JOptionPane.showMessageDialog(null,"Name: Daniel Gabric\nID: ******\nAssignment # 3\nInstructor: David Mccaughan");
            }
        }
    }

    /*Sets up the button bar*/
    public void setUpToolBar() {
        JToolBar tools = new JToolBar();
        JButton newFile = setIconButton("newFile.000.png");
        newFile.addActionListener(new NewListener());
        tools.add(newFile);
        JButton open = setIconButton("save-md.png");
        open.addActionListener(new OpenListener());
        tools.add(open);
        JButton save = setIconButton("save_icon.png");
        save.addActionListener(new SaveListener());
        tools.add(save);
        JButton saveAs = setIconButton("Save-as-icon.png");
        saveAs.addActionListener(new SaveAsListener());
        tools.add(saveAs);
        JButton play = setIconButton("play.png");
        play.addActionListener(new CompileAndRunListener());
        tools.add(play);
        tools.setFloatable(false);

        add(tools, BorderLayout.NORTH);

    }

    /*Sets up the buttons with pictures*/
    public JButton setIconButton(String path) {

        ImageIcon i = new ImageIcon(path);
        /*Image img = i.getImage();
         BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
         Graphics g = bi.createGraphics();
         g.drawImage(img, 0, 0, 20, 20, null);
         ImageIcon newImg = new ImageIcon(bi);*/
        JButton b = new JButton(i);

        b.setSize(new Dimension(15, 15));
        b.setHorizontalTextPosition(JButton.CENTER);
        b.setVerticalTextPosition(JButton.BOTTOM);

        //b.setMargin(new Insets(0,0,0,0));
        return b;
    }

    /*Sets up the menubar with actionlisteners and names and sttttttttuuuuffffffff*/
    public void setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        JMenu menuComp = new JMenu("Compile");
        menuFile.setMnemonic(KeyEvent.VK_C);
        JMenu menuConfig = new JMenu("Config");
        menuFile.setMnemonic(KeyEvent.VK_O);
        JMenu menuHelp = new JMenu("Help");
        menuFile.setMnemonic(KeyEvent.VK_H);
        JMenuItem[] itemFile = {new JMenuItem("New"), new JMenuItem("Open"), new JMenuItem("Save"), new JMenuItem("Save As"), new JMenuItem("Quit")};
        itemFile[0].addActionListener(new NewListener());
        itemFile[0].setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        itemFile[1].addActionListener(new OpenListener());
        itemFile[1].setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        itemFile[2].addActionListener(new SaveListener());
        itemFile[2].setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        itemFile[3].addActionListener(new SaveAsListener());
        itemFile[3].setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
        itemFile[4].addActionListener(new QuitListener());
        itemFile[4].setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        JMenuItem[] itemComp = {new JMenuItem("Compile"), new JMenuItem("Compile and run")};
        itemComp[0].addActionListener(new CompileListener());
        itemComp[0].setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        itemComp[1].addActionListener(new CompileAndRunListener());
        itemComp[1].setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        JMenuItem[] itemConfig = {new JMenuItem("Java Compiler"), new JMenuItem("Compile options"),
            new JMenuItem("Java Run-time"), new JMenuItem("Run-time options"), new JMenuItem("Working Directory"),new JMenuItem("Compile Mode")};
        JMenuItem[] itemHelp = {new JMenuItem("Help"), new JMenuItem("About")};
        itemConfig[0].setAccelerator(KeyStroke.getKeyStroke("F1"));
        itemConfig[1].setAccelerator(KeyStroke.getKeyStroke("F2"));
        itemConfig[2].setAccelerator(KeyStroke.getKeyStroke("F3"));
        itemConfig[3].setAccelerator(KeyStroke.getKeyStroke("F4"));
        itemConfig[4].setAccelerator(KeyStroke.getKeyStroke("F5"));
        itemConfig[5].setAccelerator(KeyStroke.getKeyStroke("F6"));
        itemHelp[0].setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        itemHelp[1].setAccelerator(KeyStroke.getKeyStroke("ctrl A"));

        for (JMenuItem j : itemFile) {
            menuFile.add(j);
        }
        for (JMenuItem j : itemComp) {
            menuComp.add(j);
        }
        for (JMenuItem j : itemConfig) {
            j.addActionListener(new ConfigListener());
            menuConfig.add(j);
        }
        for (JMenuItem j : itemHelp) {
            j.addActionListener(new HelpListener());
            menuHelp.add(j);
        }

        menuBar.add(menuFile);
        menuBar.add(menuComp);
        menuBar.add(menuConfig);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
        menuBar.setVisible(true);
    }

    /*invokes a new IDE*/
    public static void main(String[] args) {
        new Dialogc();
    }

}
