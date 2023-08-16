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
public class EditProductPage extends JFrame implements ActionListener{
    
    JLabel lbldesc;
    JLabel lblstudyid;
    JLabel lblpi;
    JLabel lblsite_location;
    JLabel lbldescription;
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
    
    JButton btnUpdate;
    
    Image _iconImage;
    String productid;
    Connect conn;
    private String _table;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private JMenuItem prev;
    
    
    public EditProductPage(Image iconImage, String selected, String table){
        _iconImage = iconImage;
        productid = selected.split(",")[0];
        _table = table;
        conn = new Connect();
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(800,500));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Editing Product " + selected);
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Return to homepage");
        exit.addActionListener(this);
        prev = new JMenuItem("Pick a different product");
        prev.addActionListener(this);
        exitMenu.add(exit);
        exitMenu.add(prev);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel("Make any changes below, click 'Update' to save:");
        lblstudyid = new JLabel("Study ID:");
        lblpi = new JLabel("PI:");
        lblsite_location = new JLabel("Site location:");
        lbldescription = new JLabel("Description:");
        lbltested_as = new JLabel("Tested As:");
        lblpatch_type = new JLabel("Patch Type:");
        lblapplication_frequency = new JLabel("Application Frequency:");
        lblnotes = new JLabel("Notes:");
        
        String[] studyids = conn.getStudyIdPsn();
        studyids[0] = "";
        cbstudyid = new JComboBox(studyids);
        txtpi = new JTextField();
        txtsite_location = new JTextField();
        tadescription = new JTextArea(10,5);
        tadescription.setLineWrap(true);
        tadescription.setWrapStyleWord(true);
        tadescription.setEditable(true);
        JScrollPane spdescription = new JScrollPane(tadescription);
        spdescription.setPreferredSize(new Dimension(100,100));
        txttested_as = new JTextField();
        String[] patches = {"", "Occlusive", "Semi-occlusive"};
        cbpatch_type = new JComboBox(patches);
        txtapplication_frequency = new JTextField();
        tanotes = new JTextArea();
        tanotes.setEditable(true);
        tanotes.setLineWrap(true);
        tanotes.setWrapStyleWord(true);
        JScrollPane spnotes = new JScrollPane(tanotes);
        spnotes.setPreferredSize(new Dimension(100,100));
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this);
        
        
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this);
        
        
        
        
        String[] info = conn.getProductInfoForUpdate(productid);
        //find matching id/psn combo
        int idx = 0;
        while (!info[0].equals(studyids[idx].split(",")[0])){
            idx++;
        }
        cbstudyid.setSelectedItem(studyids[idx]);
        txtpi.setText(info[1]);
        tadescription.setText(info[2]);
        txttested_as.setText(info[3]);
        cbpatch_type.setSelectedItem(info[4]);
        txtapplication_frequency.setText(info[5]);
        txtsite_location.setText(info[6]);
        tanotes.setText(info[7]);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
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
        this.add(btnUpdate, gbc);
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnUpdate){
            if (isValidInput()){
                String[] info = new String[8];
                info[0] = cbstudyid.getSelectedItem().toString().split(",")[0];
                info[1] = txtpi.getText();
                info[2] = tadescription.getText();
                info[3] = txttested_as.getText();
                info[4] = cbpatch_type.getSelectedItem().toString();
                info[5] = txtapplication_frequency.getText();
                info[6] = txtsite_location.getText();
                info[7] = tanotes.getText();
                conn.updateProducts(productid, info);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Make sure you've selected a study ID and entered a PI number");
            }
        }else if (e.getSource() == prev){
            new PickRecordPage(_iconImage, _table);
            this.dispose();
        }else if (e.getSource() == exit){
            this.dispose();
        }
        
    }
    
    private boolean isValidInput(){
        return !cbstudyid.getSelectedItem().toString().equals("") &&
                !txtpi.getText().equals("");
    }
}
