package com.xfl.boot.dao;

import com.xfl.boot.bean.UserVo;

import java.util.List;

public interface UserVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    UserVo selectByPrimaryKey(Integer id);

    List<UserVo> selectByName(String userName);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);
}