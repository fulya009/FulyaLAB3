package com.example.poster1019;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The one place that owns the app's movie data: all posters, the selection, and the favorites.
 */
public class PosterRepository {

    // Notified whenever favorites are added or removed (Favorite List, badge).
    public interface FavoritesListener{
        void onFavoritesChanged();
    }

    private static PosterRepository instance;

    private final List<Posters> allPosters;
    private final FavoritesStore favoritesStore;
    private final List<FavoritesListener> favoritesListeners = new ArrayList<>();

    // Creates the single instance on first use. It keeps the application context,
    // never an Activity: holding an Activity in a singleton would leak it.
    public static synchronized PosterRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PosterRepository(context.getApplicationContext());
        }
        return instance;
    }

    private PosterRepository(Context appContext) {
        allPosters = Collections.unmodifiableList(createPosters());
        favoritesStore = new FavoritesStore(appContext);

        // Forget saved favorites whose poster no longer exists in the app.
        List<String> validIds = new ArrayList<>();
        for (Posters poster : allPosters) {
            validIds.add(poster.id);
        }
        favoritesStore.retainOnly(validIds);
    }

    public List<Posters> getAllPosters(){
        return allPosters;
    }

    public Posters findById(String id){
        for(Posters posters:allPosters){
            if(posters.id.equals(id)){
                return posters;
            }
        }
        return null;
    }

    // deselects every poster
    public void clearSelection(){
        for(Posters poster : allPosters){
            poster.isSelected = false;
        }
    }

    //get favorite posters, in the order they were added
    public List<Posters> getFavorites(){
        List<Posters> favorites = new ArrayList<>();
        for (String id : favoritesStore.getIds()) {
            Posters poster = findById(id);
            if (poster != null) {
                favorites.add(poster);
            }
        }
        return favorites;
    }

    public int getFavoriteCount(){
        return favoritesStore.getIds().size();
    }

    // Adds posters to favorites, skipping ones already there.
    // Returns how many were new, so the page can show the right message.
    public int addFavorites(List<Posters> posters) {
        List<String> ids = new ArrayList<>();
        for (Posters poster : posters) {
            ids.add(poster.id);
        }
        int added = favoritesStore.addAll(ids);
        if (added > 0) {
            notifyFavoritesChanged();
        }
        return added;
    }

    // Removes one favorite. Returns true if it was a favorite.
    public boolean removeFavorite(String id) {
        boolean removed = favoritesStore.remove(id);
        if (removed) {
            notifyFavoritesChanged();
        }
        return removed;
    }

    // The listener (observer) pattern: pages register to hear about changes.
    // Whoever adds a listener must remove it again (onDestroyView / onDestroy).
    public void addFavoritesListener(FavoritesListener listener) {
        if (!favoritesListeners.contains(listener)) {
            favoritesListeners.add(listener);
        }
    }

    public void removeFavoritesListener(FavoritesListener listener) {
        favoritesListeners.remove(listener);
    }

    private void notifyFavoritesChanged() {
        for(FavoritesListener listener : new ArrayList<>(favoritesListeners)){
            listener.onFavoritesChanged();
        }
    }

    // Builds the list of five movie posters shown in the app.
    private static List<Posters> createPosters() {
        List<Posters> posters = new ArrayList<>();

        Posters p1 = new Posters();
        p1.id = "movie1";
        p1.image = R.drawable.my_poster1;
        p1.name = "The Last Ferry";
        p1.rating = 4f;
        p1.createdBy = "Elena Marsh";
        p1.story = "A night-shift captain races a storm to bring strangers home.";
        posters.add(p1);

        Posters p2 = new Posters();
        p2.id = "movie2";
        p2.image = R.drawable.my_poster2;
        p2.name = "Paper Lanterns";
        p2.rating = 5f;
        p2.createdBy = "Hiro Tanaka";
        p2.story = "Two rival chefs fall for each other over one long festival night.";
        posters.add(p2);

        Posters p3 = new Posters();
        p3.id = "movie3";
        p3.image = R.drawable.my_poster3;
        p3.name = "Dust & Diesel";
        p3.rating = 3f;
        p3.createdBy = "Marcus Vale";
        p3.story = "A retired racer gets pulled back for one last desert run.";
        posters.add(p3);

        Posters p4 = new Posters();
        p4.id = "movie4";
        p4.image = R.drawable.my_poster4;
        p4.name = "The Quiet Floor";
        p4.rating = 4f;
        p4.createdBy = "Nadia Okoye";
        p4.story = "A night janitor discovers the office is hiding something awake.";
        posters.add(p4);

        Posters p5 = new Posters();
        p5.id = "movie5";
        p5.image = R.drawable.my_poster5;
        p5.name = "Signal Lost";
        p5.rating = 5f;
        p5.createdBy = "Theo Brandt";
        p5.story = "A hiker's livestream keeps recording after she disappears.";
        posters.add(p5);

        return posters;
    }


}
