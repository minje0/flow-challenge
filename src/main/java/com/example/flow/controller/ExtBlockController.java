package com.example.flow.controller;

import com.example.flow.dto.ExtensionDto;
import com.example.flow.service.ExtBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtBlockController {
    private final ExtBlockService extBlockService;

    @GetMapping("/")
    public String viewExtBlockPage(Model model) {
        List<ExtensionDto> fixedExtensionList = extBlockService.getFixedExtensionList();
        List<ExtensionDto> customExtensionList = extBlockService.getCustomExtensionList();
        List<ExtensionDto> customFixedExtensionList = extBlockService.getCustomFixedExtensionList();

        log.debug("fixedExtensionList : {}", fixedExtensionList);
        log.debug("customExtensionList : {}", customExtensionList);
        log.debug("customFixedExtensionList : {}", customFixedExtensionList);

        model.addAttribute("fixedExtList", fixedExtensionList);
        model.addAttribute("customExtList", customExtensionList);
        model.addAttribute("customFixedExtList", customFixedExtensionList);

        return "index";
    }

    @PostMapping("/fixed/update")
    @ResponseBody
    public ResponseEntity<?> updateFixedExtension(@RequestBody ExtensionDto inDto) {
        log.debug("updateFixedExtension inDto : {}", inDto);
        try {
            extBlockService.updateFixedExtension(inDto.getExtension(), inDto.getBlockYn());
            return ResponseEntity.ok(Map.of("message", "차단 설정이 저장되었습니다."));
        } catch (Exception e) {
            log.debug("updateFixedExtension error", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "고정된 확장자 차단 설정에 실패하였습니다."));
        }
    }

    @PostMapping("/custom/add")
    @ResponseBody
    public ResponseEntity<?> addCustomExtension(@RequestBody ExtensionDto inDto) {
        log.debug("addCustomExtension inDto : {}", inDto);
        try {
            ExtensionDto outDto = extBlockService.addCustomExtension(inDto);
            return ResponseEntity.ok(outDto);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/custom/remove")
    @ResponseBody
    public ResponseEntity<?> removeCustomExtension(@RequestBody ExtensionDto inDto) {
        log.debug("removeCustomExtension inDto : {}", inDto);
        try {
            extBlockService.removeCustomExtension(inDto.getExtension());
            return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/fixed/add")
    public ResponseEntity<?> convertToFixed(@RequestBody ExtensionDto inDto) {
        log.debug("convertToFixed inDto : {}", inDto);
        try {
            ExtensionDto outDto = extBlockService.convertCustomToFixed(inDto);
            return ResponseEntity.ok(outDto); // ✅ extension + description 포함
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }


}
