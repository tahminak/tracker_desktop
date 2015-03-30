
package com.tracker.view;

import com.google.gson.stream.JsonReader;
import com.tracker.model.Issue;
import com.tracker.model.Mainmenu;
import com.tracker.model.Make;
import com.tracker.model.Model;
import com.tracker.model.Notes;
import com.tracker.model.Script;
import com.tracker.model.Scripts;
import com.tracker.model.Step;
import com.tracker.model.Submenu;
import com.tracker.utility.Frenchparser;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.joda.time.Duration;



import com.tracker.utility.ReadJsonFiles;

/**
 *
 * @author Tahmina Khan
 */

// UI for the tracker
public class tracker_notesUI extends javax.swing.JFrame {

    /**
     * Creates new form tracker_notesUI
     */
    private List<Notes> notes;
    //Variables for Scripts Tab
    private Scripts scripts = null;
    private List<Make> devices = null;
    private int notecounter;
    private Mainmenu selectedMenu;
    private String selectecSubmenTitle;
    private HashMap<String, Component> componentMap;
    private final JPopupMenu popup;
    private final JMenuItem undoMenuItem;
    private final JMenuItem redoMenuItem;
    //undo helpers
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    private final UndoManager undoManager;
    private int selectedMakeIndex = -1;
    /**
     * ***
     *
     * Voice Mail Nodes
     *
     */
    private String[] voicemailnode;
    //reading the log file
    //String logFilename= "C:/Documents and Settings/"+System.getProperty("user.name")+"/Application Data/LogMeIn Rescue/LMIRescue.log";
    String logFilename = "C:/LogMeIn Rescue/LMIRescue.log";
    File file = new File(logFilename);
    //List linesIterator = FileUtils.readLines(file, "UTF-8");
    /*
     * 
     * Timer Chat Per Hour
     * 
     */
    Toolkit toolkit;
    Timer timer;

    
    
