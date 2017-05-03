package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by pivotal on 5/3/17.
 */

public class S3Store implements BlobStore {


    private AmazonS3Client s3Client;
    private String s3BucketName;
    private final Tika tika = new Tika();

    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
    this.s3BucketName = s3BucketName;
    this.s3Client = s3Client;
    }




    @Override
    public void put(Blob blob) throws IOException {
/*
        if (!s3Client.doesObjectExist(s3BucketName)) {
           // return Optional.empty();
        }
        s3Client.createBucket(s3BucketName);
        for (Bucket bucket : s3Client.listBuckets()) {
            System.out.println(" List of Buckets ----> " + bucket.getName());
        }*/
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.contentType);

        s3Client.putObject(s3BucketName,blob.name,blob.inputStream, objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {

        if (!s3Client.doesObjectExist(s3BucketName, name)) {
            return Optional.empty();
        }

        try (S3Object s3Object = s3Client.getObject(s3BucketName, name)) {
            S3ObjectInputStream content = s3Object.getObjectContent();

            byte[] bytes = IOUtils.toByteArray(content);

            return Optional.of(new Blob(
                    name,
                    new ByteArrayInputStream(bytes),
                    tika.detect(bytes)
            ));
        }
    }

}
