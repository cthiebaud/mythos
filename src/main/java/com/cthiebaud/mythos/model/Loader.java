package com.cthiebaud.mythos.model;

import java.io.InputStream;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cthiebaud.mythos.model.Model.ActorRecord;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class Loader {
    // Method to load and deserialize YAML data into a stream of Actor records
    static Optional<Stream<ActorRecord>> load(String filePath) {

        try {
            // Get the class loader
            ClassLoader classLoader = Loader.class.getClassLoader();

            // Load the YAML file as an input stream
            InputStream inputStream = classLoader.getResourceAsStream(filePath);

            // Create ObjectMapper to read YAML data
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Read YAML data into a MappingIterator of Actor records
            MappingIterator<ActorRecord> acterator = mapper
                    .readerFor(ActorRecord.class)
                    .readValues(inputStream);

            // Create a custom Spliterator for the MappingIterator
            Spliterator<ActorRecord> spliteractor = Spliterators
                    .spliteratorUnknownSize(acterator, Spliterator.ORDERED);

            // Convert the Spliterator to a Stream of Actor records
            Stream<ActorRecord> actorStream = StreamSupport
                    .stream(spliteractor, false);

            // Return an Optional containing the Stream of Actor records
            return Optional.of(actorStream);

        } catch (Exception e) {
            // Print an error message if loading fails
            System.err.println(
                    "Unable to load model. The most likely cause is that the resource \"" + filePath
                            + "\" was not found on the classpath.");
            // Return an empty Optional if an exception occurs
            return Optional.empty();
        }
    }
}
