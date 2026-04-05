package com.friend.hollow.controller.api;

import com.friend.hollow.dto.CheerSurpriseCreateRequest;
import com.friend.hollow.dto.CheerSurpriseListResponse;
import com.friend.hollow.service.CheerSurpriseService;
import com.friend.hollow.util.PagePathUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheerSurpriseApiController {

    private final CheerSurpriseService cheerSurpriseService;

    public CheerSurpriseApiController(CheerSurpriseService cheerSurpriseService) {
        this.cheerSurpriseService = cheerSurpriseService;
    }

    @GetMapping(PagePathUtil.CHEER_SURPRISES_API)
    public CheerSurpriseListResponse list() {
        return cheerSurpriseService.listAll();
    }

    @PostMapping(PagePathUtil.CHEER_SURPRISE_API)
    public CheerSurpriseListResponse create(@Valid @RequestBody CheerSurpriseCreateRequest request) {
        return cheerSurpriseService.add(request);
    }
}
