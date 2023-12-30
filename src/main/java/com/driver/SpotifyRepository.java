package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;
    User user = new User();
    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {

        for(User curUser : users){

            if(user.getMobile().equals(mobile))
                return curUser;
        }
        User newUser = new User(name, mobile);
        users.add(newUser);

        return newUser;
    }

    public Artist createArtist(String name) {

        for(Artist curArtist : artists){

            if(curArtist.getName().equals(name))
                return curArtist;
        }
        Artist newArtist = new Artist(name);
        artists.add(newArtist);

        return newArtist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = createArtist(artistName);
        for(Album curAlbum : albums){

            if(curAlbum.getTitle().equals(title))
                return curAlbum;
        }
        Album newAlbum = new Album(title);
        albums.add(newAlbum);

        List<Album> albums1 = new ArrayList<>();

        if(artistAlbumMap.containsKey(artist)){

            albums1 = artistAlbumMap.get(artist);
        }
        albums1.add(newAlbum);
        artistAlbumMap.put(artist, albums1);

        return newAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean flag = false;
        Album album = new Album();
        for(Album curAlbum : albums){
            if(curAlbum.getTitle().equals(albumName)) {
                album = curAlbum;
                flag = true;
                break;
            }
        }

        if(flag == false){
            throw new Exception("Album does not exist");
        }
        Song newSong = new Song(title, length);
        songs.add(newSong);

        List<Song> list1 = new ArrayList<>();

        if(albumSongMap.containsKey(album)){
            list1 = albumSongMap.get(album);
        }
        list1.add(newSong);
        albumSongMap.put(album, list1);
        return newSong;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist newPlaylist = new Playlist(title);
        playlists.add(newPlaylist);

        List<Song> list1 = new ArrayList<>();
        for(Song song : songs){

            if(song.getLength() == length){
                list1.add(song);
            }
        }
        playlistSongMap.put(newPlaylist, list1);

        User user = new User();
        boolean flag = false;
        for(User curUser : users){

            if(curUser.getMobile().equals(mobile)){
                user = curUser;
                flag = true;
                break;
            }
        }
        if(flag == false){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
//        if(playlistListenerMap.containsKey(newPlaylist)){
//            userslist=playlistListenerMap.get(newPlaylist);
//        }
        userslist.add(user);
        playlistListenerMap.put(newPlaylist,userslist);

        creatorPlaylistMap.put(user, newPlaylist);

        List<Playlist> playlists1 = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            playlists1 = userPlaylistMap.get(user);
        }
        playlists1.add(newPlaylist);

        userPlaylistMap.put(user, playlists1);
        return newPlaylist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist newPlaylist = new Playlist(title);
        playlists.add(newPlaylist);

        List<Song> list1 = new ArrayList<>();
        for(Song song : songs){

            if(songTitles.contains(song.getTitle())){
                list1.add(song);
            }
        }
        playlistSongMap.put(newPlaylist, list1);

        User user = new User();
        boolean flag = false;
        for(User curUser : users){

            if(curUser.getMobile().equals(mobile)){
                user = curUser;
                flag = true;
                break;
            }
        }
        if(flag == false){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
//        if(playlistListenerMap.containsKey(newPlaylist)){
//            userslist=playlistListenerMap.get(newPlaylist);
//        }
        userslist.add(user);
        playlistListenerMap.put(newPlaylist,userslist);

        creatorPlaylistMap.put(user, newPlaylist);

        List<Playlist> playlists1 = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            playlists1 = userPlaylistMap.get(user);
        }
        playlists1.add(newPlaylist);
        userPlaylistMap.put(user, playlists1);

        return newPlaylist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        User user = null;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
                break;
            }
        }
        if(user == null)
            throw new Exception("User does not exist");

        Playlist playlist = null;
        for(Playlist playlist1:playlists){
            if(playlist1.getTitle().equals(playlistTitle)){
                playlist = playlist1;
                break;
            }
        }
        if(playlist==null)
            throw new Exception("Playlist does not exist");

        if(creatorPlaylistMap.containsKey(user))
            return playlist;

        List<User> listener = playlistListenerMap.get(playlist);
        for(User user1:listener){
            if(user1==user)
                return playlist;
        }

        listener.add(user);
        playlistListenerMap.put(playlist,listener);

        List<Playlist> playlists1 = userPlaylistMap.get(user);
        if(playlists1 == null){
            playlists1 = new ArrayList<>();
        }
        playlists1.add(playlist);
        userPlaylistMap.put(user,playlists1);

        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = null;
        for(User user1:users){
            if(user1.getMobile()==mobile){
                user=user1;
                break;
            }
        }
        if(user==null)
            throw new Exception("User does not exist");

        Song song = null;
        for(Song song1:songs){
            if(song1.getTitle()==songTitle){
                song=song1;
                break;
            }
        }
        if (song==null)
            throw new Exception("Song does not exist");

        if(songLikeMap.containsKey(song)){
            List<User> list = songLikeMap.get(song);
            if(list.contains(user)){
                return song;
            }else {
                int likes = song.getLikes() + 1;
                song.setLikes(likes);
                list.add(user);
                songLikeMap.put(song,list);

                Album album=null;
                for(Album album1:albumSongMap.keySet()){
                    List<Song> songList = albumSongMap.get(album1);
                    if(songList.contains(song)){
                        album = album1;
                        break;
                    }
                }
                Artist artist = null;
                for(Artist artist1:artistAlbumMap.keySet()){
                    List<Album> albumList = artistAlbumMap.get(artist1);
                    if (albumList.contains(album)){
                        artist = artist1;
                        break;
                    }
                }
                int likes1 = artist.getLikes() +1;
                artist.setLikes(likes1);
                artists.add(artist);
                return song;
            }
        }else {
            int likes = song.getLikes() + 1;
            song.setLikes(likes);
            List<User> list = new ArrayList<>();
            list.add(user);
            songLikeMap.put(song,list);

            Album album=null;
            for(Album album1:albumSongMap.keySet()){
                List<Song> songList = albumSongMap.get(album1);
                if(songList.contains(song)){
                    album = album1;
                    break;
                }
            }
            Artist artist = null;
            for(Artist artist1:artistAlbumMap.keySet()){
                List<Album> albumList = artistAlbumMap.get(artist1);
                if (albumList.contains(album)){
                    artist = artist1;
                    break;
                }
            }
            int likes1 = artist.getLikes() +1;
            artist.setLikes(likes1);
            artists.add(artist);

            return song;
        }
    }

    public String mostPopularArtist() {
        int max = 0;
        Artist artist1=null;

        for(Artist artist:artists){
            if(artist.getLikes()>=max){
                artist1=artist;
                max = artist.getLikes();
            }
        }
        if(artist1==null)
            return null;
        else
            return artist1.getName();
    }

    public String mostPopularSong() {
        int max = 0;
        Song song = null;

        for (Song song1 : songLikeMap.keySet()) {
            if (song1.getLikes() >= max) {
                song = song1;
                max = song1.getLikes();
            }
        }
        if (song == null)
            return null;
        else
            return song.getTitle();
    }
}
