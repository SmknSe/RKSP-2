package PR3.ex4;


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