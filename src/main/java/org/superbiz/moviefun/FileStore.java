package org.superbiz.moviefun;

import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;

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
//@Component
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





}
