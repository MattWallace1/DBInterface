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

import java.util.ArrayList;
import java.util.Arrays;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author joe_admin
 */
public class PayRecruitsPage extends JFrame implements ActionListener{
    
    Image _iconImage;
    private JLabel lbldesc;
    private JButton btnUnpaidStudies;
    private JButton btnPickStudy;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    public PayRecruitsPage(Image iconImage){
        _iconImage = iconImage;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setIconImage(_iconImage);
        this.setPreferredSize(new Dimension(500,300));
        this.setTitle("Pay Recruits");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        
        lbldesc = new JLabel("You can pay all subjects from the previous month, or select a single study:");
        btnUnpaidStudies = new JButton("All studies");
        btnPickStudy = new JButton("Choose a study");
        
        btnUnpaidStudies.addActionListener(this);
        btnPickStudy.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,20,0,20);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0,20,0,0);
        this.add(btnUnpaidStudies, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0,20,0,30);
        this.add(btnPickStudy, gbc);
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Connect conn = new Connect();
        if (e.getSource() == btnUnpaidStudies){
            
            createPaymentTablePage("");
            
            
        }else if (e.getSource() == btnPickStudy){
            // get a list of study ids & psns where paid = 0
            String[] studyidpsn = conn.getStudyIdPsnUnpaid();
            JComboBox cbstudyids = new JComboBox(studyidpsn);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Enter a study id from the drop down list:"));
            panel.add(cbstudyids);
            
            String studyid = "";
            boolean isValidInput = false;
            while (!isValidInput && !(studyid == null)){
                studyid = (String)JOptionPane.showInputDialog(this, panel, "Enter a Study ID", JOptionPane.PLAIN_MESSAGE, null, null, "");
                for (String study:studyidpsn){
                    if (!(studyid == null)){
                        if (studyid.equals(study.split(",")[0])){
                            isValidInput = true;
                        }
                    }
                }
            }
            
            if (!(studyid == null)){
                createPaymentTablePage(studyid);
            }
            
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private void createPaymentTablePage(String studyid){
        Connect conn = new Connect();
        String title = "";
        String[][] data = conn.getPaymentInfo(studyid);
        String[] headers = {"Visit ID", "Recruit ID", "First Name", "Last Name", "Phone", "Address", "City", "State", "ZIP", "Study ID", "PSN", "Type", "Start Date", "End Date", "Closed Date", "Panelist Num", "Total Pay", "Paid Date", "Check Num"};
        
        title = "Enter Check Numbers";
        
        try{
            new PaymentTablePage(data, headers, title, studyid, _iconImage);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
}
