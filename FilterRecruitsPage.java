/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;
import SQLConnection.Connect;

import java.util.ArrayList;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import org.apache.commons.validator.routines.DateValidator;
/**
 *
 * @author joe_admin
 */
public class FilterRecruitsPage extends JFrame implements ActionListener{
    private final JLabel lbldesc;
    private final JLabel lbllower;
    private final JLabel lblupper;
    private final JLabel lblsex;
    private final JLabel lblfst;
    private final JLabel lblap;
    private final JLabel lblst;
    private final JLabel lblse;
    private final JLabel lblss;
    private final JLabel lblcontacts;
    private final JLabel lblnumpanels;
    private final JLabel lblnumstudies;
    private final JLabel lblla_qualified;
    private final JLabel lbltypes;
    
    private final JTextField txtlower;
    private final JTextField txtupper;
    private final JComboBox cbsex;
    private final JComboBox cbfst;
    private final JComboBox cbap;
    private final JComboBox cbst;
    private final JComboBox cbse;
    private final JComboBox cbss;
    private final JComboBox cbcontacts;
    private final JComboBox cbnum_panels;
    private final JComboBox cbnum_studies;
    private final JComboBox cbla_qualified;
    private final JList types;
    private final JButton btnRetrieve;
    
    
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    private Image _iconImage;
    Connect conn;
    
    public FilterRecruitsPage(Image iconImage){
        _iconImage = iconImage;
        // init frame params
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(900,600));
        this.setLayout(new GridBagLayout());
        this.setIconImage(iconImage);
        this.setTitle("Filter Recruits");
        
        conn = new Connect();
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        
        lbldesc = new JLabel("Choose fields below, leave blank to select all for that field:");
        lbllower = new JLabel("Low age:");
        lblupper = new JLabel("High age:");
        lblsex = new JLabel("Sex:");
        lblfst = new JLabel("FST:");
        lblap = new JLabel("AP:");
        lblst = new JLabel("ST:");
        lblse = new JLabel("SE:");
        lblss = new JLabel("SS:");
        lblcontacts = new JLabel("Contacts:");
        lblnumpanels = new JLabel("Number of current panels:");
        lblnumstudies = new JLabel("Number of current studies:");
        lblla_qualified = new JLabel("LA Qualified:");
        lbltypes = new JLabel("Preferred Study Type:");
        
        txtlower = new JTextField();
        txtupper = new JTextField();
        String[] cbsexitems = {"", "F", "M", "Non-binary"};
        cbsex = new JComboBox(cbsexitems);
        String[] cbfstitems = {"", "1","2","3","4","5","6"};
        cbfst = new JComboBox(cbfstitems);
        String[] yes_no = {"", "Y", "N"};
        String[] numStudies_items = {"", "None", "One or more"};
        cbap = new JComboBox(yes_no);
        String[] stitems = {"", "O", "D", "N", "O/D"};
        cbst = new JComboBox(stitems);
        cbse = new JComboBox(yes_no);
        cbss = new JComboBox(yes_no);
        cbcontacts = new JComboBox(yes_no);
        cbnum_panels = new JComboBox(numStudies_items);
        cbnum_studies = new JComboBox(numStudies_items);
        cbla_qualified = new JComboBox(yes_no);
        String[] studytypes = conn.getStudyTypes();
        types = new JList(studytypes);
        types.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        btnRetrieve = new JButton("Retrieve");
        btnRetrieve.addActionListener(this);
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 0; gbc.ipady = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        gbc.gridheight = 1; gbc.gridwidth = 6;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridheight = 1; gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbllower, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtlower, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblupper, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtupper, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; 
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsex, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbsex, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblfst, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbfst, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblap, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        this.add(cbap, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblst, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        this.add(cbst, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblse, gbc);
        
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        this.add(cbse,gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblss, gbc);
        
        gbc.gridx = 3; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(cbss, gbc);
        
        gbc.gridx = 4; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblcontacts, gbc);
        
