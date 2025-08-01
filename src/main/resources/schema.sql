DROP TABLE IF EXISTS EXTENSION_BLOCK;

CREATE TABLE EXTENSION_BLOCK (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    EXTENSION VARCHAR(20) NOT NULL UNIQUE COMMENT '확장자',
    DESCRIPTION VARCHAR(100) COMMENT '설명',
    BLOCK_TYPE_CD VARCHAR(2) NOT NULL COMMENT '차단구분',
    BLOCK_YN VARCHAR(1) NOT NULL COMMENT '차단여부',
    REMARK VARCHAR(200) COMMENT '비고',
    RGT_DTM DATETIME NOT NULL COMMENT '등록일시',
    MDF_DTM DATETIME NOT NULL COMMENT '수정일시'
);
