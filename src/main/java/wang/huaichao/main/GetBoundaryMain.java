package wang.huaichao.main;

import wang.huaichao.*;

import java.io.File;
import java.io.IOException;

import static wang.huaichao.ANSI_COLOR.RESET;

public class GetBoundaryMain {
    public static void main(String[] args) {

        if (args.length != 2) {
            printUsage();
            return;
        }


        final String inputDir = args[0];
        final int alphaThreshold = Integer.parseInt(args[1]);

        final PngTrimmer trimmer = new PngTrimmer();

        DirWalker.walk(
            new File(inputDir),
            new Worker<File>() {
                public void work(File file) {
                    if (!file.getName().endsWith(".png")) {
                        return;
                    }
                    try {
                        Boundary pngBoundary = trimmer.getPngBoundary(file, alphaThreshold);
                        System.out.printf(
                            "%s\n[%.4f, %.4f, %.4f, %.4f]\n",
                            file.getName(),
                            (pngBoundary.getX1() / 1.0f / pngBoundary.getOriginWidth()),
                            (pngBoundary.getY1() / 1.0f / pngBoundary.getOriginHeight()),
                            (pngBoundary.getX2() / 1.0f / pngBoundary.getOriginWidth()),
                            (pngBoundary.getY2() / 1.0f / pngBoundary.getOriginHeight())
                        );

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
                "  java wang.huaichao.main.GetBoundary <input dir> <alpha threshold>\n" +
                "example:\n" +
                "  java wang.huaichao.main.Main d:\\pngs\\ 0\n"
        );
    }
}
