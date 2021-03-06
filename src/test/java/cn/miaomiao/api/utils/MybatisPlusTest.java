package cn.miaomiao.api.utils;

import cn.miaomiao.api.entity.UserLogin;
import cn.miaomiao.api.dao.UserLoginDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author miaomiao
 * @date 2019/4/22 16:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    private UserLoginDao userLoginDao;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<UserLogin> userList = userLoginDao.selectList(null);
        Assert.assertEquals(1, userList.size());
        userList.forEach(System.out::println);
    }
}
