/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import SQLConnection.Connect;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import org.apache.commons.validator.routines.DateValidator;

/**
 *
 * @author Matthew Wallace
 */
public class InsertRecruitsPage extends JFrame implements ActionListener{
    
    private JLabel lbldesc;
    
    private JLabel lblfname;
    private JLabel lbllname;
    private JLabel lbldob;
    private JLabel lblphone;
    private JLabel lblrace;
    private JLabel lblsex;
    private JLabel lbladdress;
    private JLabel lblcity;
    private JLabel lblstate;
    private JLabel lblzip;
    private JLabel lblla_qualified;
    private JLabel lblla_screen_date;
    private JLabel lblfst;
    private JLabel lblst;
    private JLabel lblap;
    private JLabel lblse;
    private JLabel lblss;
    private JLabel lblcontact_lens;
    private JLabel lblnotes;
    private JLabel lbltypes;
    
    private JTextField txtfname;
    private JTextField txtlname;
    private JTextField txtdob;
    private JTextField txtphone;
    private JTextField txtrace;
    private JComboBox cbsex;
    private JTextField txtaddress;
    private JTextField txtcity;
    private JTextField txtstate;
    private JTextField txtzip;
    private JComboBox cbla_qualified;
    private JTextField txtla_screen_date;
    private JComboBox cbfst;
    private JComboBox cbst;
    private JComboBox cbap;
    private JComboBox cbse;
    private JComboBox cbss;
    private JComboBox cbcontact_lens;
    private JTextArea tanotes;
    private JScrollPane spnotes;
    private JList types;
    
    private JButton btnSubmit;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    Connect conn;
    
    public InsertRecruitsPage(Image iconImage){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(1200,550));
        this.setTitle("Add a New Recruit");
        this.setIconImage(iconImage);
        
        conn = new Connect();
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel("Submit all available data for the new recruit:");
        
        lblfname = new JLabel("First name:");
        lbllname = new JLabel("Last name:");
        lbldob = new JLabel("DOB (yyyy-mm-dd):");
        lblphone = new JLabel("Phone #:");
        lblrace = new JLabel("Race:");
        lblsex = new JLabel("Sex:");
        lbladdress = new JLabel("Address:");
        lblcity = new JLabel("City:");
        lblstate = new JLabel("State:");
        lblzip = new JLabel("ZIP:");
        lblla_qualified = new JLabel("LA Qualified:");
        lblla_screen_date = new JLabel("LA Screen Date:");
        lblfst = new JLabel("FST:");
        lblst = new JLabel("ST:");
        lblap = new JLabel("AP:");
        lblse = new JLabel("SE:");
        lblss = new JLabel("SS:");
        lblcontact_lens = new JLabel("Contact lens:");
        lblnotes = new JLabel("Notes:");
        lbltypes = new JLabel("Preferred Study Types:");
        
    
        txtfname = new JTextField();
        txtlname = new JTextField();
        txtdob = new JTextField();
        txtphone = new JTextField();
        txtrace = new JTextField();
        String[] sexes = {"", "F", "M", "Non-binary"};
        cbsex = new JComboBox(sexes);
        txtaddress = new JTextField();
        txtcity = new JTextField();
        txtstate = new JTextField();
        txtzip = new JTextField();
        String[] yes_no = {"", "Y", "N"};
        cbla_qualified = new JComboBox(yes_no);
        txtla_screen_date = new JTextField();
        String[] fstitems = {"", "1", "2", "3", "4", "5", "6"};
        String[] stitems = {"", "O", "D", "N", "O/D"};
        cbfst = new JComboBox(fstitems);
        cbst = new JComboBox(stitems);
        cbap = new JComboBox(yes_no);
        cbse = new JComboBox(yes_no);
        cbss = new JComboBox(yes_no);
        tanotes = new JTextArea(20,10);
        tanotes.setEditable(true);
        tanotes.setLineWrap(true);
        tanotes.setWrapStyleWord(true);
        spnotes = new JScrollPane(tanotes);
        spnotes.setPreferredSize(new Dimension(100,100));
        // get study types, add to list
        String[] studyTypes = conn.getStudyTypes();
        types = new JList(studyTypes);
        types.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
        
        cbcontact_lens = new JComboBox(yes_no);
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        btnSubmit.setPreferredSize(new Dimension(30,25));
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        
        
        //lbldesc
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 6; gbc.gridheight = 1;
        gbc.weightx = 0.0; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        //this.add(lbldesc, gbc);
        
