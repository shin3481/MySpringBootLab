package com.rookies4.myspringbootlab.controller.dto;

import com.rookies4.myspringbootlab.entity.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PublisherDTO {
    private Long id;
    private String name;
    private Long bookCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleResponse fromEntityWithCount(Publisher publisher,Long count){
        return SimpleResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .studentCount((long) department.getStudents().size())
                .build();

    }
}

