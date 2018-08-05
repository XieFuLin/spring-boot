package com.xfl.boot.Service;

import com.xfl.boot.bean.UserVo;

import java.util.List;

/**
 * Created by XFL
 * time on 2018/8/5 22:33
 * description:
 */
public interface UserService {
    List<UserVo> getUserByName(String userName);
}
