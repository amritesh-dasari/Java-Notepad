import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;
import javax.swing.undo.*;

public class Notepad extends Frame implements KeyListener {
    static String filename = "Untitled";
    static JTextArea tarea;
    Clipboard clipboard = getToolkit().getSystemClipboard();
    UndoManager manager = new UndoManager();
    JPanel bottom;
    JLabel details;

    Notepad(){
        bottom = new JPanel();
        details = new JLabel();
        tarea = new JTextArea();
        bottom.add(details);
        JScrollPane sp = new JScrollPane(tarea);
        add(sp);
        add(bottom, BorderLayout.SOUTH);
        tarea.getDocument().addUndoableEditListener(manager);
        tarea.addKeyListener(this);
        MenuBar menubar = new MenuBar();

        Menu File = new Menu("File");
        MenuItem New = new MenuItem("New");
        MenuItem NewWindow = new MenuItem("New Window");
        MenuItem Open = new MenuItem("Open");
        MenuItem Save = new MenuItem("Save");
        MenuItem SaveAs = new MenuItem("Save As");
        MenuItem PageSetup = new MenuItem("Page Setup");
        MenuItem Print = new MenuItem("Print");
        MenuItem Exit = new MenuItem("Exit");

        Menu Edit = new Menu("Edit");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem delete = new MenuItem("Delete");
        MenuItem find = new MenuItem("Find");
        MenuItem findnext = new MenuItem("Find Next");
        MenuItem findprev = new MenuItem("Find Previous");
        MenuItem replace = new MenuItem("Replace");
        MenuItem go = new MenuItem("Go To");
        MenuItem selectall = new MenuItem("Select All");
        MenuItem time = new MenuItem("Time/Date");

        Menu Format = new Menu("Format");
        MenuItem wordwrap = new MenuItem("Word Wrap");
        MenuItem font = new MenuItem("Font");

        Menu View = new Menu("View");
        Menu Zoom = new Menu("Zoom");
        MenuItem ZoomIn = new MenuItem("Zoom In");
        MenuItem ZoomOut = new MenuItem("Zoom Out");
        MenuItem Restore = new MenuItem("Restore Default Zoom");
        MenuItem StatusBar = new MenuItem("Status Bar");

        Menu Help = new Menu("Help");
        MenuItem vhelp = new MenuItem("View Help");
        MenuItem feedback = new MenuItem("Send Feedback");
        MenuItem about = new MenuItem("About Notepad");

        New.addActionListener(new New());
        NewWindow.addActionListener(new New());
        Open.addActionListener(new Open());
        Save.addActionListener(new Save());
        SaveAs.addActionListener(new Save());
        Exit.addActionListener(new Exit());
        undo.addActionListener(new Undo());
        redo.addActionListener(new Redo());
        cut.addActionListener(new Cut());
        copy.addActionListener(new Copy());
        paste.addActionListener(new Paste());
        delete.addActionListener(new Delete());

        File.add(New);
        File.add(NewWindow);
        File.add(Open);
        File.add(Save);
        File.add(SaveAs);
        File.add(PageSetup);
        File.add(Print);
        File.add(Exit);

        Edit.add(undo);
        Edit.add(redo);
        Edit.add(cut);
        Edit.add(copy);
        Edit.add(paste);
        Edit.add(delete);
        Edit.add(find);
        Edit.add(findnext);
        Edit.add(findprev);
        Edit.add(replace);
        Edit.add(go);
        Edit.add(selectall);
        Edit.add(time);

        Format.add(wordwrap);
        Format.add(font);

        Zoom.add(ZoomIn);
        Zoom.add(ZoomOut);
        Zoom.add(Restore);
        View.add(Zoom);
        View.add(StatusBar);

        Help.add(vhelp);
        Help.add(feedback);
        Help.add(about);

        menubar.add(File);
        menubar.add(Edit);
        menubar.add(Format);
        menubar.add(View);
        menubar.add(Help);
        setMenuBar(menubar);

        mylistener mylist = new mylistener();
        addWindowListener(mylist);
    }

