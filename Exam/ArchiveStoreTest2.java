import java.time.LocalDate;
import java.util.*;

class NonExistingItemException2 extends Exception{
    public NonExistingItemException2(String message){
        super(message);
    }
}

abstract class Archive2{
    private int id;
    LocalDate dateArchived;

    public Archive2(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive2 extends Archive2{
    private LocalDate dateToOpen;

    public LockedArchive2(int id, LocalDate dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
        dateArchived = null;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive2 extends Archive2{
    private int maxOpen;
    private int openCount;

    public SpecialArchive2(int id, int maxOpen){
        super(id);
        this.maxOpen = maxOpen;
        dateArchived = null;
        openCount = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void incrementOpenCount() {
        openCount++;
    }

    public boolean canOpen(){
        return openCount < maxOpen;
    }
}

class ArchiveStore2{
    List<Archive2> archives;
    Map<Integer,Archive2> archiveMap;
    List<String> logs;

    public ArchiveStore2(){
        archives = new ArrayList<>();
        archiveMap = new HashMap<>();
        logs = new ArrayList<>();
    }

    public void archiveItem(Archive2 item, LocalDate date){
        item.setDateArchived(date);
        archives.add(item);
        archiveMap.put(item.getId(),item);
        logs.add("Item " + item.getId() + " archived at " + date);
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException2 {
        if (!archiveMap.containsKey(id)){
            throw new NonExistingItemException2("Item with id " + id + " doesn't exist");
        }

        Archive2 current = archiveMap.get(id);
        if (current instanceof LockedArchive2){
            if (date.isBefore(((LockedArchive2) current).getDateToOpen())){
                logs.add("Item " + id + " cannot be opened before " + ((LockedArchive2) current).getDateToOpen());
                return;
            }
        } else if (current instanceof SpecialArchive2){
            if (!((SpecialArchive2) current).canOpen()){
                logs.add("Item " + id + " cannot be opened more than " + ((SpecialArchive2) current).getMaxOpen() + " times");
                return;
            }else{
                ((SpecialArchive2) current).incrementOpenCount();
            }
        }

        logs.add("Item " + id + " opened at " + date);
    }

    public String getLog(){
        StringBuilder sb = new StringBuilder();
        logs.forEach(log -> sb.append(log).append('\n'));
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}

public class ArchiveStoreTest2 {
    public static void main(String[] args) {
        ArchiveStore2 store = new ArchiveStore2();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            LocalDate dateToOpen = date.plusDays(days);
            LockedArchive2 lockedArchive = new LockedArchive2(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive2 specialArchive = new SpecialArchive2(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException2 e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}