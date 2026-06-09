import java.util.*;

class IndentPrinter {
    public static String get(int level) {
        return "    ".repeat(level);
    }
}

interface IFile {
    String getFileName();
    String getContent(int level);
}

class File implements IFile {
    private String fileName;

    public File(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getContent(int level) {
        return String.format("%s%s\n", IndentPrinter.get(level), fileName);
    }
}

class Folder implements IFile {
    String folderName;
    List<IFile> children;

    public Folder(String folderName) {
        this.folderName = folderName;
        this.children = new ArrayList<>();
    }

    @Override
    public String getFileName() {
        return folderName;
    }

    public void addObject(String[] parts, int depth) {
        Folder current = this;

        for (int i = depth; i < parts.length - 1; i++) {
            String folderName = parts[i];

            Folder subfolder = current.children.stream()
                    .filter(c -> c instanceof Folder && c.getFileName().equals(folderName))
                    .map(c -> (Folder) c)
                    .findFirst()
                    .orElse(null);

            if (subfolder == null) {
                subfolder = new Folder(folderName);
                current.children.add(subfolder);
            }

            current = subfolder;
        }

        current.children.add(new File(parts[parts.length - 1]));
    }


    public boolean removeObject(String[] parts, int depth) {
        if (depth == parts.length - 1) {
            // Last segment → remove the matching file
            children.removeIf(c -> c instanceof File && c.getFileName().equals(parts[depth]));
        } else {
            String folderName = parts[depth];
            children.stream()
                    .filter(c -> c instanceof Folder && c.getFileName().equals(folderName))
                    .map(c -> (Folder) c)
                    .findFirst()
                    .ifPresent(subfolder -> {
                        boolean isEmpty = subfolder.removeObject(parts, depth + 1);
                        if (isEmpty) {
                            children.remove(subfolder);
                        }
                    });
        }

        return children.isEmpty();
    }

    @Override
    public String getContent(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%s/\n", IndentPrinter.get(level), folderName));
        for (IFile child : children) {
            sb.append(child.getContent(level + 1));
        }
        return sb.toString();
    }
}

class Bucket {
    private String name;
    private Folder root;

    public Bucket(String name) {
        this.name = name;
        this.root = new Folder(name);
    }

    public void addObject(String key) {
        // e.g. "docs/2024/exam1.pdf" → ["docs", "2024", "exam1.pdf"]
        String[] parts = key.split("/");
        root.addObject(parts, 0);
    }

    public void removeObject(String key) {
        String[] parts = key.split("/");
        root.removeObject(parts, 0);
    }

    @Override
    public String toString() {
        return root.getContent(0);
    }
}

public class BucketTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bucket bucket = new Bucket("bucket");

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0];

            if (command.equalsIgnoreCase("ADD")) {
                bucket.addObject(parts[1]);
            } else if (command.equalsIgnoreCase("REMOVE")) {
                bucket.removeObject(parts[1]);
            } else if (command.equalsIgnoreCase("PRINT")) {
                System.out.print(bucket);
            }
        }
    }
}