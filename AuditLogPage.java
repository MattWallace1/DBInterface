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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.validator.routines.DateValidator;

/**
 *
 * @author joe_admin
 */
public class AuditLogPage extends JFrame implements ActionListener{
    
    JLabel lbldesc;
    JTextField txtdate1;
    JLabel lblto;
    JTextField txtdate2;
    JButton btnRetrieve;
    
    Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    public AuditLogPage(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(_iconImage);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setTitle("Retrieve an Audit Log");
        this.setPreferredSize(new Dimension(500,300));
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel("<html>Enter a date range to pull log records from:<br><i>Dates should be 'yyyy-mm-dd'");
        txtdate1 = new JTextField();
        lblto = new JLabel("to");
        txtdate2 = new JTextField();
        btnRetrieve = new JButton("Retrieve");
        btnRetrieve.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,30);
        this.add(txtdate1, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0,120,0,0);
        this.add(lblto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.3;
        gbc.insets = new Insets(0,20,0,30);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(txtdate2, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.5; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,30);
        this.add(btnRetrieve, gbc);
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRetrieve){
        
            String date1 = txtdate1.getText();
            String date2 = txtdate2.getText();
            
            if (isValidInput(date1, date2)){
                Connect conn = new Connect();
                String[][] data = conn.getAuditLog(date1, date2);
                if (data == null){
                    String[][] empty_data = new String[1][1];
                    empty_data[0][0] = "";
                    String[] colnames = new String[1];
                    colnames[0] = "";
                    new OutputPage(empty_data, colnames, "No records found", _iconImage);
                }else{
                    String[] colnames = {"timestamp", "event type", "source ip", "user", "description"};
                    new OutputPage(data, colnames, "Activity from " + date1 + " to " + date2, _iconImage);
                    this.dispose();
                }
            }else{
                JOptionPane.showMessageDialog(this, "Dates need to be in the format 'yyyy-mm-dd'");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(String date1, String date2){
        return DateValidator.getInstance().isValid(date1, "yyyy-MM-dd") &&
                DateValidator.getInstance().isValid(date2, "yyyy-MM-dd");
    }
}
