import com.sun.source.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String roomName){
        super(roomName);
    }
}

class NoSuchUserException extends Exception{
    public NoSuchUserException(String userName){
        super(userName);
    }
}

class ChatRoom{
    private Set<String> users;
    private String name;

    public ChatRoom(String name){
        this.name = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username){
        users.add(username);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('\n');
        if (users.isEmpty()){
            sb.append("EMPTY").append('\n');
            return sb.toString();
        }
        users.forEach(user -> sb.append(user).append('\n'));
        return sb.toString();
    }

    public boolean hasUser(String username){
        return users.contains(username);
    }

    public int numUsers(){
        return users.size();
    }

    public Set<String> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

}

class ChatSystem{
    private Set<String> users;
    private Map<String,ChatRoom> rooms;

    public ChatSystem(){
        this.users = new TreeSet<>();
        this.rooms = new TreeMap<>();
    }

    public void addRoom(String roomName){
        rooms.putIfAbsent(roomName,new ChatRoom(roomName));
    }

    public void removeRoom(String roomname){
        rooms.remove(roomname);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!rooms.containsKey(roomName)){
            throw new NoSuchRoomException(roomName);
        }
        return rooms.get(roomName);
    }

    public void register(String userName){
        users.add(userName);
        ChatRoom room = rooms.values()
                .stream()
                .min(Comparator.comparing(ChatRoom::numUsers))
                .orElse(null);

        if (room!= null){
            room.addUser(userName);
        }


    }

    public void registerAndJoin(String userName, String roomName){
        users.add(userName);
        if (rooms.containsKey(roomName)){
            rooms.get(roomName).addUser(userName);
        }
    }

    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!users.contains(userName)){
            throw new NoSuchUserException(userName);
        }
        if (!rooms.containsKey(roomName)){
            throw new NoSuchRoomException(roomName);
        }

        rooms.get(roomName).addUser(userName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName)){
            throw new NoSuchRoomException(roomName);
        }
        if (!users.contains(userName)){
            throw new NoSuchUserException(userName);
        }
        rooms.get(roomName).removeUser(userName);
    }

    public void followFriend(String username,String friend_username) throws NoSuchUserException {
        if (!users.contains(username)){
            throw new NoSuchUserException(username);
        }
        if (!users.contains(friend_username)){
            throw new NoSuchUserException(friend_username);
        }

        rooms.values()
                .stream()
                .filter(room -> room.hasUser(friend_username))
                .forEach(room -> room.addUser(username));
    }
}

public class ChatSystemTest {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}
