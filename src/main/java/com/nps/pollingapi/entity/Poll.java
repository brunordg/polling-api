package com.nps.pollingapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Poll {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String question;

    private Instant postedDate;

    private Instant expireAt;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Option> options;

    public Poll(Long id) {
        this.id = id;
    }
}
