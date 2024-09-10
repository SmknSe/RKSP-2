package PR3.ex4;

import java.util.Arrays;

public enum FileType {
    XML("1","XML"),
    JSON("2","JSON"),
    XLS("3","XLS"),
    UNTYPED("4","UNTYPED");

    final String code;
    final String description;

    FileType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FileType getFileTypeFromCode(String code) {
        return Arrays.stream(values())
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElse(UNTYPED);
    }
}
