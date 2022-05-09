package com.zy.fastarrival.controller;


import com.zy.fastarrival.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${fast.path}")
    private String bathPath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //临时文件
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String filename = UUID.randomUUID().toString() + suffix;

        //file是个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        File dir = new File(bathPath);
        //判断目录是否存在
        if(!dir.exists()){
            dir.mkdirs();
        }


        try {
            file.transferTo(new File(bathPath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(bathPath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器中显示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("/image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
