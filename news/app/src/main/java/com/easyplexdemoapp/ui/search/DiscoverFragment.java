package com.easyplexdemoapp.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.search.SearchResponse;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.FragmentSearchBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.viewmodels.GenresViewModel;
import com.easyplexdemoapp.ui.viewmodels.SearchViewModel;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.LoadingStateController;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import timber.log.Timber;
public class  DiscoverFragment extends Fragment implements Injectable {


    FragmentSearchBinding binding;


    private AdapterSuggestionSearch mAdapterSuggestion;

    @Inject
    LoadingStateController loadingStateController;

    @Inject
    TokenManager tokenManager;

    @Inject
    SharedPreferences preferences;

    @Inject
    AuthManager authManager;

    @Inject
    SettingsManager settingsManager;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    AnimeRepository animeRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Inject
    ByGenreAdapter byGenreAdapter;


    private SearchViewModel searchViewModel;

    private GenresViewModel genresViewModel;

    private SearchAdapter searchAdapter;

    private DiscoverStyleAdapter discoverStyleAdapter;

    private List<Media> searchMoviesList;
    private TextWatcher textWatcher;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        requireActivity().setTheme(Utils.getAppTheme( requireActivity()));

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);


        binding.setController(loadingStateController);

        searchViewModel = new ViewModelProvider(this, viewModelFactory).get(SearchViewModel.class);

        genresViewModel = new ViewModelProvider(this, viewModelFactory).get(GenresViewModel.class);

        searchAdapter = new SearchAdapter();

        discoverStyleAdapter = new DiscoverStyleAdapter(mediaRepository,byGenreAdapter);

        onLoadDiscoverStyle();
        onToolbarLoad();
        setupSearchRecycleView();
        setupSuggestionSearch();
        setupSuggestedMovies();
        setupSuggestedNames();
        setsearch();
        onUseScroll();
        setHasOptionsMenu(true);





        if (settingsManager.getSettings().getSearchhistory() == 1){

            showSuggestionSearch();
        }else {

            binding.lytSuggestion.setVisibility(View.GONE);
        }


        binding.progressBar.setVisibility(View.GONE);
        binding.rvSearch.setVisibility(View.GONE);
        binding.linearMovies.setVisibility(View.GONE);
        binding.btClear.setVisibility(View.GONE);


        binding.rvGenres.setLayoutManager(new GridLayoutManager(requireActivity(), 4));
        binding.rvGenres.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        binding.rvGenres.setItemAnimator(new DefaultItemAnimator());
        binding.rvGenres.setAdapter(discoverStyleAdapter);


        searchViewModel.getSuggestedMovies();

        // Clear the results
        binding.btClear.setOnClickListener(view -> {

            if (binding.progressBar.getVisibility() == View.VISIBLE) {

                binding.progressBar.setVisibility(View.GONE);
            }

            binding.rvSearch.setVisibility(View.GONE);
            binding.etSearch.setText("");
            binding.rvSuggested.setVisibility(View.VISIBLE);
            binding.linearMovies.setVisibility(View.GONE);
            binding.linearSuggested.setVisibility(View.VISIBLE);
            binding.noResults.setVisibility(View.GONE);
            binding.btClear.setVisibility(View.GONE);
            searchViewModel.getSuggestedMovies();
            searchViewModel.movieDetailMutableLiveData.observe(getViewLifecycleOwner(), suggested -> searchAdapter.setSearch(suggested.getSuggested(),requireActivity()
                    ,settingsManager,mediaRepository,authManager));

        });





        binding.etSearch.setOnClickListener(v -> {

            if (settingsManager.getSettings().getSearchhistory() == 1){


                if (binding.lytSuggestion.getVisibility() == View.GONE){


                    showSuggestionSearch();



                }else {

                    hideSuggestionSearch();

                }

            }else {

                binding.lytSuggestion.setVisibility(View.GONE);

            }
        });





        binding.btClose.setOnClickListener(v -> binding.etSearch.performClick());


        if (settingsManager.getSettings().getDiscoverStyle() == 0){

            binding.linearGenres.setVisibility(View.GONE);
        }else {

            binding.linearGenres.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();

    }

    private void onUseScroll() {

        binding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Check if the user is scrolling down
            if (scrollY > oldScrollY) {
                // User is scrolling down, hide the suggestion layout
                binding.lytSuggestion.setVisibility(View.GONE);
            }

            // You can customize this logic based on your requirements
            // For example, you may want to hide the suggestion layout only when scrolling past a certain point
            // Adjust the conditions according to your specific use case
        });
    }

    private void hideSuggestionSearch() {

        ItemAnimation.collapse(binding.lytSuggestion);
    }

    private void setupSuggestionSearch() {


        binding.recyclerSuggestion.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerSuggestion.setHasFixedSize(true);

        //set data and list adapter suggestion
        mAdapterSuggestion = new AdapterSuggestionSearch(requireActivity());
        binding.recyclerSuggestion.setAdapter(mAdapterSuggestion);

        loadingStateController.isSuggestionsExpanded.set(mAdapterSuggestion.getItemCount() > 0);

        mAdapterSuggestion.setOnItemClickListener((view, viewModel, pos) -> {
            binding.etSearch.setText(viewModel);
            ItemAnimation.collapse(binding.lytSuggestion);
            hideKeyboard();
            setsearch();
        });
    }

    private void onLoadDiscoverStyle() {

        if (settingsManager.getSettings().getDiscoverStyle() == 1) {


            genresViewModel.getMoviesGenresList();
            genresViewModel.movieDetailMutableLiveData.observe(getViewLifecycleOwner()
                    , genres ->
                            discoverStyleAdapter.addMain(genres.getGenresPlayer(),requireActivity()));


            binding.rvSuggested.setVisibility(View.GONE);
            binding.linearSuggested.setVisibility(View.GONE);
            binding.rvGenres.setVisibility(View.VISIBLE);

        }else {


            binding.rvGenres.setVisibility(View.GONE);
            binding.rvSuggested.setVisibility(View.VISIBLE);
            binding.linearSuggested.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (Tools.checkIfHasNetwork(requireActivity())) {
            setupSuggestedMovies();
        }
    }

    private void setupSuggestedNames() {
       //
    }


    // Return Suggested Movies and Series
    private void setupSuggestedMovies() {


        binding.rvSuggested.setAdapter(searchAdapter);
        binding.rvSuggested.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvSuggested.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        binding.rvSuggested.setHasFixedSize(true);
        binding.rvSuggested.setItemViewCacheSize(8);
        searchViewModel.movieDetailMutableLiveData.observe(getViewLifecycleOwner(), suggested -> searchAdapter.setSearch(suggested.getSuggested(), requireActivity()
                , settingsManager, mediaRepository, authManager));

    }


    public Observable<String> fromView(EditText searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {

                if (c.toString().trim().length() == 0) {

                    binding.btClear.setVisibility(View.GONE);

                }else {

                    binding.btClear.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {

                //


            }


            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals("") && searchMoviesList !=null) {

                    searchMoviesList.clear();
                    binding.noResults.setVisibility(View.VISIBLE);
                    binding.linearMovies.setVisibility(View.GONE);
                    binding.btClear.setVisibility(View.GONE);


                }

                if (s.toString().isEmpty()){


                    hideKeyboard();



                }else {

                    binding.progressBar.setVisibility(View.VISIBLE);
                    subject.onNext(s.toString());

                }


            }
        });

        return subject;
    }





    // Launch the search when the user finish the typing with a Debounce time of 700 MILLISECONDS
    @SuppressLint("SetTextI18n")
    void setsearch() {

        binding.linearMovies.setVisibility(View.GONE);

        compositeDisposable.add(fromView(binding.etSearch)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter(text -> text.length() > 0)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<SearchResponse>>) query -> {
                    handleSearchHistory(query);
                    return searchViewModel
                            .search(query, settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResponse -> {
                    if (searchResponse.getSearch() != null && !searchResponse.getSearch().isEmpty()) {
                        binding.btClear.setVisibility(View.VISIBLE);
                        updateSearchResultsUI(searchResponse.getSearch());
                    } else {
                        binding.btClear.setVisibility(View.GONE);
                        updateNoResultsUI();
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    binding.noResults.setVisibility(View.VISIBLE);
                    binding.btClear.setVisibility(View.GONE); // Ensure close button is hidden on error
                }));
    }


    private void handleSearchHistory(String query) {
        if (query.length() >= 3) {
            mAdapterSuggestion.addSearchHistory(query);
        }
    }

    private void updateSearchResultsUI(List<Media> searchResults) {

        if (binding.lytSuggestion.getVisibility() == View.VISIBLE){

            binding.lytSuggestion.setVisibility(View.GONE);
        }

        binding.titlesResult.setText(getString(R.string.search_results) + " - " + searchResults.size());

        searchMoviesList = searchResults;

        binding.progressBar.setVisibility(View.GONE);
        binding.rvSearch.setVisibility(View.VISIBLE);
        binding.rvSuggested.setVisibility(View.GONE);
        binding.linearSuggested.setVisibility(View.GONE);
        binding.linearMovies.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.VISIBLE);

        searchAdapter.setSearch(searchResults, requireActivity(),
                settingsManager, mediaRepository, authManager);
        binding.noResults.setVisibility(View.GONE);
    }

    private void updateNoResultsUI() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvSearch.setVisibility(View.GONE);
        binding.rvSuggested.setVisibility(View.GONE);
        binding.linearSuggested.setVisibility(View.GONE);
        binding.linearMovies.setVisibility(View.GONE);
        binding.noResults.setVisibility(View.VISIBLE);
    }







    // Setup recycleview & Adapter for the results
    private void setupSearchRecycleView() {

        binding.rvSearch.setAdapter(searchAdapter);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvSearch.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        binding.rvSearch.setHasFixedSize(true);
        binding.rvSearch.setItemViewCacheSize(8);




    }


    private void showSuggestionSearch() {


        if (mAdapterSuggestion.getItemCount() == 0) {

            binding.lytSuggestion.setVisibility(View.GONE);

        }

        mAdapterSuggestion.refreshItems();
        ItemAnimation.expand(binding.lytSuggestion);

    }

    // Load Toolbar
    private void onToolbarLoad() {

        Tools.loadToolbar(((AppCompatActivity)requireActivity()),binding.toolbar,null);
        Tools.setSystemBarTransparent(getActivity());

    }



    // Hide Keyboard
    private void hideKeyboard() {
        View view = this.requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (binding.lytSuggestion.getVisibility() == View.VISIBLE){


            binding.lytSuggestion.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

}

