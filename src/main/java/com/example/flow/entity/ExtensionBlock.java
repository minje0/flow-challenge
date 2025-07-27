package com.example.flow.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "EXTENSION_BLOCK")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtensionBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EXTENSION", nullable = false, unique = true, length = 20)
    private String extension;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "BLOCK_TYPE_CD", nullable = false, length = 2)
    private String blockTypeCd;

    @Column(name = "REMARK", length = 200)
    private String remark;

    @Column(name = "BLOCK_YN", nullable = false, length = 1)
    private String blockYn;

    @Column(name = "RGT_DTM", nullable = false, updatable = false)
    private LocalDateTime rgtDtm;

    @Column(name = "MDF_DTM", nullable = false)
    private LocalDateTime mdfDtm;

    @PrePersist
    public void onCreate() {
        this.rgtDtm = this.mdfDtm = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.mdfDtm = LocalDateTime.now();
    }

    public void updateBlockYn(String blockYn) {
        this.blockYn = blockYn;
    }

    public void updateBlockTypeCd(String blockTypeCd) {
        this.blockTypeCd = blockTypeCd;
    }
}

