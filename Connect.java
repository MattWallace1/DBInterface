package SQLConnection;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Matthew Wallace
 */
public class Connect {
    
    /**
     * Establishes a connection to the MySQL server on this device
     * @return a JDBC driver connection
     */
    public Connection connect(){
        Connection conn = null;
        
        /*
        // FOR CONNECTING LOCALLY
        String url = "jdbc:mysql://localhost:3306/pii?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String username = "root";
        String password = "";
        try{
            File pwfile = new File("F:/MySQLServer/pw.txt");
            Scanner scanner = new Scanner(pwfile);
            password = scanner.nextLine();
            scanner.close();
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, e);
        }
        */
        
        String[] usernamePassword = getUsernamePasswordFromFile(getLocalIP());
        
        String db_host = "192.168.100.7";
        String port = "3306";
        String schema = "pii";
        String username = usernamePassword[0];
        String password = usernamePassword[1];
        String url = "jdbc:mysql://" + db_host + ":" + port + "/" + schema;
        
        try{
            // get class driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        return conn;
    }
    
    /**
     * Gets the matching username and password from file for the IP address given
     * @param ip
     * @return 
     */
    private String[] getUsernamePasswordFromFile(String ip){
        String[] info = new String[2];
        try{
            File userfile = new File("F:/MySQLServer/Users.txt");
            Scanner infile = new Scanner(userfile);
            while (infile.hasNextLine()){
                String[] line = infile.nextLine().split(",");
                if (ip.equals(line[0])){
                    info[0] = line[1];
                    info[1] = line[2];
                }
            }
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, e);
        }
        return info;
    }