        gbc.gridx = 5; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbcontacts, gbc);
        
        gbc.gridx = 4; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblnumpanels, gbc);
        
        gbc.gridx = 5; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbnum_panels, gbc);
        
        gbc.gridx = 4; gbc.gridy = 3;
        gbc.weightx = 0.1; 
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblnumstudies, gbc);
        
        gbc.gridx = 5; gbc.gridy = 3;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbnum_studies, gbc);
        
        gbc.gridx = 4; gbc.gridy = 4;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblla_qualified, gbc);
        
        gbc.gridx = 5; gbc.gridy = 4;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(0,0,0,20);
        this.add(cbla_qualified, gbc);
        
        gbc.gridx = 6; gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(lbltypes, gbc);
        
        gbc.gridx = 7; gbc.gridy = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(0,0,0,20);
        this.add(types, gbc);
        
        gbc.gridx = 7; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(btnRetrieve, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRetrieve){ // execute if retrieve button is clicked
            String lower = txtlower.getText();
            String upper = txtupper.getText();
            String sex = cbsex.getSelectedItem().toString();
            String fst = cbfst.getSelectedItem().toString();
            String ap = cbap.getSelectedItem().toString();
            String st = cbst.getSelectedItem().toString();
            String se = cbse.getSelectedItem().toString();
            String ss = cbss.getSelectedItem().toString();
            String contacts = cbcontacts.getSelectedItem().toString();
            String num_panels = cbnum_panels.getSelectedItem().toString();
            String num_studies = cbnum_studies.getSelectedItem().toString();
            String la_qualified = cbla_qualified.getSelectedItem().toString();
            
            List<String> selectedTypes = new ArrayList<>();
            selectedTypes = types.getSelectedValuesList();
            
            if (isValidAgeInput(lower, upper)){
                Connect conn = new Connect();
                String[][] data_with_headers = conn.filterRecruits(selectedTypes, upper, lower, sex, fst, ap, st, se, ss, contacts, num_panels, num_studies, la_qualified);
            
                String[] colnames = {""};
                String[][] data = {{""}};
                if (!(data_with_headers == null)){
                    colnames = data_with_headers[0];
                    data = new String[data_with_headers.length-1][data_with_headers[1].length];
                    for (int i = 1 ; i < data_with_headers.length ; i++){
                        for (int j = 0 ; j < data_with_headers[0].length ; j++){
                            data[i-1][j] = data_with_headers[i][j];
                        }
                    }
                }
                new OutputPage(data, colnames, "Filtered Results", _iconImage);


                txtlower.setText("");
                txtupper.setText("");
                cbsex.setSelectedItem("");
                cbfst.setSelectedItem("");
                cbap.setSelectedItem("");
                cbst.setSelectedItem("");
                cbse.setSelectedItem("");
                cbss.setSelectedItem("");
                cbcontacts.setSelectedItem("");
                cbnum_panels.setSelectedItem("");
                cbnum_studies.setSelectedItem("");
                int[] idxs = {};
                types.setSelectedIndices(idxs);
                
                

            }else{
                JOptionPane.showMessageDialog(this, "Please make sure both ages are entered properly");
            }
            
            
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    /**
     * Makes sure the age TextFields satisfy:
     *  -Both or neither are filled
     *  -Both are of type int
     * @param lower
     * @param upper
     * @return 
     */
    private boolean isValidAgeInput(String lower, String upper){
        boolean isValid = true;
        
        if ((lower.equals("") && !upper.equals("")) || ( !lower.equals("") && upper.equals(""))){
            isValid = false;
        }else{
            for (int i = 0 ; i < lower.length() ; i++){
                if (!Character.isDigit(lower.charAt(i))){
                    isValid = false;
                }
            }
            for(int i = 0 ; i < upper.length() ; i++){
                if (!Character.isDigit(upper.charAt(i))){
                    isValid = false;
                }
            }
        }
        return isValid;
    }
    
    
}
