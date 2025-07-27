package com.example.flow.service;

import com.example.flow.common.CommonConstants;
import com.example.flow.dto.ExtensionDto;
import com.example.flow.entity.ExtensionBlock;
import com.example.flow.repository.ExtensionBlockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtBlockService {
    private final ExtensionBlockRepository extBlockRepository;

    /**
     * 고정 확장자 리스트 조회
     */
    public List<ExtensionDto> getFixedExtensionList() {
        return extBlockRepository.findByBlockTypeCd(CommonConstants.BLOCK_TYPE_FIXED)
                .stream()
                .map(ExtensionDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 커스텀 확장자 리스트 조회
     */
    public List<ExtensionDto> getCustomExtensionList() {
        return extBlockRepository.findByBlockTypeCdAndBlockYn(CommonConstants.BLOCK_TYPE_CUSTOM, CommonConstants.STR_Y)
                .stream()
                .map(ExtensionDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 커스텀 고정 확장자 리스트 조회
     */
    public List<ExtensionDto> getCustomFixedExtensionList() {
        return extBlockRepository.findByBlockTypeCd(CommonConstants.BLOCK_TYPE_CUSTOM_FIXED)
                .stream()
                .map(ExtensionDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 확장자 상태 변경
     */
    @Transactional
    public void updateFixedExtension(String extension, String blockYn) {
        ExtensionBlock block = extBlockRepository.findByExtension(extension)
                .orElseThrow(() -> new IllegalArgumentException("해당 확장자가 존재하지 않습니다: " + extension));

        block.updateBlockYn(blockYn);
    }

    /**
     * 커스텀 확장자 추가
     */
    @Transactional
    public ExtensionDto addCustomExtension(ExtensionDto inDto) {
        String ext = inDto.getExtension();
        String description = inDto.getDescription();

        if (ext == null || ext.isBlank()) {
            throw new IllegalArgumentException("확장자를 입력해주세요.");
        }
        if (ext.contains(" ")) {
            throw new IllegalArgumentException("확장자에 공백을 포함할 수 없습니다.");
        }
        if (ext.length() > 20) {
            throw new IllegalArgumentException("확장자는 최대 20자입니다.");
        }
        if (!ext.matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("확장자는 영문 소문자와 숫자만 입력 가능합니다.");
        }

        if (description.length() > 100) {
            throw new IllegalArgumentException("설명은 최대 100자입니다.");
        }

        long customBlockedCount = extBlockRepository.countByBlockTypeCdAndBlockYn(
                CommonConstants.BLOCK_TYPE_CUSTOM, CommonConstants.STR_Y
        );
        if (customBlockedCount >= 200) {
            throw new IllegalArgumentException("커스텀 확장자는 최대 200개까지 등록할 수 있습니다.");
        }

        // 소문자 처리
        ext = ext.toLowerCase();

        Optional<ExtensionBlock> opExtBlock = extBlockRepository.findByExtension(ext);
        ExtensionBlock extBlock = validNotFixedExt(ext, opExtBlock);

        // 이미 존재하는 차단 상태면 등록 불가
        if (extBlock != null) {
            if (CommonConstants.STR_Y.equalsIgnoreCase(extBlock.getBlockYn())) {
                throw new IllegalArgumentException("이미 등록된 확장자입니다.");
            } else if (CommonConstants.BLOCK_TYPE_CUSTOM_FIXED.equalsIgnoreCase(extBlock.getBlockTypeCd())) {
                throw new IllegalArgumentException("이미 등록된 확장자입니다.");
            }
            // 존재하지만 차단 안 된 경우 → 차단 활성화
            extBlock.updateBlockTypeCd(CommonConstants.BLOCK_TYPE_CUSTOM);
            extBlock.updateBlockYn(CommonConstants.STR_Y);
            return ExtensionDto.fromEntity(extBlock);
        }
        if (description == null || description.trim().isEmpty()) {
            description = "설명 없음";
        }
        // 존재하지 않으면 새로 저장
        ExtensionBlock saveEntity = ExtensionBlock.builder()
                .extension(ext)
                .description(description)
                .blockYn(CommonConstants.STR_Y)
                .blockTypeCd(CommonConstants.BLOCK_TYPE_CUSTOM)
                .build();

        return ExtensionDto.fromEntity(extBlockRepository.save(saveEntity));
    }

    /**
     * 수정 가능한 지 확인
     */
    private ExtensionBlock validNotFixedExt(String extension, Optional<ExtensionBlock> optional) {
        if (optional.isPresent()) {
            ExtensionBlock entity = optional.get();

            if (CommonConstants.BLOCK_TYPE_FIXED.equalsIgnoreCase(entity.getBlockTypeCd())) {
                throw new IllegalArgumentException("고정된 확장자는 수정할 수 없습니다: " + extension);
            }

            return entity;
        }
        return null;
    }

    /**
     * 커스텀 확장자 삭제
     */
    @Transactional
    public void removeCustomExtension(String extension) {
        Optional<ExtensionBlock> opExtBlock = extBlockRepository.findByExtension(extension);
        ExtensionBlock extBlock = validNotFixedExt(extension, opExtBlock);

        if (extBlock == null) {
            throw new IllegalArgumentException("해당 확장자가 존재하지 않습니다: " + extension);
        }

        if (CommonConstants.BLOCK_TYPE_CUSTOM.equals(extBlock.getBlockTypeCd())) {
            if (!CommonConstants.STR_Y.equalsIgnoreCase(extBlock.getBlockYn())) {
                throw new IllegalArgumentException("이미 차단된 확장자입니다.");
            }
        } else {
            extBlock.updateBlockTypeCd(CommonConstants.BLOCK_TYPE_CUSTOM);
        }
        extBlock.updateBlockYn(CommonConstants.STR_N);
    }

    /**
     * 커스텀 확장자를 고정된 확장자로 변경
     */
    @Transactional
    public ExtensionDto convertCustomToFixed(ExtensionDto inDto) {
        String ext = inDto.getExtension();

        ExtensionBlock entity = extBlockRepository.findByExtension(ext)
                .orElseThrow(() -> new IllegalArgumentException("해당 확장자가 존재하지 않습니다."));

        entity.updateBlockTypeCd(CommonConstants.BLOCK_TYPE_CUSTOM_FIXED);
        entity.updateBlockYn(CommonConstants.STR_Y);
        ExtensionDto.fromEntity(entity);

        return ExtensionDto.fromEntity(entity);
    }

}
