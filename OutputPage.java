/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Matthew Wallace
 */


public class OutputPage extends JFrame implements ActionListener{
    
    private JMenuBar mb;
    private JMenu exportMenu;
    private JMenu exitMenu;
    private JMenuItem exportExcel;
    private JMenuItem exit;
    private JTable outTable;
    private String[][] _data;
    private String[] _colnames;
    private String _title;
    
    
    
    /**
     * Given row data and field names, prints the data
     * to a JTable in a new page
     * 
     * @param data
     *      2d String array of the row data
     * @param colnames
     *      String array of column headers
     */
    public OutputPage(String[][] data, String[] colnames, String title, Image iconImage){
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(900,600));
        this.setIconImage(iconImage);
        
        mb = new JMenuBar();
        exportMenu = new JMenu("Export");
        exitMenu = new JMenu("Exit");
        exportExcel = new JMenuItem("Export to Excel");
        exportExcel.addActionListener(this);
        exit = new JMenuItem("Close page");
        exit.addActionListener(this);
        
        exportMenu.add(exportExcel);
        exitMenu.add(exit);
        mb.add(exportMenu);
        mb.add(exitMenu);
        
        _data = data;
        _colnames = colnames;
        _title = title;
        outTable = new JTable(_data, _colnames);
        
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
        
        this.setJMenuBar(mb);
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == exportExcel){
            Object[][] data = getTableData();
            try {
                writeToExcel(data);
            }catch(IOException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }else if (e.getSource() == exit){
            this.dispose();
        }
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
        
            XSSFSheet sheet = wb.createSheet(_title);
            XSSFRow row;
            Map<Integer, Object[]> dataMap = new TreeMap<>();

            // create tree map to link each record to a row key
            dataMap.put(1, (Object[])_colnames);
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
