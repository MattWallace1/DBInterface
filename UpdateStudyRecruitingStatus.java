/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import SQLConnection.Connect;
import java.awt.Insets;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author joe_admin
 */
public class UpdateStudyRecruitingStatus extends JFrame implements ActionListener{
    
    JLabel lbldesc;
    JComboBox cbstudyids;
    JButton btnUpdate;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private Image _iconImage;
    public UpdateStudyRecruitingStatus(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(iconImage);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(400,300));
        this.setTitle("Begin Recruiting For a Study");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        Connect conn = new Connect();
        
        lbldesc = new JLabel("Choose a study to begin recruiting for");
        String[] cbstudyitems = conn.getNonRecruitingStudyIdPsn();
        cbstudyids = new JComboBox(cbstudyitems);
        btnUpdate = new JButton("Update status");
        btnUpdate.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.4;
        gbc.insets = new Insets(0,20,0,30);
        this.add(cbstudyids, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(btnUpdate, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdate){
            if (!(cbstudyids.getSelectedItem() == null)){
                String[] cbSelection = cbstudyids.getSelectedItem().toString().split(",");
                String selectedID = cbSelection[0];
                int new_status = JOptionPane.showConfirmDialog(this, "Would you like to set " + cbSelection[1] + ", ID: " + selectedID + ", as active?", "Update Active Status", JOptionPane.YES_NO_OPTION);
                new_status = 1 - new_status;
                System.out.println(new_status);
                if (new_status == 1){
                    Connect conn = new Connect();
                    conn.updateIsRecruitingStudy(selectedID, Integer.toString(new_status));
                    this.dispose();
                    new UpdateStudyRecruitingStatus(_iconImage);
                }
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
}
