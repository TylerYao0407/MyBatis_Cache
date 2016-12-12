package com.tyler.test;

import com.tyler.mapper.UserMapper;
import com.tyler.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * Created by tyler on 2016/12/12.
 */
public class MyBatisTest {
    private SqlSession sqlSession = null;
    private SqlSession sqlSession1 = null;
    @Test
    public void select(){
        try {
            sqlSession = SqlSessionFactoryUtil.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //第一次查询
            User user = userMapper.selectUser(1);
            //还在此次会话范围内，
            //使用一级缓存查询
            //因此次句不产生sql语句
            User user1 = userMapper.selectUser(1);
            //事务被提交，本次会话结束，超过一级缓存范围
            sqlSession.commit();
            //会话二，使用二级缓存
            //使用二级缓存之后，以下不走sql语句
            sqlSession1 = SqlSessionFactoryUtil.openSession();
            UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
            User user2 = userMapper1.selectUser(1);
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            if (sqlSession!=null){
                sqlSession.close();
            }
        }
    }
}
