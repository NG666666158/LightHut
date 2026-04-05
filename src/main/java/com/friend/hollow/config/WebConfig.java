package com.friend.hollow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * 将本地上传的星光墙图片、冥想白噪音目录、侧栏轻音乐目录映射为可访问的 URL。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Path uploadRoot;
    private final Path meditationNoiseRoot;
    private final Path homeMusicRoot;

    public WebConfig(
            @Value("${app.starlight.upload-dir:uploads/starlight}") String uploadDir,
            @Value("${app.meditation-noise-dir:mp3}") String meditationNoiseDir,
            @Value("${app.home-music-dir:musics}") String homeMusicDir
    ) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        this.meditationNoiseRoot = Path.of(meditationNoiseDir).toAbsolutePath().normalize();
        this.homeMusicRoot = Path.of(homeMusicDir).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadRoot.toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/starlight/**")
                .addResourceLocations(location);

        String medLoc = meditationNoiseRoot.toUri().toString();
        if (!medLoc.endsWith("/")) {
            medLoc = medLoc + "/";
        }
        registry.addResourceHandler("/meditation-mp3/**")
                .addResourceLocations(medLoc)
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());

        String hmLoc = homeMusicRoot.toUri().toString();
        if (!hmLoc.endsWith("/")) {
            hmLoc = hmLoc + "/";
        }
        registry.addResourceHandler("/home-musics/**")
                .addResourceLocations(hmLoc)
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());
    }
}
