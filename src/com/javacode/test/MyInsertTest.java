package com.javacode.test;

import java.sql.*;
//调试用的代码，主要是测试是否可以写入到数据库
public class MyInsertTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/pinter?useUnicode=true&characterEncoding=utf8";
        String username = "root";//连接数据库的用户名
        String password = "root";//连接数据库的密码

        Connection conn=null;//连接
        PreparedStatement ps =null;//用来存储sql中查询的结果

        String sql = "INSERT into `user`(user_name,password) VALUES (?,?);";

        //注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");

            //建立连接，通过url，以及username以及password 获取数据库连接
            try {
                conn = DriverManager.getConnection(url, username, password);
                //执行步骤
                ps = conn.prepareStatement(sql);//预执行，将sql语句执行后的结果放到ps 中

                //真正的执行
                ps.setString(1,"username1");//替换sql中设置第一个问号值为username1
                ps.setString(2,"123456");//替换sql中设置第2个问号值为123456
                int row = ps.executeUpdate();//更新，修改了多少行记录
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
