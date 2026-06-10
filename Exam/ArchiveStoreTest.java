import java.util.*;

class NonExistingItemException extends Exception{
    public NonExistingItemException(String message){
        super(message);
    }
}

abstract class Archive{
    private int id;
    Date dateArchived;

    public Archive(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive extends Archive{
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
        dateArchived = null;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive{
    private int maxOpen;
    private int openCount;

    public SpecialArchive(int id, int maxOpen){
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

class ArchiveStore{
    List<Archive> archives;
    Map<Integer,Archive> archiveMap;
    List<String> logs;
    public ArchiveStore(){
        archives = new ArrayList<>();
        archiveMap = new HashMap<>();
        logs = new ArrayList<>();
    }

    public void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archives.add(item);
        archiveMap.put(item.getId(),item);
        logs.add("Item " + item.getId() + " archived at " + date);
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        if (!archiveMap.containsKey(id)){
            throw new NonExistingItemException("Item with id " + id + " doesn't exist");
        }

        Archive current = archiveMap.get(id);
        if (current instanceof LockedArchive){
            if (date.before(((LockedArchive) current).getDateToOpen())){
                logs.add("Item " + id + " cannot be opened before " + ((LockedArchive) current).getDateToOpen());
                return;
            }
        } else if (current instanceof SpecialArchive){
            if (!((SpecialArchive) current).canOpen()){
                logs.add("Item " + id + " cannot be opened more than " + ((SpecialArchive) current).getMaxOpen() + " times");
                return;
            }else{
                ((SpecialArchive) current).incrementOpenCount();
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

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.NOVEMBER, 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
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
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}




