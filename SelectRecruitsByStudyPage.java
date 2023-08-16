/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import SQLConnection.Connect;
import java.util.Arrays;

/**
 *
 * @author Matthew Wallace
 */
public class SelectRecruitsByStudyPage extends JFrame implements ActionListener{
    
    private JLabel lbldesc;
    private JLabel lblid;
    private JComboBox cbid;
    private JLabel lblpsn;
    private JComboBox cbpsn;
    private JButton btnRetrieve;
    private Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    SelectRecruitsByStudyPage(Image iconImage){
        iconImage = iconImage;
        this.setIconImage(iconImage);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(500,300));
        this.setTitle("See Recruits that Participated in a Study");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        Connect conn = new Connect();
        String[] psns = conn.getVisitPsns();
        String[] psn_items;
        if (!(psns == null)){
            psn_items = new String[psns.length+1];
            psn_items[0] = "";
            for (int i = 0 ; i < psns.length ; i++){
                psn_items[i+1] = psns[i];
            }
        }else{
            psn_items = new String[1];
            psn_items[0] = "";
        }
        
        String[] studyids = conn.selectDistinctField("visits", "study_id");
        String[] studyid_items = new String[studyids.length+1];
        studyid_items[0] = "";
        for (int i = 0 ; i < studyids.length ; i++){
            studyid_items[i+1] = studyids[i];
        }
        
        
        lbldesc = new JLabel("Search for a study by study id OR psn if id is unknown");
        lblid = new JLabel("Enter study id:");
        cbid = new JComboBox(studyid_items);
        lblpsn = new JLabel("Enter psn:");
        cbpsn = new JComboBox(psn_items);
        btnRetrieve = new JButton("Retrieve");
        btnRetrieve.addActionListener(this);
        
        this.add(lbldesc, new GridBagConstraints(0,0,4,1,0.0,0.1,
                                                    GridBagConstraints.FIRST_LINE_START, 
                                                    GridBagConstraints.NONE, 
                                                    new Insets(20,20,0,0), 0,0));
        this.add(lblid, new GridBagConstraints(0,1,1,1,0.1,0.3,
                                                    GridBagConstraints.FIRST_LINE_START, 
                                                    GridBagConstraints.HORIZONTAL, 
                                                    new Insets(0,20,0,0), 0,0));
        this.add(cbid, new GridBagConstraints(1,1, 1, 1, 0.5,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,0,0,0), 0,0));
        this.add(lblpsn, new GridBagConstraints(2,1, 1, 1, 0.1,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,0), 0,0));
        this.add(cbpsn, new GridBagConstraints(3,1, 1, 1, 0.5,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,20), 0,0));
        this.add(btnRetrieve, new GridBagConstraints(0,2, 1, 1, 0.0,0.7,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,0), 0,0));       
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRetrieve){
            if (isValidInput()){
                String id = cbid.getSelectedItem().toString();
                String psn = cbpsn.getSelectedItem().toString();
                cbid.setSelectedItem("");

                Connect conn = new Connect();
                String[][] data = conn.selectRecruitsByStudy(id, psn);
                
                if (!(data == null)){
                    String[][] data_no_headers = new String[data.length-1][data[0].length];
                    for (int i = 1 ; i < data.length ; i++){
                        for (int j = 0 ; j < data[0].length ; j++){
                            data_no_headers[i-1][j] = data[i][j];
                        }
                    }
                    
                    String title = "";
                    if (!(id == null || id.equals(""))){
                        title = "Recruits in Study " + id + ", PSN: " + conn.getPsn(id);
                    }else{
                        title = "Recruits in PSN: " + psn;
                    }
                    
                    String[] colnames = {"Recruit ID", "Name", "Phone", "DOB", "Visit ID", "Panelist Num", "Total Pay", "Paid Date", "Check Num"};
                    new OutputPage(data_no_headers, colnames, title, _iconImage);
                }else{
                    String[] empty_colnames = {""};
                    String[][] empty_data = {{""}};
                    new OutputPage(empty_data, empty_colnames, "No records found", _iconImage);
                }

                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Please make a selection");
            }
            
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(){
        return !cbid.getSelectedItem().toString().equals("") ||
                !cbpsn.getSelectedItem().toString().equals("");
    }
}
