/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import SQLConnection.Connect;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

/**
 *
 * @author joe_admin
 */
public class DisplayRecruitPage extends JFrame implements ActionListener{
    
    
    
    JMenuBar mb;
    JMenu editMenu;
    JMenuItem edit;
    JMenu exportMenu;
    JMenuItem export;
    JMenu exitMenu;
    JMenuItem pickOtherRecruit;
    JMenuItem exit;
    
    JTable outTable;
    String[] colnames = {"Full Name", "First Name", "Last Name", "Phone", "Race", "Sex", "DOB", "Address", "City", "State", "ZIP", "FST", "ST", "AP", "SE", "SS", "Contacts", "LA Qualified:", "LA Screen Date:", "Notes", "Preferred Study Types"};
    String title;
    
    Image _iconImage;
    Connect _conn;
    String _selected;
    String _table;
    
    public DisplayRecruitPage(Image iconImage, String selected, String table){
        _iconImage = iconImage;
        _selected = selected;
        _table = table;
        title = selected.split(",")[1] + ", ID " + selected.split(",")[0];
        
        mb = new JMenuBar();
        editMenu = new JMenu("Edit");
        edit = new JMenuItem("Edit This Recruit");
        edit.addActionListener(this);
        editMenu.add(edit);
        exportMenu = new JMenu("Export");
        export = new JMenuItem("Export to Excel");
        export.addActionListener(this);
        exportMenu.add(export);
        exitMenu = new JMenu("Exit");
        pickOtherRecruit = new JMenuItem("Pick a Different Recruit");
        pickOtherRecruit.addActionListener(this);
        exit = new JMenuItem("Close This Page");
        exit.addActionListener(this);
        exitMenu.add(pickOtherRecruit);
        exitMenu.add(exit);
        mb.add(editMenu);
        mb.add(exportMenu);
        mb.add(exitMenu);
        
        
        this.setJMenuBar(mb);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000,300));
        this.setTitle("Enter a Recruit ID");
        this.setIconImage(_iconImage);
        
        
        _conn = new Connect();
        String[] recruit_info = _conn.getRecruitInfoForUpdate(_selected.split(",")[0]);
        String[][] tableData = new String[1][recruit_info.length];
        tableData[0] = recruit_info;
        String[][] tableDataWithTypes = new String[tableData.length][tableData[0].length+1];
        for (int i = 0 ; i < tableDataWithTypes.length ; i ++){
            for (int j = 0 ; j < tableDataWithTypes[0].length ; j++){
                if (j == tableDataWithTypes[0].length - 1){
                    // get all study types for this recruit
                    String[] studyTypes = _conn.getStudyTypesForRecruit(selected.split(",")[0]);
                    String tableTypes = "";
                    for (String type:studyTypes){
                        tableTypes += type + ", ";
                    }
                    if (!tableTypes.equals("")){
                        tableTypes = tableTypes.substring(0, tableTypes.length()-2);
                    }
                    tableDataWithTypes[i][j] = tableTypes;
                }else{
                    tableDataWithTypes[i][j] = tableData[i][j];
                }
            }
        }
        
        
        outTable = new JTable(tableDataWithTypes, colnames);
        
        final TableColumnModel columnModel = outTable.getColumnModel();
        for (int col = 0; col < outTable.getColumnCount() ; col++){
            int width = 80;
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
        JScrollPane sp = new JScrollPane(outTable);
        this.add(sp, BorderLayout.CENTER);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == edit){
            new EditRecruitPage(_iconImage, _selected, "recruits");
        }else if (e.getSource() == pickOtherRecruit){
            new PickRecordPage(_iconImage, _table);
        }else if (e.getSource() == export){
            try{
                writeToExcel(getTableData());
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, ex);
            }
            
        }
        this.dispose();
    }
    
    private void writeToExcel(Object[][] data) throws FileNotFoundException, IOException{
        XSSFWorkbook wb = null;
        JFileChooser jfc = new JFileChooser();
        jfc.showDialog(this, "Save");
        File file = jfc.getSelectedFile();
        
        if (!(file == null)){
            
            String path = file.getPath();
            path = path.concat(".xlsx");
            try{
            wb = new XSSFWorkbook();
            }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            }
        
            XSSFSheet sheet = wb.createSheet(title);
            XSSFRow row;
            Map<Integer, Object[]> dataMap = new TreeMap<>();

            // create tree map to link each record to a row key
            dataMap.put(1, (Object[])colnames);
            for (int i = 0 ; i < data.length ; i++){
                dataMap.put(i+2, data[i]);
            }
            Set<Integer> keys = dataMap.keySet();

            // write data to sheet
            int rowidx = 0;
            for (Integer key:keys){
                row = sheet.createRow(rowidx++);
                Object[] arr = dataMap.get(key);
                int colidx = 0;
                for (Object obj:arr){
                    Cell cell = row.createCell(colidx++);
                    cell.setCellValue((String)obj);
                }
            }

            FileOutputStream outfile = new FileOutputStream(new File(path));
            wb.write(outfile);
            outfile.close();
            JOptionPane.showMessageDialog(this, "Export successful");
        }
        
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
    
}
