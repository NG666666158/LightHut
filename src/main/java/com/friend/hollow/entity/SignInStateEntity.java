package com.friend.hollow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "sign_in_state")
public class SignInStateEntity {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private int streakDays;

    private LocalDate lastSignInDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }
    public LocalDate getLastSignInDate() { return lastSignInDate; }
    public void setLastSignInDate(LocalDate lastSignInDate) { this.lastSignInDate = lastSignInDate; }
}
