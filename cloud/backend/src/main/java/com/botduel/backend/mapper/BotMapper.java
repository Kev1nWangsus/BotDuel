package com.botduel.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.botduel.backend.pojo.Bot;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangs
 */
@Mapper
public interface BotMapper extends BaseMapper<Bot>  {

}
