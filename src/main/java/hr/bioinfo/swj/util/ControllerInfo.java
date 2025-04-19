package hr.bioinfo.swj.util;

import lombok.Getter;

// Pomoćna klasa za informacije o kontroleru
@Getter
public class ControllerInfo {
    private final String className;
    private final String methodName;
    private final String sourceFile;
    private final int lineNumber;

    public ControllerInfo(String className, String methodName, String sourceFile, int lineNumber) {
        this.className = className;
        this.methodName = methodName;
        this.sourceFile = sourceFile;
        this.lineNumber = lineNumber;
    }
}