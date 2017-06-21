/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author SahaN
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
//    static {
//        try {
//            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
//        } catch (Throwable ex) {
//
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
    
    
    static {
        try {

            Properties dbConnectionProperties = null;
            Connection connection = null;

            try {
                dbConnectionProperties = new Properties();

                //String path = System.getenv("DB_PATH");
                String path = "/home/sandun/Desktop/SanasaOperational/Code/SanasaOperational/src";
                File file = new File(path+ "/hibernate.properties");
                FileInputStream fi = new FileInputStream(file);
                dbConnectionProperties.load(fi);
                fi.close();

                String location = dbConnectionProperties.getProperty("hibernate.connection.url");
                String username = dbConnectionProperties.getProperty("hibernate.connection.username");
                String password = dbConnectionProperties.getProperty("hibernate.connection.password");

                try {
                    connection = DriverManager.getConnection(location, username, password);
                } catch (SQLException sQLException) {
                    JOptionPane.showMessageDialog(null, "Database Cofiguration Cannot Load. Please Set the ENV variable DB_PATH for the property file directory. Check for correct connection settings");
                    System.exit(0);
                }

            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(null, "Database Cofiguration Cannot Load. Please Set the ENV variable DB_PATH for the property file directory. Check for correct connection settings");
                System.exit(0);
            }

            //dbConnectionProperties.load(HibernateUtil.class.getClassLoader().getSystemClassLoader().getResourceAsStream("hibernate.properties"));
            sessionFactory = new AnnotationConfiguration().mergeProperties(dbConnectionProperties).configure("hibernate.cfg.xml").buildSessionFactory();

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }

    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
