package com.xfl.boot.Service.impl;

import com.xfl.boot.Service.UserService;
import com.xfl.boot.bean.UserVo;
import com.xfl.boot.dao.UserVoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XFL
 * time on 2018/8/5 22:34
 * description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserVoMapper userVoMapper;

    @Override
    public List<UserVo> getUserByName(String userName) {
        Long st1Time = System.currentTimeMillis();
        List<UserVo> userVoList = userVoMapper.selectByName(userName);
        System.out.println("firstQueryResukt: " + userVoList);
        System.out.println("firstQueryTime:" + (System.currentTimeMillis() - st1Time));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        Long st2Time = System.currentTimeMillis();
        List<UserVo> userVoList2 = userVoMapper.selectByName(userName);
        System.out.println("secondQueryResukt: " + userVoList2);
        System.out.println("secondQueryTime:" + (System.currentTimeMillis() - st2Time));
        return userVoList2;
    }
}
