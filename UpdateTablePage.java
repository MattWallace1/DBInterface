/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

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
public class UpdateTablePage extends JFrame implements ActionListener{
    
    Image _iconImage;
    JLabel lbldesc;
    JComboBox cbtables;
    JButton btnSubmit;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    public UpdateTablePage(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(300,250));
        this.setTitle("Pick a table");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0; gbc.ipady = 0;
        
        lbldesc = new JLabel("Select a table to edit:");
        String[] tables = {"", "recruits", "studies", "products"};
        cbtables = new JComboBox(tables);
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.4;
        gbc.insets = new Insets(0,20,0,30);
        this.add(cbtables, gbc);
        
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
                new PickRecordPage(_iconImage, cbtables.getSelectedItem().toString());
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Please select a table");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(){
        return !cbtables.getSelectedItem().toString().equals("");
    }
}