    class mylistener extends WindowAdapter{
        public void windowClosing (WindowEvent e)
        {
            System.exit(0);
        }
    }

    class New implements ActionListener{
        public void actionPerformed(ActionEvent e){
            tarea.setText("");
            setTitle(filename+" - Java Notepad");
        }
    }

    class Open implements ActionListener{
        public void actionPerformed(ActionEvent e){
            FileDialog fd = new FileDialog(Notepad.this, "Select Text File",FileDialog.LOAD);
            fd.show();
            if (fd.getFile()!=null){
                filename = fd.getDirectory() + fd.getFile();
                setTitle(fd.getFile()+" - Java Notepad");
                ReadFile();
            }
            tarea.requestFocus();
        }
    }

    class Save implements ActionListener{
        public void actionPerformed(ActionEvent e){
            FileDialog fd = new FileDialog(Notepad.this,"Save File",FileDialog.SAVE);
            fd.show();
            if (fd.getFile()!=null){
                filename = fd.getDirectory() + fd.getFile();
                setTitle(fd.getFile()+" - Java Notepad");
                try{
                    DataOutputStream d = new DataOutputStream(new FileOutputStream(filename));
                    String line = tarea.getText();
                    BufferedReader br = new BufferedReader(new StringReader(line));
                    while((line = br.readLine())!=null){
                        d.writeBytes(line + "\r\n");
                    }
                    d.close();
                }
                catch(Exception ex){
                    System.out.println("File not found");
                }
                tarea.requestFocus();
            }
        }
    }

    class Exit implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    class Undo implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                manager.undo();
            }
            catch(Exception exc){}
        }
    }

    class Redo implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                manager.redo();
            }
            catch(Exception exc){}
        }
    }

    class Cut implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String sel = tarea.getSelectedText();
            StringSelection ss = new StringSelection(sel);
            clipboard.setContents(ss,ss);
            tarea.replaceRange("",tarea.getSelectionStart(),tarea.getSelectionEnd());
        }
    }

    class Copy implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String sel = tarea.getSelectedText();
            StringSelection clipString = new StringSelection(sel);
            clipboard.setContents(clipString,clipString);
        }
    }

    class Paste implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Transferable cliptran = clipboard.getContents(Notepad.this);
            try{
                String sel = (String) cliptran.getTransferData(DataFlavor.stringFlavor);
                tarea.replaceRange(sel,tarea.getSelectionStart(),tarea.getSelectionEnd());
            }
            catch(Exception exc){}
        }
    }

    class Delete implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                tarea.replaceRange("",tarea.getSelectionStart(),tarea.getSelectionEnd());
            }
            catch(Exception exc){}
        }

    }

    void ReadFile(){
        BufferedReader d;
        StringBuffer sb = new StringBuffer();
        try{
            d = new BufferedReader(new FileReader(filename));
            String line;
            while((line=d.readLine())!=null)
                sb.append(line + "\n");
            tarea.setText(sb.toString());
            d.close();
        }
        catch(FileNotFoundException fe){
            System.out.println("File not Found");
        }
        catch(IOException ioe){}
    }

    public void keyTyped(KeyEvent ke){
        int nw = tarea.getText().split(" ").length;
        int cl= tarea.getText().length();
        int linecount = tarea.getLineCount();
        details.setText("Words : "+nw+" Characters: "+cl+" Lines: "+linecount);
    }
    @Override
    public void keyPressed(KeyEvent keyEvent) {}
    @Override
    public void keyReleased(KeyEvent keyEvent) {}

    public static void main(String args[]){
        Frame f = new Notepad();
        f.setTitle(Notepad.filename+" - Java Notepad");
        f.setSize(1366,768);
        f.setVisible(true);
        f.show();
    }
}