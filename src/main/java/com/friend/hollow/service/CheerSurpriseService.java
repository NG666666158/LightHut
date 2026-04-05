package com.friend.hollow.service;

import com.friend.hollow.dto.CheerSurpriseCreateRequest;
import com.friend.hollow.dto.CheerSurpriseListResponse;

public interface CheerSurpriseService {

    CheerSurpriseListResponse listAll();

    CheerSurpriseListResponse add(CheerSurpriseCreateRequest request);
}
