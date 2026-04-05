package com.friend.hollow.config;

import com.friend.hollow.entity.CheerSurpriseEntity;
import com.friend.hollow.repository.jpa.CheerSurpriseJpaDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(100)
public class CheerSurpriseSeedRunner implements ApplicationRunner {

    private final CheerSurpriseJpaDao dao;

    public CheerSurpriseSeedRunner(CheerSurpriseJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (dao.count() > 0) {
            return;
        }
        insert("小森林", "seed-xsl", "刚刚收到了一封跨越 1000 公里的信件");
        insert("雨过天晴", "seed-ygq", "在情绪树洞里被一张陌生人的插画治愈");
        insert("莫奈的花园", "seed-mn", "解锁了「给我惊喜」隐藏彩蛋");
    }

    private void insert(String name, String seed, String blessing) {
        CheerSurpriseEntity e = new CheerSurpriseEntity();
        e.setDisplayName(name);
        e.setAvatarSeed(seed);
        e.setBlessing(blessing);
        dao.save(e);
    }
}
