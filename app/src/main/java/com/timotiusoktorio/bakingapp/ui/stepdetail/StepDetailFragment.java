package com.timotiusoktorio.bakingapp.ui.stepdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class StepDetailFragment extends Fragment {

    private static final String ARG_STEP_JSON = "ARG_STEP_JSON";
    private static final String ARG_STEP_POSITION = "ARG_STEP_POSITION";
    private static final String ARG_LAST_STEP_POSITION = "ARG_LAST_STEP_POSITION";
    private static final String STATE_PLAYBACK_POSITION = "STATE_PLAYBACK_POSITION";
    private static final String STATE_CURRENT_WINDOW = "STATE_CURRENT_WINDOW";
    private static final String STATE_PLAY_WHEN_READY = "STATE_PLAY_WHEN_READY";

    @BindView(R.id.exo_player_container) FrameLayout exoPlayerContainer;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.tv_short_description) TextView shortDescriptionTv;
    @BindView(R.id.tv_description) TextView descriptionTv;
    @BindView(R.id.iv_thumbnail) ImageView thumbnailIv;
    @Nullable @BindView(R.id.fab_before) FloatingActionButton beforeFab;
    @Nullable @BindView(R.id.fab_next) FloatingActionButton nextFab;

    private Unbinder unbinder;
    private Step step;
    private int stepPosition;
    private int lastStepPosition;
    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;

    public static StepDetailFragment newInstance(@NonNull String stepJson, int stepPosition, int lastStepPosition) {
        Bundle args = new Bundle();
        args.putString(ARG_STEP_JSON, stepJson);
        args.putInt(ARG_STEP_POSITION, stepPosition);
        args.putInt(ARG_LAST_STEP_POSITION, lastStepPosition);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public StepDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof StepDetailFragmentListener)) {
            throw new RuntimeException(context.getClass().getSimpleName() + " must implements StepDetailFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalStateException("No arguments sent here");
        }

        step = Step.fromJson(getArguments().getString(ARG_STEP_JSON));
        stepPosition = getArguments().getInt(ARG_STEP_POSITION);
        lastStepPosition = getArguments().getInt(ARG_LAST_STEP_POSITION);

        if (TextUtils.isEmpty(step.getVideoURL())) {
            exoPlayerContainer.setVisibility(View.GONE);
        }

        shortDescriptionTv.setText(step.getShortDescription());
        descriptionTv.setText(step.getDescription());

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.get().load(step.getThumbnailURL()).into(thumbnailIv);
            thumbnailIv.setVisibility(View.VISIBLE);
        }

        boolean twoPaneMode = getResources().getBoolean(R.bool.two_pane_mode);
        if (beforeFab != null && (stepPosition == 0 || twoPaneMode)) {
            beforeFab.setVisibility(View.GONE);
        }

        if (nextFab != null && (stepPosition == lastStepPosition || twoPaneMode)) {
            nextFab.setVisibility(View.GONE);
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23 && !TextUtils.isEmpty(step.getVideoURL())) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean immersiveMode = getResources().getBoolean(R.bool.immersive_mode);
        if (immersiveMode) {
            hideSystemUi();
        }

        if ((Build.VERSION.SDK_INT <= 23 || player == null) && !TextUtils.isEmpty(step.getVideoURL())) {
            initializePlayer();
        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
        exoPlayerView.setPlayer(player);

        player.seekTo(currentWindow, playbackPosition);
        player.setPlayWhenReady(playWhenReady);

        Uri videoUri = Uri.parse(step.getVideoURL());
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);

        player.prepare(videoSource, true, false);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        exoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(STATE_PLAYBACK_POSITION, player.getCurrentPosition());
            outState.putInt(STATE_CURRENT_WINDOW, player.getCurrentWindowIndex());
            outState.putBoolean(STATE_PLAY_WHEN_READY, player.getPlayWhenReady());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Optional
    @OnClick(R.id.fab_before)
    public void onBeforeFabClick() {
        ((StepDetailFragmentListener) requireActivity()).onNavigateBefore(stepPosition, lastStepPosition);
    }

    @Optional
    @OnClick(R.id.fab_next)
    public void onNextFabClick() {
        ((StepDetailFragmentListener) requireActivity()).onNavigateNext(stepPosition, lastStepPosition);
    }

    public interface StepDetailFragmentListener {

        void onNavigateBefore(int stepPosition, int lastStepPosition);

        void onNavigateNext(int stepPosition, int lastStepPosition);
    }
}