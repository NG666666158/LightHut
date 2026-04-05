package com.friend.hollow.service;

import com.friend.hollow.dto.HugTodayResponse;

public interface CheerHugService {

    HugTodayResponse today();

    HugTodayResponse increment();
}
