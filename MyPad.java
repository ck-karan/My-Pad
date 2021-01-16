/*
               --------------  Karan Chavan  ---------------
 */
package mypad;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.TextAction;
import java.awt.event.KeyEvent;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

/**
 *
 * @author globa
 */
public class MyPad extends JFrame{

    JMenu menuFile,menuEdit,menuHelp;
    JMenuItem itmNew,itmOpen,itmSave,itmSaveAs,itmPrint,itmExit,itmAbout,itmFind;
    JMenuBar mb;
    JTextArea txtArea;
    JComboBox cmbFont,cmbSize;
    JCheckBox cbBold,cbItalic;
    JPopupMenu popupMenu;
    JScrollPane scroll;
    boolean flag = true;
    String currFname;
    JFileChooser jfc;
    int valItalic = Font.PLAIN, valBold = Font.PLAIN;

    public MyPad(){

    mb = new JMenuBar();

    menuFile = new JMenu("File");
    menuEdit = new JMenu("Edit");
    menuHelp = new JMenu("Help");
    itmOpen = new JMenuItem("Open");
    itmSave = new JMenuItem("Save");
    itmSaveAs = new JMenuItem("Save As...");
    itmPrint = new JMenuItem("Print");
    itmExit = new JMenuItem("Exit");
    itmAbout = new JMenuItem("About");
    itmNew = new JMenuItem("New");
    itmFind = new JMenuItem("Find");
    txtArea = new JTextArea();

   // txtArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    //txtArea.setCodeFoldingEnabled(true);

    popupMenu = new JPopupMenu();
    scroll = new JScrollPane(txtArea);

    setSize(700,700);
    scroll.setBounds(0,26,this.getWidth()-17,this.getHeight()-95);
    //scroll.setSize(this.getBounds().getSize());
    scroll.setHorizontalScrollBarPolicy(RTextScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    String fontName[] = GraphicsEnvironment.getLocalGraphicsEnvironment().
						getAvailableFontFamilyNames();

    cmbFont = new JComboBox();
        for (String fontName1 : fontName) {
            cmbFont.addItem(fontName1);
        }

    cmbSize = new JComboBox(new String[]{"12","14","16","18","20","22","24","26","28","30","32","34","36","40","44","50"});

    cbBold = new JCheckBox("Bold");
    cbItalic = new JCheckBox("Italic");

    cmbFont.setBounds(0,0,170,25);
    cmbSize.setBounds(175,0,50,25);
    cbBold.setBounds(235,0,50,25);
    cbItalic.setBounds(285,0,60,25);

    mb.add(menuFile);
    mb.add(menuEdit);
    mb.add(menuHelp);

    menuFile.add(itmNew);
    menuFile.add(itmOpen);
    menuFile.add(itmSave);
    menuFile.add(itmSaveAs);
    menuFile.add(itmPrint);
    menuFile.add(itmExit);
    menuHelp.add(itmAbout);
    menuEdit.add(itmFind);

    setJMenuBar(mb);
    add(cmbFont);
    add(cmbSize);
    add(cbBold);
    add(cbItalic);
    //txtArea.setBounds(0,26,1000,1000);
    cutCopyPaste();
    //add(txtArea);
    add(scroll, BorderLayout.CENTER);

    setLocation(100,100);
    setTitle("My Pad - CK's Pad");

    setLayout(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
     //CUT COPY PASTE FUNC
    setVisible(true);

    //NEW
    itmNew.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {

            String data = txtArea.getText();
            if(data.equals("")){
               flag = true;

            }
            else{
                saveIt(data);
            }
            txtArea.setText("");
        }
    });

    //OPEN
    itmOpen.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String data = null,fname = null;
            int rpl;
            if(!("".equals(txtArea.getText()))){
                if(flag){
                    rpl =  JOptionPane.showConfirmDialog(null,"Want to save the current file?","Save or not", JOptionPane.YES_NO_OPTION);
                    if(rpl == 0)
                        saveIt(txtArea.getText());
                }
            }
            int r = 0;
            jfc = new JFileChooser();
            r = jfc.showOpenDialog(null);

            if(r == JFileChooser.APPROVE_OPTION){

                fname = jfc.getSelectedFile().getAbsolutePath();
                flag = false;
                currFname = fname;
                try {
                    data = new String(Files.readAllBytes(Paths.get(fname)));
                    } catch (IOException ex) {}
                txtArea.setText(data);
            }
            else
                txtArea.setText("");
        }
    });

    //SAVE
    itmSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            saveIt(txtArea.getText());
        }
    });

    //Save AS
    itmSaveAs.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            flag = true;
            saveIt(txtArea.getText());
            flag = false;
        }
    });

    //Exit
    itmExit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

           System.exit(0);
        }
    });

    //Print
    itmPrint.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                txtArea.print();
            } catch (PrinterException ex) {}
        }
    });

    //BOLD
    cbBold.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ie){

				if(cbBold.isSelected())
					valBold = Font.BOLD;
				else
					valBold = Font.PLAIN;
				change();
			}
		});

    //ITALIC
    cbItalic.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ie){

				if(cbItalic.isSelected())
					valItalic = Font.ITALIC;
				else
					valItalic = Font.PLAIN;
				change();
			}
		});

    //FONT STYLE
    cmbFont.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ie){

				change();
			}
		});

    //FONT SIZE
    cmbSize.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ie){

				change();
			}
		});

    itmFind.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            new JFind().setVisible(true);
        }
    });
    }

    //Save FUNC
    public void saveIt(String data) {

        int r = 0;
        String fname = null;

        if(flag){
            jfc = new JFileChooser();
            r = jfc.showSaveDialog(null); // returns actual save or not value,,,/save = 0  / cancel = 1

            if(r == JFileChooser.APPROVE_OPTION){  //APPROVE_OPTION returns 0 and CANCEL_OPTION returns 1
                fname = jfc.getSelectedFile().getAbsolutePath();
                currFname = fname;
                flag = false;
            }
        }
        if(!(currFname == null)){

            try{
                FileWriter fw = new FileWriter(currFname);
                fw.write(data);
                fw.close();
            }catch(IOException e){}
        }
    }

    //CHANGE FONT FUNC
    public void change(){

		String fontName = cmbFont.getSelectedItem().toString();
		int fontSize = Integer.parseInt(cmbSize.getSelectedItem().toString());
		txtArea.setFont(new Font(fontName, valBold+valItalic, fontSize));
	}

    //CUT COPY PASTE FUNC
    public void cutCopyPaste(){

        addAction(new DefaultEditorKit.CutAction(), KeyEvent.VK_X, "Cut");//addAction method
        addAction(new DefaultEditorKit.CopyAction(), KeyEvent.VK_C, "Copy");
        addAction(new DefaultEditorKit.PasteAction(), KeyEvent.VK_V, "Paste");

        txtArea.setComponentPopupMenu(popupMenu);

    }

    //addAction Method
    public void addAction(TextAction action, int key, String text){

        action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(key, InputEvent.CTRL_DOWN_MASK));
        action.putValue(AbstractAction.NAME, text);

        popupMenu.add(new JMenuItem(action));
        menuEdit.add(new JMenuItem(action));
    }

    //PAINT METHOD
    public void paint(Graphics g){

        super.paint(g);
        scroll.setBounds(0,26,this.getWidth()-17,this.getHeight()-95);
    }

    
public static void main(String args[]){

    new MyPad();
    }
}