    @SuppressWarnings("empty-statement")
    public tracker_notesUI() throws FileNotFoundException, IOException, ParseException {

        //Header for the tracker
        super("Tracker for Rogers Wireless Live Chat");

        
        //Hardcoded voicemail 
        this.voicemailnode = new String[]{"+1-905-922-1188", "+1-514-992-1188", "+1-604-618-1188",
            "+1-403-714-1188", "+1-647-278-9961", "+1-647-278-9951", "+1-647-530-1103", "+1-514-290-0728", "+1-778-288-1453", "+1-647-802-9327", "+1-647-839-6178"};

        //Initialize Components 
        initComponents();
        
        
        UIManager.put("Button.select", Color.black);
        
        //Read notes json file
        notes = ReadJsonFiles.readNotesJson("notes.json");
        
        
         //Read scripts json file
        scripts = ReadJsonFiles.readScriptsJson("scripts.json");
        
        //Read devices for settings
        devices = ReadJsonFiles.readDevicesJson("devices.json");

        //Sort Notes List
        Collections.sort(notes);

        //Update all the scripts
        updateAllNotes();

        genrateScriptsButtons();
        genrateDeviceSettingButtons();

        /*
         * implementing right click pop up menu
         * 
         */
        popup = new JPopupMenu();
        undoManager = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        undoMenuItem = new JMenuItem(undoAction);
        redoMenuItem = new JMenuItem(redoAction);

        notesTextArea0.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea1.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea2.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea3.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea4.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea5.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        notesTextArea.getDocument().addUndoableEditListener(new MyUndoableEditListener());

        undoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
                undoMenuItem.setEnabled(undoManager.canUndo());
                redoMenuItem.setEnabled(undoManager.canRedo());
            }
        });
        redoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
                undoMenuItem.setEnabled(undoManager.canUndo());
                redoMenuItem.setEnabled(undoManager.canRedo());
            }
        });
        popup.add(undoMenuItem);
        popup.add(redoMenuItem);
        popup.addSeparator();
        popup.add(new JMenuItem(new DefaultEditorKit.CopyAction()));
        popup.add(new JMenuItem(new DefaultEditorKit.PasteAction()));
        popup.add(new JMenuItem(new DefaultEditorKit.CutAction()));

        notesTextArea0.setComponentPopupMenu(popup);
        notesTextArea1.setComponentPopupMenu(popup);
        notesTextArea2.setComponentPopupMenu(popup);
        notesTextArea3.setComponentPopupMenu(popup);
        notesTextArea4.setComponentPopupMenu(popup);
        notesTextArea5.setComponentPopupMenu(popup);
        notesTextArea.setComponentPopupMenu(popup);

        vmnodebutton.setText(voicemailnode[vmnodebox.getSelectedIndex()]);
        //System.out.println(validateIMEI("3557940525597504"));

        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        timer.schedule(new RemindTask(), 5 * 1000, 600000);
        populateChatPerHourTable();
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            try {
                populateChatPerHourTable();
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void updateAllNotes() throws IOException {
        notes = ReadJsonFiles.readNotesJson("notes.json");

        //Sorting Notes List
        Collections.sort(notes);
        updateNotesTitleBox(noteListBox1);
        updateNotesTitleBox(noteListBox2);
        updateNotesTitleBox(noteListBox3);
        updateNotesTitleBox(noteListBox4);
        updateNotesTitleBox(noteListBox5);
        updateNotesTitleBox(noteListBox6);

    }

    private void updateNotesTitleBox(JComboBox notestilebox) {
        // Getting the size f the 
        notecounter = notes.size();

        // creating tile box array size is one more than the size of the notes
        // to add 'add notes'" item
        String[] notestilesforbox = new String[notecounter];

        // initializing notes title box 
        int i = 0;
        for (Notes note : notes) {
            notestilesforbox[i] = note.getNotestitle();
            i++;
        }

        notestilebox.setModel(new javax.swing.DefaultComboBoxModel(notestilesforbox));
        //notestilebox.setSelectedIndex(notecounter);

    }

 
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        copyNotesButton = new javax.swing.JButton();
        clearNotesButton = new javax.swing.JButton();
        NotesTabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        noteListBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesTextArea0 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        noteListBox2 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        notesTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        noteListBox3 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        notesTextArea2 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        noteListBox4 = new javax.swing.JComboBox();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        notesTextArea3 = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        noteListBox5 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        notesTextArea4 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        noteListBox6 = new javax.swing.JComboBox();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        notesTextArea5 = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        showsidepanelButton = new javax.swing.JButton();
        settingsTabs = new javax.swing.JTabbedPane();
        scriptsPanel = new javax.swing.JPanel();
        showMenuPanel = new javax.swing.JPanel();
        subMenuBox = new javax.swing.JComboBox();
        englishScriptlbl = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        englishScriptTextArea = new javax.swing.JTextArea();
        frenchScriptlbl = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        frenchScriptTextArea = new javax.swing.JTextArea();
        copyEnglishScriptsButton = new javax.swing.JButton();
        copyFrenchScriptsButton = new javax.swing.JButton();
        devicesettingsPanel = new javax.swing.JPanel();
        showDeviceSettingsPanel = new javax.swing.JPanel();
        modelBox = new javax.swing.JComboBox();
        ModelLvl = new javax.swing.JLabel();
        issueBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        englihIssuetTextArea = new javax.swing.JTextArea();
        englishIssueCopyBtn = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        frenchIssueTextArea = new javax.swing.JTextArea();
        frnechIssueCopyBtn = new javax.swing.JButton();
        voiceMailTab = new javax.swing.JPanel();
        vmnodebox = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        iosVmComboBox = new javax.swing.JComboBox();
        jScrollPane11 = new javax.swing.JScrollPane();
        iosVmTextarea = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        androidVmComboBox = new javax.swing.JComboBox();
        jScrollPane12 = new javax.swing.JScrollPane();
        androidVmTextarea = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        bbVmComboBox = new javax.swing.JComboBox();
        jScrollPane13 = new javax.swing.JScrollPane();
        blackberryVMTextArea = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        windowsVMTextArea = new javax.swing.JTextArea();
        wnVmComboBox = new javax.swing.JComboBox();
        vmnodebutton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        otherVmComboBox = new javax.swing.JComboBox();
        jScrollPane15 = new javax.swing.JScrollPane();
        otherVmTextArea = new javax.swing.JTextArea();
        jButton7 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        chatPerHourTable = new javax.swing.JTable();
        javax.swing.JButton chatPerHourRefreshButton = new javax.swing.JButton();
        notesTab = new javax.swing.JPanel();
        notesScrollpane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        openNotesEditorMenu = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        refreshScripts = new javax.swing.JMenuItem();
        refreshScriptsMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setRequestFocusEnabled(false);

        copyNotesButton.setText("Copy/Save");
        copyNotesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyNotesButtonActionPerformed(evt);
            }
        });

        clearNotesButton.setText("Clear");
        clearNotesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearNotesButtonActionPerformed(evt);
            }
        });

        NotesTabs.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Notes");

        noteListBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        notesTextArea0.setColumns(20);
        notesTextArea0.setLineWrap(true);
        notesTextArea0.setRows(5);
        notesTextArea0.setWrapStyleWord(true);
        notesTextArea0.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        notesTextArea0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea0KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(notesTextArea0);

        jLabel2.setText("CTN :");
        jLabel2.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox1, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(noteListBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.getAccessibleContext().setAccessibleName("Ctn:");

        NotesTabs.addTab("One", jPanel2);

        jLabel5.setText("Notes");

        noteListBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        notesTextArea1.setColumns(20);
        notesTextArea1.setLineWrap(true);
        notesTextArea1.setRows(5);
        notesTextArea1.setWrapStyleWord(true);
        notesTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(notesTextArea1);

        jLabel6.setText("CTN :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox2, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(noteListBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        NotesTabs.addTab("Two", jPanel5);

        jLabel3.setText("Notes");

        noteListBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox3ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        notesTextArea2.setColumns(20);
        notesTextArea2.setLineWrap(true);
        notesTextArea2.setRows(5);
        notesTextArea2.setWrapStyleWord(true);
        notesTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea2KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(notesTextArea2);

        jLabel4.setText("CTN:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox3, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(noteListBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        NotesTabs.addTab("Three", jPanel3);

        jLabel7.setText("Notes");

        noteListBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox4ActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        notesTextArea3.setColumns(20);
        notesTextArea3.setLineWrap(true);
        notesTextArea3.setRows(5);
        notesTextArea3.setWrapStyleWord(true);
        notesTextArea3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea3KeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(notesTextArea3);

        jLabel8.setText("CTN :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox4, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(noteListBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        NotesTabs.addTab("Four", jPanel4);

        jLabel9.setText("Notes");

        noteListBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox5ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        notesTextArea4.setColumns(20);
        notesTextArea4.setLineWrap(true);
        notesTextArea4.setRows(5);
        notesTextArea4.setWrapStyleWord(true);
        notesTextArea4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea4KeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(notesTextArea4);

        jLabel10.setText("CTN :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox5, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(noteListBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        NotesTabs.addTab("Five", jPanel6);

        jLabel11.setText("Notes");

        noteListBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteListBox6ActionPerformed(evt);
            }
        });

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        notesTextArea5.setColumns(20);
        notesTextArea5.setLineWrap(true);
        notesTextArea5.setRows(5);
        notesTextArea5.setWrapStyleWord(true);
        notesTextArea5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesTextArea5KeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(notesTextArea5);

        jLabel12.setText("CTN :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noteListBox6, 0, 255, Short.MAX_VALUE)
                            .addComponent(jTextField6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(noteListBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        NotesTabs.addTab("Six", jPanel7);

        showsidepanelButton.setText("+");
        showsidepanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showsidepanelButtonActionPerformed(evt);
            }
        });

        settingsTabs.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        settingsTabs.setMaximumSize(new java.awt.Dimension(492, 632));

        scriptsPanel.setMaximumSize(new java.awt.Dimension(483, 598));

        showMenuPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        showMenuPanel.setMaximumSize(new java.awt.Dimension(250, 128));
        showMenuPanel.setPreferredSize(new java.awt.Dimension(250, 128));
        showMenuPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout showMenuPanelLayout = new javax.swing.GroupLayout(showMenuPanel);
        showMenuPanel.setLayout(showMenuPanelLayout);
        showMenuPanelLayout.setHorizontalGroup(
            showMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );
        showMenuPanelLayout.setVerticalGroup(
            showMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 127, Short.MAX_VALUE)
        );

        subMenuBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuBoxActionPerformed(evt);
            }
        });

        englishScriptTextArea.setColumns(20);
        englishScriptTextArea.setLineWrap(true);
        englishScriptTextArea.setRows(5);
        englishScriptTextArea.setWrapStyleWord(true);
        jScrollPane7.setViewportView(englishScriptTextArea);

        frenchScriptTextArea.setColumns(20);
        frenchScriptTextArea.setLineWrap(true);
        frenchScriptTextArea.setRows(5);
        frenchScriptTextArea.setWrapStyleWord(true);
        jScrollPane8.setViewportView(frenchScriptTextArea);

        copyEnglishScriptsButton.setText("Copy");
        copyEnglishScriptsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyEnglishScriptsButtonActionPerformed(evt);
            }
        });

        copyFrenchScriptsButton.setText("Copy");
        copyFrenchScriptsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyFrenchScriptsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scriptsPanelLayout = new javax.swing.GroupLayout(scriptsPanel);
        scriptsPanel.setLayout(scriptsPanelLayout);
        scriptsPanelLayout.setHorizontalGroup(
            scriptsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scriptsPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(scriptsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frenchScriptlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copyFrenchScriptsButton)
                    .addComponent(subMenuBox, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(englishScriptlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copyEnglishScriptsButton)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        scriptsPanelLayout.setVerticalGroup(
            scriptsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scriptsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(subMenuBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(englishScriptlbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(copyEnglishScriptsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frenchScriptlbl, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(copyFrenchScriptsButton)
                .addContainerGap())
        );

        settingsTabs.addTab("Scripts", scriptsPanel);

        showDeviceSettingsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        showDeviceSettingsPanel.setMaximumSize(new java.awt.Dimension(441, 137));

        javax.swing.GroupLayout showDeviceSettingsPanelLayout = new javax.swing.GroupLayout(showDeviceSettingsPanel);
        showDeviceSettingsPanel.setLayout(showDeviceSettingsPanelLayout);
        showDeviceSettingsPanelLayout.setHorizontalGroup(
            showDeviceSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );
        showDeviceSettingsPanelLayout.setVerticalGroup(
            showDeviceSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
        );

        modelBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelBoxActionPerformed(evt);
            }
        });

        ModelLvl.setText("Model/Os");

        issueBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                issueBoxActionPerformed(evt);
            }
        });

        jLabel13.setText("Issues");

        englihIssuetTextArea.setColumns(20);
        englihIssuetTextArea.setLineWrap(true);
        englihIssuetTextArea.setRows(5);
        englihIssuetTextArea.setWrapStyleWord(true);
        jScrollPane9.setViewportView(englihIssuetTextArea);

        englishIssueCopyBtn.setText("Copy");
        englishIssueCopyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                englishIssueCopyBtnActionPerformed(evt);
            }
        });

        frenchIssueTextArea.setColumns(20);
        frenchIssueTextArea.setLineWrap(true);
        frenchIssueTextArea.setRows(5);
        frenchIssueTextArea.setWrapStyleWord(true);
        jScrollPane10.setViewportView(frenchIssueTextArea);

        frnechIssueCopyBtn.setLabel("Copy");
        frnechIssueCopyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frnechIssueCopyBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout devicesettingsPanelLayout = new javax.swing.GroupLayout(devicesettingsPanel);
        devicesettingsPanel.setLayout(devicesettingsPanelLayout);
        devicesettingsPanelLayout.setHorizontalGroup(
            devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devicesettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showDeviceSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(devicesettingsPanelLayout.createSequentialGroup()
                        .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ModelLvl, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(issueBox, 0, 222, Short.MAX_VALUE)
                            .addComponent(modelBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(englishIssueCopyBtn)
                    .addComponent(frnechIssueCopyBtn)
                    .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        devicesettingsPanelLayout.setVerticalGroup(
            devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devicesettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showDeviceSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(modelBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ModelLvl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(devicesettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(issueBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(englishIssueCopyBtn)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frnechIssueCopyBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Device Settings", devicesettingsPanel);

        vmnodebox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Box_200 (+1-416-822-1142)", "Box_201 (+1-514-827-5913)", "Box_202 (+1-604-618-1142)", "Box_203 (+1-403-714-1142)", "Box_206 (+1-647-278-9960)", "Box_207 (+1-647-278-9950)", "Box_400 (+1-647-530-1009)", "Box_401 (+1-514-245-0693)", "Box_402 (+1-778-238-0143)", "Box_300 (+1-647-801-4873)", "Box_301 (+1-647-839-6148)" }));
        vmnodebox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vmnodeboxActionPerformed(evt);
            }
        });

        jLabel14.setText("VM Box");

        jLabel15.setText("iOS");
        jLabel15.setToolTipText("");
        jLabel15.setAutoscrolls(true);

        iosVmComboBox.setEditable(true);
        iosVmComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ios 7.0", "ios 6.0" }));
        iosVmComboBox.setAutoscrolls(true);
        iosVmComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        iosVmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iosVmComboBoxActionPerformed(evt);
            }
        });

        iosVmTextarea.setColumns(20);
        iosVmTextarea.setLineWrap(true);
        iosVmTextarea.setRows(5);
        iosVmTextarea.setWrapStyleWord(true);
        jScrollPane11.setViewportView(iosVmTextarea);

        jLabel16.setText("Android");
        jLabel16.setToolTipText("");

        androidVmComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4.x", "2.x", "1.x" }));
        androidVmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                androidVmComboBoxActionPerformed(evt);
            }
        });

        androidVmTextarea.setColumns(20);
        androidVmTextarea.setRows(5);
        jScrollPane12.setViewportView(androidVmTextarea);

        jLabel17.setText("Blackberr");

        bbVmComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "os 10", "os 6 / 7", "os 5 and earlier" }));
        bbVmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbVmComboBoxActionPerformed(evt);
            }
        });

        blackberryVMTextArea.setColumns(20);
        blackberryVMTextArea.setLineWrap(true);
        blackberryVMTextArea.setRows(5);
        jScrollPane13.setViewportView(blackberryVMTextArea);

        jLabel18.setText("Windows");

        windowsVMTextArea.setColumns(20);
        windowsVMTextArea.setRows(5);
        jScrollPane14.setViewportView(windowsVMTextArea);

        wnVmComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Win 8", "Win 7", "Nokia Lumia 710.1" }));
        wnVmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wnVmComboBoxActionPerformed(evt);
            }
        });

        vmnodebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vmnodebuttonActionPerformed(evt);
            }
        });

        jButton3.setText("Copy");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Copy");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Copy");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Copy");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel19.setText("Other");

        otherVmComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Wireless HomePhone" }));
        otherVmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherVmComboBoxActionPerformed(evt);
            }
        });

        otherVmTextArea.setColumns(20);
        otherVmTextArea.setRows(5);
        jScrollPane15.setViewportView(otherVmTextArea);

        jButton7.setText("Copy");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout voiceMailTabLayout = new javax.swing.GroupLayout(voiceMailTab);
        voiceMailTab.setLayout(voiceMailTabLayout);
        voiceMailTabLayout.setHorizontalGroup(
            voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(voiceMailTabLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(voiceMailTabLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(62, 62, 62)
                        .addComponent(otherVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(voiceMailTabLayout.createSequentialGroup()
                        .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(voiceMailTabLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(43, 43, 43)
                                .addComponent(bbVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(voiceMailTabLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(androidVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(voiceMailTabLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(47, 47, 47)
                                .addComponent(wnVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(18, 18, 18)
                        .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6)
                            .addComponent(jButton7)))
                    .addGroup(voiceMailTabLayout.createSequentialGroup()
                        .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(voiceMailTabLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vmnodebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vmnodebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(voiceMailTabLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(iosVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        voiceMailTabLayout.setVerticalGroup(
            voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(voiceMailTabLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vmnodebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vmnodebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)))
                .addGap(18, 18, 18)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(iosVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(androidVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(bbVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(wnVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(otherVmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(voiceMailTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Voice Mail", voiceMailTab);

        chatPerHourTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chatPerHourTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Logged in Time", "Sessions", "CPH"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        chatPerHourTable.setGridColor(Color.black);
        chatPerHourTable.setIntercellSpacing(new java.awt.Dimension(2, 1));
        jScrollPane16.setViewportView(chatPerHourTable);
        if (chatPerHourTable.getColumnModel().getColumnCount() > 0) {
            chatPerHourTable.getColumnModel().getColumn(0).setResizable(false);
            chatPerHourTable.getColumnModel().getColumn(1).setResizable(false);
            chatPerHourTable.getColumnModel().getColumn(2).setResizable(false);
            chatPerHourTable.getColumnModel().getColumn(3).setResizable(false);
        }
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        chatPerHourTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        chatPerHourTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        chatPerHourTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        chatPerHourTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        chatPerHourRefreshButton.setText("Refresh");
        chatPerHourRefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatPerHourRefreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(chatPerHourRefreshButton)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(chatPerHourRefreshButton)
                .addContainerGap(334, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Chat Per Hour", jPanel10);

        notesTextArea.setColumns(20);
        notesTextArea.setLineWrap(true);
        notesTextArea.setRows(5);
        notesTextArea.setText("Session ID: 211111111\nCustomer Name/Nom du Client: Jhon Doe\nStatus: Active\nTime: 01:27\nTotal Time: 01:32\nPlatform: Windows 7 Google Chrome\nChannel: NTSD General Support - Wireless\nType: Instant Chat\nPostal Code: A1A1A1\nCTN/Phone: (999) 999-999\n\n\nStatus: white\nService: Rogers_devices\nCreation: 2013-09-03T16:51:20Z\nMSISDN: 19999999999\nIMSI: 123456789012345\nIMEI: 1212121212121212\nDevice: Apple iPhone 5\nDevice pic adress : http://172.16.224.34:8002/webcmd/devicepic?brand=BlackBerry&model=9700\nDevice: Apple iPhone 5\nDevice pic adress : http://172.16.224.34:8002/webcmd/devicepic?brand=Apple&model=iPhone 5\nGroups: rogers\nOTASupport: true\nAPNs: blackberry.net,goam.com,internet.com,isp.apn,ltedata.apn,lteinternet.apn,ltemobile.apn,media.com,rogers-core-appl1.apn\nDeviceCapa:  Ok\nSingle Shot Provisioning: ext1,yes,2012-11-04T12:07:52Z\nSettings: \nHistory:");
        notesTextArea.setWrapStyleWord(true);
        notesTextArea.setInheritsPopupMenu(true);
        notesScrollpane.setViewportView(notesTextArea);

        jButton1.setText("Copy");

        jButton8.setText("Clear");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notesTabLayout = new javax.swing.GroupLayout(notesTab);
        notesTab.setLayout(notesTabLayout);
        notesTabLayout.setHorizontalGroup(
            notesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(notesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notesScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(notesTabLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        notesTabLayout.setVerticalGroup(
            notesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notesTabLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(notesScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(notesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabs.addTab("Notes", notesTab);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(copyNotesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearNotesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(showsidepanelButton))
                    .addComponent(NotesTabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(settingsTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(NotesTabs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyNotesButton)
                    .addComponent(clearNotesButton)
                    .addComponent(showsidepanelButton))
                .addGap(32, 32, 32))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(settingsTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu2.setText("File");

        openNotesEditorMenu.setText("Open Notes Editor");
        openNotesEditorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openNotesEditorMenuActionPerformed(evt);
            }
        });
        jMenu2.add(openNotesEditorMenu);

        jMenuItem2.setText("Chat Per Hour Counter");
        jMenu2.add(jMenuItem2);

        refreshScripts.setText("Refresh Notes");
        refreshScripts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshScriptsActionPerformed(evt);
            }
        });
        jMenu2.add(refreshScripts);

        refreshScriptsMenu.setText("Refresh Scripts");
        jMenu2.add(refreshScriptsMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Clear");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  

    private void noteListBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox1ActionPerformed

        Notes selectedNotes = notes.get(noteListBox1.getSelectedIndex());

        notesTextArea0.setText(notesTextArea0.getText() + "\n" + selectedNotes.getNotes());

    }//GEN-LAST:event_noteListBox1ActionPerformed

    private void noteListBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox2ActionPerformed
        Notes selectedNotes = notes.get(noteListBox2.getSelectedIndex());

        notesTextArea1.setText(notesTextArea1.getText() + "\n" + selectedNotes.getNotes());
    }//GEN-LAST:event_noteListBox2ActionPerformed

    private void noteListBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox3ActionPerformed
        Notes selectedNotes = notes.get(noteListBox3.getSelectedIndex());

        notesTextArea2.setText(notesTextArea2.getText() + "\n" + selectedNotes.getNotes());
    }//GEN-LAST:event_noteListBox3ActionPerformed

    private void noteListBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox4ActionPerformed
        Notes selectedNotes = notes.get(noteListBox4.getSelectedIndex());

        notesTextArea3.setText(notesTextArea3.getText() + "\n" + selectedNotes.getNotes());
    }//GEN-LAST:event_noteListBox4ActionPerformed

    private void noteListBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox5ActionPerformed
        Notes selectedNotes = notes.get(noteListBox5.getSelectedIndex());

        notesTextArea4.setText(notesTextArea4.getText() + "\n" + selectedNotes.getNotes());

        //notesTextArea5.setText(selectedNotes.getNotes())
    }//GEN-LAST:event_noteListBox5ActionPerformed

    private void noteListBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteListBox6ActionPerformed
        Notes selectedNotes = notes.get(noteListBox6.getSelectedIndex());

        notesTextArea5.setText(notesTextArea5.getText() + "\n" + selectedNotes.getNotes());

        //notesTextArea5.setText(selectedNotes.getNotes())
    }//GEN-LAST:event_noteListBox6ActionPerformed

    private void showsidepanelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showsidepanelButtonActionPerformed

        if (getSize().getWidth() == 550) {
            this.setSize(1080, 710);
            showsidepanelButton.setText("-");

        } else {
            this.setSize(550, 710);
            showsidepanelButton.setText("+");
        }

    }//GEN-LAST:event_showsidepanelButtonActionPerformed

    private void subMenuBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuBoxActionPerformed

        updateScriptsBoxes();

    }//GEN-LAST:event_subMenuBoxActionPerformed

    private void copyNotesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNotesButtonActionPerformed
        int selectedtaindex = NotesTabs.getSelectedIndex();

        if (selectedtaindex == 0) {
            copytoClipboard(notesTextArea0.getText());
        } else if (selectedtaindex == 1) {
            copytoClipboard(notesTextArea1.getText());
        } else if (selectedtaindex == 2) {
            copytoClipboard(notesTextArea2.getText());
        } else if (selectedtaindex == 3) {
            copytoClipboard(notesTextArea3.getText());
        } else if (selectedtaindex == 4) {
            copytoClipboard(notesTextArea4.getText());
        } else if (selectedtaindex == 5) {
            copytoClipboard(notesTextArea5.getText());
        } else {
            copytoClipboard("");
        }

    }//GEN-LAST:event_copyNotesButtonActionPerformed
    private void createComponentMap() {
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else {
            return null;
        }
    }
    private void copyEnglishScriptsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyEnglishScriptsButtonActionPerformed
        copytoClipboard(englishScriptTextArea.getText());
    }//GEN-LAST:event_copyEnglishScriptsButtonActionPerformed

    private void copyFrenchScriptsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyFrenchScriptsButtonActionPerformed
        copytoClipboard(frenchScriptTextArea.getText());
    }//GEN-LAST:event_copyFrenchScriptsButtonActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField1.getText());
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField3.getText());
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField2.getText());
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField4.getText());
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField5.getText());
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), jTextField6.getText());
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void notesTextArea0KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea0KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {

            String title = searchCustomerName(notesTextArea0.getText());
            String ctn = searchCtn(notesTextArea0.getText());

            //System.out.println(ctn);
            String formatedText = formateCustomerInformations(notesTextArea0.getText()) + formateDeviceInformations(notesTextArea0.getText());

            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "One");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel2.setText("CTN: ");
                jTextField1.setText(ctn);
                copytoClipboard(ctn);
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea0.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField1.setText(imei);

                jLabel2.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField1.setText("111111111111111");

                jLabel2.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea0.getText();

                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                // System.out.println(notesTextArea0.getText(notesTextArea0.getCaretPosition(), notesTextArea0.getLineCount()+2));
                notesTextArea0.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_notesTextArea0KeyPressed

    private void notesTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            String title = searchCustomerName(notesTextArea1.getText());
            String ctn = searchCtn(notesTextArea1.getText());
            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Two");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel6.setText("CTN: ");
                jTextField3.setText(ctn);
                copytoClipboard(ctn);

            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea1.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField3.setText(imei);

                jLabel6.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField3.setText("111111111111111");

                jLabel6.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea1.getText();

                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                //.out.println(formatedInfo);
                notesTextArea1.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_notesTextArea1KeyPressed

    private void notesTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            String title = searchCustomerName(notesTextArea2.getText());
            String ctn = searchCtn(notesTextArea2.getText());
            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Three");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel4.setText("CTN: ");
                jTextField2.setText(ctn);
                copytoClipboard(ctn);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea2.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField2.setText(imei);

                jLabel4.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField2.setText("111111111111111");

                jLabel4.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea2.getText();

                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                // System.out.println(formatedInfo);
                notesTextArea2.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_notesTextArea2KeyPressed

    private void notesTextArea3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            String title = searchCustomerName(notesTextArea3.getText());
            String ctn = searchCtn(notesTextArea3.getText());
            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Four");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel8.setText("CTN: ");
                jTextField4.setText(ctn);
                copytoClipboard(ctn);
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea3.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField4.setText(imei);

                jLabel8.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField4.setText("111111111111111");

                jLabel8.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea3.getText();

                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                //System.out.println(formatedInfo);
                notesTextArea3.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_notesTextArea3KeyPressed

    private void notesTextArea4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            String title = searchCustomerName(notesTextArea4.getText());

            String ctn = searchCtn(notesTextArea4.getText());
            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Five");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel10.setText("CTN: ");
                jTextField5.setText(ctn);
                copytoClipboard(ctn);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea4.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField5.setText(imei);

                jLabel10.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField5.setText("111111111111111");

                jLabel10.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea4.getText();
                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                //System.out.println(formatedInfo);
                notesTextArea4.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_notesTextArea4KeyPressed

    private void notesTextArea5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesTextArea5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            String title = searchCustomerName(notesTextArea5.getText());
            String ctn = searchCtn(notesTextArea5.getText());
            if (title.equals("")) {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Six");
            } else {
                NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), title);
                jLabel12.setText("CTN: ");
                jTextField6.setText(ctn);
                copytoClipboard(ctn);
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            String imei = validateIMEI(searchImei(notesTextArea5.getText()));
            if (!imei.isEmpty()) {
                copytoClipboard(imei);
                jTextField6.setText(imei);

                jLabel12.setText("IMEI: ");
            } else {
                copytoClipboard("111111111111111");

                jTextField6.setText("111111111111111");

                jLabel12.setText("IMEI: ");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_F11) {

            String formatedInfo = "";
            //copytoClipboard(imei);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            try {

                formatedInfo = notesTextArea5.getText();
                if (formatedInfo.equals("")) {
                    formatedInfo = (String) clipboard.getData(DataFlavor.stringFlavor);
                }

                // System.out.println(formatedInfo);
                notesTextArea5.setText(formateCustomerInformations(formatedInfo) + formateDeviceInformations(formatedInfo));

            } catch (UnsupportedFlavorException ex) {
                //notesTextArea0.setText(formatedInfo);
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_notesTextArea5KeyPressed

    private String searchCtn(String noteText) {
        String ctn = "";
        String formatedPhonenumber = "";
        if (noteText.equals("")) {
            return "";
        } else {

            int ctntindex = noteText.lastIndexOf("PhoneNumber:");

            //System.out.println(ctntindex);
            if (ctntindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                ctn = noteText.substring(ctntindex + 13);
                String[] s = ctn.split("\n");
                formatedPhonenumber = s[0].replaceAll("[^0-9]", "");
                return formatedPhonenumber;
            }

            ctntindex = noteText.lastIndexOf("CTN/Phone:");

            if (ctntindex > -1) {

                ctn = noteText.substring(ctntindex + 12);
                String[] s = ctn.split("\n");
                formatedPhonenumber = s[0].replaceAll("[^0-9]", "");
                return formatedPhonenumber;

            }

            return "";
        }
    }

    private String searchImei(String noteText) {

        String imei = "";

        if (noteText.equals("")) {
            return "";
        } else {
            int imeiindex = noteText.lastIndexOf("IMEI:");

            //System.out.println(ctntindex);
            if (imeiindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                imei = noteText.substring(imeiindex + 6);
                String[] s = imei.split("\n");

                return s[0];

            }
            return "";
        }
    }

    private String validateIMEI(String IMEI) {

        String validIMei = "";

        int sum = 0;

        if (IMEI.length() < 16) {
            return IMEI;
        } else {
            boolean errorflag = false;
            int[] imeiArray = new int[15];

            String[] charArray = IMEI.split("");

            for (String s : charArray) {
                //System.out.println("Test : "+s);
            }

            for (int i = 0; i < 14; i++) {

                //getting ascii value for each character
                imeiArray[i] = Integer.parseInt(charArray[i + 1]);
                validIMei += imeiArray[i] + "";
                // System.out.println(imeiArray[i]+" IMEI ");

                // charArray[i]=IMEI.split("");
                //System.out.println(charArray[i]+"");
                if (i % 2 != 0) {
                    imeiArray[i] = imeiArray[i] * 2;
                }

                while (imeiArray[i] > 9) {
                    imeiArray[i] = (imeiArray[i] % 10) + (imeiArray[i] / 10);
                }

                // System.out.println(imeiArray[i]+" IMEI 2");
            }

            int totalValue = 0;
            for (int j = 0; j < 14; j++) {
                totalValue += imeiArray[j];
                // System.out.println("Total Value is: "+totalValue);

            }
            // System.out.println("Total Value is: "+totalValue);
            if (0 == totalValue % 10) {
                imeiArray[14] = 0;
            } else {

                imeiArray[14] = (10 - (totalValue % 10));
            }

            //Make the new IMEI
            //  for(int i:imeiArray){
            // validIMei+=i+"";
            // }
            return validIMei + imeiArray[14];
        }

    }

    private String formateCustomerInformations(String text) {

        String customerInfromations = "Wireless Live Chat\n";

        customerInfromations = customerInfromations + "Session ID:" + searchSessionID(text) + "\n" + "Client Name: " + searchCustomerName(text)
                + "\nPhoneNumber: " + searchCtn(text) + "\nPostal Code:" + searchPostalCode(text) + "\nChannel:" + searchQueue(text) + "\n---------------------------------------------------------------";

        //System.out.println(customerInfromations);
        return customerInfromations;

    }

    private String searchSessionID(String text) {

        String sessionId = "";
        if (text.equals("")) {
            return "";
        } else {

            int sessionIdIndex = text.lastIndexOf("Session ID:");

            if (sessionIdIndex > -1) {
                sessionId = text.substring(sessionIdIndex + 11);

                String[] s = sessionId.split("\n");

                return s[0];
            }
            return "";

        }

    }

    private String searchPostalCode(String text) {

        String postalcode = "";

        if (text.equals("")) {
            return "";
        } else {
            int postalcodeindex = text.lastIndexOf("Postal Code:");

            //System.out.println(ctntindex);
            if (postalcodeindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                postalcode = text.substring(postalcodeindex + 12);
                String[] s = postalcode.split("\n");

                return s[0].toUpperCase();

            }
            return "";
        }

    }

    private String searchQueue(String text) {

        String Queue = "";

        if (text.equals("")) {
            return "";
        } else {
            int Queueindex = text.lastIndexOf("Channel:");

            //System.out.println(ctntindex);
            if (Queueindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                Queue = text.substring(Queueindex + 8);
                String[] s = Queue.split("\n");

                return s[0];

            }
            return "";
        }

    }

    private String formateDeviceInformations(String text) {

        String deviceInformations = "";

        deviceInformations = "\nDevice:" + searchDevice(text) + "\nDevice Type:" + checkDeviceType(text) + "\nIMEI: " + validateIMEI(searchImei(text)) + "\nIMSI:" + searchIMSI(text) + "\nAPNs:" + searchAPNs(text) + "\n---------------------------------------------------------------";

        //System.out.println(deviceInformations);
        return deviceInformations;
    }

    private String searchIMSI(String text) {

        String IMSI = "";

        if (text.equals("")) {
            return "";
        } else {
            int IMSIindex = text.lastIndexOf("IMSI:");

            //System.out.println(ctntindex);
            if (IMSIindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                IMSI = text.substring(IMSIindex + 5);
                String[] s = IMSI.split("\n");

                return s[0];

            }
            return "";
        }

    }

    private String searchDevice(String text) {

        String device = "";

        if (text.equals("")) {
            return "";
        } else {
            int deviceindex = text.lastIndexOf("Device:");

            //System.out.println(ctntindex);
            if (deviceindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                device = text.substring(deviceindex + 7);
                String[] s = device.split("\n");

                return s[0];

            }
            return "";
        }

    }

    private String checkDeviceType(String text) {

        String devicetype = "";

        if (text.equals("")) {
            return "";
        } else {
            int devicetypeindex = text.lastIndexOf("Warning:");

            //System.out.println(ctntindex);
            if (devicetypeindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                devicetype = text.substring(devicetypeindex + 8);
                String[] s = devicetype.split("\n");

                // if(s[0].equals("1 Imei not registered"))
                return " **** NON Rogers Device ******";
                //else 
                //return " Rogers Device";

            }
            return "";
        }
    }

    private String searchAPNs(String text) {
        String APNs = "";

        if (text.equals("")) {
            return "";
        } else {
            int APNsindex = text.lastIndexOf("APNs:");

            //System.out.println(ctntindex);
            if (APNsindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                APNs = text.substring(APNsindex + 5);
                String[] s = APNs.split("\n");

                return s[0].replaceAll("\n", "");

            }
            return "";
        }
    }
    private void clearNotesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearNotesButtonActionPerformed
        clearTabs();
    }//GEN-LAST:event_clearNotesButtonActionPerformed

    private void englishIssueCopyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_englishIssueCopyBtnActionPerformed
        copytoClipboard(englihIssuetTextArea.getText());
    }//GEN-LAST:event_englishIssueCopyBtnActionPerformed

    private void modelBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelBoxActionPerformed
        updateIssueTittleBox(selectedMakeIndex, modelBox.getSelectedIndex());
    }//GEN-LAST:event_modelBoxActionPerformed

    private void issueBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_issueBoxActionPerformed

        String selectedIssue = (String) issueBox.getSelectedItem();
        Issue issue = null;

        issue = devices.get(selectedMakeIndex).getModels().get(modelBox.getSelectedIndex()).getIssues().get(issueBox.getSelectedIndex());
        // issueText.setText(issue.getIssueName());

        // englishIssueTitleText.setText(issue.getIssueSteps().get(0).getStepTitle());
        englihIssuetTextArea.setText(issue.getIssueSteps().get(0).getStepText());
        //frenchIssueTtileText.setText(Frenchparser.decodeToAcutesHTML(issue.getIssueSteps().get(1).getStepTitle()));
        frenchIssueTextArea.setText(Frenchparser.decodeToAcutesHTML(issue.getIssueSteps().get(1).getStepText()));

    }//GEN-LAST:event_issueBoxActionPerformed

    private void frnechIssueCopyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frnechIssueCopyBtnActionPerformed
        copytoClipboard(frenchIssueTextArea.getText());
    }//GEN-LAST:event_frnechIssueCopyBtnActionPerformed

    /**
     * *
     *
     *
     * Populating vm node button
     */
    private void vmnodeboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vmnodeboxActionPerformed
        vmnodebutton.setText(voicemailnode[vmnodebox.getSelectedIndex()]);
    }//GEN-LAST:event_vmnodeboxActionPerformed

    private void wnVmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wnVmComboBoxActionPerformed
        String stepsV6 = "Can you please dial the following code on your device as is :\n"
                + " \n"
                + "    *5005*86*" + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "# and then call" + "\nPlease enter  the asterisks, Number sign and the PLUS sign.\n"
                + "The  PLUS sign can be obtained if you press and hold down Zero";
        String stepsV7 = "Can you please dial the following code on your device as is :\n"
                + " \n"
                + "    *5005*86*" + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "# and then call" + "\nPlease enter  the asterisks, Number sign and the PLUS sign.\n"
                + "The  PLUS sign can be obtained if you press and hold down Zero";

        String nokia710 = "Select phone icon > > call settings and the voicemail number.\n"
                + "\n"
                + "Enter " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + ", and select save.";

        if (wnVmComboBox.getSelectedIndex() == 2) {
            windowsVMTextArea.setText(nokia710);
        }

    }//GEN-LAST:event_wnVmComboBoxActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        copytoClipboard(windowsVMTextArea.getText());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void vmnodebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vmnodebuttonActionPerformed
        copytoClipboard(vmnodebutton.getText());
    }//GEN-LAST:event_vmnodebuttonActionPerformed

    private void iosVmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iosVmComboBoxActionPerformed

        String stepsV6 = "Can you please dial the following code on your device as is :\n"
                + " \n"
                + "    *5005*86*" + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "# and then call" + "\nPlease enter  the asterisks, Number sign and the PLUS sign.\n"
                + "The  PLUS sign can be obtained if you press and hold down Zero";
        String stepsV7 = "Can you please dial the following code on your device as is :\n"
                + " \n"
                + "    *5005*86*" + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "# and then call" + "\nPlease enter  the asterisks, Number sign and the PLUS sign.\n"
                + "The  PLUS sign can be obtained if you press and hold down Zero";

        if (iosVmComboBox.getSelectedIndex() == 0) {
            iosVmTextarea.setText(stepsV7);
        } else {
            iosVmTextarea.setText(stepsV6);
        }
    }//GEN-LAST:event_iosVmComboBoxActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        copytoClipboard(iosVmTextarea.getText());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void bbVmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbVmComboBoxActionPerformed

        String stepsV10 = "Can you please go to  Phone > Swipe down from the top of the screen > Settings > Voice Mail and enter"
                + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()])
                + " Including the PLUS sign and Number 1 :";
        String stepsV6;
        stepsV6 = "In the phone application, press the Menu key > Click Options > Click Voice Mail > Type a voice mail access number   and enter " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "\nPlease enter  the asterisks, Number sign and the PLUS sign.\n"
                + " Including the PLUS sign and Number 1 :";
        String stepsV5 = "Press the Send key (green key).\n"
                + "Press the Menu button.( The key with blackberry logo on it)\n"
                + "Select Options.\n"
                + "Select Voicemail.\n"
                + "Input " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + " in the Access Number field. \n"
                + "Note: Make sure the + and 1 is entered before the actual number.\n"
                + "Press the Menu.\n"
                + "Select Save.";

        if (bbVmComboBox.getSelectedIndex() == 0) {
            blackberryVMTextArea.setText(stepsV10);
        } else if (bbVmComboBox.getSelectedIndex() == 1) {
            blackberryVMTextArea.setText(stepsV6);
        } else {
            blackberryVMTextArea.setText(stepsV5);
        }
    }//GEN-LAST:event_bbVmComboBoxActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        copytoClipboard(blackberryVMTextArea.getText());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void openNotesEditorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openNotesEditorMenuActionPerformed
        // user = System.getProperty("user.name");
        //  cmd = "java -jar C/Users/" + user + "/appdata/Roaming/<folder>/<file>.jar";
        String cmd = "java -jar tracker_v01.jar";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openNotesEditorMenuActionPerformed

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        clearTabs();
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        notesTextArea.setText("");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void chatPerHourRefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatPerHourRefreshButtonActionPerformed
        try {
            populateChatPerHourTable();
        } catch (IOException ex) {
            Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_chatPerHourRefreshButtonActionPerformed

    private void androidVmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_androidVmComboBoxActionPerformed
        String steps4x = "Select the Phone icon.\n"
                + "Press the Menu button.\n"
                + "Select Call Settings.\n"
                + "Select Voicemail Settings.\n"
                + "Select Voicemail Number. Enter " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]);

        String steps2x = "Open Applications.\n"
                + "Select Settings.\n"
                + "Select Call Settings.\n"
                + "Select Voicemail Settings.\n"
                + "Select Voicemail Number.\n"
                + "Enter " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]) + "\n"
                + "Tap OK.";

        String steps1x = "Go to Menu.\n"
                + "Choose Settings.\n"
                + "Select Call Settings.\n"
                + "Go to Voicemail.\n"
                + "Enter " + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]);

        if (androidVmComboBox.getSelectedIndex() == 0) {
            androidVmTextarea.setText(steps4x);
        }
        if (androidVmComboBox.getSelectedIndex() == 1) {
            androidVmTextarea.setText(steps2x);
        } else {
            androidVmTextarea.setText(steps1x);
        }
    }//GEN-LAST:event_androidVmComboBoxActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        copytoClipboard(otherVmTextArea.getText());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void otherVmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherVmComboBoxActionPerformed

        String wirelessHP = "Have a handset connected to the Wireless Home phone box.\n"
                + "To program the Voicemail number dial the following code from your home phone handset:\n"
                + " \n" + "*983*866*" + fomrateVoiceMailRetrivalNumber(voicemailnode[vmnodebox.getSelectedIndex()]).substring(2) + "#";

        if (otherVmComboBox.getSelectedIndex() == 0) {
            otherVmTextArea.setText(wirelessHP);
        }
    }//GEN-LAST:event_otherVmComboBoxActionPerformed

    private void refreshScriptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshScriptsActionPerformed
        try {
            updateAllNotes();
        } catch (IOException ex) {
            Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshScriptsActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        copytoClipboard(androidVmTextarea.getText());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void copytoClipboard(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    private String fomrateVoiceMailRetrivalNumber(String text) {

        String[] result = text.split("-");

        String resultedNumber = "";

        for (int x = 0; x < result.length; x++) {
            resultedNumber += result[x];
        }

        return resultedNumber;

    }

    /*private String voiceMailSteps(int selectedVersion){
        
     String iosVmSteps[] ={"Can you please dial the following code on your device as is :\n" +
     " \n" +
     "    *5005*86*+16472789951# and then call"};
     }*/
    private String searchCustomerName(String noteText) {
        String customername = "";

        if (noteText.equals("")) {
            return "";
        } else {

            int customernamefirstindex = noteText.lastIndexOf("Customer Name/Nom du Client:");

            //System.out.println(customernamefirstindex);
            if (customernamefirstindex > -1) {
                // customernamefirstindex=noteText.lastIndexOf("Client name:")+10;

                customername = noteText.substring(customernamefirstindex + 28);
                String[] s = customername.split("\n");

                return s[0];

            }

            customernamefirstindex = noteText.lastIndexOf("Client Name:");

            if (customernamefirstindex > -1) {

                customername = noteText.substring(customernamefirstindex + 12);
                String[] s = customername.split("\n");

                return s[0];

            }

            return "";
        }

    }

    private void updateSubmenuTitleBox(int i) {

        if (i > -1 && (scripts.getMenus().get(i).getSubMenus() != null)) {
            List<Mainmenu> mMenu = scripts.getMenus();

            selectedMenu = mMenu.get(i);
            List<Submenu> submenus = selectedMenu.getSubMenus();
            // Sorting submenus
            Collections.sort(submenus);
            String subMenuBoxText[] = new String[submenus.size()];

            int j = 0;
            for (Submenu smenu : submenus) {
                subMenuBoxText[j] = smenu.getsSubMenuTitle();
                j++;
            }

            subMenuBox.setModel(new javax.swing.DefaultComboBoxModel(subMenuBoxText));

        }

    }

    private void updateScriptsBoxes() {
        selectecSubmenTitle = (String) subMenuBox.getSelectedItem();
        Submenu selectedSubmeu = null;

        selectedSubmeu = selectedMenu.getSubMenus().get(subMenuBox.getSelectedIndex());

        englishScriptlbl.setText(selectedSubmeu.getSubMenuScripts().get(0).getScriptTitle());
        englishScriptTextArea.setText(selectedSubmeu.getSubMenuScripts().get(0).getScriptText());
        frenchScriptlbl.setText(Frenchparser.decodeToAcutesHTML(selectedSubmeu.getSubMenuScripts().get(1).getScriptTitle()));
        frenchScriptTextArea.setText(Frenchparser.decodeToAcutesHTML(selectedSubmeu.getSubMenuScripts().get(1).getScriptText()));
    }

    private void clearTabs() {
        int selectedtaindex = NotesTabs.getSelectedIndex();

        if (selectedtaindex == 0) {
            notesTextArea0.setText("");
            jTextField1.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "One");
            jLabel2.setText("CTN:");
        } else if (selectedtaindex == 1) {
            notesTextArea1.setText("");
            jTextField3.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Two");
            jLabel6.setText("CTN:");
        } else if (selectedtaindex == 2) {
            notesTextArea2.setText("");
            jTextField2.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Three");
            jLabel4.setText("CTN:");
        } else if (selectedtaindex == 3) {
            notesTextArea3.setText("");
            jTextField4.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Four");
            jLabel8.setText("CTN:");
        } else if (selectedtaindex == 4) {
            notesTextArea4.setText("");
            jTextField5.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Five");
            jLabel10.setText("CTN:");
        } else if (selectedtaindex == 5) {
            notesTextArea5.setText("");
            jTextField6.setText("");
            NotesTabs.setTitleAt(NotesTabs.getSelectedIndex(), "Six");
            jLabel12.setText("CTN:");
        } else {
            copytoClipboard("");
        }
    }

    private void populateChatPerHourTable() throws IOException, ParseException {
        Date today = new Date();
        DateFormat df = DateFormat.getInstance();
        String sd = df.format(today);
        int totalSessions;
        long totallogintime;
        int totalHours = 0, totalMinutes = 0;
        double chatPerHour = 0.00;
        String CPH, loggedInTimeString;
        try {

            totalSessions = countSessions();

            totallogintime = countTotalLoginTime();

            //System.out.println("Total Sessions : "+totalSessions+"\nTotal Time :"+totallogintime);
            totalHours = (int) totallogintime / 60;
            totalMinutes = (int) totallogintime % 60;
            chatPerHour = (double) totalSessions / ((float) totallogintime / 60);
            CPH = String.format("%.2f", chatPerHour);
            loggedInTimeString = String.format("%d:%02d", totalHours, totalMinutes);

            chatPerHourTable.getModel().setValueAt(sd, 0, 0);
            chatPerHourTable.getModel().setValueAt(loggedInTimeString, 0, 1);
            chatPerHourTable.getModel().setValueAt(totalSessions, 0, 2);
            chatPerHourTable.getModel().setValueAt(CPH, 0, 3);

            //System.out.println("Total Hours : "+ totalHours+" Total Minutes: "+totalMinutes);
            //System.out.println(linesIterator.get(0).toString());
            //System.out.println(logFilename);
        } catch (FileNotFoundException ex) {
            System.out.println(" file is not found");
        }
    }

    private int countSessions() throws IOException {

        LineIterator lines = FileUtils.lineIterator(file);
        String line = "";
        Pattern sessionPattern, timePattern;

        sessionPattern = Pattern.compile("Session \\d+ ready");
        timePattern = Pattern.compile("^[\\[]*.*]");

        Matcher matchSession, matchTime;

        Set<String> uniqueSessions = new LinkedHashSet<String>();
        String[] splitsStrings = new String[3];

        try {
            while (lines.hasNext()) {

                line = lines.nextLine();

                matchSession = sessionPattern.matcher(line);

                //Counting Unique sessions
                if (matchSession.find()) {
                    splitsStrings = line.substring(matchSession.start()).split(" ");
                    uniqueSessions.add(splitsStrings[1]);

                }

            }
        } finally {
            LineIterator.closeQuietly(lines);
        }

        //System.out.println(uniqueSessions.toString());
        return uniqueSessions.size();

    }

    private long countTotalLoginTime() throws IOException, ParseException {

        double totalLoginTime = 0.00;
        long totalLoggedinHours = 0;
        //  LineIterator linesIterator=FileUtils.lineIterator(file);
        List<String> lines = new ArrayList<String>();
        lines = FileUtils.readLines(file);

        String timeWithoutBracket = "";

        Pattern sessionReadyPattern, sessionLogoutPattern, timePattern;

        sessionReadyPattern = Pattern.compile("Console ready");
        sessionLogoutPattern = Pattern.compile("Logout successful");
        timePattern = Pattern.compile("^[\\[]*.*]");

        Matcher matchSessionReady, matchSessionLogout, matchTime;

        Date startTime, endTime;
        DateTime startDatetime = null, endDatetime = null;
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aaa");
        Duration duration;

        try {

            for (int i = 0; i < lines.size(); i++) {

                // Checking start and end time
                if (i == 0 || (i == (lines.size() - 1))) {

                    matchTime = timePattern.matcher(lines.get(i));
                    if (matchTime.find()) {
                        timeWithoutBracket = lines.get(i).substring(matchTime.start() + 1, matchTime.end() - 1);

                        if (i == 0) {
                            startTime = dateformat.parse(timeWithoutBracket);
                            startDatetime = new DateTime(startTime);
                            //System.out.println(startDatetime.toString());
                        } else {
                            endTime = dateformat.parse(timeWithoutBracket);
                            endDatetime = new DateTime(endTime);
                            //System.out.println(endDatetime.toString());
                        }
                    }
                }

                //Checking session ready status, and calculating start time  
                matchSessionReady = sessionReadyPattern.matcher(lines.get(i));
                if (matchSessionReady.find()) {
                    matchTime = timePattern.matcher(lines.get(i));
                    if (matchTime.find()) {
                        timeWithoutBracket = lines.get(i).substring(matchTime.start() + 1, matchTime.end() - 1);
                        startTime = dateformat.parse(timeWithoutBracket);
                        startDatetime = new DateTime(startTime);
                    }
                }

                //Checking session Logout status, and calculating Logout time  
                matchSessionLogout = sessionLogoutPattern.matcher(lines.get(i));
                if (matchSessionLogout.find()) {
                    matchTime = timePattern.matcher(lines.get(i));
                    if (matchTime.find()) {
                        timeWithoutBracket = lines.get(i).substring(matchTime.start() + 1, matchTime.end() - 1);
                        endTime = dateformat.parse(timeWithoutBracket);
                        endDatetime = new DateTime(endTime);
                    }
                }

                if (startDatetime != null && endDatetime != null) {

                    duration = new Duration(startDatetime, endDatetime);
                    totalLoggedinHours += duration.getStandardMinutes();
                    startDatetime = null;
                    endDatetime = null;
                    // System.out.println(totalLoggedinHours);

                }
            }

        } finally {
            //  LineIterator.closeQuietly(linesIterator);
        }

        return totalLoggedinHours;

    }

    //This one listens for edits that can be undone.
    protected class MyUndoableEditListener
            implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tracker_notesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tracker_notesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tracker_notesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tracker_notesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new tracker_notesUI().setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(tracker_notesUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ModelLvl;
    private javax.swing.JTabbedPane NotesTabs;
    private javax.swing.JComboBox androidVmComboBox;
    private javax.swing.JTextArea androidVmTextarea;
    private javax.swing.JComboBox bbVmComboBox;
    private javax.swing.JTextArea blackberryVMTextArea;
    private javax.swing.JTable chatPerHourTable;
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    private javax.swing.JButton clearNotesButton;
    private javax.swing.JButton copyEnglishScriptsButton;
    private javax.swing.JButton copyFrenchScriptsButton;
    private javax.swing.JButton copyNotesButton;
    private javax.swing.JPanel devicesettingsPanel;
    private javax.swing.JTextArea englihIssuetTextArea;
    private javax.swing.JButton englishIssueCopyBtn;
    private javax.swing.JTextArea englishScriptTextArea;
    private javax.swing.JLabel englishScriptlbl;
    private javax.swing.JTextArea frenchIssueTextArea;
    private javax.swing.JTextArea frenchScriptTextArea;
    private javax.swing.JLabel frenchScriptlbl;
    private javax.swing.JButton frnechIssueCopyBtn;
    private javax.swing.JComboBox iosVmComboBox;
    private javax.swing.JTextArea iosVmTextarea;
    private javax.swing.JComboBox issueBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JComboBox modelBox;
    private javax.swing.JComboBox noteListBox1;
    private javax.swing.JComboBox noteListBox2;
    private javax.swing.JComboBox noteListBox3;
    private javax.swing.JComboBox noteListBox4;
    private javax.swing.JComboBox noteListBox5;
    private javax.swing.JComboBox noteListBox6;
    private javax.swing.JScrollPane notesScrollpane;
    private javax.swing.JPanel notesTab;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JTextArea notesTextArea0;
    private javax.swing.JTextArea notesTextArea1;
    private javax.swing.JTextArea notesTextArea2;
    private javax.swing.JTextArea notesTextArea3;
    private javax.swing.JTextArea notesTextArea4;
    private javax.swing.JTextArea notesTextArea5;
    private javax.swing.JMenuItem openNotesEditorMenu;
    private javax.swing.JComboBox otherVmComboBox;
    private javax.swing.JTextArea otherVmTextArea;
    private javax.swing.JMenuItem refreshScripts;
    private javax.swing.JMenuItem refreshScriptsMenu;
    private javax.swing.JPanel scriptsPanel;
    private javax.swing.JTabbedPane settingsTabs;
    private javax.swing.JPanel showDeviceSettingsPanel;
    private javax.swing.JPanel showMenuPanel;
    private javax.swing.JButton showsidepanelButton;
    private javax.swing.JComboBox subMenuBox;
    private javax.swing.JComboBox vmnodebox;
    private javax.swing.JButton vmnodebutton;
    private javax.swing.JPanel voiceMailTab;
    private javax.swing.JTextArea windowsVMTextArea;
    private javax.swing.JComboBox wnVmComboBox;
    // End of variables declaration//GEN-END:variables

    private void genrateScriptsButtons() {
        showMenuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //showMenuPanel.setPreferredSize(new Dimension(150,128));  
        //showMenuPanel.setMaximumSize(showMenuPanel.getPreferredSize());
        int i = 0;
        for (; i < scripts.getMenus().size(); ++i) {
            final JButton menuButton = new JButton(scripts.getMenus().get(i).getMenuName());
            final int j = i;
            //menuButton.setContentAreaFilled(false);
            menuButton.setOpaque(true);

            menuButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // menuButton.setBackground(Color.blue);
                    updateSubmenuTitleBox(j);
                    updateScriptsBoxes();
                    //this.setBackground(Color.RED);
                    //this.setOpaque(true);
                }
            });
            showMenuPanel.add(menuButton);
            //showMenuPanel.setVisible(false);
        }

    }

    private void genrateDeviceSettingButtons() {
        showDeviceSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        showDeviceSettingsPanel.setPreferredSize(new Dimension(459, 150));
        showDeviceSettingsPanel.setMaximumSize(showMenuPanel.getPreferredSize());
        int i = 0;
        for (; i < devices.size(); ++i) {
            final JButton menuButton = new JButton(devices.get(i).getMakeName());
            final int j = i;
            //menuButton.setContentAreaFilled(false);
            menuButton.setOpaque(true);

            menuButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    updateModelTitleBox(j);
                    updateIssueTittleBox();
                    englihIssuetTextArea.setText("");
                    frenchIssueTextArea.setText("");

                }
            });
            showDeviceSettingsPanel.add(menuButton);

        }

    }

    private void updateModelTitleBox(int i) {

        List<Model> mModel = devices.get(i).getModels();

        String modelBoxText[] = new String[mModel.size()];

        int j = 0;
        for (Model model : mModel) {
            modelBoxText[j] = model.getModelName();
            j++;
        }

        selectedMakeIndex = i;

        modelBox.setModel(new javax.swing.DefaultComboBoxModel(modelBoxText));

    }

    private void updateIssueTittleBox(int makeIndex, int modelIndex) {

        String issueBoxText[] = new String[devices.get(makeIndex).getModels().get(modelIndex).getIssues().size()];
        List<Issue> issues = devices.get(makeIndex).getModels().get(modelIndex).getIssues();

        int j = 0;
        for (Issue issue : issues) {
            issueBoxText[j] = issue.getIssueName();
            j++;
        }

        issueBox.setModel(new javax.swing.DefaultComboBoxModel(issueBoxText));

    }

    private void updateIssueTittleBox() {

        issueBox.setModel(new javax.swing.DefaultComboBoxModel());

    }
}
