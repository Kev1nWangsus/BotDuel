package com.botduel.backend.controller.rank;

import com.alibaba.fastjson.JSONObject;
import com.botduel.backend.service.rank.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RankListController {
    @Autowired
    private RankListService rankListService;

    @GetMapping("/rank/getlist")
    JSONObject getList(@RequestParam Map<String, String> data) {
        Integer pageNumber = Integer.parseInt(data.get("page"));
        return rankListService.getList(pageNumber);
    }
}
