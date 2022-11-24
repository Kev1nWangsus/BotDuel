package com.botduel.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.botduel.backend.mapper.RecordMapper;
import com.botduel.backend.mapper.UserMapper;
import com.botduel.backend.pojo.Record;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.record.RecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecordListServiceImpl implements RecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer pageNumber) {
        // backend pagination
        IPage<Record> recordIPage = new Page<>(pageNumber, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> recordList = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> itemList = new LinkedList<>();
        for (Record record : recordList) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String result = "Draw";
            if ("A".equals(record.getLoser())) {
                result = "B Win!";
            } else if ("B".equals(record.getLoser())) {
                result = "A Win!";
            }
            item.put("result", result);
            item.put("record", record);
            itemList.add(item);
        }

        resp.put("record_list", itemList);
        resp.put("record_count", recordMapper.selectCount(null));
        return resp;
    }
}
