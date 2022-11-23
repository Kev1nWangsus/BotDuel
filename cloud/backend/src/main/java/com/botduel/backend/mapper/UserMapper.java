package com.botduel.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.botduel.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangs
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
