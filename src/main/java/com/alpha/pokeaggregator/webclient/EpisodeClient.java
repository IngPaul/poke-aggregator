package com.alpha.pokeaggregator.webclient;

import com.alpha.pokeaggregator.model.Episode;
import reactor.core.publisher.Mono;

public interface EpisodeClient {
    public Mono<Episode> getEpisode(Long id, String status);
}
