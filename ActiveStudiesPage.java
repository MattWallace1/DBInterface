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
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author joe_admin
 */
public class ActiveStudiesPage extends JFrame implements ActionListener{
    
    
    private JMenuBar mb;
    private JMenu closeMenu, moreInfoMenu, exitMenu;
    private JMenuItem closeStudy, moreRecruitInfo, moreStudyInfo, exit;
    
    private Image _iconImage;
    private String[][] _data;
    private String[] _colnames;
    private JTable outTable;
    private JScrollPane sp;
    
    public ActiveStudiesPage(String[][] data, String[] colnames, Image iconImage){
        _iconImage = iconImage;
        _data = data;
        _colnames = colnames;
        this.setIconImage(_iconImage);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Active Studies");
        this.setPreferredSize(new Dimension(900,600));
        
        int[] counts = getCounts(_data);
        
        
        mb = new JMenuBar();
        closeMenu = new JMenu("Close Study");
        moreInfoMenu = new JMenu("See More");
        exitMenu = new JMenu("Exit");
        closeStudy = new JMenuItem("Close a Study");
        moreRecruitInfo = new JMenuItem("See More Recruit Info");
        moreStudyInfo = new JMenuItem("See More Study Info");
        exit = new JMenuItem("Close Page");
        closeStudy.addActionListener(this);
        moreRecruitInfo.addActionListener(this);
        moreStudyInfo.addActionListener(this);
        exit.addActionListener(this);
        closeMenu.add(closeStudy);
        moreInfoMenu.add(moreRecruitInfo);
        moreInfoMenu.add(moreStudyInfo);
        exitMenu.add(exit);
        mb.add(closeMenu);
        mb.add(moreInfoMenu);
        mb.add(exitMenu);
        
        this.setJMenuBar(mb);
        
        String[][] dataCounts = addCountsToData(_data);
        outTable = new JTable(dataCounts, _colnames);
        
        
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
        if (e.getSource() == closeStudy){
            new CloseStudyPage(_iconImage);
            this.dispose();
        }else if (e.getSource() == moreRecruitInfo){
            new MoreRecruitInfoPage(_iconImage, "ongoingstudies");
        }else if (e.getSource() == moreStudyInfo){
            createMoreStudyInfoPage();
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    
    private void createMoreStudyInfoPage(){
        Connect conn = new Connect();
        String[][] data = conn.getActiveStudyInfo();
        String[] colnames = conn.getAllColNames("studies");
        if (!(data == null)){
            new OutputPage(data, colnames, "Active Studies", _iconImage);

        }else{
            String[][] emptydata = {{""}};
            String[] emptycols = {""};
            new OutputPage(emptydata, emptycols, "No records found", _iconImage);
        }
    }
    
    private String[][] addCountsToData(String[][] data){
        int[] counts = getCounts(data);
        int M = data.length ; int N = data[0].length;
        String[][] dataCounts = new String[M+1][N];
        for (int i = 0 ; i < counts.length ; i++){
            dataCounts[0][i] = "<html><b>" + counts[i] + " Subject(s)";
        }
        for (int i = 0 ; i < M ; i++){
            for (int j = 0 ; j < N ; j++){
                dataCounts[i+1][j] = data[i][j];
            }
        }
        return dataCounts;
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
    
    
    
    

    
    
}
