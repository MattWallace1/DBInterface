/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

import SQLConnection.Connect;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.commons.validator.routines.DateValidator;

/**
 *
 * @author joe_admin
 */
public class CloseStudyPage extends JFrame implements ActionListener{
    
    private JLabel lblselect;
    private JComboBox cbpanels;
    private JButton btnSubmit;
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    private Image _iconImage;
    public CloseStudyPage(Image iconImage){
        _iconImage = iconImage;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(400,300));
        this.setIconImage(iconImage);
        this.setTitle("Close a Panel");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        lblselect = new JLabel("Select a study to close:");
        Connect conn = new Connect();
        String[][] activePanels = conn.getActiveStudyIdPsn();
        if (!(activePanels == null)){
            String[] cbitems = new String[activePanels.length];
            for (int i = 0 ; i < cbitems.length ; i++){
                cbitems[i] = activePanels[i][0] + ", PSN: " + activePanels[i][1];
            }
            cbpanels = new JComboBox(cbitems);
        }else{
            String[] empty_cbitems = {""};
            cbpanels = new JComboBox(empty_cbitems);
        }
        
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0; gbc.ipady = 0;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridheight = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20,20,0,0);
        gbc.weightx = 0.1; gbc.weighty = 0.1;
        this.add(lblselect, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0,20,0,0);
        gbc.weightx = 0.2; gbc.weighty = 0.8;
        this.add(cbpanels, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(0,50,0,20);
        gbc.weightx = 0.1; gbc.weighty = 0.8;
        this.add(btnSubmit, gbc);
        
        
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            Connect conn = new Connect();
            String idpsn = cbpanels.getSelectedItem().toString().split(",")[0];
            if (!(idpsn.equals("") || idpsn == null)){
                String[] panelinfo = conn.getStudyInfoFromId(idpsn.split(" ")[0]); // 0 -> study id, 1 -> psn, 2 -> study type
                String study_id = panelinfo[0]; String study_psn = panelinfo[1]; String study_type = panelinfo[2];
                int selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to end this study?\n"
                        + "Id: " + study_id + ", PSN: " + study_psn + ", " + study_type);

                if (selection == 0){

                    String paid_date = "";
                    while (!isValidDateInput(paid_date)){
                        paid_date = JOptionPane.showInputDialog(this, "What date will the recruits be paid?");
                    }


                    // get all recruits, loop through, prompt for payment amt for each
                    String[][] recruitlist = conn.getActiveStudyRecruits(idpsn);
                    String payment_amt = "";
                    double total_pay = 0;
                    if (!(recruitlist == null)){
                        for (String[] recruit:recruitlist){
                            String msg = "Payment amount for " + recruit[2] + " " + recruit[1] + ", ID " + recruit[0] + ":";
                            payment_amt = JOptionPane.showInputDialog(this, msg);
                            total_pay = Double.parseDouble(payment_amt);
                            conn.insertVisits(recruit[0], study_id, total_pay, paid_date, recruit[2]);
                            conn.updateNumStudies(recruit[0], -1);
                        }
                    }
                    
                    conn.updateIsActiveStudy(study_id, "0");
                    conn.deletePanel(study_id); // delete records from ongoingstudies table
                    conn.updateStudiesClosedDate(study_id);
                    conn.updateStudyClosedBy(conn.getMySQLUser(), study_id);
                    this.dispose();
                    //createViewStudiesPage();
                }
            }else{
                JOptionPane.showMessageDialog(this, "There are currently no active studies");
            }
        }else if(e.getSource() == exit){
            this.dispose();
        }
    }
    
    private boolean isValidDateInput(String date){
        return DateValidator.getInstance().isValid(date, "yyyy-MM-dd");
    }
    
    
    
    private void createViewStudiesPage(){
        Connect conn = new Connect();
        String[][] data = conn.selectAllActiveStudies();
        if (data == null){
            String[][] table = {{""}};
            String[] colnames = {""};
            new ActiveStudiesPage(table, colnames, _iconImage);
        }else{
            
            ArrayList<ArrayList<String>> rows = partitionOnStudies(data);
            String[][] table = fillGaps(rows);
            table = transpose(table);
            String[][] studyidsPsns = conn.getActiveStudyIdPsn();
            String[] colnames = new String[studyidsPsns.length];
            for (String[] row:studyidsPsns){
                System.out.println(Arrays.toString(row));
            }
            for (int i = 0 ; i < colnames.length ; i++){
                colnames[i] = "PSN: " + studyidsPsns[i][1] + ", ID: " + studyidsPsns[i][0];
            }
            
            // trim down colnames to match length of data
            String[] short_colnames = new String[table[0].length];
            System.out.println("table:");
            for (String[] row:table){
                System.out.println(Arrays.toString(row));
            }
            System.out.println("\nColnames:");
            System.out.println(Arrays.toString(colnames));
            System.out.println("\nshort colnames length: " + short_colnames.length);
            for (int i = 0 ; i < table[0].length ; i++){
               short_colnames[i] = colnames[i];
            }
            
            new ActiveStudiesPage(table, short_colnames, _iconImage);
        }
    }
    

    
    
    /**
     * @param table
     * @return the matrix transpose of the table
     */
    private String[][] transpose(String[][] table){
        String[][] ret = new String[table[0].length][table.length];
        for (int i = 0 ; i < table[0].length ; i++){
            for (int j = 0 ; j < table.length ; j++){
                ret[i][j] = table[j][i];
            }
        }
        return ret;
    }
    
    /**
     * pad the shorter rows of a matrix with empty strings
     * 
     * @param rows
     *      2d partitioned ArrayList
     * @return the padded partitioned data
     */
    private String[][] fillGaps(ArrayList<ArrayList<String>> rows){
        int maxlength = 0;
        for (ArrayList<String> row:rows){
            if (row.size() > maxlength){
                maxlength = row.size();
            }
        }
        
        for (ArrayList<String> row:rows){
            int lendifference = maxlength - row.size();
            for (int i = 0 ; i < lendifference ; i++){
                row.add("");
            }
        }
        
        
        String[][] table = new String[rows.size()][rows.get(0).size()];
        for (int i = 0 ; i < table.length ; i++){
            for (int j = 0 ; j < table[0].length ; j++){
                table[i][j] = rows.get(i).get(j);
            }
        }
        return table;
    }
    
    /**
     * For each pair of recruit & study id's in data, 
     * places recruit id's with the same corresponding 
     * study id in the same list
     * @param data
     * @return a 2d ArrayList of partitioned data
     */
    private ArrayList<ArrayList<String>> partitionOnStudies(String[][] data){
        String[] studyids = new String[data.length];
        for (int i = 0 ; i < data.length ; i++){
            studyids[i] = data[i][2];
        }
        String[] distinctstudyids = new String[countDistinct(studyids)];
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        int coli = 0;
        
        for (int i = 0 ; i < data.length ; i++){
            ArrayList<String> row = new ArrayList<>();
            while (i < data.length - 1 && studyids[i].equals(studyids[i+1])){
                row.add(data[i][1] + ", ID " + data[i][0]);
                i++;
            }
            row.add(data[i][1] + ", ID " + data[i][0]);
            rows.add(row);
            distinctstudyids[coli] = studyids[i];
            coli++;
        }
        return rows;
    }
    
    /**
     * 
     * @param arr
     * @return the number of distinct elements in arr
     */
    private int countDistinct(String[] arr){
        HashSet<String> hs = new HashSet<>();
        for (String s:arr){
            hs.add(s);
        }
        return hs.size();
    }
}