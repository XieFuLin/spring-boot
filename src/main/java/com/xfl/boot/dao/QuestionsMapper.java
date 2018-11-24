package com.xfl.boot.dao;

import com.xfl.boot.common.datasource.TargetDataSource;

@TargetDataSource(name = "survey")
public interface QuestionsMapper {
    //    @TargetDataSource(name = "survey")
    int selectCount();
}