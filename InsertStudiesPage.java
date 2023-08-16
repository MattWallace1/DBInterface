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
import javax.swing.JCheckBox;
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
import org.apache.commons.validator.routines.DateValidator;

/**
 *
 * @author joe_admin
 */
public class InsertStudiesPage extends JFrame implements ActionListener{
    
    private JLabel lblpsn;
    private JLabel lblsite;
    private JLabel lbltype;
    private JLabel lblcompany;
    private JLabel lblcontact;
    private JLabel lblstart_date;
    private JLabel lblend_date;
    private JLabel lblproject_manager;
    
    
    private JTextField txtpsn;
    private JComboBox cbsite;
    private JTextArea tatype;
    private JTextField txtcompany;
    private JTextField txtcontact;
    private JTextField txtstart_date;
    private JTextField txtend_date;
    private JTextField txtproject_manager;
    
    private JButton btnSubmit;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    public InsertStudiesPage(Image iconImage){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(1000,500));
        this.setTitle("Add a new study");
        this.setIconImage(iconImage);
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lblpsn = new JLabel("PSN:");
        lblsite = new JLabel("Site:");
        lbltype = new JLabel("Type:");
        lblcompany = new JLabel("Company:");
        lblcontact = new JLabel("Contact:");
        lblstart_date = new JLabel("Start Date:");
        lblend_date = new JLabel("End Date:");
        lblproject_manager = new JLabel("Project Manager:");
        
        txtpsn = new JTextField();
        String[] sites = {"", "PA", "CA"};
        cbsite = new JComboBox(sites);
        //JScrollPane sp = new JScrollPane();
        //sp.setPreferredSize(new Dimension(50,50));
        tatype = new JTextArea(10,5);
        tatype.setEditable(true);
        tatype.setLineWrap(true);
        tatype.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(tatype);
        sp.setPreferredSize(new Dimension(30,100));
        
        txtcompany = new JTextField();
        txtcontact = new JTextField();
        txtstart_date = new JTextField();
        txtend_date = new JTextField();
        txtproject_manager = new JTextField();
        
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        
        txtpsn.requestFocusInWindow();
        txtpsn.setNextFocusableComponent(cbsite);
        cbsite.setNextFocusableComponent(tatype);
        tatype.setNextFocusableComponent(txtcompany);
        txtcompany.setNextFocusableComponent(txtcontact);
        txtcontact.setNextFocusableComponent(txtstart_date);
        txtstart_date.setNextFocusableComponent(txtend_date);
        txtend_date.setNextFocusableComponent(btnSubmit);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lblpsn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsite, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbltype, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,0);
        this.add(txtpsn, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(cbsite, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 2;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(sp, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(30,20,0,0);
        this.add(lblcompany, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblcontact, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,30,0,0);
        this.add(lblstart_date, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblend_date, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(30,0,0,20);
        this.add(txtcompany, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(txtcontact, gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,0,0,20);
        this.add(txtstart_date, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(txtend_date, gbc);
        
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,0);
        this.add(lblproject_manager, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.weightx = 0.3; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,20);
        this.add(txtproject_manager, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,30,0,20);
        this.add(btnSubmit, gbc);
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            if (isValidInput()){
                String[] insertVals = new String[5];
                insertVals[0] = txtpsn.getText();
                insertVals[1] = cbsite.getSelectedItem().toString();
                insertVals[2] = tatype.getText();
                insertVals[3] = txtcompany.getText();
                insertVals[4] = txtcontact.getText();
                String start_date = txtstart_date.getText();
                String end_date = txtend_date.getText();
                String project_manager = txtproject_manager.getText();


                Connect conn = new Connect();
                String user = conn.getMySQLUser();
                conn.insertStudies(insertVals, start_date, end_date, user, project_manager);

                txtpsn.setText("");
                cbsite.setSelectedItem("");
                tatype.setText("");
                txtcompany.setText("");
                txtcontact.setText("");
                txtstart_date.setText("");
                txtend_date.setText("");
                txtproject_manager.setText("");

                txtpsn.requestFocusInWindow();

            }else{
                JOptionPane.showMessageDialog(null, "<html>All fields are required<br>Dates need to be in the format 'yyyy-mm-dd'</html>");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(){
        return !txtpsn.getText().equals("") &&
                !cbsite.getSelectedItem().toString().equals("") &&
                !tatype.getText().equals("") &&
                !txtcompany.getText().equals("") &&
                !txtcontact.getText().equals("") &&
                !txtstart_date.getText().equals("") &&
                !txtend_date.getText().equals("") &&
                DateValidator.getInstance().isValid(txtstart_date.getText(), "yyyy-MM-dd") &&
                DateValidator.getInstance().isValid(txtend_date.getText(), "yyyy-MM-dd");
        
    }
}
