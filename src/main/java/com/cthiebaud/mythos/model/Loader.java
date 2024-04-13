package com.cthiebaud.mythos.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class VerifyCloseInputStream extends InputStream {
    private InputStream inputStream;
    private boolean closed;

    public VerifyCloseInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.closed = false;
    }

    @Override
    public int read() throws IOException {
        if (closed) {
            throw new IOException("Stream is closed");
        }
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        closed = true;
        // Add additional verification logic here if needed
        System.out.println("Stream closed successfully");
    }
}

class Loader {
    // Method to load and deserialize YAML data into a stream of Actor objects
    static Optional<Stream<Model.ActorRecord>> load(String filePath) {

        ClassLoader classLoader = Loader.class.getClassLoader();

        try {
            InputStream inputStream = classLoader.getResourceAsStream(filePath);

            // Create ObjectMapper to read YAML data
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Read YAML data into a MappingIterator of Actor objects
            MappingIterator<Model.ActorRecord> actors = mapper.readerFor(Model.ActorRecord.class)
                    .readValues(inputStream);

            // Create a custom Spliterator for the MappingIterator
            Spliterator<Model.ActorRecord> spliteractors = Spliterators.spliteratorUnknownSize(actors,
                    Spliterator.ORDERED);

            // Convert the Spliterator to a Stream of Actor objects
            Stream<Model.ActorRecord> actorStream = StreamSupport.stream(spliteractors, false);

            // Return an Optional containing the Stream of Actor objects
            return Optional.of(actorStream);

        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            System.err.println(
                    "Unable to load model. The most likely cause is that the ressource \"" + filePath
                            + "\" was not found on the classpath.");
            // Return an empty Optional if an exception occurs
            return Optional.empty();
        }
    }
}
