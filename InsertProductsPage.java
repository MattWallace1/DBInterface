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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author joe_admin
 */
public class InsertProductsPage extends JFrame implements ActionListener{
    
    
    JLabel lblstudyid;
    JLabel lblpi;
    JLabel lbldescription;
    JLabel lblsite_location;
    JLabel lbltested_as;
    JLabel lblpatch_type;
    JLabel lblapplication_frequency;
    JLabel lblnotes;
    
    JComboBox cbstudyid;
    JTextField txtpi;
    JTextArea tadescription;
    JTextField txtsite_location;
    JTextField txttested_as;
    JComboBox cbpatch_type;
    JTextField txtapplication_frequency;
    JTextArea tanotes;
    
    JButton btnSubmit;
    
    
    Connect conn;
    Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    public InsertProductsPage(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(900,500));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Enter a New Product");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        conn = new Connect();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0; gbc.ipady = 0;
        
        
        lblstudyid = new JLabel("Study ID:");
        lblpi = new JLabel("PI:");
        lbldescription = new JLabel("Description:");
        lblsite_location = new JLabel("Site Location:");
        lbltested_as = new JLabel("Tested As:");
        lblpatch_type = new JLabel("Patch Type:");
        lblapplication_frequency = new JLabel("Application Frequency:");
        lblnotes = new JLabel("Notes:");
        
        
        String[] studydata = conn.getStudyIdPsnNotPaid();
        cbstudyid = new JComboBox(studydata);
        txtpi = new JTextField();
        tadescription = new JTextArea(10,5);
        tadescription.setEditable(true);
        tadescription.setLineWrap(true);
        tadescription.setWrapStyleWord(true);
        JScrollPane spdescription = new JScrollPane(tadescription);
        spdescription.setPreferredSize(new Dimension(100,100));
        txtsite_location = new JTextField();
        txttested_as = new JTextField();
        String[] cbpatches = {"", "Occlusive", "Semi-occlusive"};
        cbpatch_type = new JComboBox(cbpatches);
        txtapplication_frequency = new JTextField();
        tanotes = new JTextArea(10,5);
        tanotes.setEditable(true);
        tanotes.setLineWrap(true);
        tanotes.setWrapStyleWord(true);
        JScrollPane spnotes = new JScrollPane(tanotes);
        spnotes.setPreferredSize(new Dimension(100,100));
        
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        
        
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lblstudyid, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,0);
        this.add(cbstudyid, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblpi, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtpi, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsite_location, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(txtsite_location, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbldescription, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(spdescription, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbltested_as, gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,30);
        this.add(txttested_as, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblpatch_type, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbpatch_type, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblapplication_frequency, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtapplication_frequency, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblnotes, gbc);
        
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(spnotes, gbc);
        
        gbc.gridx = 3; gbc.gridy = 4;
        gbc.insets = new Insets(0,0,0,30);
        this.add(btnSubmit, gbc);
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            if (isValidInput()){
                
                String studyid = cbstudyid.getSelectedItem().toString().split(",")[0];
                String pi = txtpi.getText();
                String description = tadescription.getText();
                String site_location = txtsite_location.getText();
                String tested_as = txttested_as.getText();
                String patch_type = cbpatch_type.getSelectedItem().toString();
                String application_frequency = txtapplication_frequency.getText();
                String notes = tanotes.getText();
                
                String[] toInsert = new String[8];
                
                toInsert[0] = studyid;
                toInsert[1] = pi;
                toInsert[2] = description;
                toInsert[3] = site_location;
                toInsert[4] = tested_as;
                toInsert[5] = patch_type;
                toInsert[6] = application_frequency;
                toInsert[7] = notes;
                
                conn.insertProducts(toInsert);
                
                cbstudyid.setSelectedItem("");
                txtpi.setText("");
                tadescription.setText("");
                txtsite_location.setText("");
                txttested_as.setText("");
                cbpatch_type.setSelectedItem("");
                txtapplication_frequency.setText("");
                tanotes.setText("");
                
            }else{
                JOptionPane.showMessageDialog(this, "Make sure you've selected a study ID and entered a PI number");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
        
    }
    
    private boolean isValidInput(){
        return !cbstudyid.getSelectedItem().toString().equals("") &&
                !txtpi.getText().equals("");
    }
}
