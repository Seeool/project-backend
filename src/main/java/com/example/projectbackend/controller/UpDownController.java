package com.example.projectbackend.controller;

import com.example.projectbackend.dto.UploadFileDTO;
import com.example.projectbackend.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {
    @Value("${com.example.upload.path}")
    private String mainPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(UploadFileDTO uploadFileDTO) {
        System.out.println("업로드DTO :" +uploadFileDTO);
        System.out.println("업로드DTO에서 getFiles() : "+uploadFileDTO.getFiles());
        if(uploadFileDTO.getFiles() != null) {

            List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();

                String year = String.valueOf(LocalDateTime.now().getYear());
                String month = String.valueOf(LocalDateTime.now().getMonthValue());
                String day = String.valueOf(LocalDateTime.now().getDayOfMonth());

                String subPath = year + File.separator + month + File.separator + day;

                //************폴더 생성을 위한 단계************
                File makesubPath = new File(mainPath, subPath); //콤마대신 + File.separator +로 해도 된다

                if (makesubPath.exists() == false) {
                    makesubPath.mkdirs();
                }
                //*******************************************

                String uploadPath = mainPath + File.separator + subPath;

                Path savePath = Paths.get(uploadPath,uuid+"_"+originalName ); //콤마대신 + File.separator +로 해도 된다

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .subPath(subPath)
                        .fileName(originalName)
                        .build());
            });
            return ResponseEntity.ok(list);
        }
        return null;
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewFileGet(@RequestParam("path") String fileName) {
        System.out.println(fileName);
        Resource resouce = new FileSystemResource(mainPath+File.separator+fileName);
        String resourceName = resouce.getFilename();
        System.out.println("resouceName : "+resourceName);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resouce.getFile().toPath()));
        }
        catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resouce);
    }

    @DeleteMapping("/view")
    public Map<String, Boolean> removeFile(@RequestParam("path") String fileName) {
        Resource resouce = new FileSystemResource(mainPath+File.separator+fileName);
        String resourceName = resouce.getFilename();
        System.out.println("resouceName : "+resourceName);
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resouce.getFile().toPath());
            removed = resouce.getFile().delete();

            if(contentType.startsWith("image")) {
                File thumbnailFile = new File(mainPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
        resultMap.put("result",removed);
        return resultMap;
    }
}
