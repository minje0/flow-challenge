package com.example.flow.dto;

import com.example.flow.entity.ExtensionBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionDto {
    private String extension;
    private String description;
    private String blockYn;

    public static ExtensionDto fromEntity(ExtensionBlock entity) {
        return ExtensionDto.builder()
                .extension(entity.getExtension())
                .description(entity.getDescription())
                .blockYn(entity.getBlockYn())
                .build();
    }

}