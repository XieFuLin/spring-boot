package com.xfl.boot.Service.impl;

import com.xfl.boot.Service.UserService;
import com.xfl.boot.bean.UserVo;
import com.xfl.boot.dao.QuestionsMapper;
import com.xfl.boot.dao.UserVoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by XFL
 * time on 2018/8/5 22:34
 * description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserVoMapper userVoMapper;
    @Autowired
    private QuestionsMapper questionsMapper;

    @Override
    public List<UserVo> getUserByName(String userName) {
        Long st1Time = System.currentTimeMillis();
        List<UserVo> userVoList = userVoMapper.selectByName(userName);
        System.out.println("firstQueryResukt: " + userVoList);
        System.out.println("firstQueryTime:" + (System.currentTimeMillis() - st1Time));
        Long st2Time = System.currentTimeMillis();
        List<UserVo> userVoList2 = userVoMapper.selectByName(userName);
        System.out.println("secondQueryResukt: " + userVoList2);
        System.out.println("secondQueryTime:" + (System.currentTimeMillis() - st2Time));
        int count = questionsMapper.selectCount();
        System.out.println("count:" + count);
        return userVoList2;
    }
}
