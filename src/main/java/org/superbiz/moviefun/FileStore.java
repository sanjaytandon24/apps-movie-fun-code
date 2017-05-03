package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;

/**
 * Created by pivotal on 5/2/17.
 */
@Component
public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {

        byte[] bytes = IOUtils.toByteArray(blob.inputStream);

        try (FileOutputStream outputStream = new FileOutputStream(blob.name)) {
            outputStream.write(bytes);

        }

    }
    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {


        System.out.print(name);
        Path coverFilePath = null;
        File coverFile = new File(name);


        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else
            {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }
       Optional<Blob> blob  = Optional.of(new Blob(name, new FileInputStream(coverFilePath.toFile()),new Tika().detect(coverFilePath)));
        return blob;
    }





   /* @Value("${s3.endpointUrl}") String s3EndpointUrl;
    @Value("${s3.accessKey}") String s3AccessKey;
    @Value("${s3.secretKey}") String s3SecretKey;
    @Value("${s3.bucketName}") String s3BucketName;

    @Bean
    public BlobStore blobStore() {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        s3Client.setEndpoint(s3EndpointUrl);

        return null;//new s3Store(s3Client, s3BucketName);
    }*/
}
