package com.gavril.app.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.gavril.app.model.FileProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
//@CrossOrigin("*")
@RequestMapping("/upload")
public class MyResource {
    private final String bucketName = "elasticbeanstalk-us-east-2-633009133272";
    private final AmazonS3 s3;

    @Autowired
    public MyResource(AmazonS3 s3) {
        this.s3 = s3;
    }

    @GetMapping(path = "/api/images")
    public List<FileProps> getFilesList(HttpServletRequest request) {
        ////        List<String> list = new ArrayList<>();
        ////        Files.walk(Paths.get("."), 5).forEach(item->{
        ////            list.add(item.toString());
        ////        });

        ObjectListing ol = s3.listObjects(bucketName);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();
        List<FileProps> filesProps = new ArrayList<>();
        String baseURL = request.getHeader("host");

        List<String> existentImages = new ArrayList<>();
        objects.stream().forEach(item -> {
            if (item.getKey().toString().contains("images/")) {
                existentImages.add(item.getKey().substring(7));
            }
        });

        AtomicInteger i = new AtomicInteger(1);
        existentImages.stream().forEach(item -> {
            filesProps.add(
                    new FileProps(i.getAndIncrement(),
                            item.substring(0, item.lastIndexOf('.')),
                            item, "http://" + baseURL + "/upload/api/image/" + item));
        });

        return filesProps;
    }

    //Get 1 image from images url
    @GetMapping(path = "/api/image/{imgName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getImage(@PathVariable("imgName") String imgName) throws IOException {

        ObjectListing ol = s3.listObjects(bucketName);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();
        final String[] key = {""};

        objects.stream().forEach(item -> {
            if (item.getKey().toString().contains("images/") && item.getKey().substring(7).equals(imgName)) {
                key[0] = item.getKey();
            }
        });

        S3Object object = s3.getObject(bucketName, key[0]);
        System.out.println(object.getKey());
        InputStream inputStream = object.getObjectContent();

        byte[] image = IOUtils.toByteArray(object.getObjectContent());
        return image;
    }

    //Single file upload
    @PostMapping(
            path = "/api",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadImage(@RequestParam("file") MultipartFile file) {
        String folderNameInS3 = "images";
        String fileNameInS3 = file.getOriginalFilename();
        PutObjectRequest request = null;
        System.out.println(fileNameInS3);
        try {
            request = new PutObjectRequest(bucketName, folderNameInS3 + "/" + fileNameInS3, file.getInputStream(), new ObjectMetadata());
        } catch (IOException e) {
            e.printStackTrace();
        }
        s3.putObject(request);
    }

    //Multiple file upload
    @PostMapping(path = "/api/multiple")
    public String uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        String folderNameInS3 = "images";
        files.stream().forEach(file -> {
            String fileNameInS3 = file.getOriginalFilename();
            PutObjectRequest request = null;
            System.out.println(fileNameInS3);
            try {
                request = new PutObjectRequest(bucketName, folderNameInS3 + "/" + fileNameInS3, file.getInputStream(), new ObjectMetadata());
            } catch (IOException e) {
                e.printStackTrace();
            }
            s3.putObject(request);
        });
        return "Uploaded: " + files.size() + " files";
    }

    @PostMapping(path = "/api/deleteFiles")
    public void deleteImages() {
        ObjectListing ol = s3.listObjects(bucketName);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();

        objects.stream().forEach(item -> {
            if (item.getKey().contains("images/")) {
                s3.deleteObject(bucketName, item.getKey());
            }
        });
    }

    @PostMapping(path = "/api/deleteFile/{name}")
    public void deleteImage(@PathVariable("name") String name) {
        ObjectListing ol = s3.listObjects(bucketName);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();

        objects.stream().forEach(item -> {
            if (item.getKey().contains("images/" + name)) {
                s3.deleteObject(bucketName, item.getKey());
            }
        });
    }
}
