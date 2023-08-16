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
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
public class EditRecruitPage extends JFrame implements ActionListener{
    
    JLabel lbldesc;
    JLabel lblfullname;
    JLabel lblfname;
    JLabel lbllname;
    JLabel lblphone;
    JLabel lblrace;
    JLabel lblsex;
    JLabel lbldob;
    JLabel lbladdress;
    JLabel lblcity;
    JLabel lblstate;
    JLabel lblzip;
    JLabel lblfst;
    JLabel lblst;
    JLabel lblap;
    JLabel lblse;
    JLabel lblss;
    JLabel lblcontacts;
    JLabel lblla_qualified;
    JLabel lblla_screen_date;
    JLabel lbltypes;
    JLabel lblnotes;
    
    
    JTextField txtfullname;
    JTextField txtfname;
    JTextField txtlname;
    JTextField txtphone;
    JTextField txtrace;
    JComboBox cbsex;
    JTextField txtdob;
    JTextField txtaddress;
    JTextField txtcity;
    JTextField txtstate;
    JTextField txtzip;
    JComboBox cbfst;
    JComboBox cbst;
    JComboBox cbap;
    JComboBox cbse;
    JComboBox cbss;
    JComboBox cbcontacts;
    JComboBox cbla_qualified;
    JTextField txtla_screen_date;
    JList types;
    JTextArea tanotes;
    JScrollPane spnotes;
    
    JButton btnUpdate;
    
    Image _iconImage;
    Connect conn;
    String recruitid;
    String _table;
    Connect _conn;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private JMenuItem prev;
    
    public EditRecruitPage(Image iconImage, String info, String table){
        _iconImage = iconImage;
        recruitid = info.split(",")[0];
        _table = table;
        _conn = new Connect();
        this.setIconImage(_iconImage);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(1200,800));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Editing ID " + info);
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Return to homepage");
        exit.addActionListener(this);
        prev = new JMenuItem("Pick a different recruit");
        prev.addActionListener(this);
        exitMenu.add(exit);
        exitMenu.add(prev);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        String[] cbsex_items = {"","F","M"};
        String[] cbcontacts_items = {"","Y","N"};
        
        lbldesc = new JLabel("Make any changes below, click 'Update' to save:");
        lblfullname = new JLabel("Full name");
        lblfname = new JLabel("First name:");
        lbllname = new JLabel("Last name:");
        lblphone = new JLabel("Phone:");
        lblrace = new JLabel("Race:");
        lblsex = new JLabel("Sex:");
        lbldob = new JLabel("DOB (YYYY-MM-DD):");
        lbladdress = new JLabel("Address:");
        lblcity = new JLabel("City:");
        lblstate = new JLabel("State:");
        lblzip = new JLabel("ZIP:");
        lblfst = new JLabel("FST:");
        lblst = new JLabel("ST:");
        lblap = new JLabel("AP:");
        lblse = new JLabel("SE:");
        lblss = new JLabel("SS:");
        lblcontacts = new JLabel("Contacts:");
        lblla_qualified = new JLabel("LA Qualified:");
        lblla_screen_date = new JLabel("LA Screen Date:");
        lblnotes = new JLabel("Preferred Study Types:");
        lblnotes = new JLabel("Notes:");
        lbltypes = new JLabel("Preferred Study Types:");

