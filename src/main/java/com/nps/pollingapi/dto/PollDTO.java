package com.nps.pollingapi.dto;

import com.nps.pollingapi.entity.Option;
import com.nps.pollingapi.entity.Poll;

import java.time.Instant;
import java.util.List;

public record PollDTO(Long id, String question, Instant postedDate, Instant expireAt, List<OptionDTO> options) {

    public PollDTO(Long id) {
        this(id, null, null, null, null);
    }

    public static PollDTO fromEntity(Poll poll) {
        List<OptionDTO> optionDTOs = poll.getOptions() != null ? poll.getOptions().stream()
                .map(OptionDTO::fromEntity)
                .toList() : null;

        return new PollDTO(
                poll.getId(),
                poll.getQuestion(),
                poll.getPostedDate(),
                poll.getExpireAt(),
                optionDTOs
        );
    }

    public Poll toEntity() {
        Poll poll = new Poll();
        poll.setId(this.id);
        poll.setQuestion(this.question);
        poll.setPostedDate(this.postedDate);
        poll.setExpireAt(this.expireAt);

        if (this.options != null) {
            List<Option> optionEntities = this.options.stream()
                    .map(OptionDTO::toEntity)
                    .toList();
            poll.setOptions(optionEntities);

        } else {
            poll.setOptions(null);
        }
        return poll;
    }

}
