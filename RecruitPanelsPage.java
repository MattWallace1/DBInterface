/*,
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import SQLConnection.Connect;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;


class MultiLineTableHeaderRenderer extends JTextArea implements TableCellRenderer{
    public MultiLineTableHeaderRenderer(){
        setEditable(false);
        setLineWrap(true);
        setOpaque(false);
        setFocusable(false);
        setWrapStyleWord(true);
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        int width = table.getColumnModel().getColumn(column).getWidth();
        setText((String)value);
        setSize(width, getPreferredSize().height);
        return this;
    }
}

/**
 *
 * @author joe_admin
 */
public class RecruitPanelsPage extends JFrame implements ActionListener{
    
    private JMenuBar mb;
    private JMenu editMenu, infoMenu, finalizeMenu, exitMenu;
    private JMenuItem addRecruit, removeRecruit, recruitInfo, studyInfo, finalize, exit;
    private Connect conn;
    
    private JScrollPane sp;
    private JTable outTable;
    private String[][] _data;
    private String[] _colnames;
    
    
    Image _iconImage;
    public RecruitPanelsPage(String[][] data, String[] colnames, Image iconImage){
        _data = data;
        _colnames = colnames;
        _iconImage = iconImage;
        this.setIconImage(_iconImage);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Recruit Panels");
        this.setPreferredSize(new Dimension(900,600));
        
        int[] counts = getCounts(_data);
        
        
        mb = new JMenuBar();
        editMenu = new JMenu("Edit Panels");
        infoMenu = new JMenu("See More");
        finalizeMenu = new JMenu("Finalize");
        exitMenu = new JMenu("Exit");
        addRecruit = new JMenuItem("Add a Recruit");
        removeRecruit = new JMenuItem("Remove a Recruit");
        recruitInfo = new JMenuItem("See More Recruit Info");
        studyInfo = new JMenuItem("See More Study Info");
        finalize = new JMenuItem("Finalize a Study");
        exit = new JMenuItem("Close Page");
        addRecruit.addActionListener(this);
        removeRecruit.addActionListener(this);
        recruitInfo.addActionListener(this);
        studyInfo.addActionListener(this);
        finalize.addActionListener(this);
        exit.addActionListener(this);
        editMenu.add(addRecruit); editMenu.add(removeRecruit);
        infoMenu.add(recruitInfo); infoMenu.add(studyInfo);
        finalizeMenu.add(finalize);
        exitMenu.add(exit);
        mb.add(editMenu); mb.add(infoMenu); mb.add(finalizeMenu); mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        String[][] dataWithCounts = addCountsToData(_data);
        
        outTable = new JTable(dataWithCounts, _colnames);
        
        
        final TableColumnModel columnModel = outTable.getColumnModel();
        for (int col = 0 ; col < outTable.getColumnCount() ; col++){
            int width = 120;
            for (int row = 0 ; row < outTable.getRowCount() ; row++){
                TableCellRenderer renderer = outTable.getCellRenderer(row, col);
                Component comp = outTable.prepareRenderer(renderer, row, col);
                width = Math.max(comp.getPreferredSize().width+1, width);
            }
            if (width > 600){
                width = 600;
            }
            columnModel.getColumn(col).setPreferredWidth(width);
        }
        outTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sp = new JScrollPane(outTable);
        this.add(sp, BorderLayout.CENTER);
        
        
        this.pack();
        this.setVisible(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addRecruit){
            addRecruitToPanel();
            
        }else if (e.getSource() == removeRecruit){
            removeRecruitFromPanel();
            redrawTable();
        }else if (e.getSource() == recruitInfo){
            new MoreRecruitInfoPage(_iconImage, "recruitpanels");
            
        }else if (e.getSource() == studyInfo){
            createMoreStudyInfoPage();
        }else if (e.getSource() == finalize){
            finalizeStudy();
            redrawTable();
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    
    
    private void finalizeStudy(){
        
        conn = new Connect();
        String[][] studyIdsPSNs = conn.getPanelStudyIdsPSNs();
        if (studyIdsPSNs == null){
            return;
        }
        String[] studyIds = new String[studyIdsPSNs.length];
        for (int i = 0 ; i < studyIds.length ; i++){
            studyIds[i] = studyIdsPSNs[i][0];
        }
        if (studyIds == null){
            JOptionPane.showMessageDialog(this, "There are no studies to recruit for at this time");
        }else{
            boolean isValidStudyId = false;
            String studyid = "";
            while (!isValidStudyId && !(studyid == null)){
                studyid = JOptionPane.showInputDialog("Enter the ID of the study you would like to finalize:", JOptionPane.OK_CANCEL_OPTION);
                for (String study:studyIds){
                    if (study.equals(studyid)){
                        isValidStudyId = true;
                    }
                }
            }
            
            int selection = JOptionPane.showConfirmDialog(this, "<html>Are you sure you want to finalize this panel?<br><i>This action cannot be undone</html>", "Please Confirm", JOptionPane.YES_NO_OPTION);
            if (selection == 1){
                return;
            }
            
            // get recruits from recruitpanels where study_id = studyid
            // -> join recruits table to get name, phone
            
            String[][] panelists = conn.getRecruitsFromPanel(studyid);
            String[] colnames = {"Panelist Number","Name","Recruit ID","Phone"};
            
            new EnterPanelistNumberPage(panelists, colnames, studyid, _iconImage);
            redrawTable();
        }
        
        
    }
    
    private void addRecruitToPanel(){
        conn = new Connect();
        String[][] studyIdsPSNs = conn.getIsRecruitingStudyIdsPsns();
        if (studyIdsPSNs == null){
            return;
        }
        String[] studyids = new String[studyIdsPSNs.length];
        String[] cbitems = new String[studyIdsPSNs.length];
        for (int i = 0 ; i < studyIdsPSNs.length ; i++){
            studyids[i] = studyIdsPSNs[i][0];
            cbitems[i] = studyIdsPSNs[i][0] + ", PSN: " + studyIdsPSNs[i][1];
        }
        JComboBox cb = new JComboBox(cbitems);
        JPanel studypanel = new JPanel();
        studypanel.add(new JLabel("Enter a valid study id from the drop-down list:"));
        studypanel.add(cb);
        
        // get study to remove from
        String studyid = "";
        boolean isValidStudyInput = false;
        while (!(studyid == null) && !isValidStudyInput){
            
            studyid = (String)JOptionPane.showInputDialog(this, studypanel, "Enter a Study ID", JOptionPane.PLAIN_MESSAGE, null,null,"");
            for (String study:studyids){
                if (study.equals(studyid)){
                    isValidStudyInput = true;
                }
            }
        }
        
        boolean cancelled = studyid == null;
        
        
        // loop here
        int keepAddingSelection = 0;
        int cancel_selection = -1;
        while (keepAddingSelection == 0 && !cancelled){
        
            String[] recruitids = conn.getRecruitIdNameForPanelSelection(studyid);
            JComboBox recruit_cb = new JComboBox(recruitids);
            JPanel recruitpanel = new JPanel();
            recruitpanel.add(new JLabel("Enter a valid recruit id from the drop-down list:"));
            recruitpanel.add(recruit_cb);


            String recruitid = "";
            boolean isValidRecruitInput = false;
            while (!(recruitid == null) && !isValidRecruitInput && !cancelled){
                recruitid = (String)JOptionPane.showInputDialog(this, recruitpanel, "Enter a Recruit ID", JOptionPane.PLAIN_MESSAGE, null,null,"");
                
                if (!(recruitid == null)){
                    for (String id_name:recruitids){
                        if (recruitid.equals(id_name.split(",")[0]) && !recruitid.equals("")){
                            isValidRecruitInput = true;
                            System.out.println("found a match");
                        }
                    }
                }
            }
            cancelled = cancelled || recruitid == null;


            if (!cancelled){

                int num_panels = conn.getNumPanels(recruitid);
                int num_studies = conn.getNumStudies(recruitid);

                ArrayList<String[]> other_panels = new ArrayList<>();
                ArrayList<String[]> other_studies = new ArrayList<>();
                String msg = "";
                boolean inOtherPanels = false;
                boolean inOtherStudies = false;
                if (num_panels > 0){
                    inOtherPanels = true;
                    other_panels = conn.getRecruitPanels(recruitid);

                }
                if (num_studies > 0){
                    inOtherStudies = true;
                    other_studies = conn.getRecruitActiveStudies(recruitid);
                }


                boolean canAdd = false;
                
                if (!inOtherPanels && !inOtherStudies){
                    canAdd = true;
                }else{
                    if (inOtherPanels){
                        msg += "The recruit is in the following panel(s):\n";
                        for (String[] row:other_panels){
                            msg += "ID: " + row[0] + ", PSN: " + row[1] + ", " + row[2];
                        }
                    }
                    if (inOtherStudies){
                        msg += "\n\nThe recruit is in the following study(s):";
                        for (String[] row:other_studies){
                            msg += "ID: " + row[0] + ", PSN: " + row[1] + ", " + row[2];
                        }
                    }

                    msg += "\n\nWould you still like to add the recruit?";

                    cancel_selection = JOptionPane.showConfirmDialog(this, msg, "Proceed?", JOptionPane.YES_NO_OPTION);


                }
                if (cancel_selection == 0 || canAdd){
                    conn.insertPanelRecruit(studyid, recruitid);
                    conn.updateNumPanels(recruitid, 1);
                }

            }
            redrawTable();
            if (!cancelled){
                keepAddingSelection = JOptionPane.showConfirmDialog(this, "Would you like to keep adding recruits to PSN: " + conn.getPsn(studyid) + ", ID: " + studyid + "?", "Continue?", JOptionPane.YES_NO_OPTION);
            }
        }
        
    }
    

    
    
    
    private void removeRecruitFromPanel(){
        conn = new Connect();
        String[][] studyIdsPSNs = conn.getPanelStudyIdsPSNs();
        if (studyIdsPSNs == null){
            return;
        }
        String[] studyIds = new String[studyIdsPSNs.length];
        for (int i = 0 ; i < studyIds.length ; i++){
            studyIds[i] = studyIdsPSNs[i][0];
        }
        if (studyIds == null){
            JOptionPane.showMessageDialog(this, "There are no studies to remove from at this time");
        }else{
            boolean isValidStudyId = false;
            String studyid = "";
            String recruitid = "";
            while (!isValidStudyId && !(studyid == null)){
                studyid = (String)JOptionPane.showInputDialog(this,"Enter the ID of the study you would like to remove from:", "Enter a Study ID", JOptionPane.PLAIN_MESSAGE, null,null,"");
                for (String study:studyIds){
                    if (study.equals(studyid)){
                        isValidStudyId = true;
                    }
                }
            }
            
            if (!(studyid == null)){
                String[] recruitIds = conn.getPanelRecruitIds(studyid);
                boolean isValidRecruitId = false;
                while (!isValidRecruitId && !(recruitid == null)){
                    recruitid = (String)JOptionPane.showInputDialog(this,"Enter the ID of the recruit you would like to remove:", "Enter a Recruit ID", JOptionPane.PLAIN_MESSAGE, null,null,"");
                    if (!(recruitid == null)){
                        for (String recruit:recruitIds){
                            if (recruit.equals(recruitid)){
                                isValidRecruitId = true;
                            }
                        }
                    }
                }
                if (!(recruitid == null)){
                    conn.deleteRecruitFromPanel(studyid, recruitid);
                    conn.updateNumPanels(recruitid, -1);
                }
            }
            
            
        }
    }
    
    /**
     * Collects more study info from connect and sends to output page
     */
    private void createMoreStudyInfoPage(){
        Connect conn = new Connect();
        String[][] data = conn.getPanelInfo();
        String[] colnames = conn.getAllColNames("studies");
        if (!(data == null)){
            new OutputPage(data, colnames, "Studies from Recruit Panels", _iconImage);

        }else{
            String[][] emptydata = {{""}};
            String[] emptycols = {""};
            new OutputPage(emptydata, emptycols, "No records found", _iconImage);
        }
    }
    
    
    
    
    /**
     * Recreates the JTable on the page to account for recruits being added or removed
     */
    public void redrawTable(){
        this.getContentPane().remove(sp);
        Connect conn = new Connect();
        String[][] data = conn.selectAllPanels();
        if (data == null){
            String[][] table = {{""}};
            String[] colnames = {""};
            new RecruitPanelsPage(table, colnames, _iconImage);
        }else{
            
            ArrayList<ArrayList<String>> rows = partitionOnStudies(data);
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
            
            String[][] dataWithCounts = addCountsToData(table);
            
            outTable = new JTable(dataWithCounts, short_colnames);
            
            final TableColumnModel columnModel = outTable.getColumnModel();
            for (int col = 0 ; col < outTable.getColumnCount() ; col++){
                int width = 120;
                for (int row = 0 ; row < outTable.getRowCount() ; row++){
                    TableCellRenderer renderer = outTable.getCellRenderer(row, col);
                    Component comp = outTable.prepareRenderer(renderer, row, col);
                    width = Math.max(comp.getPreferredSize().width+1, width);
                }
                if (width > 600){
                    width = 600;
                }
                columnModel.getColumn(col).setPreferredWidth(width);
            }
            outTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            sp = new JScrollPane(outTable);
            this.getContentPane().add(sp, BorderLayout.CENTER);
            this.getContentPane().revalidate();
            this.getContentPane().repaint();
        }
    }
    
    
    private int[] getCounts(String[][] data){
        int[] counts = new int[data[0].length];
        for (int i = 0 ; i < data.length ; i++){
            for (int j = 0 ; j < data[0].length ; j++){
                if (!data[i][j].equals("")){
                    counts[j]++;
                }
            }
        }
        return counts;
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
    
    
    private String[][] addCountsToData(String[][] data){
        int[] counts = getCounts(data);
        int M = data.length ; int N = data[0].length;
        String[][] dataCounts = new String[M+1][N];
        // add counts to first row
        for (int i = 0 ; i < counts.length ; i++){
            dataCounts[0][i] = "<html><b>" + counts[i] + " Recruit(s)";
        }
        for (int i = 0 ; i < M ; i++){
            for (int j = 0 ; j < N ; j++){
                dataCounts[i+1][j] = data[i][j];
            }
        }
        return dataCounts;
    }
    
   
}
