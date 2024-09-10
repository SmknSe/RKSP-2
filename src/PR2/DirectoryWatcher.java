package PR2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectoryWatcher {
    private Map<Path, String> fileContents = new HashMap<>();
    private Path directory;
    private WatchService watchService;

    public DirectoryWatcher(Path directoryToWatch) throws IOException {
        this.directory = Path.of("src/PR2/ex4");
        this.watchService = FileSystems.getDefault().newWatchService();
        this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
    }

    public void startWatching() throws IOException, InterruptedException {
        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();
                Path filePath = directory.resolve(fileName);
                if (fileName.toString().endsWith("~")) {
                    break;
                }

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("File created: " + fileName);
                    String content = Files.readString(filePath);
                    fileContents.put(filePath, content);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("File modified: " + fileName);
                    String newContent = Files.readString(filePath);
                    String oldContent = fileContents.get(filePath);

                    if (oldContent != null) {
                        printDifferences(oldContent, newContent);
                    }

                    fileContents.put(filePath, newContent);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("File deleted: " + fileName);
                    String oldContent = fileContents.remove(filePath);

                    if (oldContent != null) {
                        byte[] contentBytes = oldContent.getBytes();
                        long fileSize = contentBytes.length;
                        var checksum = calculateChecksum(contentBytes);

                        System.out.println("Size: " + fileSize + " bytes");
                        System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath,
                                checksum);
                    }
                }
            }

            if (!key.reset()) {
                break;
            }
        }
    }

    private short calculateChecksum(byte[] contentBytes) throws IOException {
        Files.write(Path.of("temp.txt"), contentBytes);
        File f = new File("temp.txt");
        var sum = calculateChecksum(f.getPath());
        return sum;
    }

    private void printDifferences(String oldContent, String newContent) {
        String[] oldLines = oldContent.split("\n");
        String[] newLines = newContent.split("\n");

        Set<String> oldSet = new HashSet<>(Arrays.asList(oldLines));
        Set<String> newSet = new HashSet<>(Arrays.asList(newLines));

        oldSet.removeAll(newSet); // строки, которые были удалены
        System.out.println("Deleted lines:");
        for (String line : oldSet) {
            System.out.println(line);
        }

        newSet.removeAll(new HashSet<>(Arrays.asList(oldLines))); // строки, которые были добавлены
        System.out.println("Added lines:");
        for (String line : newSet) {
            System.out.println(line);
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        Path directoryToWatch = Paths.get("src/PR2/ex4");
        DirectoryWatcher watcher = new DirectoryWatcher(directoryToWatch);
        watcher.startWatching();
    }

    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get();
                }
                buffer.clear();
            }
            return checksum;
        }
    }
}