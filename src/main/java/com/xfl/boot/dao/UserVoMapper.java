package com.xfl.boot.dao;

import com.xfl.boot.bean.UserVo;

public interface UserVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    UserVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);
}