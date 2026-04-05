package com.friend.hollow.service.impl;

import com.friend.hollow.dto.SignInActionResponse;
import com.friend.hollow.dto.SignInStatusResponse;
import com.friend.hollow.entity.SignInStateEntity;
import com.friend.hollow.repository.jpa.SignInStateJpaDao;
import com.friend.hollow.service.SignInService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SignInServiceImpl implements SignInService {

    private final SignInStateJpaDao signInStateJpaDao;

    public SignInServiceImpl(SignInStateJpaDao signInStateJpaDao) {
        this.signInStateJpaDao = signInStateJpaDao;
    }

    @PostConstruct
    public void ensureDefaults() {
        if (signInStateJpaDao.findById(1L).isEmpty()) {
            SignInStateEntity e = new SignInStateEntity();
            e.setId(1L);
            e.setStreakDays(7);
            e.setLastSignInDate(LocalDate.now().minusDays(1));
            signInStateJpaDao.save(e);
        }
    }

    @Override
    public synchronized SignInStatusResponse getStatus() {
        SignInStateEntity state = loadState();
        refreshStreakIfNeeded(state);
        SignInStatusResponse response = new SignInStatusResponse();
        response.setSignedToday(isSignedToday(state));
        response.setStreakDays(state.getStreakDays());
        response.setGrowthProgress(calculateGrowthProgress(state.getStreakDays()));
        response.setTreeStage(calculateTreeStage(state.getStreakDays()));
        return response;
    }

    @Override
    public synchronized SignInActionResponse signIn() {
        SignInStateEntity state = loadState();
        refreshStreakIfNeeded(state);
        SignInActionResponse response = new SignInActionResponse();

        if (isSignedToday(state)) {
            response.setMessage("今天已经签到过啦，明天再来给小树浇水吧。");
        } else {
            LocalDate today = LocalDate.now();
            LocalDate last = state.getLastSignInDate();
            if (last != null && ChronoUnit.DAYS.between(last, today) == 1) {
                state.setStreakDays(state.getStreakDays() + 1);
            } else {
                state.setStreakDays(1);
            }
            state.setLastSignInDate(today);
            signInStateJpaDao.save(state);
            response.setMessage("签到成功！小树苗又长高了一点点。");
        }

        response.setSignedToday(isSignedToday(state));
        response.setStreakDays(state.getStreakDays());
        response.setGrowthProgress(calculateGrowthProgress(state.getStreakDays()));
        response.setTreeStage(calculateTreeStage(state.getStreakDays()));
        return response;
    }

    private SignInStateEntity loadState() {
        return signInStateJpaDao.findById(1L).orElseThrow();
    }

    private void refreshStreakIfNeeded(SignInStateEntity state) {
        LocalDate lastSignInDate = state.getLastSignInDate();
        if (lastSignInDate == null) {
            return;
        }
        long days = ChronoUnit.DAYS.between(lastSignInDate, LocalDate.now());
        if (days > 1 && state.getStreakDays() != 0) {
            state.setStreakDays(0);
            signInStateJpaDao.save(state);
        }
    }

    private boolean isSignedToday(SignInStateEntity state) {
        return LocalDate.now().equals(state.getLastSignInDate());
    }

    private int calculateGrowthProgress(int days) {
        return Math.min(100, Math.max(0, (days * 100) / 30));
    }

    private int calculateTreeStage(int days) {
        if (days >= 30) {
            return 5;
        }
        if (days >= 20) {
            return 4;
        }
        if (days >= 10) {
            return 3;
        }
        if (days >= 3) {
            return 2;
        }
        return 1;
    }
}
