package com.alpha.pokeaggregator.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
@Data
@ToString
public class Character {
        private Long id;
        private String name;
        private String status;
        private String gender;
        private Origin origin;
        private Location location;
        private String images;
        private List<String> episode;
        private String url;
        private LocalDate created;

        @Override
        public String toString() {
                return "Character{" + "id=" + id + ", name='" + name + '\'' + ", status='" + status + '\''
                    + ", gender='" + gender + '\'' + ", origin=" + origin + ", location=" + location + ", images='"
                    + images + '\'' + ", episode=" + episode + ", url='" + url + '\'' + ", created=" + created + '}';
        }

}
