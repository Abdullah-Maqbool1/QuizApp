package com.quizapp.service;

import com.quizapp.exception.QuestionsNotFoundException;
import com.quizapp.factory.QuestionFactory;
import com.quizapp.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads questions from the bundled resource file and builds Question objects
 * via the {@link QuestionFactory}.
 *
 * SRP: only responsible for reading + parsing the data source.
 * Exception handling: wraps low-level IO problems into a domain exception.
 */
public class QuestionLoader {

    private static final String RESOURCE = "/questions.txt";

    private final List<Question> all = new ArrayList<>();

    /** Reads and parses every question. */
    public void load() throws QuestionsNotFoundException {
        all.clear();
        try (InputStream in = QuestionLoader.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new QuestionsNotFoundException("Questions file not found: " + RESOURCE);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                int lineNo = 0;
                while ((line = reader.readLine()) != null) {
                    lineNo++;
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue; // skip blanks & comments
                    }
                    try {
                        all.add(QuestionFactory.fromLine(trimmed));
                    } catch (IllegalArgumentException ex) {
                        // Skip a bad line but keep loading the rest.
                        System.err.println("Skipping invalid line " + lineNo + ": " + ex.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new QuestionsNotFoundException("Failed to read questions file.", e);
        }

        if (all.isEmpty()) {
            throw new QuestionsNotFoundException("No valid questions were loaded.");
        }
    }

    /** Distinct categories, preserving file order. */
    public List<String> getCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (Question q : all) {
            categories.add(q.getCategory());
        }
        return new ArrayList<>(categories);
    }

    public List<Question> byCategory(String category) {
        return all.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Question> getAll() {
        return new ArrayList<>(all);
    }
}
