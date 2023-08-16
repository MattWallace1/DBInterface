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
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
/**
 *
 * @author Matthew Wallace
 */
public class SelectStudiesByRecruitPage extends JFrame implements ActionListener{
    
    private JLabel lbldirections;
    private JLabel lblid;
    private JComboBox cbid;
    private JLabel lblname;
    private JComboBox cbname;
    private JButton btnRetrieve;
    private Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    SelectStudiesByRecruitPage(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(iconImage);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(600,300));
        this.setTitle("See Studies Completed by a Recruit");
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        // get lists of names for combo boxes
        Connect conn = new Connect();
        
        String[] ids = conn.selectDistinctField("visits", "recruit_id");
        String[] id_items = new String[ids.length+1];
        id_items[0] = "";
        for (int i = 0 ; i < ids.length ; i++){
            id_items[i+1] = ids[i];
        }
        String[] names = conn.getVisitNames();
        if (names == null){
            names = new String[1];
            names[0] = "";
        }
        String[] name_items = new String[names.length+1];
        name_items[0] = "";
        for (int i = 0 ; i < names.length ; i++){
            name_items[i+1] = names[i];
        }
                
        
        lbldirections = new JLabel("Enter a recruit's id OR full name if id is unknown:");
        lblid = new JLabel("id:");
        cbid = new JComboBox(id_items);
        lblname = new JLabel("name:");
        cbname = new JComboBox(name_items);
        btnRetrieve = new JButton("Retrieve");
        btnRetrieve.addActionListener(this);
        btnRetrieve.setFocusable(false);
        
        this.add(lbldirections, new GridBagConstraints(0,0,4,1,0.0,0.1,
                                                    GridBagConstraints.FIRST_LINE_START, 
                                                    GridBagConstraints.NONE, 
                                                    new Insets(20,20,0,0), 0,0));
        this.add(lblid, new GridBagConstraints(0,1,1,1,0.1,0.3,
                                                    GridBagConstraints.FIRST_LINE_START, 
                                                    GridBagConstraints.HORIZONTAL, 
                                                    new Insets(0,20,0,0), 0,0));
        this.add(cbid, new GridBagConstraints(1,1, 1, 1, 0.5,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,0,0,0), 0,0));
        this.add(lblname, new GridBagConstraints(2,1, 1, 1, 0.1,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,0), 0,0));
        this.add(cbname, new GridBagConstraints(3,1, 1, 1, 0.5,0.0,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,20), 0,0));
        this.add(btnRetrieve, new GridBagConstraints(0,2, 1, 1, 0.0,0.7,
                                                    GridBagConstraints.FIRST_LINE_START,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0,20,0,0), 0,0));
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRetrieve){
            if (isValidInput()){
                String id = cbid.getSelectedItem().toString();
                String name = cbname.getSelectedItem().toString();
                cbid.setSelectedItem("");
                cbname.setSelectedItem("");

                Connect conn = new Connect();
                String[][] data = conn.selectStudiesByRecruit(id, name);

                if (!(data == null)){
                    //String[] colnames = data[0];
                    String[][] data_no_headers = new String[data.length-1][data[0].length];
                    for (int i = 1 ; i < data.length ; i++){
                        for (int j = 0 ; j < data[0].length ; j++){
                            data_no_headers[i-1][j] = data[i][j];
                        }
                    }
                    String title = "All Studies Done by ";
                    if (id == null || id.equals("")){
                        title += name;
                    }else{
                        title += conn.getRecruitNameFromId(id) + ", ID: " + id;
                    }
                    String[] colnames = {"Study ID", "PSN", "Type", "Start Date", "End Date", "Closed Date", "Visit ID", "Panelist Num", "Total Pay", "Paid Date", "Check Num"};
                    new OutputPage(data_no_headers, colnames, title, _iconImage);
                }else{
                    String[] empty_colnames = {""};
                    String[][] empty_data = {{""}};
                    new OutputPage(empty_data, empty_colnames, "No records found", _iconImage);
                }
                this.dispose();
                
            }
            
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidInput(){
        return !cbid.getSelectedItem().toString().equals("") || 
                !cbname.getSelectedItem().toString().equals("");
    }
    
}
