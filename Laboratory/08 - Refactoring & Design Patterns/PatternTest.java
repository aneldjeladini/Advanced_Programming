import java.util.ArrayList;
import java.util.List;

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde
class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{title=" + title + ", artist=" + artist + "}";
    }
}

class MP3Player {
    private List<Song> songList;
    private int currentSong;
    private PlayerState state;

    private enum PlayerState {
        STOPPED, PAUSED, PLAYING
    }

    public MP3Player(List<Song> songs) {
        this.songList = songs;
        this.currentSong = 0;
        this.state = PlayerState.STOPPED;
    }

    public void pressPlay() {
        if (state == PlayerState.PLAYING) {
            System.out.println("Song is already playing");
        } else {
            state = PlayerState.PLAYING;
            System.out.println("Song " + currentSong + " is playing");
        }
    }

    public void pressStop() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
            System.out.println("Song " + currentSong + " is paused");
        } else if (state == PlayerState.PAUSED) {
            state = PlayerState.STOPPED;
            currentSong = 0;
            System.out.println("Songs are stopped");
        } else {
            System.out.println("Songs are already stopped");
        }
    }

    public void pressFWD() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
        }
        currentSong = (currentSong + 1) % songList.size();
        System.out.println("Forward...");
    }

    public void pressREW() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
        }
        currentSong = (currentSong - 1 + songList.size()) % songList.size();
        System.out.println("Reward...");
    }

    public void printCurrentSong() {
        System.out.println(songList.get(currentSong));
    }

    @Override
    public String toString() {
        return "MP3Player{currentSong = " + currentSong + ", songList = " + songList + "}";
    }
}