package PR1.Ex3;

public class File {
    private final FileType fileType;
    private final int fileSize;
    public File(FileType fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
    public FileType getFileType() {
        return fileType;
    }
    public int getFileSize() {
        return fileSize;
    }
}