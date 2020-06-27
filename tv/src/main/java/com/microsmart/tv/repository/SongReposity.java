package com.microsmart.tv.repository;

import com.microsmart.tv.Dao.SongDao;
import com.microsmart.tv.model.Song;
import com.microsmart.tv.util.Formant;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.Sort;

public class SongReposity implements SongDao {

    private static SongReposity INSTANCE = null;
    private Realm mRealm;

    private SongReposity() {
        this.mRealm = Realm.getDefaultInstance();
    }

    public static SongReposity getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SongReposity();

        return INSTANCE;
    }

    @Override
    public List<Song> getAllSong() {
        return mRealm.where(Song.class).findAll().sort("createdDate", Sort.DESCENDING);
    }

    @Override
    public Song getSongById(int id) {
        return mRealm.where(Song.class).equalTo("id", id).findFirst();
    }

    @Override
    public Song getSongByArtistId(int artistId) {
        return mRealm.where(Song.class).equalTo("artistId", artistId).findFirst();
    }

    @Override
    public Song getSongByAlbumId(int albumId) {
        return mRealm.where(Song.class).equalTo("albumId", albumId).findFirst();
    }

    @Override
    public void saveSong(Song song) {
        mRealm.executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Song.class).max("id");
            int nextId;
            if (currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            song.setId(nextId);
            song.setNumber(Formant.decimal(nextId));
            realm.copyToRealmOrUpdate(song);
        });
    }

    @Override
    public void updateSong(Song song) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(song);
            }
        });
    }

    @Override
    public void deleteSong(Song song) {

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Objects.requireNonNull(realm.where(Song.class).equalTo("id", song.getId())
                        .findFirst())
                        .deleteFromRealm();
            }
        });
    }
}
