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
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Joe_admin
 */
public class MainPage extends JFrame implements ActionListener{
    
    
    private JMenuBar mb;
    private JMenu selectMenu, searchMenu, insertMenu, recruitPanelsMenu, activeStudiesMenu, paymentMenu, auditMenu, exitMenu;
    private JMenuItem selectAll, filter, selectRecruitsByStudy, selectStudiesPerRecruit, selectProductsinStudy, selectProductsByRecruit;
    private JMenuItem search;
    private JMenuItem insertRecruits, insertStudies, insertProducts;
    private JMenuItem viewPanels, setStudyActive;
    private JMenuItem viewStudies;
    private JMenuItem payRecruits;
    private JMenuItem auditing;
    private JMenuItem exit;
    private Image _iconImage;
    
    private Connect _conn;
    
    /**
     * Creates the home page
     */
    MainPage(){
        _conn = new Connect();
        mb = new JMenuBar();
        selectMenu = new JMenu("Pull Records");
        searchMenu = new JMenu("Search");
        insertMenu = new JMenu("Insert New");
        recruitPanelsMenu = new JMenu("Recruit Panels");
        activeStudiesMenu = new JMenu("Active Studies");
        paymentMenu = new JMenu("Payment");
        auditMenu = new JMenu("Audit Logs");
        exitMenu = new JMenu("Exit");
        
        selectAll = new JMenuItem("View whole table");
        filter = new JMenuItem("Filter Recruits");
        selectRecruitsByStudy = new JMenuItem("Recruits in a Study");
        selectStudiesPerRecruit = new JMenuItem("Studies Completed by a Recruit");
        selectProductsinStudy = new JMenuItem("Products Used in a Study");
        selectProductsByRecruit = new JMenuItem("Products Used by a Recruit");
        search = new JMenuItem("Search a Table");
        insertRecruits = new JMenuItem("New Recruit");
        insertStudies = new JMenuItem("New Study");
        insertProducts = new JMenuItem("Add a Product to a Study");
        viewPanels = new JMenuItem("View Recruit Panels");
        setStudyActive = new JMenuItem("Begin Recruiting For a Study");
        viewStudies = new JMenuItem("View Active Studies");
        payRecruits = new JMenuItem("Pay Recruits");
        auditing = new JMenuItem("View Audit Log");
        exit = new JMenuItem("Close Program");
        
        selectAll.addActionListener(this);
        filter.addActionListener(this);
        selectRecruitsByStudy.addActionListener(this);
        selectStudiesPerRecruit.addActionListener(this);
        selectProductsinStudy.addActionListener(this);
        selectProductsByRecruit.addActionListener(this);
        search.addActionListener(this);
        insertRecruits.addActionListener(this);
        insertStudies.addActionListener(this);
        insertProducts.addActionListener(this);
        viewPanels.addActionListener(this);
        setStudyActive.addActionListener(this);
        viewStudies.addActionListener(this);
        payRecruits.addActionListener(this);
        auditing.addActionListener(this);
        exit.addActionListener(this);
        
        selectMenu.add(selectAll);
        selectMenu.add(selectRecruitsByStudy);
        selectMenu.add(selectStudiesPerRecruit);
        selectMenu.add(selectProductsinStudy);
        selectMenu.add(selectProductsByRecruit);
        searchMenu.add(search);
        searchMenu.add(filter);
        insertMenu.add(insertRecruits);
        insertMenu.add(insertStudies);
        insertMenu.add(insertProducts);
        recruitPanelsMenu.add(viewPanels);
        recruitPanelsMenu.add(setStudyActive);
        activeStudiesMenu.add(viewStudies);
        paymentMenu.add(payRecruits);
        auditMenu.add(auditing);
        exitMenu.add(exit);
        
        mb.add(selectMenu);
        mb.add(searchMenu);
        mb.add(insertMenu);
        mb.add(recruitPanelsMenu);
        mb.add(activeStudiesMenu);
        mb.add(paymentMenu);
        mb.add(auditMenu);
        mb.add(exitMenu);
        
        this.setJMenuBar(mb);
        
        this.setTitle("Home Page");
        _iconImage = new ImageIcon(getClass().getResource("/Images/PII_logo.png")).getImage();
        this.setIconImage(_iconImage);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(750,400));
        
        
        String label = """
                       <html>
                       <head>
                       <style>
                       h{
                       text-align: center;
                       font-size: 24px;
                       }
                       p{
                       text-align: center;
                       font-size: 16px;
                       }
                       </style>
                       </head>
                       <body>
                       
                       <h> Welcome to the PII Database!</h>
                       <p> This is the text where more info will go<br>
                       this is the second line</br></html>
                       """;
        
