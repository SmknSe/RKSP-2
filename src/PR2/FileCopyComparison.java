package PR2;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileCopyComparison {

    private static long memoryUsed = 0L;

    public static void main(String[] args) throws IOException {
        String sourceFile = "source.txt"; // Имя исходного файла
        String destinationFile = "destination.txt"; // Имя файла назначения
        // Создание файла размером 100 МБ
        createLargeFile(sourceFile, 100);
        // Метод 1: FileInputStream/FileOutputStream
        long startTime1 = System.currentTimeMillis();
        copyUsingFileStreams(sourceFile, destinationFile);
        long endTime1 = System.currentTimeMillis();
        printTimeAndMemoryUsage("FileInputStream/FileOutputStream", startTime1,
                endTime1);
        // Метод 2: FileChannel
        long startTime2 = System.currentTimeMillis();
        copyUsingFileChannel(sourceFile, destinationFile);
        long endTime2 = System.currentTimeMillis();
        printTimeAndMemoryUsage("FileChannel", startTime2, endTime2);
        // Метод 3: Apache Commons IO
        long startTime3 = System.currentTimeMillis();
        copyUsingApacheCommonsIO(sourceFile, destinationFile);
        long endTime3 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Apache Commons IO", startTime3, endTime3);
        // Метод 4: Files class
        long startTime4 = System.currentTimeMillis();
        copyUsingFilesClass(sourceFile, destinationFile);
        long endTime4 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Files class", startTime4, endTime4);
    }
    private static void createLargeFile(String fileName, int sizeInMB) throws
            IOException {
        byte[] data = new byte[1024 * 1024];
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            for (int i = 0; i < sizeInMB; i++) {
                fos.write(data);
            }
        }
    }
    private static void copyUsingFileStreams(String source, String destination)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
    private static void copyUsingFileChannel(String source, String destination)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            FileChannel sourceChannel = fis.getChannel();
            FileChannel destinationChannel = fos.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
            sourceChannel.close();
            destinationChannel.close();
        }
    }

    private static void copyUsingApacheCommonsIO(String source, String
            destination) throws IOException {
        File sourceFile = new File(source);
        File destFile = new File(destination);
        FileUtils.copyFile(sourceFile, destFile);
    }
    private static void copyUsingFilesClass(String source, String destination)
            throws IOException {
        Path sourcePath = Path.of(source);
        Path destinationPath = Path.of(destination);
        Files.copy(sourcePath, destinationPath,
                StandardCopyOption.REPLACE_EXISTING);
    }
    private static void printTimeAndMemoryUsage(String method, long startTime,
                                                long endTime)
    {
        long elapsedTime = endTime - startTime;
        System.out.println("Метод " + method + ":");
        System.out.println("Время выполнения: " + elapsedTime + " мс");
        Runtime runtime = Runtime.getRuntime();
        long memoryOperationUsed = memoryUsed == 0 || runtime.freeMemory() > memoryUsed
                ? runtime.totalMemory() - runtime.freeMemory()
                : memoryUsed - runtime.freeMemory();
        memoryUsed = runtime.freeMemory();
        System.out.println("Использование памяти: " + memoryOperationUsed / (1024 * 1024)
                + " МБ");
        System.out.println();
    }
}

