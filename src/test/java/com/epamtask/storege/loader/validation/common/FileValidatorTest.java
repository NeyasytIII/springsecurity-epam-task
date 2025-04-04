package com.epamtask.storege.loader.validation.common;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    private static class DummyResource implements Resource {
        private final boolean exists;
        private final byte[] data;

        public DummyResource(boolean exists, byte[] data) {
            this.exists = exists;
            this.data = data;
        }

        @Override
        public boolean exists() {
            return exists;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(data);
        }

        @Override
        public String getDescription() {
            return "DummyResource";
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public URL getURL() throws IOException {
            return new URL("file://dummy");
        }

        @Override
        public URI getURI() throws IOException {
            return URI.create("file://dummy");
        }

        @Override
        public File getFile() throws IOException {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public long contentLength() throws IOException {
            return data.length;
        }

        @Override
        public long lastModified() throws IOException {
            return 0;
        }

        @Override
        public Resource createRelative(String relativePath) throws IOException {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public String getFilename() {
            return "dummy.txt";
        }
    }

    @Test
    void validFile() {
        Resource resource = new DummyResource(true, "data".getBytes());
        assertTrue(FileValidator.isFileValid(resource, "dummy.txt"));
    }

    @Test
    void nonExistingFile() {
        Resource resource = new DummyResource(false, "data".getBytes());
        assertFalse(FileValidator.isFileValid(resource, "dummy.txt"));
    }

    @Test
    void emptyFile() {
        Resource resource = new DummyResource(true, new byte[0]);
        assertFalse(FileValidator.isFileValid(resource, "dummy.txt"));
    }
}