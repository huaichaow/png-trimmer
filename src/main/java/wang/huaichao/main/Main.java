package wang.huaichao.main;

import wang.huaichao.DirWalker;
import wang.huaichao.PngTrimmer;
import wang.huaichao.Worker;

import java.io.File;
import java.io.IOException;

import static wang.huaichao.ANSI_COLOR.CYAN;
import static wang.huaichao.ANSI_COLOR.RESET;

public class Main {
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
                        System.out.println(CYAN + "trimmed " + file + RESET);
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
                "  java wang.huaichao.main.Main <trim direction> <input dir> <output dir> <alpha threshold>\n" +
                "Arguments:\n" +
                "  <trim direction>:\n" +
                "    1: left and right;\n" +
                "    2: top and bottom;\n" +
                "    3: all directions.\n" +
                "example:\n" +
                "  java wang.huaichao.main.Main 1 d:\\pngs\\ d:\\trimmed-pngs\\ 0\n"
        );
    }
}
