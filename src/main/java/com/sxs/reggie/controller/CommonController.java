package com.sxs.reggie.controller;

import com.sxs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author sxs
 * @create 2022-08-22 20:49
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
//        log.info("文件名称：{}", file.getOriginalFilename());

        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString()+suffix;
        try {
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(basePath + name));
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len=0;
            while ((len=bis.read(bytes))!=-1){
                os.write(bytes, 0, len);
                os.flush();
            }
            os.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
