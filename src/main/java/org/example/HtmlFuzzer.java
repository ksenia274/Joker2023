
package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HtmlFuzzer {
    private HtmlGenerator generator = new HtmlGenerator();
    private MutationEngine mutator = new MutationEngine();
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger failureCount = new AtomicInteger(0);

    public void fuzz(int iterations, int maxDepth, int numThreads, boolean flag) { //если флаг, то используем мутации
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < iterations; i++) {

            executor.submit(() -> {
                String html;
                if (!flag) {
                    html = generator.generateHtml(randomDepth(maxDepth));
                } else {
                    if (Math.random() < 0.5) {
                        // Генерируем новый HTML
                        html = generator.generateHtml(randomDepth(maxDepth));
                    } else {
                        // Мутируем существующий HTML из корпуса
                        String seed = initialCorpus.get((int) (Math.random() * initialCorpus.size()));
                        html = mutator.mutate(seed);
                    }
                }

                try {
                    Document doc = Jsoup.parse(html);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    saveFailure(html, e);
                }
            });

        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printReport();
    }

    private int randomDepth(int maxDepth) {
        return (int) (Math.random() * maxDepth) + 1;
    }

    private void saveFailure(String html, Exception e) {
        try (FileWriter writer = new FileWriter("failures.txt", true)) {
            writer.write("HTML:\n");
            writer.write(html);
            writer.write("\nError: " + e.getMessage() + "\n");
            writer.write("-----------------------------\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void printReport() {
        int totalIterations = successCount.get() + failureCount.get();
        double successRate = totalIterations > 0 ? (double) successCount.get() / totalIterations * 100 : 0;

        System.out.println("Fuzzing completed!");
        System.out.println("Total Iterations: " + totalIterations);
        System.out.println("Successful Parses: " + successCount.get());
        System.out.println("Failed Parses: " + failureCount.get());
        System.out.println("Success Rate: " + String.format("%.2f", successRate) + "%");
    }

    // Метод для загрузки стартового набора
    private List<String> initialCorpus = new ArrayList<>();

    private void loadInitialCorpus(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Invalid initial corpus directory.");
            return;
        }
        initialCorpus = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    initialCorpus.add(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        int iterations = 10000;
        int maxDepth = 5;
        int numThreads = 4;
        String corpusDir = null;
        boolean mutationFlag = false;

        // Check for command line arguments
        if (args.length >= 1) {
            try {
                iterations = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number for iterations, using default: " + iterations);
            }
        }
        if (args.length >= 2) {
            try {
                maxDepth = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number for max depth, using default: " + maxDepth);
            }
        }
        if (args.length >= 3) {
            try {
                numThreads = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number for number of threads, using default: " + numThreads);
            }
        }
        if (args.length >= 4) {
            corpusDir = args[3];
        }

        HtmlFuzzer fuzzer = new HtmlFuzzer();
        if (corpusDir != null) {
            fuzzer.loadInitialCorpus(corpusDir);
        }
        fuzzer.fuzz(iterations, maxDepth, numThreads, mutationFlag);

    }
}