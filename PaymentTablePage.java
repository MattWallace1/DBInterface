/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import SQLConnection.Connect;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashSet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joe_admin
 */
public class PaymentTablePage extends JFrame implements ActionListener{
    
    JTable outTable;
    JMenuBar mb;
    JMenu submitMenu, exitMenu;
    JMenuItem submitPayments, exit;
    String _studyid;
    
    public PaymentTablePage(String[][] data, String[] colnames, String title, String studyid, Image iconImage) throws Exception{
        
        
        _studyid = studyid;
        
        submitPayments = new JMenuItem("Submit payments");
        submitPayments.addActionListener(this);
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        mb = new JMenuBar();
        submitMenu = new JMenu("Submit");
        exitMenu = new JMenu("Exit");
        submitMenu.add(submitPayments);
        exitMenu.add(exit);
        mb.add(submitMenu);
        mb.add(exitMenu);
        
        this.setJMenuBar(mb);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(900,600));
        this.setIconImage(iconImage);
        //get starting check number
        String starting_num = "";
        while (!isNumeric(starting_num)){
            starting_num = (String)JOptionPane.showInputDialog(this, "Enter the starting check number:", "Input Required", JOptionPane.PLAIN_MESSAGE, null, null, "");
        }
        if (starting_num == null){
            return;
        }
        
        int starting_checknum = Integer.parseInt(starting_num);
        
        
        // adjust format of paid date for conversion to paper checks
        // add check numbers
        if (!(data.length == 1 && data[0][0].equals(""))){
            for (int i = 0 ; i < data.length ; i++){
                data[i][17] = switchDateFormat(data[i][17]);
                data[i][18] = Integer.toString(starting_checknum++);
            }
        }
        outTable = new JTable(data, colnames); //create and populate new Jtable
        
        // auto resize columns based on content
        final TableColumnModel columnModel = outTable.getColumnModel();
        for (int col = 0 ; col < outTable.getColumnCount() ; col++){
            int width = 80; // min width
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
        outTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // turn off to allow for horizontal scroll bar
        JScrollPane sp = new JScrollPane(outTable);
        
        this.add(sp, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == submitPayments){
            Object[][] tableData = getTableData();
            String[] visitIds = getVisitIds();
            String[] checknums = getCheckNums();
            if (isValidChecknumInput(checknums)){
                Connect conn = new Connect();
                for (int i = 0 ; i < visitIds.length ; i++){
                    conn.updateCheckNum(visitIds[i], checknums[i]);
                }
                JOptionPane.showMessageDialog(this, "The new check numbers have been added");
                if (_studyid.equals("")){
                    String[] studyidsToUpdate = conn.getStudyIdUnpaid();
                    for (String id:studyidsToUpdate){
                        conn.updateSinglePaidStatus(id);
                    }
                }else{
                    conn.updateSinglePaidStatus(_studyid);
                }
                try {
                    writeToExcel(tableData);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Make sure check numbers have been entered properly");
            }
        }else if (e.getSource() == exit){
            int selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit Page?", JOptionPane.YES_NO_OPTION);
            if (selection == 0){
                this.dispose();
            }
        }
    }
    
    private boolean isNumeric(String checknum){
        if (checknum == null){
            return true;
        }
        try {
            int num = Integer.parseInt(checknum);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    
    
    private boolean isValidChecknumInput(String[] checknums){
        HashSet<String> hs = new HashSet<>();
        for (String num:checknums){
            if (!num.equals("")){
                hs.add(num);
            }
        }
        return hs.size() == checknums.length;
    }
    
    private void writeToExcel(Object[][] data) throws FileNotFoundException, IOException{
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook();
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        XSSFSheet sheet = wb.createSheet("Payment info");
        XSSFRow row;
        Map<Integer, Object[]> visitData = new TreeMap<>();
        
        
        // create tree map to link each record to a row key
        visitData.put(1, new Object[]{"Visit ID","Recruit ID","First Name","Last Name", "Phone", "Address","City","State","ZIP","Study ID","PSN","Study Type","Study Start Date","Study End date","Study Closed Date", "Panelist Num", "Total Pay","Paid Date","Check Number"});
        for (int i = 0 ; i < data.length ; i++){
            visitData.put(i+2, data[i]);
        }
        Set<Integer> keys = visitData.keySet();
        
        
        // Write data to sheet
        int rowidx = 0;
        for (Integer key:keys){
            row = sheet.createRow(rowidx++);
            Object[] arr = visitData.get(key);
            int colidx = 0;
            for (Object obj:arr){
                Cell cell = row.createCell(colidx++);
                cell.setCellValue((String)obj);
            }
        }
        
        String currdate = java.time.Clock.systemUTC().instant().toString().substring(0,10);
        String path = "F:/LUCILE 1/SubjectPaymentSheets/" + currdate + ".xlsx";
        
        FileOutputStream outfile = new FileOutputStream(new File(path));
        wb.write(outfile);
        outfile.close();
    }
    
    private String[] getVisitIds(){
        TableModel dtm = outTable.getModel();
        int nrows = dtm.getRowCount();
        String[] checknums = new String[nrows];
        for (int i = 0 ; i < nrows ; i++){
            checknums[i] = dtm.getValueAt(i, 0).toString();
        }
        return checknums;
    }
    
    private String[] getCheckNums(){
        TableModel dtm = outTable.getModel();
        int nrows = dtm.getRowCount();
        String[] checknums = new String[nrows];
        for (int i = 0 ; i < nrows ; i++){
            Object val = dtm.getValueAt(i, 18);
            if (val == null){
                checknums[i] = "";
            }else{
                checknums[i] = val.toString();
            }
        }
        return checknums;
    }
    
    private Object[][] getTableData(){
        TableModel dtm = outTable.getModel();
        int nrows = dtm.getRowCount(); int ncols = dtm.getColumnCount();
        Object[][] data = new Object[nrows][ncols];
        for (int i = 0 ; i < nrows ; i++){
            for (int j = 0 ; j < ncols ; j++){
                data[i][j] = dtm.getValueAt(i, j);
            }
        }
        return data;
    }
    
    /**
     * Takes a date of format YYYY-MM-DD and converts
     * it to MM-DD-YYYY
     * @param original
     * @return 
     */
    private String switchDateFormat(String original){
        String year = original.substring(0,4);
        String month = original.substring(5,7);
        String day = original.substring(8,10);
        
        return month+"-"+day+"-"+year;
        
    }
    
}
