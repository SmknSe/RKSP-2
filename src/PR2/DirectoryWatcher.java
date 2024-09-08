package PR2;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryWatcher {
    private static Map<Path, List<String>> fileContentsMap = new HashMap<>();
    public static void main(String[] args) throws IOException,
            InterruptedException {
        Path directory = Paths.get("./Zad4_Base"); // Наблюдаем здесь
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    // При создании нового файла выводим его название
                    Path filePath = (Path) event.context();
                    System.out.println("Создан новый файл: " + filePath);
                    fileContentsMap.put(filePath,
                            readLinesFromFile(directory.resolve(filePath)));
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    // При изменении файла выводим список изменений
                    Path filePath = (Path) event.context();
                    System.out.println("Файл изменен: " + filePath);
                    detectFileChanges(directory.resolve(filePath));
                }
            }
            key.reset();
        }
    }
    private static void detectFileChanges(Path filePath) throws IOException {
        List<String> newFileContents = readLinesFromFile(filePath);
        List<String> oldFileContents = fileContentsMap.get(filePath);
        if (oldFileContents != null) {
            List<String> addedLines = newFileContents.stream()
                    .filter(line -> !oldFileContents.contains(line))
                    .toList();
            List<String> deletedLines = oldFileContents.stream()
                    .filter(line -> !newFileContents.contains(line))
                    .toList();
            if (!addedLines.isEmpty()) {
                System.out.println("Добавленные строки в файле " + filePath +
                        ":");
                addedLines.forEach(line -> System.out.println("+ " + line));
            }
            if (!deletedLines.isEmpty()) {
                System.out.println("Удаленные строки из файла " + filePath +
                        ":");
                deletedLines.forEach(line -> System.out.println("- " + line));
            }
        }
        // Обновляем хранимое содержимое файла
        fileContentsMap.put(filePath, newFileContents);
    }
    private static List<String> readLinesFromFile(Path filePath) throws
            IOException, FileSystemException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}

