package com.friend.hollow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "hug_click_daily")
public class HugClickDailyEntity {

    /** H2 保留字 DAY / COUNT，列名需避开 */
    @Id
    @Column(name = "hug_day", nullable = false)
    private LocalDate day;

    @Column(name = "click_count", nullable = false)
    private long count;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
