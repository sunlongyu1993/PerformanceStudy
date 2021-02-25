package com.javacode.test;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Myinsert implements JavaSamplerClient {
    String sql = "insert into user(user_name,password) vaule (?,?)";
    PreparedStatement ps;
    Connection conn;

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        String url = javaSamplerContext.getParameter("url");
        String username = javaSamplerContext.getParameter("dbname");
        String password = javaSamplerContext.getParameter("dbpassword");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult =new SampleResult();
        //给请求写名称
        sampleResult.setSampleLabel("insert");
        //请求开始
        sampleResult.sampleStart();
        //执行sql 语句，返回更新的行数
        int row = 0;
        try {
            String name = javaSamplerContext.getParameter("name");
            String pwd = javaSamplerContext.getParameter("pwd");
            ps.execute();
        //断言
//            if (row =1){
//                sampleResult.setSuccessful(true);
//            }
//            else {
//                sampleResult.setSuccessful(false);
//            }


        }catch (Exception e){
            e.printStackTrace();
        }
        //请求结束
        sampleResult.sampleEnd();
        return sampleResult;
    }

    @Override
    public void teardownTest(JavaSamplerContext javaSamplerContext) {
        //关闭连接
        try {
            ps.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("url","");
        arguments.addArgument("dbname","root");
        arguments.addArgument("dbpassword","root");
        arguments.addArgument("name","");
        arguments.addArgument("pwd","");
        return arguments;
    }
//调试
    public static void main(String[] args) {
        //获取参数默认值

        // 往参数中添加值
        Arguments arguments = new Arguments();
//        arguments.addArgument("url",url);
//        arguments.addArgument("dbname",name);
//        arguments.addArgument("dbpwd",pwd);
        //
        arguments.addArgument("name","sly");
        arguments.addArgument("pwd","123456");

        JavaSamplerContext javaSamplerContext1 = new JavaSamplerContext(arguments);

    }
}
