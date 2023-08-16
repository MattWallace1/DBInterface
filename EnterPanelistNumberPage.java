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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
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

/**
 *
 * @author joe_admin
 */
public class EnterPanelistNumberPage extends JFrame implements ActionListener{
    
    private JMenuBar mb;
    private JMenu submitMenu;
    private JMenu exitMenu;
    private JMenuItem submitPanelists;
    private JMenuItem exit;
    
    private String _studyid;
    private JTable _table;
    private String[][] _data;
    private String[] _colnames;
    private JScrollPane _sp;
    private Connect _conn;
    private Image _iconImage;
    
    public EnterPanelistNumberPage(String[][] panelists, String[] colnames, String studyid, Image iconImage){
        _iconImage = iconImage;
        _studyid = studyid;
        _data = panelists;
        _colnames = colnames;
        this.setIconImage(_iconImage);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(900,600));
        this.setTitle("Enter Panelist Numbers");
        
        mb = new JMenuBar();
        submitMenu = new JMenu("Submit");
        exitMenu = new JMenu("Exit");
        submitPanelists = new JMenuItem("Submit Panelists");
        exit = new JMenuItem("Close Page");
        submitPanelists.addActionListener(this);
        exit.addActionListener(this);
        submitMenu.add(submitPanelists);
        exitMenu.add(exit);
        mb.add(submitMenu);
        mb.add(exitMenu);
        
        // produce jtable with empty column for panelist number
        // add submit menu item to enter panelist numbers
        
        _table = new JTable(_data, _colnames);
        final TableColumnModel columnModel = _table.getColumnModel();
        for (int col = 0 ; col < _table.getColumnCount() ; col++){
            int width = 120;
            for (int row = 0 ; row < _table.getRowCount() ; row++){
                TableCellRenderer renderer = _table.getCellRenderer(row, col);
                Component comp = _table.prepareRenderer(renderer, row, col);
                width = Math.max(comp.getPreferredSize().width+1, width);
            }
            if (width > 600){
                width = 600;
            }
            columnModel.getColumn(col).setPreferredWidth(width);
        }
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _sp = new JScrollPane(_table);
        this.add(_sp, BorderLayout.CENTER);
        
        this.setJMenuBar(mb);
        this.pack();
        this.setVisible(true);
        
        
        
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == submitPanelists){
            _conn = new Connect();
            String[] panelistnums = getPanelistNums();
            
            if (isValidNumsInput(panelistnums)){
                String[][] tabledata = getTableData();
                for (String[] panelist:tabledata){
                    _conn.updateNumPanels(panelist[2], -1);
                    _conn.updateNumStudies(panelist[2], 1);
                }
                _conn.insertOngoingStudies(tabledata, _studyid);
                _conn.deleteRecruitingPanel(_studyid);
                _conn.updateRecruitingToActive(_studyid);
                this.dispose();
            }else{
                String[][] tabledata = getTableData();
                JOptionPane.showMessageDialog(this, "Make sure that all panelist numbers have been entered properly");
                redrawTable(tabledata);
            }
        }else if (e.getSource() == exit){
            int selection = JOptionPane.showConfirmDialog(this, "<html>Are you sure you want to quit?<br><i>All data entered will be lost</html>", "Please Confirm", JOptionPane.YES_NO_OPTION);
            if (selection == 0){
                this.dispose();
            }
        }
    }
    
    
    private String[][] getTableData(){
        TableModel tm = _table.getModel();
        int nrows = tm.getRowCount();
        int ncols = tm.getColumnCount();
        String[][] data = new String[nrows][ncols];
        for (int i = 0 ; i < nrows ; i++){
            for (int j = 0 ; j < ncols ; j++){
                data[i][j] = tm.getValueAt(i, j).toString();
            }
        }
        return data;
    }
    
    
    private void redrawTable(String[][] tabledata){
        this.getContentPane().remove(_sp);
        _table = new JTable(tabledata, _colnames);
        final TableColumnModel columnModel = _table.getColumnModel();
        for (int col = 0 ; col < _table.getColumnCount() ; col++){
            int width = 120;
            for (int row = 0 ; row < _table.getRowCount() ; row++){
                TableCellRenderer renderer = _table.getCellRenderer(row, col);
                Component comp = _table.prepareRenderer(renderer, row, col);
                width = Math.max(comp.getPreferredSize().width+1, width);
            }
            if (width > 600){
                width = 600;
            }
            columnModel.getColumn(col).setPreferredWidth(width);
        }
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        _sp = new JScrollPane(_table);
        this.getContentPane().add(_sp, BorderLayout.CENTER);
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }
    
    
    private boolean isValidNumsInput(String[] nums){
        boolean allNumsInRange = true;
        HashSet<String> hs = new HashSet<>();
        for (String num:nums){
            if (!(num == null || num.equals(""))){
                hs.add(num);
                if ((Integer.parseInt(num) < 1 || Integer.parseInt(num) > nums.length)){
                    allNumsInRange = false;
                }
            }
            
            
            
        }
        
        return hs.size() == nums.length && allNumsInRange;
    }
    
    /**
     * Returns the panelist numbers in the first column of the table
     * @return 
     */
    private String[] getPanelistNums(){
        TableModel tm = _table.getModel();
        int nrows = tm.getRowCount();
        String[] panelistnums = new String[nrows];
        for (int i = 0 ; i < nrows ; i++){
            String val = tm.getValueAt(i, 0).toString();
            panelistnums[i] = val;
        }
        return panelistnums;
    }
    
}
