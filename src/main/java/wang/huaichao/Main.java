package wang.huaichao;

import java.io.File;
import java.io.IOException;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final int TrimDirX = 0x01;
    public static final int TrimDirY = 0x02;
    public static final int TrimDirAll = TrimDirX | TrimDirY;

    public static void main(String[] args) {

        if (args.length != 4) {
            printUsage();
            return;
        }


        final int direction = Integer.parseInt(args[0]);
        final String inputDir = args[1];
        final String outputDir = args[2];
        final int alphaThreshold = Integer.parseInt(args[3]);

        final PngTrimmer trimmer = new PngTrimmer();

        DirWalker.walk(
            new File(inputDir),
            new Worker<File>() {
                public void work(File file) {
                    if (!file.getName().endsWith(".png")) {
                        return;
                    }
                    try {
                        System.out.println("trimming " + file);
                        switch (direction) {
                            case TrimDirX:
                                trimmer.trimX(file, new File(outputDir, file.getName()), alphaThreshold);
                                break;
                            case TrimDirY:
                                trimmer.trimY(file, new File(outputDir, file.getName()), alphaThreshold);
                                break;
                            case TrimDirAll:
                                trimmer.trimAll(file, new File(outputDir, file.getName()), alphaThreshold);
                                break;
                        }
                        System.out.println(ANSI_CYAN + "trimmed " + file + ANSI_RESET);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    private static void printUsage() {
        System.out.println(
            "Usage: \n" +
                "  java wang.huaichao.Main <trim direction> <input dir> <output dir> <alpha threshold>\n" +
                "Arguments:\n" +
                "  <trim direction>:\n" +
                "    1: left and right;\n" +
                "    2: top and bottom;\n" +
                "    3: all directions.\n" +
                "example:\n" +
                "  java wang.huaichao.Main 1 d:\\pngs\\ d:\\trimmed-pngs\\ 0\n"
        );
    }
}
