package com.nps.pollingapi.dto;

import com.nps.pollingapi.entity.Option;
import com.nps.pollingapi.entity.Poll;

public record OptionDTO(Long id, String title, Long pollId) {

    public OptionDTO(Long id) {
        this(id, null, 0L);
    }

    public static OptionDTO fromEntity(Option option) {
        return new OptionDTO(
                option.getId(),
                option.getTitle(),
                option.getPoll().getId()
        );
    }

    public Option toEntity() {
        Option option = new Option();
        option.setId(this.id);
        option.setTitle(this.title);
        if (this.pollId == null) {
            option.setPoll(null);
        } else {
            option.setPoll(new Poll(this.pollId));
        }

        return option;
    }
}
