package com.cthiebaud.mythos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import opennlp.tools.tokenize.SimpleTokenizer;

public enum Model {
    MODEL;

    final public String classPathRessource = "model.yaml";
    final private Set<ActorRecord> actorRecordSet;
    final private Map<String, ActorRecord> name2actorRecord;
    final private Set<Actor> actors;
    final private Map<String, Actor> name2actor;
    final private List<Actor> actorList;

    // initialization block
    Model() {
        try (Stream<ActorRecord> actorRecordStream = Loader.load(classPathRessource).orElseGet(Stream::empty)) {
            actorRecordSet = actorRecordStream.collect(Collectors.toSet());
            name2actorRecord = actorRecordSet.stream().collect(Collectors.toMap(
                    actor -> actor.name().toLowerCase(),
                    Function.identity()));
            actors = actorRecordSet.stream().map(Actor::new).collect(Collectors.toSet());
            name2actor = actors.stream().collect(Collectors.toMap(
                    actor -> actor.getName().toLowerCase(),
                    Function.identity()));
            actorList = new ArrayList<>(actors);
        }
    }

    public final Set<Actor> actors() {
        return Collections.unmodifiableSet(this.actors);
    }

    public final Optional<Actor> findActorByName(String actorName) {
        return Optional.ofNullable(actorName)
                .map(String::toLowerCase) // Convert actorName to lowercase
                .map(name2actor::get) // Get the Actor object corresponding to the actorName
                .flatMap(Optional::ofNullable); // Wrap the result in an Optional
    }

    public final Actor getRandomActor() {
        Random random = new Random();
        return actorList.get(random.nextInt(actorList.size()));
    }

    private String generateHtmlDescription(ActorRecord actor) {

        String inputText = actor.description();
        if (inputText == null) {
            return null;
        }
        if (inputText.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int fromIndex = 0;
        int toIndex = 0;
        List<String> tokens = List.of(SimpleTokenizer.INSTANCE.tokenize(inputText));
        for (final String token : tokens) {
            toIndex = inputText.indexOf(token, fromIndex);
            if (toIndex != -1) {
                if (fromIndex < toIndex) {
                    sb.append(inputText.substring(fromIndex, toIndex));
                    fromIndex = toIndex;
                }
                toIndex += token.length();
                if (token.equals(inputText.substring(fromIndex, toIndex))) {
                    if (!token.isEmpty() &&
                            Character.isUpperCase(token.charAt(0)) &&
                            !token.equalsIgnoreCase(actor.name()) &&
                            name2actorRecord.get(token.toLowerCase()) != null) {
                        sb.append(String.format("<a title='#%1$s'>%1$s</a>", token));
                    } else {
                        sb.append(token);
                    }
                    fromIndex = toIndex;
                } else {
                    System.err.println("This should neve be printed");
                }
            }
        }
        sb.append(inputText.substring(fromIndex, inputText.length()));

        return sb.toString();
    }

    record ActorRecord(
            @JsonProperty("name") String name,
            @JsonProperty("didascalia") String didascalia,
            @JsonProperty("description") String description) {
    }

    public final class Actor {
        private final ActorRecord actorRecord;
        private final String htmlDescription;

        public Actor(ActorRecord actorRecord) {
            if (actorRecord == null) {
                throw new IllegalArgumentException("actorRecord must be not null");
            }
            this.actorRecord = actorRecord;
            this.htmlDescription = generateHtmlDescription(actorRecord);
        }

        public String getName() {
            return this.actorRecord.name();
        }

        public String getDidascalia() {
            return this.actorRecord.didascalia();
        }

        public String getDescription() {
            return this.actorRecord.description();
        }

        public String getHtmlDescription() {
            return this.htmlDescription;
        }

        @Override
        public int hashCode() {
            return actorRecord.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return actorRecord.equals(o);
        }
    }
}
