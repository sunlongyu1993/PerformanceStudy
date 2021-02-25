package com.javacode.study20210110;

import java.sql.*;
//自己用的代码
public class MyInsertTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/pinter?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "root";
        Connection conn=null;
        String sql = "INSERT into `user`(user_name,password) VALUES (?,?);";
        PreparedStatement ps =null;
        //注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //建立连接，通过url，以及username以及password 获取数据库连接
            try {
                conn = DriverManager.getConnection(url, username, password);
                ps = conn.prepareStatement(sql);//预执行，将运行结果放到ps 中
                //真正的执行
                ps.setString(1,"username1");//设置第一个问号值为username1
                ps.setString(2,"123456");//设置第2个问号值为123456
                int row = ps.executeUpdate();//修改了多少行记录
                System.out.println(row);
                ps.close();//预处理需要关闭
                conn.close();//连接池需要关闭
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
