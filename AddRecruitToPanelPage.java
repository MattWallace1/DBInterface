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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author joe_admin
 */
public class AddRecruitToPanelPage extends JFrame implements ActionListener{
    
    private JLabel lbldesc;
    private JLabel lblstudyid;
    private JLabel lblrecruitid;
    private JComboBox cbstudyid;
    private JComboBox cbrecruitid;
    private JButton btnSubmit;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    private JFrame _panelsPage;
    
    public AddRecruitToPanelPage(Image iconImage, JFrame panelsPage){
        _panelsPage = panelsPage;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(540,250));
        this.setIconImage(iconImage);
        this.setTitle("Add a Recruit to a Panel");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close this page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel("Choose an active study, and a recruit to enter into that study:");
        lblstudyid = new JLabel("Study Id:");
        lblrecruitid = new JLabel("Recruit Id:");
        
        Connect conn = new Connect();
        String[][] panel_info = conn.getPanelStudyIdsPSNs();
        if (!(panel_info == null)){
            String[] cbstudyitems = new String[panel_info.length+1];
            cbstudyitems[0] = "";
            for (int i = 0 ; i < panel_info.length ; i++){
                cbstudyitems[i+1] = panel_info[i][0] + " (" + panel_info[i][1] + ")";
            }
            cbstudyid = new JComboBox(cbstudyitems);
        }else{
            String[] cbstudyitems = {""};
            cbstudyid = new JComboBox(cbstudyitems);
        }
        
        String[][] recruit_info = conn.getRecruitNameId();
        if (!(recruit_info == null)){
            String[] cbrecruititems = new String[recruit_info.length+1];
            cbrecruititems[0] = "";
            for (int i = 0 ; i < recruit_info.length ; i++){
                cbrecruititems[i+1] = recruit_info[i][0] + " (" + recruit_info[i][1] + ")";
            }
            cbrecruitid = new JComboBox(cbrecruititems);
        }else{
            String[] cbrecruititems = {""};
            cbrecruitid = new JComboBox(cbrecruititems);
        }
        
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.ipadx = 0; gbc.ipady = 0;
        
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.weighty = 0.1;
        gbc.gridheight = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.gridheight = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblstudyid, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(lblrecruitid, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbstudyid, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        this.add(cbrecruitid, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,30,0,30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(btnSubmit, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            if (!isValidInput()){
                JOptionPane.showMessageDialog(null, "Make a selection for both fields");
            }else{
                String studyid = cbstudyid.getSelectedItem().toString().split(" ")[0];
                String recruitid = cbrecruitid.getSelectedItem().toString().split(" ")[0];
                Connect conn = new Connect();

                int num_studies = conn.getNumStudies(recruitid);
                int selection = -1;

                if (num_studies == 0){
                    //insert, increase num studies
                    conn.insertPanelRecruit(studyid, recruitid);
                    conn.updateNumStudies(recruitid, 1); // add 1 to number of studies for recruit


                }else{
                    ArrayList<String[]> recruit_other_studies = conn.getRecruitActiveStudies(recruitid); // id, psn, study type

                    String msg = "This recruit is already in the following panel(s):\n";
                    for (String[] row:recruit_other_studies){
                        msg += "Id: " + row[0] + ", PSN: " + row[1] + ", " + row[2] + "\n";
                    }
                    msg += "\nDo you want to continue?";
                    selection = JOptionPane.showConfirmDialog(null, msg);


                    if (selection == 0){ // if user chooses 'yes'
                        conn.insertPanelRecruit(studyid, recruitid);
                        conn.updateNumStudies(recruitid, 1); // add 1 to number of studies for recruit
                    }

                }

                cbstudyid.setSelectedItem("");
                cbrecruitid.setSelectedItem("");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    public boolean isValidInput(){
        return !cbstudyid.getSelectedItem().toString().equals("") &&
                !cbrecruitid.getSelectedItem().toString().equals("");
    }
}