        JLabel header = new JLabel("""
                                   <html>
                                   <style>
                                   div{
                                   text-align: center;
                                   font-size: 18px
                                   }
                                   </style>
                                   <div>Welcome to the PII Database!</div></html>
                                   """);
        JLabel info = new JLabel("""
                                 <html>
                                 <style>
                                 div{
                                 text-align: center;
                                 font-size: 12 px;
                                 }
                                 ul li{
                                    list-style-type: none;
                                    text-align: center;
                                 }
                                 </style>
                                 <div>
                                    Select an option from the menu bar to get started<br><br><br>
                                    Things to know:<br>
                                 </div>
                                 <list>
                                    <ul>
                                        <li>• The 'Users' text file found in F:/MySQLServer/Users must not be edited or moved</li>
                                        <li>• If your desktop icon stops working, an original copy of the program can be found in F:/DBAppInfo/Interface</li>
                                        <li>• To set up a new user, follow the steps outlined in F:/DBAppInfo</li>
                                        <li>• The IP address for the server host is 192.168.100.7</li>
                                    </ul>
                                 </list>
                                 </html>
                                 """);
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(30,0,0,0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(header, gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0,0,0,0);
        this.add(info, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == selectAll){
            new SelectAllPage(_iconImage);
        }else if (e.getSource() == filter){
            new FilterRecruitsPage(_iconImage);
        }else if (e.getSource() == selectRecruitsByStudy){
            new SelectRecruitsByStudyPage(_iconImage);
        }else if (e.getSource() == selectStudiesPerRecruit){
            new SelectStudiesByRecruitPage(_iconImage);
        }else if (e.getSource() == selectProductsinStudy){
            new SelectProductsinStudyPage(_iconImage);
        }else if (e.getSource() == selectProductsByRecruit){
            new SelectProductsByRecruitPage(_iconImage);
        }else if (e.getSource() == search){
            new UpdateTablePage(_iconImage);
        }else if (e.getSource() == insertRecruits){
            new InsertRecruitsPage(_iconImage);
        }else if (e.getSource() == insertStudies){
            new InsertStudiesPage(_iconImage);
        }else if (e.getSource() == insertProducts){
            new InsertProductsPage(_iconImage);
        }else if (e.getSource() == viewPanels){
            createViewPanelsPage();
        }else if (e.getSource() == setStudyActive){
            new UpdateStudyRecruitingStatus(_iconImage);
        }else if (e.getSource() == viewStudies){
            createViewStudiesPage();
        }else if (e.getSource() == payRecruits){
            if (_conn.getCountUnpaidStudies() > 0){
                new PayRecruitsPage(_iconImage);
            }else{
                JOptionPane.showMessageDialog(this, "All subject payments are up to date");
            }
        }else if (e.getSource() == auditing){
            new AuditLogPage(_iconImage);
        }else if (e.getSource() == exit){
            this.dispose();
            System.exit(1);
        }
        
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
            for (int i = 0 ; i < table[0].length ; i++){
               short_colnames[i] = colnames[i];
            }
            
            new ActiveStudiesPage(table, short_colnames, _iconImage);
        }
    }
    
    /**
     * Collects all recruit panels, partitions on each panel, sends to output page
     */
    private void createViewPanelsPage(){
        Connect conn = new Connect();
        String[][] data = conn.selectAllPanels();
        if (data == null){
            String[][] table = {{""}};
            String[] colnames = {""};
            new RecruitPanelsPage(table, colnames, _iconImage);
        }else{
            
            ArrayList<ArrayList<String>> rows = partitionOnPanels(data);
            String[][] table = fillGaps(rows);
            table = transpose(table);
            String[][] studyidsPsns = conn.getPanelStudyIdsPSNs();
            String[] colnames = new String[studyidsPsns.length];
            for (int i = 0 ; i < colnames.length ; i++){
                colnames[i] = "PSN: " + studyidsPsns[i][1] + ", ID: " + studyidsPsns[i][0];
            }
            
            // trim down colnames to match length of data
            String[] short_colnames = new String[table[0].length];
            for (int i = 0 ; i < table[0].length ; i++){
               short_colnames[i] = colnames[i];
            }
            
            new RecruitPanelsPage(table, short_colnames, _iconImage);
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
     * Same functionality of partitionOnPanels, but handles panelist number of active studies
     * @param data
     * @return 
     */
    private ArrayList<ArrayList<String>> partitionOnStudies(String[][] data){
        String[] studyids = new String[data.length];
        for (int i = 0 ; i < data.length ; i++){
            studyids[i] = data[i][3];
        }
        
        String[] distinctstudyids = new String[countDistinct(studyids)];
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        int coli = 0;
        
        for (int i = 0 ; i < data.length ; i++){
            ArrayList<String> row = new ArrayList<>();
            while (i < data.length - 1 && studyids[i].equals(studyids[i+1])){
                row.add(data[i][0] + " " + data[i][2] + ", ID: " + data[i][1]);
                i++;
            }
            row.add(data[i][0] + " " + data[i][2] + ", ID: " + data[i][1]);
            rows.add(row);
            distinctstudyids[coli] = studyids[i];
            coli++;
        }
        return rows;
    }
    
    /**
     * For each pair of recruit & study id's in data, 
     * places recruit id's with the same corresponding 
     * study id in the same list
     * @param data
     * @return a 2d ArrayList of partitioned data
     */
    private ArrayList<ArrayList<String>> partitionOnPanels(String[][] data){
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
