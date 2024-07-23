package com.myjob.useapi.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
public class MyFactory {
    
    SqlSession session;

    public MyFactory(){
        try {
            Reader reader = Resources.getResourceAsReader("com/myjob/useapi/mybatis/config.xml"); 
            SqlSessionFactory factory= new SqlSessionFactoryBuilder().build(reader);
            session = factory.openSession();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public SqlSession getSession(){
        return session;
    }
}
