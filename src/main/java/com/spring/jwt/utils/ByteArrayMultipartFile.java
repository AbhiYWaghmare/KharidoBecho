package com.spring.jwt.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;

public class ByteArrayMultipartFile implements MultipartFile {

    private final byte[] bytes;
    private final String originalFilename;
    private final String contentType;

    public ByteArrayMultipartFile(byte[] bytes, String originalFilename, String contentType) {
        this.bytes = bytes;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() { return originalFilename; }

    @Override
    public String getOriginalFilename() { return originalFilename; }

    @Override
    public String getContentType() { return contentType; }

    @Override
    public boolean isEmpty() { return bytes.length == 0; }

    @Override
    public long getSize() { return bytes.length; }

    @Override
    public byte[] getBytes() { return bytes; }

    @Override
    public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (OutputStream out = new FileOutputStream(dest)) {
            out.write(bytes);
        }
    }
}
