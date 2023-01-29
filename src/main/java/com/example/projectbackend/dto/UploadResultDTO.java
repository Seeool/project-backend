package com.example.projectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    private String uuid;
    private String fileName;
    private String subPath;
    public String getLink() throws UnsupportedEncodingException { //JSON처리를 위함 -> link속성으로 JSON데이터가 생성됨
        String path = URLEncoder.encode(subPath + "/" + uuid + "_" + fileName, "UTF-8");
        return "http://localhost:9000/view?path=" + path;
    }
}
