package org.superbiz.moviefun;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by pivotal on 5/2/17.
 */
public interface BlobStore {

    void put(Blob blob) throws IOException;

    Optional<Blob> get(String name) throws IOException, URISyntaxException;
}
