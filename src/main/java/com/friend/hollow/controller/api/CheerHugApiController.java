package com.friend.hollow.controller.api;

import com.friend.hollow.dto.HugTodayResponse;
import com.friend.hollow.service.CheerHugService;
import com.friend.hollow.util.PagePathUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheerHugApiController {

    private final CheerHugService cheerHugService;

    public CheerHugApiController(CheerHugService cheerHugService) {
        this.cheerHugService = cheerHugService;
    }

    @GetMapping(PagePathUtil.CHEER_HUG_TODAY_API)
    public HugTodayResponse today() {
        return cheerHugService.today();
    }

    @PostMapping(PagePathUtil.CHEER_HUG_API)
    public HugTodayResponse increment() {
        return cheerHugService.increment();
    }
}