    /**
     * Get the IP address of the local machine
     * @return 
     */
    private String getLocalIP(){
        String ip = "";
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("google.com", 80));
            ip = socket.getLocalAddress().getHostAddress();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ip;
    }
    
    public void turnOnGeneralLog(){
        String sql1 = "SET GLOBAL general_log=1;";
        String sql2 = "SET GLOBAL log_output='table';";
        try (Connection conn = this.connect()){
            PreparedStatement pst1 = conn.prepareStatement(sql1);
            PreparedStatement pst2 = conn.prepareStatement(sql2);
            pst1.execute();
            pst2.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Gets recruit and study information for recruit panels
     * 
     * @return a 2d array of recruit data and corresponding study info
     */
    public String[][] selectAllPanels(){
        
        String sql = """
                     SELECT r.id AS 'recruit id', r.fullname, s.id AS 'study id', s.psn
                     FROM pii.recruitpanels rp
                     JOIN pii.recruits r ON r.id = rp.recruit_id
                     JOIN pii.studies s ON s.id = rp.study_id
                     ORDER BY rp.study_id, r.lname
                     """;
        int count = 0;
        boolean recordsExist = false;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }                    
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    /**
     * Gets recruit and study information for active studies
     * 
     * @return a 2d array of recruit data and corresponding study info
     */
    public String[][] selectAllActiveStudies(){
        
        String sql = """
                     SELECT o.panelist_num, r.id AS 'recruit id', r.fullname, s.id AS 'study id', s.psn
                     FROM pii.ongoingstudies o
                     JOIN pii.recruits r ON r.id = o.recruit_id
                     JOIN pii.studies s ON s.id = o.study_id
                     ORDER BY o.study_id, r.lname
                     """;
        int count = 0;
        boolean recordsExist = false;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }                    
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    /**
     * Queries recruits table for records that
     * match the given fields
     * 
     * @param upper 
     * @param lower 
     * @param sex
     * @param fst
     * @param ap
     * @param st
     * @param se
     * @param ss
     * @param contacts
     * @param num_studies
     * @return 
     *      a list of queried matches
     *  
     */
    public String[][] filterRecruits(List<String> selectedTypes, String upper, String lower, String sex, String fst, String ap, String st, String se, String ss, String contacts, String num_panels, String num_studies, String la_qualified){
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        String sql = """
                     SELECT *
                     FROM pii.recruits r
                     """;
        
        boolean filteringStudyTypes = selectedTypes.size() > 0;
        boolean filteringAge = !upper.equals("") && !lower.equals("");
        boolean filteringSex = !sex.equals("");
        boolean filteringFST = !fst.equals("");
        boolean filteringAP = !ap.equals("");
        boolean filteringST = !st.equals("");
        boolean filteringSE = !se.equals("");
        boolean filteringSS = !ss.equals("");
        boolean filteringContacts = !contacts.equals("");
        boolean filteringNumPanels = !num_panels.equals("");
        boolean filteringNumStudies = !num_studies.equals("");
        boolean filteringLaQualified = !la_qualified.equals("");
        
        boolean firstCondition = true;
        
        if (filteringStudyTypes){
            sql += """
                   JOIN recruitStudyTypes rst ON r.id = rst.recruit_id
                   JOIN studyTypes st ON st.id = rst.type_id
                   WHERE rst.type_id IN (SELECT id
                                         FROM pii.studyTypes
                                         WHERE `type` IN (
                   """;
            
            String typesListed = "";
            for (String type:selectedTypes){
                typesListed += "'" + type + "'" + ",";
            }
            // trim last comma
            sql += typesListed.substring(0, typesListed.length()-1) + "))";
            firstCondition = false;
        }
        
        
        
        if (filteringAge){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += """
                    dob BETWEEN
                     	DATE_SUB(NOW(), INTERVAL ? YEAR) AND
                        DATE_SUB(NOW(), INTERVAL ? YEAR)
                   """;
        }       
        if (filteringSex){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " sex = ?";
        }
        if (filteringFST){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " FST = ?";
        }
        if (filteringAP){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " AP = ?";
        }
        if (filteringST){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " ST = ?";
        }
        if (filteringSE){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " SE = ?";
        }
        if (filteringSS){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " SS = ?";
        }
        if (filteringContacts){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql +=  " contact_lens = ?";
        }
        if (filteringNumPanels){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            if (num_panels.equals("None")){
                sql += " num_panels = 0";
            }else{
                sql += " num_panels > 0";
            }
        }
        if (filteringNumStudies){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            if (num_studies.equals("None")){
                sql += " num_studies = 0";
            }else{
                sql += " num_studies > 0";
            }
        }
        if (filteringLaQualified){
            if (firstCondition){
                sql += " WHERE";
                firstCondition = false;
            }else{
                sql += " AND";
            }
            sql += " la_qualified = ?";
        }
        
        
        
        sql += " ORDER BY lname";
        
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            int param_num = 1;
            if (filteringAge){
                pst.setString(param_num, upper);
                pst.setString(param_num+1, lower);
                param_num += 2;
            }
            if (filteringSex){
                pst.setString(param_num, sex);
                param_num++;
            }
            if (filteringFST){
                pst.setString(param_num, fst);
                param_num++;
            }
            if (filteringAP){
                pst.setString(param_num, ap);
                param_num++;
            }
            if (filteringST){
                pst.setString(param_num, st);
                param_num++;
            }
            if (filteringSE){
                pst.setString(param_num, se);
                param_num++;
            }
            if (filteringSS){
                pst.setString(param_num, ss);
                param_num++;
            }
            if (filteringContacts){
                pst.setString(param_num, contacts);
                param_num++;
            }
            if (filteringLaQualified){
                pst.setString(param_num, la_qualified);
                param_num++;
            }
            
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            //add column names to first index of list
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            datalist.add(colnames);
            while (rs.next()){
                recordsExist = true;
                String[] arr = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    arr[i] = rs.getString(i+1);
                }
                datalist.add(arr);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < data.length ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    
    /**
     * Gets all of the products that were used in the specified study
     * 
     * @param id
     *      study id to find matching products for
     * @param psn
     *      study panel number if id is left empty
     * @return 
     *      queried matching results
     */
    public String[][] selectProductsinStudy(String id, String psn){
        boolean recordsExist = false;
        ArrayList<String[]> data = new ArrayList<>();
        String sql = """
                     SELECT p.id, p.pi, p.`description`, p.test_mode, p.site_location
                     FROM pii.products p
                     JOIN pii.studies s ON s.id = p.study_id
                     """;
        
        boolean usingId = true;
        if (!id.equals("")){
            sql = sql.concat("WHERE s.id = ?");
        }else{
            sql = sql.concat("WHERE s.psn = ?");
            usingId = false;
        }
        
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            if (usingId){
                pst.setString(1, id);
            }else{
                pst.setString(1, psn);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            
            data.add(colnames);
            
            while (rs.next()){
                recordsExist = true;
                String[] arr = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    arr[i] = rs.getString(i+1);
                }
                data.add(arr);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < data.size() ; i++){
                ret[i] = data.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets all of the recruits that participated in the specified study
     * 
     * @param id
     *      study id to find matching visits for
     * @param psn
     *      study panel number if id is left empty
     * @return 
     *      queried matching results
     */
    public String[][] selectRecruitsByStudy(String id, String psn){
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        String sql = """
                     SELECT r.id, r.fullname, r.phone, r.dob, v.id, v.panelist_num, v.total_pay, v.paid_date, v.checknum
                     FROM pii.visits v
                     JOIN pii.studies s ON v.study_id = s.id
                     JOIN pii.recruits r ON v.recruit_id = r.id
                     """;
        
        boolean usingId = true;
        if (!id.equals("")){
            sql = sql.concat("WHERE s.id = ?");
        }else{
            sql = sql.concat("WHERE s.psn = ?");
            usingId = false;
        }
        
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            if (usingId){
                pst.setString(1, id);
            }else{
                pst.setString(1, psn);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            
            datalist.add(colnames);
            
            while (rs.next()){
                recordsExist = true;
                String[] arr = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    arr[i] = rs.getString(i+1);
                }
                datalist.add(arr);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < data.length ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    /**
     * Gets all of the products that have been tested by the specified recruit
     * 
     * @param recruitid
     * @param name
     * @return a 2d array of product information
     */
    public String[][] selectProductsByRecruit(String recruitid, String name){
        String sql = """
                     SELECT p.id, p.pi, p.`description`, p.test_mode, p.site_location, s.id, s.psn
                     FROM pii.visits v
                     JOIN pii.studies s ON s.id = v.study_id
                     JOIN pii.products p ON s.id = p.study_id
                     JOIN recruits r ON r.id = v.recruit_id
                     """;
        ArrayList<String[]> data = new ArrayList<>();
        boolean recordsExist = false;
        boolean usingId = true;
        if (!recruitid.equals("")){
            sql = sql.concat("WHERE r.id = ?");
        }else{
            sql = sql.concat("WHERE r.fullname = ?");
            usingId = false;
        }
        
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            if (usingId){
                pst.setString(1, recruitid);
            }else{
                pst.setString(1, name);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            data.add(colnames);
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                data.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets all of the studies that a specified recruit has participated in
     * 
     * @param id
     *      recruit id to find matching visits for
     * @param name
     *      recruit's name to find matches for if id is empty
     * @return 
     */
    public String[][] selectStudiesByRecruit(String id, String name){
        boolean recordsExist = false;
        ArrayList<String[]> data = new ArrayList<>();
        boolean usingId;
        String sql = """
                     SELECT s.id, s.psn, s.`type`, s.start_date, s.end_date, s.closed_date, v.id, v.panelist_num, v.total_pay, v.paid_date, v.checknum
                     FROM visits v
                     JOIN recruits r ON r.id = v.recruit_id
                     JOIN studies s on s.id = v.study_id
                     """;
        if (!id.equals("")){
            sql = sql.concat("WHERE r.id = ?");
            usingId = true;
        }else{
            sql = sql.concat("WHERE r.fullname = ?");
            usingId = false;
        }
        
      
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            if (usingId){
                pst.setString(1, id);
            }else{
                pst.setString(1, name);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            
            data.add(colnames);
            
            while (rs.next()){
                recordsExist = true;
                String[] arr = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    arr[i] = rs.getString(i+1);
                }
                data.add(arr);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        return ret;
    }
    
    
    /**
     * Retrieves an entire table for general viewing
     * 
     * @param table
     *      the table to select all field from
     * @return 
     *      a list of all queried matches
     */
    public String[][] selectAll(String table){
        ArrayList<String[]> data = new ArrayList<>();
        boolean recordsExist = false;
        String sql = "SELECT * FROM pii." + table;
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
                        
            //add column names to first index of list
            String[] colnames = new String[numcols];
            for (int i = 0 ; i < numcols ; i++){
                colnames[i] = rsmd.getColumnName(i+1);
            }
            data.add(colnames);
            while (rs.next()){
                recordsExist = true;
                String[] arr = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    arr[i] = rs.getString(i+1);
                }
                data.add(arr);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Retrieves records from the general log from the last x days
     * @param days
     * @return 
     */
    public String[][] getAuditLog(String date1, String date2){
        String sql = """
                     SELECT *
                     FROM logs.audit_logs
                     WHERE `timestamp` BETWEEN ? AND ?
                     ORDER BY `timestamp` DESC
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, date1);
            pst.setString(2, date2);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets most fields for the specified recruit, replacing null values with an empty string
     * @param id A recruit id
     * @return 
     */
    public String[] getRecruitInfoForUpdate(String id){
        String sql = """
                     SELECT 
                     IFNULL(fullname, ''),
                     IFNULL(fname, ''),
                     IFNULL(lname, ''),
                     IFNULL(phone,''),
                     IFNULL(race,''),
                     IFNULL(sex,''),
                     IFNULL(dob,''),
                     IFNULL(address,''),
                     IFNULL(city,''),
                     IFNULL(state,''),
                     IFNULL(zip,''),
                     IFNULL(FST,''),
                     IFNULL(ST,''),
                     IFNULL(AP,''),
                     IFNULL(SE,''),
                     IFNULL(SS,''),
                     IFNULL(contact_lens,''),
                     IFNULL(la_qualified,''),
                     IFNULL(la_screen_date,''),
                     IFNULL(notes,'')
                     FROM pii.recruits
                     WHERE id = ?
                     """;
        String[] ret = {};
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            ret = new String[numcols];
            if (rs.next()){
                for (int i = 0 ; i < numcols ; i++){
                    ret[i] = rs.getString(i+1);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    public String[] getStudyTypesForRecruit(String recruitid){
        String sql = """
                     SELECT st.`type`
                     FROM pii.studyTypes st
                     JOIN pii.recruitStudyTypes rst ON rst.type_id = st.id
                     JOIN recruits r ON r.id = rst.recruit_id
                     WHERE r.id = ?
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
             PreparedStatement pst = conn.prepareStatement(sql);
             pst.setString(1, recruitid);
             ResultSet rs = pst.executeQuery();
             while (rs.next()){
                 datalist.add(rs.getString(1));
             }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()];
        if (ret.length > 0){
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets most field for the specified study, replacing null values with an empty string
     * @param id
     * @return 
     */
    public String[] getStudyInfoForDisplay(String id){
        String sql = """
                     SELECT 
                     IFNULL(psn, ''),
                     IFNULL(site, ''),
                     IFNULL(`type`, ''),
                     IFNULL(company, ''),
                     IFNULL(contact, ''),
                     IFNULL(start_date, ''),
                     IFNULL(end_date, ''),
                     IFNULL(closed_date, ''),
                     IFNULL(project_manager, '')
                     FROM pii.studies
                     WHERE id = ?
                     """;
        String[] ret = new String[9];
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                for (int i = 0 ; i < ret.length ; i++){
                    ret[i] = rs.getString(i+1);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Gets most field for the specified study, replacing null values with an empty string
     * @param id
     * @return 
     */
    public String[] getStudyInfoForUpdate(String id){
        String sql = """
                     SELECT 
                     IFNULL(psn, ''),
                     IFNULL(site, ''),
                     IFNULL(`type`, ''),
                     IFNULL(company, ''),
                     IFNULL(contact, ''),
                     IFNULL(start_date, ''),
                     IFNULL(end_date, ''),
                     IFNULL(project_manager, '')
                     FROM pii.studies
                     WHERE id = ?
                     """;
        String[] ret = new String[8];
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                for (int i = 0 ; i < ret.length ; i++){
                    ret[i] = rs.getString(i+1);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Gets most fields for the specified product, replacing null values with an empty string
     * @param id
     * @return 
     */
    public String[] getProductInfoForUpdate(String id){
        String sql = """
                     SELECT 
                     IFNULL(study_id, ''),
                     IFNULL(pi, ''),
                     IFNULL(`description`, ''),
                     IFNULL(tested_as, ''),
                     IFNULL(patch_type, ''),
                     IFNULL(application_frequency, ''),
                     IFNULL(site_location, ''),
                     IFNULL(notes, '')
                     FROM pii.products
                     WHERE id = ?
                     """;
        String[] ret = new String[8];
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                for (int i = 0 ; i < ret.length ; i++){
                    ret[i] = rs.getString(i+1);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Get the ids and names of all recruits with first element empty string
     * @return 
     */
    public String[] getRecruitIdName(){
        String sql = """
                     SELECT id, fullname
                     FROM pii.recruits
                     ORDER BY lname
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] data = new String[datalist.size()+1];
        data[0] = "";
        for (int i = 0 ; i < data.length-1 ; i++){
            data[i+1] = datalist.get(i);
        }
        return data;
    }
    
    /**
     * Gets the id and name of all recruits that aren't already in the specified panel
     * @return 
     */
    public String[] getRecruitIdNameForPanelSelection(String studyid){
        String sql = """
                     SELECT id, fullname
                     FROM pii.recruits
                     WHERE id NOT IN (
                        SELECT recruit_id
                        FROM pii.recruitpanels
                        WHERE study_id = ?
                     )
                     ORDER BY lname
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] data = new String[datalist.size()+1];
        data[0] = "";
        for (int i = 0 ; i < data.length-1 ; i++){
            data[i+1] = datalist.get(i);
        }
        return data;
    }
    
    /**
     * Returns the name for the recruit with the corresponding id
     * @param id
     * @return 
     */
    public String getRecruitNameFromId(String id){
        String sql = """
                     SELECT fullname
                     FROM pii.recruits
                     WHERE id = ?
                     """;
        String name = "";
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                name = rs.getString(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, name);
        }
        return name;
    }
    
    /**
     * Get the study id and PSN of all studies with first element empty string
     * @return 
     */
    public String[] getStudyIdPsn(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", PSN " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()+1];
        for (int i = 0 ; i < ret.length-1 ; i++){
            ret[i+1] = datalist.get(i);
        }
        return ret;
    }
    
    /**
     * Get the id and pi of all products with first element empty string
     * @return 
     */
    public String[] getProductIdPi(){
        String sql = """
                     SELECT id, pi
                     FROM pii.products
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", PI " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] data = new String[datalist.size()+1];
        data[0] = "";
        for (int i = 0 ; i < data.length-1 ; i++){
            data[i+1] = datalist.get(i);
        }
        return data;
    }
    
    
    public String[] getStudyIdPsnNotPaid(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE paid = 0
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", PSN: " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()+1];
        ret[0] = "";
        for (int i = 0 ; i < ret.length-1 ; i++){
            ret[i+1] = datalist.get(i);
        }
        return ret;
        
    }
    
    /**
     * Get visit info needed for submitting payments, returns null if no records found
     * @return 2d array of visit info
     */
    public String[][] getPaymentInfo(String studyid){
        String sql = """
                     SELECT v.id AS visit_id, r.id AS recruit_id, r.fname, r.lname, r.phone, r.address, r.city, r.state, r.zip, s.id AS study_id, s.psn, s.`type`, s.start_date, s.end_date, s.closed_date, v.panelist_num, v.total_pay, v.paid_date, v.checknum
                     FROM pii.visits v
                     JOIN pii.recruits r ON r.id = v.recruit_id
                     JOIN pii.studies s ON s.id = v.study_id
                     WHERE s.paid = 0 
                     """;
        boolean usingStudyid = false;
        if (!studyid.equals("")){
            usingStudyid = true;
            sql += "AND v.study_id = ?";
        }
        sql += " ORDER BY s.psn, total_pay";
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            if (usingStudyid){
                pst.setString(1, studyid);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        return data;
    }
    
    /**
     * Gets all available information on recruit panel studies
     * @return a 2d array of study information
     */
    public String[][] getPanelInfo(){
        String sql = """
                     SELECT DISTINCT s.id, s.psn, s.site, s.`type`, s.company, s.contact, s.start_date, s.end_date, s.is_recruiting, s.is_active, s.closed_date
                     FROM pii.studies s
                     JOIN pii.recruitpanels ON recruitpanels.study_id = s.id
                     ORDER BY s.id
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    /**
     * Gets all available info for active studies
     * @return 
     */
    public String[][] getActiveStudyInfo(){
        String sql = """
                     SELECT DISTINCT s.id, s.psn, s.site, s.`type`, s.company, s.contact, s.start_date, s.end_date, s.is_recruiting, s.is_active, s.closed_date, s.created_by, s.closed_by, s.project_manager, s.paid
                     FROM pii.studies s
                     JOIN pii.ongoingstudies o ON o.study_id = s.id
                     ORDER BY s.id
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        
        return data;
    }
    
    /**
     * Gets the names of recruits that have previously completed a study
     * 
     * @return an array of recruit names
     */
    public String[] getVisitNames(){
        String sql = """
                     SELECT DISTINCT r.fname, r.lname
                     FROM pii.recruits r
                     JOIN pii.visits v ON v.recruit_id = r.id
                     ORDER BY r.lname
                     """;
        boolean recordsExist = false;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                datalist.add(rs.getString(1) + " " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String[] ret = null;
        if (recordsExist){
            ret = new String[datalist.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        
        return ret;
    }
    
    /**
     * Gets all recruit information for those that are currently in the specified active study
     * @param studyid
     * @return 
     */
    public String[][] getRecruitsInActiveStudy(String studyid){
        String sql = """
                     SELECT *
                     FROM pii.recruits r
                     JOIN ongoingstudies o ON o.recruit_id = r.id
                     WHERE o.study_id = ?
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        return data;
    }
    
    /**
     * Gets all recruit information for those that are currently in the specified recruiting panel
     * 
     * @param studyid
     * @return a 2d array of recruit information
     */
    public String[][] getRecruitsInPanel(String studyid){
        String sql = """
                     SELECT *
                     FROM pii.recruits r
                     JOIN recruitpanels rp ON rp.recruit_id = r.id
                     WHERE rp.study_id = ?
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] data = null;
        if (recordsExist){
            data = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < datalist.size() ; i++){
                data[i] = datalist.get(i);
            }
        }
        return data;
    }
    
    /**
     * Returns the other panels that the recruit is already in
     * 
     * This is only called if we know the recruit is in at least one other panel
     * @param recruitid
     * @return the study id, PSN, and type of the other panels
     */
    public ArrayList<String[]> getRecruitPanels(String recruitid){
        String sql = """
                     SELECT DISTINCT s.id, s.psn, s.`type`
                     FROM pii.studies s
                     JOIN pii.recruitpanels rp ON rp.study_id = s.id
                     WHERE rp.recruit_id = ?
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            ResultSet rs = pst.executeQuery();
            int numcols = rs.getMetaData().getColumnCount();
            while (rs.next()){
                String[] row = new String[numcols];
                for(int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return datalist;
    }
    
    /**
     * Get information for studies that the specified recruit is currently in
     * 
     * @param recruitid
     * @return an ArrayList of study information
     */
    public ArrayList<String[]> getRecruitActiveStudies(String recruitid){
        String sql = """
                     SELECT DISTINCT s.id, s.psn, s.`type`
                     FROM pii.studies s
                     JOIN pii.ongoingstudies o ON o.study_id = s.id
                     WHERE o.recruit_id = ?
                     """;
        ArrayList<String[]> data = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                data.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return data;
    }
    
    /**
     * Gets the study Ids of all active studies
     * 
     * @return an array of study Ids
     */
    public String[] getActiveStudyids(){
        String sql = """
                     SELECT id
                     FROM pii.studies
                     WHERE is_active = 1
                     """;
        ArrayList<String> data = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                data.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = null;
        if (recordsExist){
            ret = new String[data.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        
        return ret;
    }
    
    /**
     * 
     * @param table
     *      db table to query
     * @return 
     *      a list of column names for the given table
     */
    public String[] getAllColNames(String table){
        String sql = "SELECT * FROM pii." + table;
        String[] ret = null;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            ret = new String[rsmd.getColumnCount()];
            for (int i = 0 ; i < rsmd.getColumnCount() ; i++){
                ret[i] = rsmd.getColumnName(i+1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Gets ID and PSN of all studies that have an active status
     * @return 
     */
    public String[][] getActiveStudyIdsPSNs(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE is_active = 1
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
           while (rs.next()){
               String[] row = new String[2];
               row[0] = rs.getString(1);
               row[1] = rs.getString(2);
               datalist.add(row);
           }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()][2];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Returns an array of study type names
     * @return 
     */
    public String[] getStudyTypes(){
        String sql = """
                     SELECT `type`
                     FROM pii.studyTypes
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()];
        for (int i = 0 ; i < ret.length ; i++){
            ret[i] = datalist.get(i);
        }
        return ret;
    }
    
    /**
     * Gets the matching PSN number for the given study ID
     * @param studyid
     * @return 
     */
    public String getPsn(String studyid){
        String sql = """
                     SELECT psn
                     FROM pii.studies
                     WHERE id = ?
                     """;
        String psn = "";
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                psn = rs.getString(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return psn;
    }
    
    public String[] getPanelRecruitIds(String studyid){
        String sql = """
                     SELECT recruit_id
                     FROM pii.recruitpanels
                     WHERE study_id = ?
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        } 
        String[] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
           
    
    public String getMostRecentRecruitID(){
        String sql = """
                     SELECT id
                     FROM recruits
                     ORDER BY id DESC
                     LIMIT 1
                     """;
        String ret = "";
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                ret = rs.getString(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    public String[][] getRecruitingStudyIdsPSNs(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE is_recruiting = 1
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
           while (rs.next()){
               String[] row = new String[2];
               row[0] = rs.getString(1);
               row[1] = rs.getString(2);
               datalist.add(row);
           }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()][2];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets the study ids of all recruiting panels
     * @return 
     */
    public String[][] getPanelStudyIdsPSNs(){
        String sql = """
                     SELECT DISTINCT s.id, s.psn
                     FROM studies s
                     INNER JOIN recruitpanels rp on rp.study_id = s.id
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
           while (rs.next()){
               String[] row = new String[2];
               row[0] = rs.getString(1);
               row[1] = rs.getString(2);
               datalist.add(row);
           }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()][2];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets the number of panels that the specified recruit is currently part of
     * @param recruitid
     * @return 
     */
    public int getNumPanels(String recruitid){
        String sql = """
                     SELECT num_panels
                     FROM pii.recruits
                     WHERE id = ?
                     """;
        int num_panels = 0;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                num_panels = rs.getInt(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return num_panels;
    }
    
    /**
     * Gets the number of studies that the specified recruit is currently participating in
     * 
     * @param recruitid
     * @return the number of studies
     */
    public int getNumStudies(String recruitid){
        String sql = """
                     SELECT num_studies
                     FROM pii.recruits
                     WHERE id = ?
                     """;
        int ret = -1;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                ret = rs.getInt(1);
            }
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Gets the study id and PSN of all active studies
     * 
     * @return an array of 2 element objects containing the id and PSN
     */
    public String[][] getActiveStudyIdPsn(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE is_active = 1
                     ORDER BY id
                     """;
        ArrayList<String[]> data = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[2];
                row[0] = rs.getString(1); row[1] = rs.getString(2);
                data.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String ret[][] = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        
        return ret;
    }
    
    /**
     * Gets the recruits ids and names of everyone in the specified panel
     * @param studyid
     * @return 
     */
    public String[] RecruitIds(String studyid){
        String sql = """
                     SELECT r.id
                     FROM recruits r
                     INNER JOIN recruitpanels rp ON rp.recruit_id = r.id
                     WHERE rp.study_id = ?
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Gets the name and id of all recruits
     * 
     * @return an array of 2 element objects containing the name and recruit id
     */
    public String[][] getRecruitNameId(){
        String sql = """
                     SELECT id, fullname
                     FROM pii.recruits
                     ORDER BY id
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[2];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[datalist.size()][datalist.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    public String getMySQLUser(){
        String sql = "SELECT USER()";
        String user = "";
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                user = rs.getString(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return user;
    }
    
    /**
     * Get the recruits in the recruit panel specified by study id
     * Adds a blank column in column 1 for panelist number input
     * @param studyid
     * @return 
     */
    public String[][] getRecruitsFromPanel(String studyid){
        String sql = """
                     SELECT r.fullname, r.id, IFNULL(r.phone, '')
                     FROM recruitpanels rp
                     JOIN recruits r ON r.id = rp.recruit_id
                     WHERE rp.study_id = ?
                     ORDER BY r.lname
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            int numcols = rs.getMetaData().getColumnCount();
            while (rs.next()){
                String[] row = new String[numcols+1];
                row[0] = "";
                for (int i = 0 ; i < numcols ; i++){
                    row[i+1] = rs.getString(i+1);
                }
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String[][] ret = new String[datalist.size()][datalist.get(0).length];
        for (int i = 0 ; i < ret.length ; i++){
            ret[i] = datalist.get(i);
        }
        return ret;
    }
    
    /**
     * Gets all PSNs that have at least one associated product
     * 
     * @return an array of PSNs
     */
    public String[] getProductPsns(){
        String sql = """
                     SELECT DISTINCT s.psn
                     FROM pii.studies s
                     JOIN pii.products p ON p.study_id = s.id
                     ORDER BY s.psn
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = null;
        if (recordsExist){
            ret = new String[datalist.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        
        return ret;
    }
    
    public String[] getStudyIdUnpaid(){
        String sql = """
                     SELECT id
                     FROM pii.studies
                     WHERE paid = 0
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()];
        for (int i = 0 ; i < ret.length ; i++){
            ret[i] = datalist.get(i);
        }
        return ret;
    }
    
    public int[] getStudyTypeIdsForRecruit(String recruitid){
        String sql = """
                     SELECT st.id
                     FROM pii.studyTypes st
                     JOIN pii.recruitStudyTypes rst ON rst.type_id = st.id
                     JOIN recruits r ON r.id = rst.recruit_id
                     WHERE r.id = ?
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        int[] ret = new int[datalist.size()];
        if (ret.length > 0){
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = Integer.parseInt(datalist.get(i));
            }
        }
        return ret;
    }
    
    public String[] getStudyIdPsnUnpaid(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE paid = 0
                     AND closed_date IS NOT NULL
                     ORDER BY psn
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", PSN: " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()];
        for (int i = 0 ; i < ret.length ; i++){
            ret[i] = datalist.get(i);
        }
        return ret;
    }
    
    public int getCountUnpaidStudies(){
        String sql = """
                     SELECT COUNT(*)
                     FROM pii.studies
                     WHERE paid = 0
                     AND closed_date IS NOT NULL
                     """;
        int count = 0;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                count = rs.getInt(1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return count;
    }
    
    /**
     * Gets all PSNs for which there are associated visit data
     * 
     * @return an array of PSNs
     */
    public String[] getVisitPsns(){
        String sql = """
                     SELECT DISTINCT s.psn
                     FROM pii.studies s
                     JOIN pii.visits v ON v.study_id = s.id
                     ORDER BY s.psn
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                recordsExist = true;
                datalist.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = null;
        if (recordsExist){
            ret = new String[datalist.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        
        return ret;
    }
    
    /**
     * Gets PSN and study type for the specified study id
     * 
     * @param id
     * @return an array of study information
     */
    public String[] getStudyInfoFromId(String id){
        String sql = """
                     SELECT id, psn, `type`
                     FROM pii.studies
                     WHERE id = ?
                     """;
        String[] ret = null;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                ResultSetMetaData rsmd = rs.getMetaData();
                int numcols = rsmd.getColumnCount();
                ret = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    ret[i] = rs.getString(i+1);
                }
            }
            
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
    }
    
    /**
     * Gets the distinct values from a specified table column
     * 
     * @param table
     *      db table to query
     * @param col
     *      the column in that table to select
     * @param condition
     *      optional condition to query
     * @return 
     *      an array of column data
     */
    public String[] selectDistinctField(String table, String col){
        ArrayList<String> data = new ArrayList<>();
        boolean recordsExist = false;
        String sql = "SELECT DISTINCT " + col + " FROM pii." + table;
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()){
                recordsExist = true;
                data.add(rs.getString(1));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret;
        if (recordsExist){
            ret = new String[data.size()];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }else{
            ret = new String[1];
            ret[0] = "";
        }
        return ret;
    }
    
    /**
     * Gets all of the recruits in the specified active study
     * 
     * @param studyid
     * @return an array of two element objects containing recruit id and name
     */
    public String[][] getActiveStudyRecruits(String studyid){
        String sql = """
                     SELECT r.id, r.fullname, o.panelist_num
                     FROM pii.recruits r
                     JOIN pii.ongoingstudies o ON o.recruit_id = r.id
                     WHERE o.study_id = ?
                     """;
        ArrayList<String[]> data = new ArrayList<>();
        boolean recordsExist = false;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();
            while (rs.next()){
                recordsExist = true;
                String[] row = new String[numcols];
                for (int i = 0 ; i < numcols ; i++){
                    row[i] = rs.getString(i+1);
                }
                data.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (recordsExist){
            ret = new String[data.size()][data.get(0).length];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = data.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Get the study id and PSN of all study ids that are open for recruiting
     * @return 
     */
    public String[][] getIsRecruitingStudyIdsPsns(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE is_recruiting = 1
                     """;
        ArrayList<String[]> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                String[] row = new String[2];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                datalist.add(row);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[][] ret = null;
        if (datalist.size() > 0){
            ret = new String[datalist.size()][2];
            for (int i = 0 ; i < ret.length ; i++){
                ret[i] = datalist.get(i);
            }
        }
        return ret;
    }
    
    /**
     * Get all study ids and PSNs from the studies table that aren't currently recruiting
     * @return an array of the concatenated ids and PSNs
     */
    public String[] getNonRecruitingStudyIdPsn(){
        String sql = """
                     SELECT id, psn
                     FROM pii.studies
                     WHERE is_recruiting = 0
                     AND paid = 0
                     AND is_active = 0
                     """;
        ArrayList<String> datalist = new ArrayList<>();
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                datalist.add(rs.getString(1) + ", PSN: " + rs.getString(2));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        String[] ret = new String[datalist.size()];
        for (int i = 0 ; i < ret.length ; i++){
            ret[i] = datalist.get(i);
        }
        return ret;
    }
    
    public void insertRecruitStudyType(String recruitid, String type){
        String sql = """
                     INSERT INTO pii.recruitStudyTypes(recruit_id, type_id)
                     VALUES (?, (SELECT id FROM pii.studyTypes WHERE `type` = ?))
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            pst.setString(2, type);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Adds a new record into the recruits table
     * @param insertVals 
     */
    public void insertRecruits(String[] insertVals){
        
        String sql = """
                     INSERT INTO pii.recruits (fullname, fname, lname, phone, race, sex, address, city, state, zip, FST, ST, AP, SE, SS, contact_lens, dob, notes, la_qualified, la_screen_date)
                     VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, insertVals[0] + " " + insertVals[1]);
            for (int i = 0 ; i < insertVals.length - 4 ; i++){
                if (insertVals[i].equals("")){
                    pst.setNull(i+2, Types.NULL);
                }else{
                    pst.setString(i+2, insertVals[i]);
                }
            }
            // set date last to preserve type
            if (insertVals[15].equals("")){
                pst.setObject(17, null);
            }else{
                pst.setDate(17, java.sql.Date.valueOf(insertVals[15]));
            }
            if (insertVals[16].equals("")){
                pst.setObject(18, null);
            }else{
                pst.setString(18, insertVals[16]);
            }
            if (insertVals[17].equals("")){
                pst.setObject(19, null);
            }else{
                pst.setString(19, insertVals[17]);
            }
            if (insertVals[18].equals("")){
                pst.setObject(20, null);
            }else{
                pst.setDate(20, java.sql.Date.valueOf(insertVals[18]));
            }
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Adds a new record into the ongoingstudies table
     * @param studyid
     * @param recruitid 
     */
    public void insertPanelRecruit(String studyid, String recruitid){
        String sql = """
                     INSERT INTO pii.recruitpanels (study_id, recruit_id)
                     VALUES (?,?)
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            pst.setString(2, recruitid);
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Recruit Added");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    /**
     * Adds a new record into the visits table
     * @param recruit_id
     * @param study_id
     * @param completed_study
     * @param total_pay
     * @param paid_date 
     */
    public void insertVisits(String recruit_id, String study_id, double total_pay, String paid_date, String panelist_num){
        String sql = """
                     INSERT INTO pii.visits (recruit_id, study_id, total_pay, paid_date, panelist_num)
                     VALUES (?,?,?,?,?)
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruit_id);
            pst.setString(2, study_id);
            pst.setDouble(3, total_pay);
            pst.setDate(4, java.sql.Date.valueOf(paid_date));
            pst.setString(5, panelist_num);
            
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    
    /**
     * Adds a new record to the studies table
     * @param insertVals
     * @param start_date
     * @param end_date
     * @param active 
     */
    public void insertStudies(String[] insertVals, String start_date, String end_date, String user, String project_manager){
        String sql = """
                     INSERT INTO pii.studies (psn, site, `type`, company, contact, start_date, end_date, created_by, project_manager)
                     VALUES (?,?,?,?,?,?,?,?,?)
                     """;
        try(Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 0 ; i < insertVals.length ; i++){
                if (insertVals[i].equals("")){
                    pst.setNull(i+1, Types.NULL);
                }else{
                    pst.setString(i+1, insertVals[i]);
                }
            }
            if (!start_date.equals("")){
                pst.setDate(6, Date.valueOf(start_date));
            }else{
                pst.setNull(6, Types.NULL);
            }
            if (!end_date.equals("")){
                pst.setDate(7, Date.valueOf(end_date));
            }else{
                pst.setNull(7, Types.NULL);
            }
            if (!user.equals("")){
                pst.setString(8, user);
            }else{
                pst.setNull(8, Types.NULL);
            }
            if (!project_manager.equals("")){
                pst.setString(9, project_manager);
            }else{
                pst.setNull(9, Types.NULL);
            }
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Adds a new record to the products table
     * @param studyid
     * @param pi
     * @param description
     * @param test_mode
     * @param site_location 
     */
    public void insertProducts(String[] toInsert){
        String sql = """
                     INSERT INTO pii.products(study_id, pi, `description`, site_location, tested_as, patch_type, application_frequency, notes)
                     VALUES (?,?,?,?,?,?,?,?)
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            
            for (int i = 0 ; i < toInsert.length ; i++){
                if (toInsert[i].equals("")){
                    pst.setNull(i+1, Types.NULL);
                }else{
                    pst.setString(i+1, toInsert[i]);
                }
            }
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Add a recruit to a recruit panel
     * @param studyid
     * @param recruitid 
     */
    public void insertRecruitPanel(String studyid, String recruitid){
        String sql = """
                     INSERT INTO pii.recruitpanel (studyid, recruitid)
                     VALUES (?,?)
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            pst.setString(2, recruitid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        JOptionPane.showMessageDialog(null, "Recruit " + recruitid + " has been added to study " + studyid);
    }
    
    /**
     * Inserts all records from data table into OngoingStudies table
     * @param data 
     */
    public void insertOngoingStudies(String[][] data, String studyid){
        // data[i][0] -> panelist_num
        // data[i][2] -> recruit_id
        String sql = "INSERT INTO pii.ongoingstudies (study_id, recruit_id, panelist_num) VALUES";
        for (int i = 0 ; i < data.length ; i++){
            sql += " (?,?,?),";
        }
        // remove last comma
        sql = sql.substring(0, sql.length()-1);
        
        int idx = 1; // sql starts indexing at 1
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            for (String[] row:data){
                pst.setString(idx, studyid);
                pst.setString(idx+1, row[2]);
                pst.setString(idx+2, row[0]);
                idx += 3;
            }
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " subjects ready for study " + studyid);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void updateSinglePaidStatus(String studyid){
        String sql = """
                     UPDATE pii.studies
                     SET paid = 1
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void updateAllPaidStatus(){
        String sql = """
                     UPDATE pii.studies
                     SET paid = 1
                     WHERE id = (
                        SELECT id
                        FROM pii.studies
                        WHERE paid = 0
                     )
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Changes the specified study from recruiting to active
     * @param studyid 
     */
    public void updateRecruitingToActive(String studyid){
        String sql = """
                      UPDATE studies
                      SET is_recruiting = 0, is_active = 1
                      WHERE id = ?
                      """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Updates all fields in the products table
     * @param productid
     * @param info 
     */
    public void updateProducts(String productid, String[] info){
        String sql = """
                     UPDATE pii.products
                     SET
                     study_id = ?,
                     pi = ?,
                     `description` = ?,
                     tested_as = ?,
                     patch_type = ?,
                     application_frequency = ?,
                     site_location = ?,
                     notes = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 0 ; i < info.length ; i++){
                if (info[i].equals("")){
                    pst.setNull(i+1, Types.NULL);
                }else{
                    pst.setString(i+1, info[i]);
                }
                
            }
            pst.setString(9, productid);
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Updates all fields in the studies table
     * @param studyid
     * @param info 
     */
    public void updateStudies(String studyid, String[] info){
        String sql = """
                     UPDATE pii.studies
                     SET
                     psn = ?,
                     site = ?,
                     `type` = ?,
                     company = ?,
                     contact = ?,
                     start_date = ?,
                     end_date = ?,
                     project_manager = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 0 ; i < info.length - 3 ; i++){
                if (info[i].equals("")){
                    pst.setNull(i+1, Types.NULL);
                }else{
                    pst.setString(i+1, info[i]);
                }
            }
            
            if (info[5].equals("")){
                pst.setNull(6, Types.NULL);
            }else{
                pst.setString(6, info[5]);
            }
            
            if (info[6].equals("")){
                pst.setNull(7, Types.NULL);
            }else{
                pst.setString(7, info[6]);
            }
            if (info[7].equals("")){
                pst.setNull(8, Types.NULL);
            }else{
                pst.setString(8, info[7]);
            }
            
            pst.setString(9, studyid);
            
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    /**
     * Updates all fields in the recruits table
     * @param recruitid
     * @param info 
     */
    public void updateRecruits(String recruitid, String[] info){
        String sql = """
                     UPDATE pii.recruits
                     SET 
                     fullname = ?,
                     fname = ?,
                     lname = ?,
                     phone = ?,
                     race = ?,
                     sex = ?,
                     dob = ?,
                     address = ?,
                     city = ?,
                     state = ?,
                     zip = ?,
                     FST = ?,
                     ST = ?,
                     AP = ?,
                     SE = ?,
                     SS = ?,
                     contact_lens = ?,
                     la_qualified = ?,
                     la_screen_date = ?,
                     notes = ?
                     WHERE id = ?
                     """;
        
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 0 ; i < info.length ; i++){
                if (i == 6 || i == 18){
                    pst.setDate(i+1, Date.valueOf(info[i]));
                }else if (info[i].equals("")){
                    pst.setNull(i+1, Types.NULL);
                }else{
                    pst.setString(i+1, info[i]);
                }
            }
            
            pst.setString(21, recruitid);
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Updates the active status of the specified study
     * @param studyid
     * @param new_isactive 
     */
    public void updateIsRecruitingStudy(String studyid, String new_isrecruiting){
        String sql = """
                     UPDATE pii.studies
                     SET is_recruiting = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, new_isrecruiting);
            pst.setString(2, studyid);
            int rows = pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void updateIsActiveStudy(String studyid, String new_isactive){
        String sql = """
                     UPDATE pii.studies
                     SET is_active = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, new_isactive);
            pst.setString(2, studyid);
            int rows = pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void updateStudyClosedBy(String user, String studyid){
        String sql = """
                     UPDATE pii.studies
                     SET closed_by = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, studyid);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Adds or subtracts from the number of panels the specified recruit is currently participating in
     * @param recruitid
     * @param add 
     */
    public void updateNumPanels(String recruitid, int add){
        String sql = """
                     UPDATE pii.recruits
                     SET num_panels = ?
                     WHERE id = ?
                     """;
        int current_num_panels = this.getNumPanels(recruitid);
        int new_num_panels = current_num_panels + add;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, new_num_panels);
            pst.setString(2, recruitid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    /**
     * Adds or subtracts from the number of studies the specified recruit is currently participating in
     * @param recruitid
     * @param add 
     */
    public void updateNumStudies(String recruitid, int add){
        String sql = """
                     UPDATE pii.recruits
                     SET num_studies = ?
                     WHERE id = ?
                     """;
        int current_num_studies = this.getNumStudies(recruitid);
        int new_num_studies = current_num_studies + add;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, new_num_studies);
            pst.setString(2, recruitid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    /**
     * Set the closed date of the specified study to the current system date
     * @param studyid 
     */
    public void updateStudiesClosedDate(String studyid){
        String sql = """
                     UPDATE pii.studies
                     SET closed_date = CURDATE()
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Sets the check number of the specified visit
     * @param visitId
     * @param checkNum 
     */
    public void updateCheckNum(String visitId, String checkNum){
        String sql = """
                     UPDATE pii.visits
                     SET checknum = ?
                     WHERE id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, checkNum);
            pst.setString(2, visitId);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void deleteRecruitStudyTypes(String recruitid){
        String sql = """
                     DELETE FROM pii.recruitStudyTypes
                     WHERE recruit_id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, recruitid);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Deletes a recruit panel specified by the study id
     * @param studyid 
     */
    public void deleteRecruitingPanel(String studyid){
        String sql = """
                     DELETE FROM pii.recruitpanels
                     WHERE study_id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Deletes all records from the ongoingstudies table that have the corresponding study id
     * @param studyid 
     */
    public void deletePanel(String studyid){
        String sql = """
                     DELETE FROM pii.ongoingstudies
                     WHERE study_id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, rows + " row(s) affected");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Remove a single recruit from a recruit panel
     * @param studyid
     * @param recruitid 
     */
    public void deleteRecruitFromPanel(String studyid, String recruitid){
        String sql = """
                     DELETE FROM pii.recruitpanels
                     WHERE study_id = ?
                     AND recruit_id = ?
                     """;
        try (Connection conn = this.connect()){
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, studyid);
            pst.setString(2, recruitid);
            int rows = pst.executeUpdate();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        JOptionPane.showMessageDialog(null, "Recruit " + recruitid + " has been removed from study " + studyid);
    }
}
