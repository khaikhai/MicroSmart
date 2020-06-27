package com.microsmart.tv;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.microsmart.tv.model.Album;
import com.microsmart.tv.model.Artist;
import com.microsmart.tv.model.Song;
import com.microsmart.tv.server.WebServer;
import com.microsmart.tv.util.Formant;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;


import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MicroSmart extends Application {
    private Realm realm;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("song.realm").build();
//        Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        /*RealmAsyncTask realmAsyncTask = realm.executeTransactionAsync(realm -> {
            Number songId = realm.where(Song.class).max("id");
            Number artistId = realm.where(Artist.class).max("id");
            Number albumId = realm.where(Album.class).max("id");
            int nextSongId;
            int nextArtistId;
            int nextAlbumId;
            if (songId == null && artistId == null && albumId == null) {
                nextSongId = 1;
                nextArtistId = 1;
                nextAlbumId = 1;
            } else {
                nextSongId = songId.intValue() + 1;
                nextArtistId = artistId.intValue() + 1;
                nextAlbumId = albumId.intValue() + 1;
            }
            Artist artist = realm.createObject(Artist.class, nextArtistId);
            artist.setName("Myo Gyi");
            artist.setPlayCount(0);
            artist.setCreatedDate(new Date());

            Album album = realm.createObject(Album.class, nextAlbumId);
            album.setAlbumName("You Like");
            album.setPlayCount(0);
            album.setCreateDate(new Date());

            Song song = realm.createObject(Song.class, nextSongId);
            song.setAlbum(album);
            song.artists.add(artist);
            song.setTitle("Take It");
            song.setNumber(Formant.decimal(nextSongId));
            song.setPlayCount(0);
            song.setPath("asdlflaskdflkas");
            song.setCreateDate(new Date());

        }, () -> Log.d("Song:", "song data insert Success"), error -> Log.d("Song:", "data insert error" + error.getMessage()));
        fectchData();*/
        try {
            new WebServer(realm.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fectchData() {
        final RealmResults<Song> realmResults = realm.where(Song.class).findAll();
        System.out.println(realmResults.asJSON());
        Log.i("Realm", realm.getPath());

    }
}
