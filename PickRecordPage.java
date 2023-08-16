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
import javax.swing.JOptionPane;

/**
 *
 * @author joe_admin
 */
public class PickRecordPage extends JFrame implements ActionListener{
    
    Connect conn;
    JLabel lbldesc;
    JComboBox cbrecords;
    JButton btnSubmit;
    Image _iconImage;
    
    String _table;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private JMenuItem prev;
    
    public PickRecordPage(Image iconImage, String table){
        _iconImage = iconImage;
        _table = table;
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Choose a Record to Edit");
        this.setPreferredSize(new Dimension(450,300));
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        prev = new JMenuItem("Return to previous page");
        prev.addActionListener(this);
        exitMenu.add(exit);
        exitMenu.add(prev);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel();
        conn = new Connect();
        String[] ids = {};
        if (table.equals("recruits")){
            ids = conn.getRecruitIdName();
            lbldesc.setText("Select a recruit to edit:");
        }else if(table.equals("studies")){
            ids = conn.getStudyIdPsn();
            lbldesc.setText("Select a study to edit:");
        }else if(table.equals("products")){
            ids = conn.getProductIdPi();
            lbldesc.setText("Select a product to edit");
        }
        
        cbrecords = new JComboBox(ids);
        btnSubmit = new JButton("Edit");
        btnSubmit.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.4;
        gbc.insets = new Insets(0,20,0,30);
        this.add(cbrecords, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(btnSubmit, gbc);
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            if (isValidInput()){
                String selected = cbrecords.getSelectedItem().toString();
                if (_table.equals("recruits")){
                    new DisplayRecruitPage(_iconImage, selected, _table);
                }else if (_table.equals("studies")){
                    new DisplayStudyPage(_iconImage, selected, _table);
                }else if (_table.equals("products")){
                    new DisplayProductPage(_iconImage, selected, _table);
                }
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Please make a selection");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }else if (e.getSource() == prev){
            this.dispose();
            new UpdateTablePage(_iconImage);
        }
    }
    
    private boolean isValidInput(){
        return !cbrecords.getSelectedItem().toString().equals("");
    }
}
