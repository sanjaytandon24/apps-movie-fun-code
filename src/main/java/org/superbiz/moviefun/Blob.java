package org.superbiz.moviefun;

import java.io.InputStream;

/**
 * Created by pivotal on 5/2/17.
 */
public class Blob {
    public final String name;
    public final InputStream inputStream;
    public final String contentType;

    public Blob(String name, InputStream inputStream, String contentType) {
        this.name = name;
        this.inputStream = inputStream;
        this.contentType = contentType;
    }
}
