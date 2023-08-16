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
public class EditStudyPage extends JFrame implements ActionListener{
    
    JLabel lbldesc;
    JLabel lblpsn;
    JLabel lblsite;
    JLabel lbltype;
    JLabel lblcompany;
    JLabel lblcontact;
    JLabel lblstart_date;
    JLabel lblend_date;
    JLabel lblproject_manager;
    
    JTextField txtpsn;
    JTextField txtsite;
    JTextField txttype;
    JTextField txtcompany;
    JTextField txtcontact;
    JTextField txtstart_date;
    JTextField txtend_date;
    JTextField txtproject_manager;
    
    JButton btnUpdate;
    
    Image _iconImage;
    String studyid;
    Connect conn;
    String _table;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private JMenuItem prev;
    
    public EditStudyPage(Image iconImage, String selected, String table){
        _iconImage = iconImage;
        studyid = selected.split(",")[0];
        _table = table;
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(600,400));
        this.setTitle("Editing study " + selected);
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Return to homepage");
        exit.addActionListener(this);
        prev = new JMenuItem("Pick a different study");
        prev.addActionListener(this);
        exitMenu.add(exit);
        exitMenu.add(prev);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lbldesc = new JLabel("Make any changes below, click 'Update' to save:");
        lblpsn = new JLabel("PSN:");
        lblsite = new JLabel("Site:");
        lbltype = new JLabel("Type:");
        lblcompany = new JLabel("Company:");
        lblcontact = new JLabel("Contact:");
        lblstart_date = new JLabel("Start date:");
        lblend_date = new JLabel("End date:");
        lblproject_manager = new JLabel("Project Manager:");
        
        txtpsn = new JTextField();
        txtsite = new JTextField();
        txttype = new JTextField();
        txtcompany = new JTextField();
        txtcontact = new JTextField();
        txtstart_date = new JTextField();
        txtend_date = new JTextField();
        txtproject_manager = new JTextField();
        
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this);
        
        conn = new Connect();
        String[] study_info = conn.getStudyInfoForUpdate(studyid);
        
        txtpsn.setText(study_info[0]);
        txtsite.setText(study_info[1]);
        txttype.setText(study_info[2]);
        txtcompany.setText(study_info[3]);
        txtcontact.setText(study_info[4]);
        txtstart_date.setText(study_info[5]);
        txtend_date.setText(study_info[6]);
        txtproject_manager.setText(study_info[7]);
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(20,20,0,0);
        this.add(lbldesc, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblpsn, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtpsn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsite, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtsite, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbltype, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txttype, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblcompany, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtcompany, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblcontact, gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtcontact, gbc);
        
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(lblstart_date, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtstart_date, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(lblend_date, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtend_date, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(lblproject_manager, gbc);
        
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtproject_manager, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(btnUpdate, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnUpdate){
            if (isValidDate(txtstart_date.getText()) && isValidDate(txtend_date.getText())){
                String[] info = new String[8];
                info[0] = txtpsn.getText();
                info[1] = txtsite.getText();
                info[2] = txttype.getText();
                info[3] = txtcompany.getText();
                info[4] = txtcontact.getText();
                info[5] = txtstart_date.getText();
                info[6] = txtend_date.getText();
                info[7] = txtproject_manager.getText();

                conn.updateStudies(studyid, info);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Start and end date need to be in the format 'yyyy-mm-dd'");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }else if (e.getSource() == prev){
            this.dispose();
            new PickRecordPage(_iconImage, _table);
        }
    }
    
    
    /**
     * return true if valid sql date format or empty
     * @param date
     * @return 
     */
    private boolean isValidDate(String date){
        return DateValidator.getInstance().isValid(date, "yyyy-MM-dd") || date.equals("");
    }
    
}
