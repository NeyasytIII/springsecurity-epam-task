package com.epamtask.storege.loader.filereader;

import com.epamtask.storege.loader.filereaders.JsonFileReader;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonFileReaderTest {

    private final JsonFileReader jsonFileReader = new JsonFileReader();

    static class Dummy {
        public int id;
        public String name;
        public Dummy() {}
    }

    @Test
    void testReadFromFile_Success() throws IOException {
        String json = "[{\"id\":1,\"name\":\"A\"}, {\"id\":2,\"name\":\"B\"}]";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        List<Dummy> result = jsonFileReader.readFromFile(resource, Dummy.class);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).id);
        assertEquals("A", result.get(0).name);
    }

    @Test
    void testReadFromFile_Failure() throws IOException {
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenThrow(new IOException("Test exception"));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                jsonFileReader.readFromFile(resource, Dummy.class)
        );
        assertTrue(exception.getMessage().contains("Error reading file for Dummy"));
    }
}