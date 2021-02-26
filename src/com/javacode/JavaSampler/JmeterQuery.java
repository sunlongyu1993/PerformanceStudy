package com.javacode.JavaSampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.sql.*;

//jmeter的jar包规定的格式_查询请求
public class JmeterQuery implements JavaSamplerClient {
    String dburl = "jdbc:mysql://localhost:3306/pinter?useUnicode=true&characterEncoding=utf8";
    String dbname = "root";
    String dbpwd = "root";

    Connection conn = null;
    PreparedStatement ps = null;//用来存储sql中查询的结果
    String sql = "select user_name,password from user where id=?";

    /**
     * getDefaultParameters:获取参数，设置的参数会在Jmeter的参数面板上显示出来
     *
     * @return
     */
    @Override//首先执行，获取默认参数
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("url", dburl);
        arguments.addArgument("dbname", dbname);
        arguments.addArgument("dbpwd", dbpwd);
        arguments.addArgument("id", "1");
        return arguments;
    }

    /**
     * setupTest:初始化方法，只执行一次，跟LR里的init方法一样的，用于建立链接
     *
     * @param arg0
     */

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        String url = arg0.getParameter("url");
        String dbname = arg0.getParameter("dbname");
        String dbpwd = arg0.getParameter("dbpwd");
        try {
            Class.forName("com.mysql.jdbc.Driver");//注册驱动
            //建立连接，通过url，以及dbname以及dbpwd 获取数据库连接
            conn = DriverManager.getConnection(url, dbname, dbpwd);
            ps = conn.prepareStatement(sql);//预执行，将运行结果放到ps 中

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * teardownTest:结束方法，只执行一次，LR里的end方法是一样的，用于释放资源
     *
     * @param arg0
     */
    @Override
    public void teardownTest(JavaSamplerContext arg0) {
        try {
            ps.close();//执行器关闭
            conn.close();//连接池关闭
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * runTest:执行N次，处理业务
     *
     * @param arg0
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        int id = Integer.parseInt(arg0.getParameter("id"));//获取参数
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("JavaQuery");//给取样器取名
        sampleResult.sampleStart();//调用取样器
        try {//替换sql语句中的1个问号，如果有3个值，则需要添加3个变量
            ps.setInt(1, id);
            ResultSet querySet = ps.executeQuery();//取样器执行，
//如果执行成功，则数据库中添加一条数据，则row大于零
            if (querySet.next()) {//querySet.next() 判断是否有下一行，如果有就执行，如果没有，就执行结束
                sampleResult.setSuccessful(true);
                sampleResult.setResponseData("javaquery查询成功", "UTF-8");
                String username = querySet.getString(1);
                String pwd = querySet.getString(2);
                System.out.println(username+":"+pwd);
            } else {
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("javaquery查询失败，请检查", "UTF-8");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        sampleResult.sampleEnd();//取样器关闭
        return sampleResult;
    }

    //调试
    public static void main(String[] args) {
        JmeterQuery jmeterQuery = new JmeterQuery();
        JavaSamplerContext context = new JavaSamplerContext(jmeterQuery.getDefaultParameters());
        jmeterQuery.setupTest(context);
        jmeterQuery.runTest(context);
        jmeterQuery.teardownTest(context);
    }
}
