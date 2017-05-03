package org.superbiz.moviefun.albums;

import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.Blob;
import org.superbiz.moviefun.BlobStore;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    @Autowired
    BlobStore blobStore;

    private final AlbumsBean albumsBean;

    public AlbumsController(AlbumsBean albumsBean) {
        this.albumsBean = albumsBean;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        saveUploadToFile(uploadedFile, getCoverFile(albumId));

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
        File file = getCoverFile(albumId);
        Optional<Blob> blob =  blobStore.get(file.getName());
       // Path coverFilePath = getExistingCoverPath(albumId);
        byte[] imageBytes = IOUtils.toByteArray(blob.get().inputStream);

        HttpHeaders headers = createImageHttpHeaders(blob.get().contentType, imageBytes);

        return new HttpEntity<>(imageBytes, headers);
    }

    private void saveUploadToFile(@RequestParam("file") MultipartFile uploadedFile, File targetFile) throws IOException {

        Blob blob = new Blob(targetFile.getName(),uploadedFile.getInputStream(),uploadedFile.getContentType());
        blobStore.put(blob);
    }


    private File getCoverFile(long albumId) {
        String coverFileName = format("covers/%d", albumId);
        return new File(coverFileName);
    }

    private HttpHeaders createImageHttpHeaders(String contentType, byte[] imageBytes) throws IOException {
        //String contentType = new Tika().detect(coverFilePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(imageBytes.length);
        return headers;
    }


    private Path getExistingCoverPath(long albumId) throws URISyntaxException {
        File coverFile = getCoverFile(albumId);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }

}
