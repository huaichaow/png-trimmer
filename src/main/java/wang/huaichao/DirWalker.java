package wang.huaichao;

import java.io.File;

public class DirWalker {
    public static void walk(File start, Worker<File> worker) {
        if (start == null || !start.exists()) {
            return;
        } else if (start.isFile()) {
            worker.work(start);
        } else {
            File[] files = start.listFiles();
            for (File file : files) {
                walk(file, worker);
            }
        }
    }
}
