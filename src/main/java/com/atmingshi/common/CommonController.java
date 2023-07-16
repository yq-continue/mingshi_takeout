package com.atmingshi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author yang
 * @create 2023-07-14 15:47
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${directory.location}")
    private String location;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> fileUpload(MultipartFile file){
        //修改文件保存名字
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //通过配置文件传输文件保存的位置
        File fileLocation = new File(location);
        //判断文件夹是否存在，若不存在则创建文件夹
        if (!fileLocation.exists()){
            fileLocation.mkdirs();
        }
        //将文件存储到指定的位置
        try {
            file.transferTo(new File(location + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.返回结果，包含文件的名字，方便调用
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //使用输入流读取文件
            inputStream = new FileInputStream(new File(location + name));
            //创建输出流
            outputStream = response.getOutputStream();
            //添加返回的数据类型
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len ;
            while ((len = inputStream.read(bytes)) != -1){
                //使用输出流将文件写回页面
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
