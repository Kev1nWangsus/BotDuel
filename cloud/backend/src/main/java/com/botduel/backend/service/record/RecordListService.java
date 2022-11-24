package com.botduel.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface RecordListService {
    JSONObject getList(Integer pageNumber);
}
