package com.shadorc.shadbot.api.json.gamestats.overwatch.stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeroPlayed {

    @JsonProperty("hero")
    private String hero;
    @JsonProperty("played")
    private String played;

    public String getHero() {
        return this.hero;
    }

    public String getPlayed() {
        return this.played;
    }

    @Override
    public String toString() {
        return "HeroPlayed{" +
                "hero='" + this.hero + '\'' +
                ", played='" + this.played + '\'' +
                '}';
    }
}
