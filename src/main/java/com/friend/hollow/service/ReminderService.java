package com.friend.hollow.service;

import com.friend.hollow.dto.ReminderCreateRequest;
import com.friend.hollow.dto.ReminderDayResponse;
import com.friend.hollow.dto.ReminderItemResponse;
import com.friend.hollow.dto.ReminderMonthResponse;

import java.time.LocalDate;

public interface ReminderService {

    ReminderMonthResponse getMonth(int year, int month);

    ReminderDayResponse listByDate(LocalDate date);

    ReminderItemResponse create(ReminderCreateRequest request);

    ReminderItemResponse setDone(long id, boolean done);
}