        // lblfname
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lblfname, gbc);
        
        // lbllname
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbllname, gbc);
        
        // lbldob
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbldob, gbc);
        
        // lblphone
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblphone, gbc);
        
        //lblrace
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblrace, gbc);
        
        //lblsex
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsex, gbc);
        
        //txtfname
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,0);
        this.add(txtfname, gbc);
        
        //txtlname
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtlname, gbc);
        
        //txtdob
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtdob, gbc);
        
        //txtphone
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtphone, gbc);
        
        //txtrace
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtrace, gbc);
        
        //txtsex
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbsex, gbc);
        
        //lbladdress
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,30,0,0);
        this.add(lbladdress, gbc);
        
        //lblcity
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblcity, gbc);
        
        //lblstate
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblstate, gbc);
        
        //lblzip
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblzip, gbc);
        
        gbc.gridx = 2; gbc.gridy = 5;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblla_screen_date, gbc);
        
        gbc.gridx = 3; gbc.gridy = 5;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtla_screen_date, gbc);
        
        gbc.gridx = 2; gbc.gridy = 6;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblla_qualified, gbc);
        
        gbc.gridx = 3; gbc.gridy = 6;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbla_qualified, gbc);
        
        //txtaddress
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,0);
        this.add(txtaddress, gbc);
        
        //txtcity
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtcity, gbc);
        
        //txtstate
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtstate, gbc);
        
        //txtzip
        gbc.gridx = 3; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtzip, gbc);
        
        //lblfst
        gbc.gridx = 4; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,30,0,0);
        this.add(lblfst, gbc);
        
        //lblst
        gbc.gridx = 4; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblst, gbc);
        
        //lblap
        gbc.gridx = 4; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblap, gbc);
        
        //lblse
        gbc.gridx = 4; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblse, gbc);
        
        //lblss
        gbc.gridx = 4; gbc.gridy = 5;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblss, gbc);
        
        //lblcontact_lens
        gbc.gridx = 4; gbc.gridy = 6;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblcontact_lens, gbc);
        
        //txtfst
        gbc.gridx = 5; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,20);
        this.add(cbfst, gbc);
        
        //txtst
        gbc.gridx = 5; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbst, gbc);
        
        //txtap
        gbc.gridx = 5; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbap, gbc);
        
        //txtse
        gbc.gridx = 5; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbse, gbc);
        
        //txtss
        gbc.gridx = 5; gbc.gridy = 5;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbss, gbc);
        
        //cbcontact_lens
        gbc.gridx = 5; gbc.gridy = 6;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbcontact_lens, gbc);
        
        gbc.gridx = 6; gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbltypes, gbc);
        
        gbc.gridx = 7; gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.gridheight = 3;
        gbc.insets = new Insets(20,0,0,30);
        this.add(types, gbc);
        
        gbc.gridx = 6; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblnotes, gbc);
        
        gbc.gridx = 7; gbc.gridy = 4;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.gridheight = 2;
        gbc.insets = new Insets(0,0,0,30);
        this.add(spnotes, gbc);
        
        //btnSubmit
        gbc.gridx = 6; gbc.gridy = 6;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.insets = new Insets(0,20,0,20);
        this.add(btnSubmit, gbc);
        
        
   
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            if (isValidInput()){
                String[] insertVals = new String[19];
                insertVals[0] = txtfname.getText();
                insertVals[1] = txtlname.getText();
                insertVals[2] = txtphone.getText();
                insertVals[3] = txtrace.getText();
                insertVals[4] = cbsex.getSelectedItem().toString();
                insertVals[5] = txtaddress.getText();
                insertVals[6] = txtcity.getText();
                insertVals[7] = txtstate.getText();
                insertVals[8] = txtzip.getText();
                insertVals[9] = cbfst.getSelectedItem().toString();
                insertVals[10] = cbst.getSelectedItem().toString();
                insertVals[11] = cbap.getSelectedItem().toString();
                insertVals[12] = cbse.getSelectedItem().toString();
                insertVals[13] = cbss.getSelectedItem().toString();
                insertVals[14] = cbcontact_lens.getSelectedItem().toString();
                insertVals[15] = txtdob.getText();
                insertVals[16] = tanotes.getText();
                insertVals[17] = cbla_qualified.getSelectedItem().toString();
                insertVals[18] = txtla_screen_date.getText();
                

                Connect conn = new Connect();
                conn.insertRecruits(insertVals);
                
                
                // get selected items from jlist
                // get most recent recruit id (the one we just added)
                // add one at a time to table
                String recruitid = conn.getMostRecentRecruitID();
                List<String> selectedTypes = new ArrayList<>();
                selectedTypes = types.getSelectedValuesList();
                
                for (String item:selectedTypes){
                    conn.insertRecruitStudyType(recruitid, item);
                }
                

                txtfname.setText("");
                txtlname.setText("");
                txtdob.setText("");
                txtphone.setText("");
                txtrace.setText("");
                cbsex.setSelectedItem("");
                txtaddress.setText("");
                txtcity.setText("");
                txtstate.setText("");
                txtzip.setText("");
                txtla_screen_date.setText("");
                cbla_qualified.setSelectedItem("");
                cbfst.setSelectedItem("");
                cbst.setSelectedItem("");
                cbap.setSelectedItem("");
                cbse.setSelectedItem("");
                cbss.setSelectedItem("");
                cbcontact_lens.setSelectedItem("");
                tanotes.setText("");
                
                
                int[] idxs = {};
                types.setSelectedIndices(idxs);

            }else{
                JOptionPane.showMessageDialog(null, "<html>First name, last name, and phone number are required<br>Dates needs to be in the format 'yyyy-mm-dd'</html>");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(){
        boolean isValid = txtdob.getText().equals("") | DateValidator.getInstance().isValid(txtdob.getText(), "yyyy-MM-dd");
        
        isValid = isValid && txtla_screen_date.equals("") | DateValidator.getInstance().isValid(txtla_screen_date.getText(), "yyyy-MM-dd");
        
        return isValid && !txtfname.getText().equals("") &&
                !txtlname.getText().equals("") &&
                !txtphone.getText().equals("");
    }
    
}


