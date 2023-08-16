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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 *
 * @author joe_admin
 */
public class MoreRecruitInfoPage extends JFrame implements ActionListener{
    
    private JLabel lblstudy;
    private JComboBox cbstudy;
    private JButton btnRetrieve;
    private Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    private String _table;
    
    public MoreRecruitInfoPage(Image iconImage, String table){
        _table = table;
        _iconImage = iconImage;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(300,250));
        this.setIconImage(iconImage);
        this.setTitle("Select a Study");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close this page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lblstudy = new JLabel("Select a study:");
        Connect conn = new Connect();
        String[][] studyidspsns = null;
        if (_table.equals("recruitpanels")){
            studyidspsns = conn.getRecruitingStudyIdsPSNs();
        }else if (_table.equals("ongoingstudies")){
            studyidspsns = conn.getActiveStudyIdsPSNs();
        }
        
        if (studyidspsns == null){
            String[] empty_ids = {"no available studies"};
            cbstudy = new JComboBox(empty_ids);
        }else{
            String[] studyids = new String[studyidspsns.length];
        for (int i = 0 ; i < studyids.length ; i++){
            studyids[i] = studyidspsns[i][0] + ", PSN: " + studyidspsns[i][1];
        }
            cbstudy = new JComboBox(studyids);
        }
        
        btnRetrieve = new JButton("Retrieve");
        btnRetrieve.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lblstudy, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,20);
        this.add(cbstudy, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.2;
        gbc.insets = new Insets(15,30,0,30);
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(btnRetrieve, gbc);
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRetrieve){
            String studyid = cbstudy.getSelectedItem().toString().split(",")[0];
            Connect conn = new Connect();
            String[][] data;
            if (_table.equals("recruitpanels")){
                data = conn.getRecruitsInPanel(studyid);
            }else{
                data = conn.getRecruitsInActiveStudy(studyid);
            }
            
            String[] colnames = conn.getAllColNames("recruits");
            if (!(data == null)){
                new OutputPage(data, colnames, "Panel " + conn.getPsn(studyid) + ", ID " + studyid, _iconImage);
            }else{
                String[] empty_colnames = {""};
                String[][] empty_data = {{""}};
                new OutputPage(empty_data, empty_colnames, "No records found", _iconImage);
            }
            this.dispose();
        }else if (e.getSource() == exit){
            this.dispose();
        }
        
        
    }
}
