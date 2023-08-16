/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frames;

import java.util.ArrayList;

import SQLConnection.Connect;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


/**
 *
 * @author Matthew Wallace
 */
public class SelectAllPage extends JFrame implements ActionListener {
     
    private GridBagConstraints gbc;
    private JLabel selectLabel;
    private JComboBox cb;
    private JButton btnSubmit;
    private Image _iconImage;
    
    private JMenuBar mb;
    private JMenu exitMenu;
    private JMenuItem exit;
    
    /**
     * Prompts the user for a table to select all records from
     */
    public SelectAllPage(Image iconImage){
        _iconImage = iconImage;
        this.setIconImage(iconImage);
        // init frame params
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(300,200));
        this.setLayout(new GridBagLayout());
        this.setTitle("View an entire table");
        
        mb = new JMenuBar();
        exitMenu = new JMenu("Exit");
        exit = new JMenuItem("Close Page");
        exit.addActionListener(this);
        exitMenu.add(exit);
        mb.add(exitMenu);
        this.setJMenuBar(mb);
        
        // init constraint obj
        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        
        // LABEL
        selectLabel = new JLabel("Choose a table:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20,20,0,0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(selectLabel, gbc);
        
        
        // COMBO BOX
        String[] tables = {"recruits", "studies", "visits", "products"};
        cb = new JComboBox(tables);
        cb.setFocusable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(20,0,0,0);
        this.add(cb, gbc);
        
        
        // BUTTON
        btnSubmit = new JButton("Retrieve");
        btnSubmit.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0,20,0,0);
        this.add(btnSubmit, gbc);
        
        this.pack();
        this.setVisible(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSubmit){
            Connect conn = new Connect();
            String selectedTable = cb.getSelectedItem().toString();
            String[][] data = conn.selectAll(selectedTable);
            if (!(data == null)){
                String[] colnames = data[0];
                String[][] data_no_headers = new String[data.length-1][data[0].length];
                for (int i = 1 ; i < data.length ; i++){
                    for (int j = 0 ; j < data[0].length ; j++){
                        data_no_headers[i-1][j] = data[i][j];
                    }
                }
                new OutputPage(data_no_headers, colnames, selectedTable + " table", _iconImage);
            }else{
                String[] empty_colnames = {""};
                String[][] empty_data = {{""}};
                new OutputPage(empty_data, empty_colnames, "No records found", _iconImage);
            }
            this.dispose();
        }else if (e.getSource() == exit){
            this.dispose();
        }
    }
    
    
}
