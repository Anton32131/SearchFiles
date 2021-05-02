import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainSearch extends SimpleFileVisitor<Path> {
    private String directory;
    private ArrayList<Path> listFiles = new ArrayList<Path>();
    private String resultPath;


    public MainSearch(String directory) {
        this.directory = directory;
        this.resultPath = directory + "\\result.txt";
    }

    public static void main(String[] args) throws Exception {

        MainSearch mainSearch;
        if (args.length != 0) {
            mainSearch = new MainSearch(args[0]);
        } else {
            throw new Exception("Directory name not passed");
        }
        try {
            Files.walkFileTree(Paths.get(mainSearch.directory), mainSearch);
            mainSearch.sort();
            mainSearch.writeFiles();
            System.out.println(String.format("Found text files : %d, the data from which was recorded " +
                    "in file: %s", mainSearch.listFiles.size(), mainSearch.resultPath));
        } catch (IOException e) {
            System.out.println("Directory not found.Please try again");
            e.printStackTrace();
        }
    }

    public void writeFiles() {
        try {
            PrintWriter print = new PrintWriter(resultPath, "windows-1251");
            for (Path file : listFiles) {
                try (BufferedReader br = Files.newBufferedReader(file, Charset.forName("windows-1251"))) {
                    while (br.ready()) {
                        print.write(br.readLine());
                        print.println();
                    }
                }
            }
            print.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sort() {
        Collections.sort(listFiles, Comparator.comparing(Path::getFileName));
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        if (file.getFileName().toString().endsWith("txt")) {
            if (!file.getFileName().toString().endsWith("result.txt")) {
                listFiles.add(file);
            }
        }
        return FileVisitResult.CONTINUE;
    }
}
