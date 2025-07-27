package com.example.flow.repository;

import com.example.flow.entity.ExtensionBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExtensionBlockRepository extends JpaRepository<ExtensionBlock, Long> {
    List<ExtensionBlock> findByBlockTypeCd(String blockTypeCd);
    List<ExtensionBlock> findByBlockTypeCdAndBlockYn(String blockTypeCd, String blockYn);
    Optional<ExtensionBlock> findByExtension(String extension);
    long countByBlockTypeCdAndBlockYn(String blockTypeCd, String blockYn);
}