        String[] yes_no = {"", "Y", "N"};
        String[] fst_items = {"","1","2","3","4","5","6"};
        String[] stitems = {"", "O", "D", "N", "O/D"};
        txtfullname = new JTextField();
        txtfname = new JTextField();
        txtlname = new JTextField();
        txtphone = new JTextField();
        txtrace = new JTextField();
        cbsex = new JComboBox(cbsex_items);
        txtdob = new JTextField();
        txtaddress = new JTextField();
        txtcity = new JTextField();
        txtstate = new JTextField();
        txtzip = new JTextField();
        cbfst = new JComboBox(fst_items);
        cbst = new JComboBox(stitems);
        cbap = new JComboBox(yes_no);
        cbse = new JComboBox(yes_no);
        cbss = new JComboBox(yes_no);
        cbcontacts = new JComboBox(cbcontacts_items);
        cbla_qualified = new JComboBox(yes_no);
        txtla_screen_date = new JTextField();
        types = new JList(_conn.getStudyTypes());
        tanotes = new JTextArea(20,10);
        tanotes.setEditable(true);
        tanotes.setLineWrap(true);
        tanotes.setWrapStyleWord(true);
        spnotes = new JScrollPane(tanotes);
        spnotes.setPreferredSize(new Dimension(100,50));
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this);
        
        conn = new Connect();
        String[] recruit_info = conn.getRecruitInfoForUpdate(info.split(",")[0]);
        
        txtfullname.setText(recruit_info[0]);
        txtfname.setText(recruit_info[1]);
        txtlname.setText(recruit_info[2]);
        txtphone.setText(recruit_info[3]);
        txtrace.setText(recruit_info[4]);
        cbsex.setSelectedItem(recruit_info[5]);
        txtdob.setText(recruit_info[6]);
        txtaddress.setText(recruit_info[7]);
        txtcity.setText(recruit_info[8]);
        txtstate.setText(recruit_info[9]);
        txtzip.setText(recruit_info[10]);
        cbfst.setSelectedItem(recruit_info[11]);
        cbst.setSelectedItem(recruit_info[12]);
        cbap.setSelectedItem(recruit_info[13]);
        cbse.setSelectedItem(recruit_info[14]);
        cbss.setSelectedItem(recruit_info[15]);
        cbcontacts.setSelectedItem(recruit_info[16]);
        cbla_qualified.setSelectedItem(recruit_info[17]);
        txtla_screen_date.setText(recruit_info[18]);
        tanotes.setText(recruit_info[19]);
        
        // subtract 1 to convert from uid to list index
        int[] idxs = _conn.getStudyTypeIdsForRecruit(recruitid);
        for (int i = 0 ; i < idxs.length ; i++){
            idxs[i] --;
        }
        
        types.setSelectedIndices(idxs);
        
        
        
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
        this.add(lblfullname, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtfullname, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblfname, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtfname, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbllname, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtlname, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblphone, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtphone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblrace, gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtrace, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lblsex, gbc);
        
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbsex, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,20,0,0);
        this.add(lbldob, gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtdob, gbc);
        
        //////////////NEXT LINE///////////////
        
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lbladdress, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtaddress, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblcity, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtcity, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblstate, gbc);
        
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtstate, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblzip, gbc);
        
        gbc.gridx = 3; gbc.gridy = 4;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(txtzip, gbc);
        
        gbc.gridx = 2; gbc.gridy = 5;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblfst, gbc);
        
        gbc.gridx = 3; gbc.gridy = 5;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbfst, gbc);
        
        gbc.gridx = 2; gbc.gridy = 6;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblst, gbc);
        
        gbc.gridx = 3; gbc.gridy = 6;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbst, gbc);
        
        gbc.gridx = 2; gbc.gridy = 7;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblap, gbc);
        
        gbc.gridx = 3; gbc.gridy = 7;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbap, gbc);
        
        ///////////////////////////////////////
        
        gbc.gridx = 4; gbc.gridy = 1;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblse, gbc);
        
        gbc.gridx = 5; gbc.gridy = 1;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbse, gbc);
        
        gbc.gridx = 4; gbc.gridy = 2;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblss, gbc);
        
        gbc.gridx = 5; gbc.gridy = 2;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbss, gbc);
        
        gbc.gridx = 4; gbc.gridy = 3;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblcontacts, gbc);
        
        gbc.gridx = 5; gbc.gridy = 3;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.insets = new Insets(0,0,0,30);
        this.add(cbcontacts, gbc);
        
        gbc.gridx = 4; gbc.gridy = 4;
        gbc.weightx = 0.1;
        this.add(lblla_qualified, gbc);
        
        gbc.gridx = 5; gbc.gridy = 4;
        gbc.weightx = 0.2;
        this.add(cbla_qualified, gbc);
        
        gbc.gridx = 4; gbc.gridy = 5;
        gbc.weightx = 0.1;
        this.add(lblla_screen_date, gbc);
        
        gbc.gridx = 5; gbc.gridy = 5;
        gbc.weightx = 0.2;
        this.add(txtla_screen_date, gbc);
        
        gbc.gridx = 4; gbc.gridy = 6;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblnotes, gbc);
        
        gbc.gridx = 5; gbc.gridy = 6;
        gbc.weightx = 0.2; gbc.weighty = 0.1;
        gbc.gridheight = 2;
        this.add(spnotes, gbc);
        
        gbc.gridx = 6; gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.gridheight = 1;
        this.add(lbltypes, gbc);
        
        gbc.gridx = 7; gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.gridheight = 4;
        this.add(types, gbc);
        
        JLabel important = new JLabel("<html>*To unselect an item: 'Ctrl+Click' on a selected item<br><i>Note that an outlined item does not indicate it being selected</html>");
        gbc.gridx = 6; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(40,0,0,0);
        this.add(important, gbc);
        
        gbc.gridx = 5; gbc.gridy = 7;
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0,20,0,0);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(btnUpdate, gbc);
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnUpdate){
            if (isValidDates()){
                
            
                String[] recruit_info = new String[20];
                recruit_info[0] = txtfullname.getText();
                recruit_info[1] = txtfname.getText();
                recruit_info[2] = txtlname.getText();
                recruit_info[3] = txtphone.getText();
                recruit_info[4] = txtrace.getText();
                recruit_info[5] = cbsex.getSelectedItem().toString();
                recruit_info[6] = txtdob.getText();
                recruit_info[7] = txtaddress.getText();
                recruit_info[8] = txtcity.getText();
                recruit_info[9] = txtstate.getText();
                recruit_info[10] = txtzip.getText();
                recruit_info[11] = cbfst.getSelectedItem().toString();
                recruit_info[12] = cbst.getSelectedItem().toString();
                recruit_info[13] = cbap.getSelectedItem().toString();
                recruit_info[14] = cbse.getSelectedItem().toString();
                recruit_info[15] = cbss.getSelectedItem().toString();
                recruit_info[16] = cbcontacts.getSelectedItem().toString();
                recruit_info[17] = cbla_qualified.getSelectedItem().toString();
                recruit_info[18] = txtla_screen_date.getText();
                recruit_info[19] = tanotes.getText();

                conn.updateRecruits(recruitid, recruit_info);
                conn.deleteRecruitStudyTypes(recruitid);
                List<String> selectedItems = new ArrayList<>();
                selectedItems = types.getSelectedValuesList();
                for (String item:selectedItems){
                    conn.insertRecruitStudyType(recruitid, item);
                }
                
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Dates needs to be in the format 'yyyy-mm-dd'");
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }else if (e.getSource() == prev){
            new PickRecordPage(_iconImage, _table);
            this.dispose();
        }
    }
    private boolean isValidDates(){
        return txtdob.getText().equals("") || DateValidator.getInstance().isValid(txtdob.getText(), "yyyy-MM-dd") &&
                txtla_screen_date.getText().equals("") || DateValidator.getInstance().isValid(txtla_screen_date.getText(), "yyyy-MM-dd");
    }
}
