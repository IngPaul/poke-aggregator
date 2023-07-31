package com.alpha.pocfilter.webclient;

import com.alpha.pocfilter.model.Episode;
import reactor.core.publisher.Mono;

public interface EpisodeClient {
    public Mono<Episode> getEpisode(Long id, String status);
}
