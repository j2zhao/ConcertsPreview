package com.example.jinjinz.concertprev.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinjinz.concertprev.adapters.SearchRecyclerAdapter;
import com.example.jinjinz.concertprev.R;
import com.example.jinjinz.concertprev.models.Concert;

import java.util.ArrayList;


public class ConcertsFragment extends Fragment implements SearchRecyclerAdapter.SearchRecyclerAdapterListener, SwipeRefreshLayout.OnRefreshListener {
    // , SearchSuggestionsAdapter.SearchSuggestionsAdapterListener
    /*  @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return new SuggestionsCursor(constraint);
    }
*/
    public static SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onConcertTap(Concert concert) {
        concertsFragmentListener.onConcertTap(concert);
    }




    public interface ConcertsFragmentListener {
        void populateConcerts(ConcertsFragment fragment, String query);
        void onConcertTap(Concert concert);

    }

    private String queryText;
    ArrayList<Concert> concerts;
    public static SearchRecyclerAdapter searchAdapter;
    ConcertsFragmentListener concertsFragmentListener;

    public ConcertsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ConcertsFragment.
     */
    public static ConcertsFragment newInstance() {
        ConcertsFragment fragment = new ConcertsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            concertsFragmentListener = (ConcertsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnConcertsFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        concerts = new ArrayList<>();
        // Create adapter passing in activity context and concerts list
        searchAdapter = new SearchRecyclerAdapter(getActivity(), concerts, this);
        // populate view
        concertsFragmentListener.populateConcerts(this, queryText);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concerts, container, false);
        // Lookup the recyclerview in the fragment and
        RecyclerView rvConcerts = (RecyclerView) view.findViewById(R.id.rvConcerts);
        // / connect adapter to recyclerview
        rvConcerts.setAdapter(searchAdapter);
        // Set layout manager to position the items
        rvConcerts.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Find the toolbar view inside the activity layout
        setHasOptionsMenu(true);

        Toolbar tbSearch = (Toolbar) view.findViewById(R.id.tbSearch);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbSearch);
        tbSearch.setTitle("Working Title");
        // swipe refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark), // deprecated but ContextCompat.getColor() doesn't work either
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
       // menu.findItem(R.id.user_profile).setOnMenuItemClickListener(new );
        final SearchView searchView = new SearchView(getActivity());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                concertsFragmentListener.populateConcerts(ConcertsFragment.this, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void addConcerts(ArrayList<Concert> concertArrayList) {
        concerts.clear();
        searchAdapter.notifyDataSetChanged();
        concerts.addAll(concertArrayList);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        concertsFragmentListener.populateConcerts(ConcertsFragment.this, null);

    }


}
