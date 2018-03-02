/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class JDBC {

    private static final String DRIVERCLASS = "org.apache.derby.jdbc.EmbeddedDriver"; // 驱动

    private static final String URL = "jdbc:derby:db_map"; // 协议

    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>(); // 用来保存数据库连接

    private static Connection conn = null; // 数据库连接

    

    static { // 通过静态方法加载数据库驱动，并且在数据库不存在的情况下创建数据库

        try {
            Class.forName(DRIVERCLASS); // 加载数据库驱动

            File databaseFile = new File("db_map");// 创建数据库文件对象

            if (!databaseFile.exists()) {// 判断数据库文件是否存在

                String[] sqls = new String[5];// 定义创建数据库的SQL语句

                sqls[0] = "create table tb_map (id int not null,name varchar(8) not null)";
                sqls[1] = "insert into tb_map(id,name) values(1,'map.jpg')";
                sqls[2] = "create table tb_sort (id int not null,father_id int not null,name varchar(20) not null,primary key (id))";
                sqls[3] = "create table tb_sign (id int not null,sort_id int not null,x int not null,y int not null,title varchar(20) not null,show int not null,scale float not null,date date not null,remark varchar(200),primary key (id))";
                sqls[4] = "create view v_sign_sort as SELECT tb_sign.x, tb_sign.y, tb_sign.title, tb_sort.id, tb_sort.name, tb_sign.show, tb_sign.scale, tb_sign.date, tb_sign.remark FROM tb_sign INNER JOIN tb_sort ON tb_sign.sort_id = tb_sort.id ";
                conn = DriverManager.getConnection(URL + ";create=true");// 创建数据库连接

                threadLocal.set(conn);// 保存数据库连接

                Statement stmt = conn.createStatement();// 创建数据库连接状态对象

                for (int i = 0; i < sqls.length; i++) {// 通过执行SQL语句创建数据库

                    stmt.execute(sqls[i]);// 执行SQL语句

                }
                stmt.close();// 关闭数据库连接状态对象

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Connection getConnection() { // 创建数据库连接的方法

        conn = (Connection) threadLocal.get(); // 从线程中获得数据库连接

        if (conn == null) { // 没有可用的数据库连接

            try {
                conn = DriverManager.getConnection(URL);// 创建新的数据库连接

                threadLocal.set(conn); // 将数据库连接保存到线程中

            } catch (Exception e) {
                String[] infos = {"未能成功连接数据库！", "请确认本软件是否已经运行！"};
                JOptionPane.showMessageDialog(null, infos);// 弹出连接数据库失败的提示

                System.exit(0);// 关闭系统

                e.printStackTrace();
            }
        }
        return conn;
    }

    protected static boolean closeConnection() { // 关闭数据库连接的方法

        boolean isClosed = true; // 默认关闭成功

        conn = (Connection) threadLocal.get(); // 从线程中获得数据库连接

        threadLocal.set(null); // 清空线程中的数据库连接

        if (conn != null) { // 数据库连接可用

            try {
                conn.close(); // 关闭数据库连接

            } catch (SQLException e) {
                isClosed = false; // 关闭失败

                e.printStackTrace();
            }
        }
        return isClosed;
    }
}
