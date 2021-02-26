package com.javacode.JavaSampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//jmeter的jar包规定的格式
public class JmeterInsert implements JavaSamplerClient {
    String dburl ="jdbc:mysql://localhost:3306/pinter?useUnicode=true&characterEncoding=utf8";
    String dbname ="root";
    String dbpwd ="root";

    Connection conn=null;
    PreparedStatement ps =null;//用来存储sql中查询的结果
    String sql = "INSERT into `user`(user_name,password) VALUES (?,?);";

    /**
     * getDefaultParameters:获取参数，设置的参数会在Jmeter的参数面板上显示出来
     * @return
     */
    @Override//首先执行，获取默认参数
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("url",dburl);
        arguments.addArgument("dbname",dbname);
        arguments.addArgument("dbpwd",dbpwd);
        arguments.addArgument("username","");
        arguments.addArgument("pwd","");
        return arguments;
    }

    /**
     * setupTest:初始化方法，只执行一次，跟LR里的init方法一样的，用于建立链接
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
     * @param arg0
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String username = arg0.getParameter("username");//获取参数
        String pwd = arg0.getParameter("pwd");//获取参数

        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("JavaInsert");//给取样器取名
        sampleResult.sampleStart();//调用取样器
        int row =0;//插入数据的行数，初始值为0
        try {//替换sql语句中的2个问号，如果有3个值，则需要添加3个变量
            ps.setString(1,username);
            ps.setString(2,pwd);
            row = ps.executeUpdate();//取样器执行，返回结果赋值给row
            //如果执行成功，则数据库中添加一条数据，则row大于零
            if (row > 0){
                sampleResult.setSuccessful(true);
                sampleResult.setResponseData("javainsert请求执行成功，数据库中添加数据为"+row,"UTF-8");
            }
            else {
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("javainsert请求执行失败，请检查","UTF-8");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        sampleResult.sampleEnd();//取样器关闭
        return sampleResult;
    }

//调试
    public static void main(String[] args) {
        String dburl ="jdbc:mysql://localhost:3306/pinter?useUnicode=true&characterEncoding=utf8";
        String dbname ="root";
        String dbpwd ="root";

        JmeterInsert jmeterInsert = new JmeterInsert();
        JavaSamplerContext context = new JavaSamplerContext(jmeterInsert.getDefaultParameters());
        context.getParameter("url");
        context.getParameter("dbname");
        context.getParameter("dbpwd");

        // 往参数中添加值
        Arguments arguments = new Arguments();
        arguments.addArgument("url",dburl);
        arguments.addArgument("dbname",dbname);
        arguments.addArgument("dbpwd",dbpwd);

        arguments.addArgument("username","sly111");
        arguments.addArgument("pwd","123456");

        JavaSamplerContext context2 = new JavaSamplerContext(arguments);//将参数值传入
        JmeterInsert jmeterInsert2 = new JmeterInsert();
        jmeterInsert2.getDefaultParameters();
        jmeterInsert2.setupTest(context2);
        jmeterInsert2.runTest(context2);
        jmeterInsert2.teardownTest(context2);

    }
}
