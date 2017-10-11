package com.comquas.mahar.Activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.comquas.mahar.Adapter.RelatedMovieAdapter;
import com.comquas.mahar.Client.APIInterface;
import com.comquas.mahar.Client.ApiClient;
import com.comquas.mahar.Client.Constants;
import com.comquas.mahar.Data.CheckDownloadRequest;
import com.comquas.mahar.Data.ContentDetails;
import com.comquas.mahar.Data.DeleteFav;
import com.comquas.mahar.Data.DownloadLinks;
import com.comquas.mahar.Data.DownloadedItems;
import com.comquas.mahar.Data.Favorites;
import com.comquas.mahar.Data.Invoice;
import com.comquas.mahar.Data.RealmDownloadData;
import com.comquas.mahar.Data.RelatedMovies;
import com.comquas.mahar.Data.TransactionLinkData;
import com.comquas.mahar.Database.DBHandler;
import com.comquas.mahar.Database.FavoritesContents;
import com.comquas.mahar.Fragment.RelatedMovie_fragment;
import com.comquas.mahar.Preference.Session;
import com.comquas.mahar.R;
import com.comquas.mahar.manager.MyDownloadManagerActivity;
import com.comquas.mahar.utils.CheckInternet;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by comquas on 8/1/17.
 */

public class MovieDetails extends AppCompatActivity implements View.OnClickListener, RelatedMovieAdapter.itemClickListener {
    public static final String CONTENT_ID = "id";
    private static final String TAG = "MovieDetails.this";
    String laravel = "https://raw.githubusercontent.com/hienhtet/mahar/master/laravel.aes";
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;
    static Realm realm;
    ImageView rootView, poster;
    ImageView fav, playIcon, shareIcon, restore, saveFavChange;


    public static ImageView downIcon;
    //    TextView

    TextView movieTitle, directorText, actorText, codeText, lookText, dotTv;
    TextView castTv, dirTv, codeTv, title, downLoadCountTv;

    Toolbar tool;
    public static final String MOVIE_PATH = "path";
    static String path;

    RelatedMovieAdapter relatedMovieAdapter;

    //    DownloadAdapter adpter;
    boolean hasTrailer;
    String trailerLink;

    //DownloadController downloadController;

    private List<Intent> openedIntents;

    List<ContentDetails.Actor> actorList;

    // view Group

    ViewGroup detail;
    ProgressBar bar;
    ProgressBar dBar;

    public static GifImageView gifIv;

    //expanadle text view
    ExpandableTextView expTv1;

    RecyclerView relatedRv;

    Session session;

    TextView titleTv;
    String mTitleEn;

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;


    private static final int REQUEST_WRITE_STORAGE = 112;


    ImageView pause;

    static String movieName;
    boolean isHide;

    int downloadId;
    String photoUrl;
    String mTitle;

    //movie id

    int contentID;

    String mPath;
    String contentId;
    ArrayList<RelatedMovies> relatedMoviesArrayList;

    DBHandler dbHandler;


    @BindView(R.id.trailer_group)
    ViewGroup trailerG;

    String downloadMoviePaht;
    String downloadMovieName;

    ArrayList<DownloadedItems> checkDownloadedItem;
    FrameLayout downloadIconGroup;

    String savePath;
    String photoFilePath;
    String mmPrice;

    CallbackManager callbackManager;
    private LoginManager manager;
    @BindView(R.id.movie_price)
    TextView mPrice;
    @BindView(R.id.downloadgroup)
    ViewGroup downloadViewgroup;
    int posterWidth;
    int posterheight;
    List<String> dirName;
    String price;
    ProgressDialog progressDialog;

    @BindView(R.id.buy_btn)
    Button buyBtn;
    boolean checkDownloadLink;
    String orderNoToken;
    String downloadLink;
    String downloadErrorMes;

    @BindView(R.id.look)
    TextView lookCount;

    @BindView(R.id.look_time)
    TextView lookTime;

    @BindView(R.id.expandable_text)
    TextView expandableTv;

    int expandableTextCount;

    String currentMoviePath;

    @BindView(R.id.viewCount_container)
    ViewGroup viewCountContainer;


    ImageView downloadedPlay;
    TextView downloaded_textview, downloaded_play;
    SimpleDateFormat simpleDateFormat;
    DownloadBean downloadBean;

    private RxDownload mRxDownload;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_collap);

        mRxDownload = RxDownload.getInstance(this);


        Hawk.init(this).build();
        FacebookSdk.sdkInitialize(getApplicationContext());
        ButterKnife.bind(this);
        dbHandler = new DBHandler(this);
        realm = Realm.getDefaultInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        dirName = new ArrayList<>();

        collapsingToolbar.setTitle(null);
        progressDialog = new ProgressDialog(MovieDetails.this);

        appbarAnimate();
        checkDownloadedItem = new ArrayList<>();

        mPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1" + File.separator +
                downloadMovieName;
        rootView = (ImageView) findViewById(R.id.blurView);
        isHide = false;

        getData();

        initView();
        posterWidth = poster.getLayoutParams().width;
        posterheight = poster.getLayoutParams().height;
        poster.requestLayout();

        if (path != null) {
            if (path.contains("/")) {
                Picasso.with(this).load(path).resize(356, 498).into(poster);
            } else {
                Picasso.with(this).load(Constants.BASE_URL_DEV + path).resize(356, 498).into(poster);

            }
        }


        loadMovieCongtent(contentId);


    }


    private void appbarAnimate() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                boolean appBarExpanded;


                int currentScrlPercentage = (Math.abs(verticalOffset)) * 100;


                if (Math.abs(verticalOffset) > 100) {
                    appBarExpanded = false;
                    //  Log.e("aaa", " layout width " + posterWidth + " poster height " + posterheight);
                    movieTitle.setVisibility(View.GONE);
                    poster.getLayoutParams().width = posterWidth - 200;
                    poster.getLayoutParams().height = posterheight - 300;
                    poster.requestLayout();
                    collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

                    if (session.getLanguage()) {
                        if (MDetect.INSTANCE.isUnicode()) {
                            collapsingToolbar.setTitle(mTitle);
                        } else {
                            collapsingToolbar.setTitle(Rabbit.uni2zg(mTitle));
                        }
                    } else {
                        collapsingToolbar.setTitle(mTitleEn);

                    }
                    movieTitle.setVisibility(View.GONE);
                } else {
                    appBarExpanded = true;
                    collapsingToolbar.setTitle(null);
                    poster.getLayoutParams().width = posterWidth;
                    poster.getLayoutParams().height = posterheight;
                    poster.requestLayout();
                    movieTitle.setVisibility(View.VISIBLE);


                }


                invalidateOptionsMenu();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        // callbackManager.onActivityResult(requestCode, responseCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    String defaultAuthHeader;
    TagContainerLayout mTagContainerLayout;
    TagContainerLayout directorTag;

    List<ContentDetails.Director> directorList;

    private void loadMovieCongtent(final String contentId) {
        Log.e("asdf", " id " + contentId);


        String userName = session.authUserName();
        String password = session.authPassowrd();

        String base = userName + ":" + password;

        defaultAuthHeader = "Basic YW5kcm9pZDphbmRyb2lkYXBwQG1haGFy";

        String authHeader = "Basic" + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Log.e("asdf", " sessoin" + session.getSessionToekn());


        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Call<ContentDetails> call = apiInterface.getMovieContents(defaultAuthHeader, session.getSessionToekn(), contentId);
        call.enqueue(new Callback<ContentDetails>() {
            @Override
            public void onResponse(Call<ContentDetails> call, Response<ContentDetails> response) {
                if (response.body() != null) {

                    bar.setVisibility(View.GONE);
                    detail.setVisibility(View.VISIBLE);


                    if (response.body().trailer != null && response.body().trailer.length() != 0) {
                        hasTrailer = true;
                        trailerLink = response.body().trailer;
                    }

                    price = response.body().price;
                    List<String> list = new ArrayList<String>();
                    if (response.body().actor != null) {

                        final int colorPrimary = ContextCompat.getColor(MovieDetails.this, R.color.white_text);
                        int actorSize = response.body().actor.size();

                        actorList = (response.body().actor);

                        for (int i = 0; i < actorSize; i++) {

                            if (session.getLanguage()) {
                                if (MDetect.INSTANCE.isUnicode()) {
                                    list.add(actorList.get(i).name_my);
                                    expandableTv.setText(response.body().description_my);


                                } else {
                                    list.add(Rabbit.uni2zg(actorList.get(i).name_my));
                                    expandableTv.setText(Rabbit.uni2zg(response.body().description_my));

                                }
                            } else {
                                list.add(actorList.get(i).name_en);
                                expandableTv.setText(response.body().description_en);
                            }
                        }

                    } else {
                        Log.i("actorList", "Null");
                    }
                    mTagContainerLayout.setTags(list);

                    mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                        @Override
                        public void onTagClick(int position, String text) {

                            Intent actorContent = new Intent(MovieDetails.this, ActorContentActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putInt(Constants.ACTOR_ID, actorList.get(position).id);
                            if (session.getLanguage()) {
                                mBundle.putString(Constants.ACTOR_NAME, actorList.get(position).name_my);

                            } else {
                                mBundle.putString(Constants.ACTOR_NAME_EN
                                        , actorList.get(position).name_en);

                            }
                            actorContent.putExtras(mBundle);
                            startActivity(actorContent);

                            //Toast.makeText(MovieDetails.this, "" + actorList.get(position).id + " name " + actorList.get(position).name_my, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTagLongClick(final int position, String text) {
                            // ...
                        }

                        @Override
                        public void onTagCrossClick(int position) {
                            // ...
                        }
                    });

                    directorList = new ArrayList<ContentDetails.Director>();
                    List<String> dirNameEn = new ArrayList<String>();
                    if (response.body().director != null) {
                        directorList.addAll(response.body().director);
                        int dirSize = directorList.size();
                        for (int i = 0; i < dirSize; i++) {
                            if (session.getLanguage()) {
                                if (MDetect.INSTANCE.isUnicode()) {
                                    dirName.add(directorList.get(i).name_my);

                                } else {
                                    dirName.add(Rabbit.uni2zg(directorList.get(i).name_my));
                                }
                            } else {
                                dirName.add(directorList.get(i).name_en);
                            }
                        }
                    }
                    directorTag.setTags(dirName);
                    directorTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                        @Override
                        public void onTagClick(int position, String text) {


                            Intent actorContent = new Intent(MovieDetails.this, ActorContentActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putInt(Constants.ACTOR_ID, directorList.get(position).id);
                            if (session.getLanguage()) {
                                mBundle.putString(Constants.ACTOR_NAME, directorList.get(position).name_my);

                            } else {
                                mBundle.putString(Constants.ACTOR_NAME_EN
                                        , directorList.get(position).name_en);
                            }
                            actorContent.putExtras(mBundle);
                            startActivity(actorContent);

                            // Toast.makeText(MovieDetails.this, text, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTagLongClick(int position, String text) {

                        }

                        @Override
                        public void onTagCrossClick(int position) {

                        }
                    });

                    if (Hawk.contains(Constants.DOWNLOADED_ITEM)) {
                        checkDownloadedItem = Hawk.get(Constants.DOWNLOADED_ITEM);
                        Log.e("hawk contain", "");

                    }

                    //checkdownloadedVideo(response.body().id);


                    int size = checkDownloadedItem.size();
                    for (int i = 0; i < size; i++) {
                        if (checkDownloadedItem.get(i).getId().equals(String.valueOf(response.body().id))) {
                            downIcon.setVisibility(View.GONE);
                            currentMoviePath = checkDownloadedItem.get(i).getDownloadMoviesPath();
                            downloadedPlay.setVisibility(View.VISIBLE);
                            downloaded_textview.setVisibility(View.GONE);
                            downloaded_play.setVisibility(View.VISIBLE);
                            trailerG.setVisibility(View.GONE);

                        }

                    }

                    if (downloadLink != null) {

                        Log.e("cccccc", downloadLink);
                    }

                    RxDownload.getInstance(MovieDetails.this)
                            .receiveDownloadStatus(downloadLink)
                            .subscribe(new Consumer<DownloadEvent>() {
                                @Override
                                public void accept(DownloadEvent downloadEvent) throws Exception {
                                    if (downloadEvent.getFlag() == DownloadFlag.STARTED) {
                                        // Log.e("asdf", "downloading");
                                        gifIv.setVisibility(View.VISIBLE);
                                        downIcon.setVisibility(View.GONE);
                                    } else {
                                        downIcon.setVisibility(View.VISIBLE);
                                        gifIv.setVisibility(View.GONE);
                                        //Log.e("asdf", " downloaded");
                                    }
                                }
                            });

                    mTitleEn = response.body().title_en;

                    // for save id

                    photoUrl = response.body().thumbnail_url;


                    mTitle = response.body().title_my;
                    contentID = response.body().id;


                    savePath = getApplicationContext().getExternalCacheDir().getAbsolutePath() + File.separator + String.valueOf(contentID);
                    photoFilePath = savePath + "/" + String.valueOf(contentID) + ".hs";

                    downloadMovieName = response.body().title_en;
                    downloadMoviePaht = response.body().download_medium_url;

                    mPrice.setText("" + response.body().mm_price + " MMK");

                    mmPrice = response.body().mm_price;
//                    if (mmPrice.equals("0") || price.equals("0")) {
//                        buyBtn.setVisibility(View.GONE);
//                    } else {
//                        buyBtn.setVisibility(View.VISIBLE);
//                    }


                    if (mmPrice.equals("0") || mmPrice == "0") {
                        viewCountContainer.setVisibility(View.VISIBLE);
                        if (session.getLanguage()) {
                            if (MDetect.INSTANCE.isUnicode()) {
                                mPrice.setText("အခမဲ့");

                            } else {

                                mPrice.setText(Rabbit.uni2zg("အခမဲ့"));

                            }
                        } else {
                            mPrice.setText("Free");
                        }
                    } else {
                        viewCountContainer.setVisibility(View.GONE);
                    }


                    if (bar.getVisibility() == View.VISIBLE) {
                        bar.setVisibility(View.GONE);
                    }


                    if (path == null) {
                        Picasso.with(MovieDetails.this).load(response.body().thumbnail_url).into(poster);


                        Picasso.with(MovieDetails.this)
                                .load(response.body().thumbnail_url)
                                .placeholder(R.mipmap.ic_launcher).into(rootView);


                    }

                    if (session.getLanguage()) {
                        movieName = response.body().title_my;
                        lookTime.setText(response.body().rating_average + "");

                        if (MDetect.INSTANCE.isUnicode()) {
                            movieTitle.setText(response.body().title_my);
//                            actorText.setText(response.body().cast_my);
//                            directorText.setText(response.body().director_my);
                            codeText.setText(response.body().id + "");
                            expTv1.setText(response.body().description_my);
                            expandableTv.setText(response.body().description_my);
                        } else {
                            movieTitle.setText(Rabbit.uni2zg(response.body().title_my));
//                            actorText.setText(Rabbit.uni2zg(response.body().cast_my));
//                            directorText.setText(Rabbit.uni2zg(response.body().director_my));
                            codeText.setText(Rabbit.uni2zg(response.body().id + ""));
                            lookText.setText(Rabbit.uni2zg(response.body().duration + ""));
                            expTv1.setText(Rabbit.uni2zg(response.body().description_my));
                            expandableTv.setText(Rabbit.uni2zg(response.body().description_my));
                            expandableTv.post(new Runnable() {
                                @Override
                                public void run() {
                                    expandableTextCount = expandableTv.getLineCount();
                                    Log.e("line count ", expandableTextCount + " line hight " + expandableTv.getLineHeight());

                                }
                            });


                        }

                    } else {
                        if (MDetect.INSTANCE.isUnicode()) {
                            movieName = response.body().title_en;
                            movieTitle.setText(response.body().title_en);
                            codeText.setText(response.body().id + "");
                            expTv1.setText(response.body().description_en);

                        } else {
                            expTv1.setText(Rabbit.uni2zg(response.body().description_en));

                        }

                        bar.setVisibility(View.GONE);
                        detail.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContentDetails> call, Throwable t) {
                Log.e("asdf", "error  error " + t.getMessage());
            }
        });

    }


    private void getData() {

        if (getIntent().getStringExtra(MOVIE_PATH) != null) {
            path = getIntent().getStringExtra(MOVIE_PATH);
        }
        contentId = getIntent().getStringExtra(CONTENT_ID);
        Log.e("asdf", contentId + "content id");
        if (path != null) {
            if (path.contains("/")) {
                Picasso.with(MovieDetails.this)
                        .load(path).into(rootView);

            } else {
                Picasso.with(MovieDetails.this)
                        .load(Constants.BASE_URL_DEV + path).into(rootView);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        session = new Session(this);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);

        downloadIconGroup = (FrameLayout) findViewById(R.id.down_group);
        gifIv = (GifImageView) findViewById(R.id.gif_down);

        detail = (ViewGroup) findViewById(R.id.detail_container);
        bar = (ProgressBar) findViewById(R.id.progress_bar);
        gifIv = (GifImageView) findViewById(R.id.gif_down);

        tool = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //imageview
        fav = findViewById(R.id.fav_roound);
        playIcon = (ImageView) findViewById(R.id.play);
        downIcon = (ImageView) findViewById(R.id.down);
        shareIcon = (ImageView) findViewById(R.id.share);
        poster = (ImageView) findViewById(R.id.movie_detail_poster);
        pause = (ImageView) findViewById(R.id.pause_iv);
        restore = (ImageView) findViewById(R.id.resume);
        saveFavChange = (ImageView) findViewById(R.id.fav_save);
        saveFavChange.setBackground(getDrawable());
        saveFavChange.setOnClickListener(this);

        downloaded_textview = findViewById(R.id.downloaded_tv);
        downloaded_play = findViewById(R.id.downloaded_tv_play);
        downloadedPlay = findViewById(R.id.downloaded_play);
        restore.setBackground(getDrawable());
        fav.setBackground(getDrawable());
        playIcon.setBackground(getDrawable());
        downIcon.setBackground(getDrawable());
        shareIcon.setBackground(getDrawable());
        gifIv.setBackground(getDrawable());
        pause.setBackground(getDrawable());
        downloadedPlay.setBackground(getDrawable());
        pause.setOnClickListener(this);
        restore.setOnClickListener(this);
        downIcon.setOnClickListener(this);
        playIcon.setOnClickListener(this);
        fav.setOnClickListener(this);
        shareIcon.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        downloadedPlay.setOnClickListener(this);

        actorList = new ArrayList<>();


        FavoritesContents isFav = realm.where(FavoritesContents.class).equalTo("contentId", contentId).findFirst();


        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.textLabel);
        directorTag = (TagContainerLayout) findViewById(R.id.tagDirector);


//        FavoritesContents isFav = realm.where(FavoritesContents.class)
//                .equalTo("contentId",contentId)
//                .or()
//                .like("contentId",contentId)
//                .findFirst();

        if (isFav != null) {
            Log.e(" this content is exist ", " ");
            fav.setVisibility(View.GONE);
            saveFavChange.setVisibility(View.VISIBLE);

        } else {
            Log.e("asdf not exist", "");
        }

        Log.e("asdf", isFav + "");


        Fragment relatedFrag = new RelatedMovie_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.related_frame, relatedFrag).addToBackStack(null).commit();


        //text view

        movieTitle = (TextView) findViewById(R.id.movie_title);
        actorText = (TextView) findViewById(R.id.cast_name);
        directorText = (TextView) findViewById(R.id.director_name);
        codeText = (TextView) findViewById(R.id.code_number);
        lookText = (TextView) findViewById(R.id.look_time);
        dotTv = (TextView) findViewById(R.id.text_dots);

//        descriptionText = (TextView) findViewById(R.id.expand_text_view);

        titleTv = (TextView) findViewById(R.id.title_tv);

        castTv = (TextView) findViewById(R.id.cast);
        codeTv = (TextView) findViewById(R.id.code);
        dirTv = (TextView) findViewById(R.id.director);
        title = (TextView) findViewById(R.id.overview);
        downLoadCountTv = (TextView) findViewById(R.id.download_count);
        downLoadCountTv.setVisibility(View.GONE);
        title.setTextColor(Color.WHITE);


        if (session.getLanguage()) {
            if (MDetect.INSTANCE.isUnicode()) {
                castTv.setText("သရုပ်ဆောင်များ");
                dirTv.setText("ဒါရိုက်တာ");
                lookCount.setText("ကြည့်ရှုသူပေါင်း");
                codeTv.setText("ကုဒ်");
                title.setText("အညွန်း");
            } else {
                castTv.setText(Rabbit.uni2zg("သရုပ်ဆောင်များ"));
                dirTv.setText(Rabbit.uni2zg("ဒါရိုက်တာ"));
                lookCount.setText(Rabbit.uni2zg("ကြည့်ရှုသူပေါင်း"));
                codeTv.setText(Rabbit.uni2zg("ကုဒ်"));
                title.setText(Rabbit.uni2zg("အညွန်း"));

            }


        } else

        {
            castTv.setText("Cast");
            dirTv.setText("Director");
            lookCount.setText("View Counts");
            codeTv.setText("Code");
            title.setText("Overview");
        }


        expTv1 = (ExpandableTextView) findViewById(R.id.sample1)
                .findViewById(R.id.expand_text_view);


        if (expandableTextCount <= 1) {
            dotTv.setVisibility(View.GONE);
        } else {
            dotTv.setVisibility(View.VISIBLE);
        }


        expTv1.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {

                if (isExpanded) {
                    dotTv.setVisibility(View.GONE);
                } else {
                    dotTv.setVisibility(View.VISIBLE);
                }


            }
        });

//        recycler view
        relatedMoviesArrayList = new ArrayList<>();
        relatedRv = (RecyclerView) findViewById(R.id.related_rv);
        relatedRv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        relatedMovieAdapter = new RelatedMovieAdapter(this, relatedMoviesArrayList, this);
        relatedRv.setAdapter(relatedMovieAdapter);

    }

//    private void checkdownloadedVideo(int id) {
//
//        Log.e("hawk", "check download");
//
//        ArrayList<DownloadedItems> downloadedItemsArrayList = Hawk.get(Constants.DOWNLOADED_ITEM);
//        Log.e("downloaded size ", downloadedItemsArrayList.size() + "");
//        if (downloadedItemsArrayList != null || downloadedItemsArrayList.size() != 0) {
//            for (DownloadedItems each : downloadedItemsArrayList) {
//                if (each.getId() == String.valueOf(id)) {
//                    downIcon.setVisibility(View.GONE);
//                    downloadedPlay.setVisibility(View.VISIBLE);
//                    currentMoviePath = each.getDownloadMoviesPath();
//                    Log.e("current movie path ", currentMoviePath);
//                    downloaded_play.setVisibility(View.VISIBLE);
//                    downloaded_textview.setVisibility(View.GONE);
//                } else {
//                    Log.e("no downloaded ", " video");
//                    downIcon.setVisibility(View.VISIBLE);
//                    downloadedPlay.setVisibility(View.GONE);
//                }
//            }
//        }
//
//    }

    @Override
    protected void onResume() {
        if (Hawk.contains(Constants.SUCCESS)) {
            if (Hawk.get(Constants.SUCCESS)) {
                progressDialog.setMessage("Loading..");
                progressDialog.show();
                APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
                Call<DownloadLinks> call = apiInterface.getSuccssUrl(Constants.defaultAuthHeader
                        , session.getSessionToekn(), orderNoToken);
                call.enqueue(new Callback<DownloadLinks>() {
                    @Override
                    public void onResponse(Call<DownloadLinks> call, Response<DownloadLinks> response) {
                        if (response.code() != 200) {
                            Log.e("asdf", "dsfasdfsafa" + "eror status code");
                        } else {
                            downloadLink = response.body().download_link;
                            Log.e("download file link ", downloadLink);
                            downloadBeanCall(downloadLink, photoUrl, mTitle);
                            Hawk.delete(Constants.ORDER_NUMBER);
                        }
                    }

                    @Override
                    public void onFailure(Call<DownloadLinks> call, Throwable t) {
                        Log.e("asdfsadf", "payment error " + t.getMessage());
                    }
                });
                Log.e("success Payment", "true");
            } else {
                Log.e("cancel payment", "false");
            }
        }


        super.onResume();
        if (session.getLanguage()) {
            if (MDetect.INSTANCE.isUnicode()) {
                castTv.setText("သရုပ်ဆောင်များ");
                dirTv.setText("ဒါရိုက်တာ");
                lookCount.setText("ကြည့်ရှုသူပေါင်း");
                codeTv.setText("ကုဒ်");
                title.setText("အညွန်း");
            } else {
                castTv.setText(Rabbit.uni2zg("သရုပ်ဆောင်များ"));
                dirTv.setText(Rabbit.uni2zg("ဒါရိုက်တာ"));
                lookCount.setText(Rabbit.uni2zg("ကြည့်ရှုသူပေါင်း"));
                codeTv.setText(Rabbit.uni2zg("ကုဒ်"));
                title.setText(Rabbit.uni2zg("အညွန်း"));
            }


        } else

        {
            castTv.setText("Cast");
            dirTv.setText("Director");
            lookCount.setText("View Count");
            codeTv.setText("Code");
            title.setText("Overview");
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public GradientDrawable getDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(80);
        drawable.setColor(Color.argb(110, 51, 51, 51));
        return drawable;
    }

    private StyleableToast styleableToast;


    ArrayList<String> setArray;


    @Override
    public void onBackPressed() {
        // do nothing.

//        if (isHide) {
//            super.onBackPressed();
//        }

        super.onBackPressed();
        return;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                isHide = true;
                onBackPressed();
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String message,String title,String ok,String cancel){

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(ok)
                .negativeText(cancel)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                // styleableToast.show();
                if (hasTrailer && trailerLink != null) {
                    Intent intent = new Intent(MovieDetails.this, UrlPlayerActivity.class);
                    intent.putExtra(Constants.MOVIEPATH, trailerLink);
                    startActivity(intent);
                } else {
                    StyleableToast ste = new StyleableToast
                            .Builder(this)
                            .duration(Toast.LENGTH_SHORT)
                            .cornerRadius(50)
                            .icon(R.drawable.com_facebook_close)
                            .text("No Available Trailer")
                            .textColor(Color.WHITE)
                            .backgroundColor(getResources().getColor(R.color.colorAccent))
                            .build();
                    ste.show();
                }
                break;



            case R.id.down:

                int CELLULAR = 0;
                int WIFI = 1;
                String resumeAbleDownloadLink = "http://s1.music.126.net/download/android/CloudMusic_official_3.7.3_153912.apk";
                orderNoToken = Hawk.get(Constants.ORDER_NUMBER);

                if (CheckInternet.isOnline(this))
                {
                    if (CheckInternet.wifiOrCellular(this)==CELLULAR)
                    {
                        if (session.getOperator()==WIFI)
                        {
                            if (session.getLanguage())
                            {
                                if (MDetect.INSTANCE.isUnicode())
                                {
                                    showMessage("","","","");
                                }else {
                                    showMessage("","","","");
                                }
                            }else {
                                showMessage("You should need to disable download only with wifi","Warning","Ok","Cancel");
                            }

                        }else {

                            if (mmPrice == "0" || mmPrice.equals("0")) {
                                progressDialog.setMessage("Loading.....");
                                progressDialog.show();
                                getOrderNumber();
                            } else {
                                progressDialog.setMessage("Loading.....");
                                progressDialog.show();
                                getOrderNumber();
                            }
                        }
                    }else if (CheckInternet.wifiOrCellular(this)==WIFI)
                    {
                        if (mmPrice == "0" || mmPrice.equals("0")) {
                            progressDialog.setMessage("Loading.....");
                            progressDialog.show();
                            getOrderNumber();
                        } else {
                            progressDialog.setMessage("Loading.....");
                            progressDialog.show();
                            getOrderNumber();
                        }
                    }



                }else {
                    if (session.getLanguage())
                    {
                        if (MDetect.INSTANCE.isUnicode())
                        {
                            showMessage("","","","");
                        }else {
                            showMessage("","","","");

                        }
                    }else {
                        showMessage("No internet connection","Warning","Ok","Cancel");
                    }
                    /*no internet connection*/
                }





//                if (CheckInternet.isOnline(this)) {
//                    if (session.getOperator() == 1 && CheckInternet.isWifi(this) == 1) {
//
//                        if (mmPrice == "0" || mmPrice.equals("0")) {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            getOrderNumber();
//                        } else {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            getOrderNumber();
//                        }
//
//                    } else if (session.getOperator() == 1 && CheckInternet.isWifi(this) == 0) {
//                        new MaterialDialog.Builder(this)
//                                .title("Warning")
//                                .content("you Should need to disable Download only with wifi")
//                                .positiveText("Ok")
//                                .show();
//                    } else {
//                        if (session.getOperator() == 0) {
//                            if (mmPrice == "0" || mmPrice.equals("0")) {
//                                progressDialog.setMessage("Loading.....");
//                                progressDialog.show();
//                                getOrderNumber();
//                            } else {
//                                progressDialog.setMessage("Loading.....");
//                                progressDialog.show();
//                                getOrderNumber();
//                            }
//                        }
//                    }
//
//                } else {
//
//                    new MaterialDialog.Builder(this)
//                            .title("Warning")
//                            .content("No Internet Connection...1")
//                            .positiveText("Ok")
//                            .show();
//                    /*no internet connection */
//                }

//                if (CheckInternet.isWifi(MovieDetails.this) == 0) {
//                    if (session.getOperator() == 0) {
//                        if (mmPrice == "0" || mmPrice.equals("0")) {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            downloadBeanCall(resumeAbleDownloadLink, photoUrl, mTitle);
//                        } else {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            getOrderNumber();
//                        }
//
//                    } else {
//
//                        new MaterialDialog.Builder(this)
//                                .title("Warning")
//                                .content("you need to change 3G only in Your Profile")
//                                .positiveText("Ok")
//                                .show();
//                    }
//                } else if (CheckInternet.isWifi(MovieDetails.this) == 1) {
//                    if (session.getOperator() == 1) {
//                        Log.e("price " + price, " mmprice " + mmPrice);
//                        if (mmPrice == "0" || mmPrice.equals("0")) {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            downloadBeanCall(resumeAbleDownloadLink, photoUrl, mTitle);
//                        } else {
//                            progressDialog.setMessage("Loading.....");
//                            progressDialog.show();
//                            getOrderNumber();
//
////                            Log.e("chekck status ", checkDownload() + "");
////                            if (checkDownloadLink != checkDownload()) {
////
////                                if (Hawk.contains(Constants.SUCCESS)) {
////                                    Log.e("hawk success ", Hawk.get(Constants.SUCCESS) + "");
////                                    if (Hawk.get(Constants.SUCCESS)) {
////                                        progressDialog.setMessage("Loading....");
////                                        progressDialog.show();
////                                        getOrderNumber();
////                                        checkDownloadLink = checkDownload();
////                                    } else {
////                                        if (session.getLanguage()) {
////                                            if (MDetect.INSTANCE.isUnicode()) {
////                                                recomPopUpDialog(MMlAN, UNI);
////
////
////                                            } else {
////                                                recomPopUpDialog(MMlAN, ZawG);
////                                            }
////                                        } else {
////                                            recomPopUpDialog(ELAN, Nothing);
////                                        }
////                                    }
////                                } else {
////                                    Toast.makeText(this, "PAY ME IS NOT SUCCESS", Toast.LENGTH_SHORT).show();
////                                    if (session.getLanguage()) {
////                                        if (MDetect.INSTANCE.isUnicode()) {
////                                            recomPopUpDialog(MMlAN, UNI);
////
////
////                                        } else {
////                                            recomPopUpDialog(MMlAN, ZawG);
////                                        }
////                                    } else {
////                                        recomPopUpDialog(ELAN, Nothing);
////                                    }
////                                }
////                            } else {
////                                Toast.makeText(this, "Already purchase this movie", Toast.LENGTH_SHORT).show();
////                                checkDownload();
////                            }
//                        }
//
//                    } else {
//                        new MaterialDialog.Builder(this)
//                                .title("Warning")
//                                .content("you need to change Wifi only in Your Profile")
//                                .positiveText("Ok")
//                                .show();
//                    }
//                }


                break;
            case R.id.pause_iv:
                pause.setVisibility(View.INVISIBLE);
                downIcon.setVisibility(View.INVISIBLE);
                FileDownloader.getImpl().pause(downloadId);
                restore.setVisibility(View.VISIBLE);
                break;
            case R.id.fav_roound:
//                Toast.makeText(this, "Click favorite", Toast.LENGTH_SHORT).show();
                loadSetFavorites(contentId);
                break;
            case R.id.fav_save:

                deleteFavApi(contentId);


                break;

            case R.id.share:

                String test = "http://s1.music.126.net/download/android/CloudMusic_official_3.7.3_153912.apk";
                String test2 = "http://194.88.106.67/mahar/laravel.mp4";
                String testPadd = "http://mahar.comquas.com/uploads/6/6.mp4";
                String urlOneMovie = "http://194.88.106.67/mahar/laravel.mp4";

                String urlTwo = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4";


                downloadBeanCall(urlTwo, photoUrl, mTitle);

//                try {
//                    testSimpleIntentWithAppLink();
//                    //testSimpleAppLinkNavigationWithExtras();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // getAppLink();
//
//
//                ShareLinkContent content = new ShareLinkContent.Builder()
//                        .setImageUrl(Uri.parse(photoUrl))
//                        .setContentUrl(Uri.parse(Constants.APP_LINKS))
//                        .setContentTitle(mTitle)
//                        .build();
//                ShareDialog shareDialog = new ShareDialog(MovieDetails.this);
//                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

                break;

            case R.id.buy_btn:
                if (session.getLanguage()) {
                    if (MDetect.INSTANCE.isUnicode()) {
                        recomPopUpDialog(MMlAN, UNI);


                    } else {
                        recomPopUpDialog(MMlAN, ZawG);
                    }
                } else {
                    recomPopUpDialog(ELAN, Nothing);
                }
                break;
            case R.id.downloaded_play: {
                if (currentMoviePath != null) {
                    Log.e("current movie path ", currentMoviePath);
                    Intent player = new Intent(MovieDetails.this, PlayerActivity.class);
                    player.putExtra(Constants.COTENTID, String.valueOf(contentId));
                    player.putExtra(Constants.MOVIEPATH, currentMoviePath);
                    startActivity(player);
                }
            }
        }
    }

    private void checkDownload(String orderNo) {
        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Log.e("order id ", orderNo + " user id " + session.getUSERID() + " content di " + contentId);
        Call<CheckDownloadRequest> call = apiInterface.getDownloadRequest(
                Constants.defaultAuthHeader,
                session.getSessionToekn(),
                orderNo,
                session.getUSERID(),
                contentId);
        call.enqueue(new Callback<CheckDownloadRequest>() {
            @Override
            public void onResponse(Call<CheckDownloadRequest> call, Response<CheckDownloadRequest> response) {

                Log.e("reponse", response.code() + "");
                if (response.code() != 200) {
                    checkDownloadLink = false;
                    progressDialog.dismiss();
                    if (session.getLanguage()) {
                        if (MDetect.INSTANCE.isUnicode()) {
                            recomPopUpDialog(MMlAN, UNI);
                        } else {
                            recomPopUpDialog(MMlAN, ZawG);
                        }
                    } else {
                        recomPopUpDialog(ELAN, Nothing);
                    }

                    Log.e("asdfdas", response.body() + " error 401");
                } else {
                    progressDialog.dismiss();
                    downloadLink = response.body().download_link;
                    checkDownloadLink = true;
                    Log.e(TAG, "already purchase download link file" + downloadLink);
                    downloadBeanCall(downloadLink, photoUrl, mTitle);
                }

            }

            @Override
            public void onFailure(Call<CheckDownloadRequest> call, Throwable t) {
                Log.e("download checck ", t.getMessage());
            }
        });

    }

    private void getOrderNumber() {
        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Call<Invoice> call = apiInterface.getInvoieNumber(Constants.defaultAuthHeader, session.getSessionToekn());
        call.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                Log.e("body", response.body().toString() + " " + response.body().order_id);
                if (response.body().order_id != null) {
                    orderNoToken = response.body().order_id;
                    if (!mmPrice.equals("0")) {

                        checkDownload(orderNoToken);
                    } else {
                        sendTransactionApiToWebView(orderNoToken);
                    }

                    Log.e("orderNotoken ", " " + orderNoToken);
                    Hawk.put(Constants.ORDER_NUMBER, response.body().order_id);
                }
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {

            }
        });
    }


    public static final int MMlAN = 0;
    public static final int ELAN = 1;
    public static final int UNI = 0;
    public static final int ZawG = 1;
    public static final int Nothing = 3;


    public void recomPopUpDialog(int lan, int uniOrZawG) {
        String title = null;
        String message = null;
        String cancel = null;
        String okL = null;
        switch (lan) {
            case MMlAN:
                if (uniOrZawG == 0) {
                    title = "ဤရုပ်ရှင်ကားအား (၇)ရက် ငှားမည်။";
                    message = "ဤရုပ်ရှင်ကားအား (၇)ရက် ငှားရန် ၁၀၀ ကျပ်ကျသင့်ပါမည်။";
                    cancel = "မငှားပါ";
                    okL = "ငှားမည်။";
                } else {
                    title = Rabbit.uni2zg("ဤရုပ်ရှင်ကားအား (၇)ရက် ငှားမည်။");
                    message = Rabbit.uni2zg("ဤရုပ်ရှင်ကားအား (၇)ရက် ငှားရန် ၁၀၀ ကျပ်ကျသင့်ပါမည်။");
                    cancel = Rabbit.uni2zg("မငှားပါ");
                    okL = Rabbit.uni2zg("ငှားမည်");
                }

                break;
            case ELAN:
                if (uniOrZawG == Nothing) {
                    title = "Rent";
                    message = "This movie is costs 100K/s to rent in 7 days";
                    cancel = "cancel";
                    okL = "Ok";
                }

                break;
        }

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .titleColor(Color.BLACK)
                .contentColor(Color.BLACK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                        String orderNumber = Hawk.get(Constants.ORDER_NUMBER);
                        Log.e("transaction", orderNumber);
                        if (orderNumber != null) {
                            progressDialog.setMessage("loading.....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            sendTransactionApiToWebView(orderNumber);
                        } else {
                            Log.e("transaction", "orderid is null");
                        }

                    }
                })
                .positiveText(okL)
                .negativeText(cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })

                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void sendTransactionApiToWebView(String orderNumber) {
        Log.e("user id ", session.getUSERID() + " content id " + contentId + " price " + mmPrice + "order id " + orderNumber + "session token " + session.getSessionToekn());
        Log.e("order id ", orderNumber);
        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Call<TransactionLinkData> call = apiInterface.getTransactionData(Constants.defaultAuthHeader,
                session.getSessionToekn(),
                orderNumber,
                session.getUSERID(),
                mmPrice,
                contentId);

        call.enqueue(new Callback<TransactionLinkData>() {
            @Override
            public void onResponse(Call<TransactionLinkData> call, Response<TransactionLinkData> response) {
                Log.e("transaction", "call links");
                //  Log.e("response ", response.body() + "");
                Log.e("code", response.code() + "      ");
                if (response.code() != 200) {
                    Log.e("transaction", "null");
                    progressDialog.setMessage("Error");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                } else {
                    if (response.body().rel.equals("download")) {
                        downloadBeanCall(response.body().href, photoUrl, mTitle);
                    } else {
                        Log.e("transaction links ", response.body().href);
                        callWebView(response.body().href);
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionLinkData> call, Throwable t) {
                Log.e("transaction error ", t.getMessage());
            }
        });
    }

    private void callWebView(String link) {
        Intent webView = new Intent(MovieDetails.this, WebViewContainer.class);
        webView.putExtra(Constants.LINK_URL, link);
        startActivity(webView);
    }

    public void downloadBeanCall(String downloadUrl, String picUrl1, String name) {


        Log.e(TAG, "downloadbean call calling *******");


        //downlink , downloadname, extra1 - photourl , extra2  - mtitle , extra3 - contentid, downloadfilePath ,
        //extra 4 - savePath, extra5 - photofilePath

        String date = simpleDateFormat.format(new Date());

        photoFilePath = savePath + "/" + contentID + ".hs";
//        downloadBean = new DownloadBean
//                .Builder(downloadUrl)
//                .setSaveName(contentID + ".mp4")      //not need.
//                .setSavePath(savePath)      //not need
//                .setExtra1(picUrl1)   //save extra info into database.
//                .setExtra2(mTitle)
//                .setExtra3(String.valueOf(contentID))
//                .setExtra4(date)
//                .build;

        Log.e(TAG,savePath);

        downloadBean = new DownloadBean
                .Builder(downloadUrl)
                .setSaveName(contentId + ".mp4")      //not need.
                .setSavePath(savePath)      //not need
                .setExtra1(photoUrl)   //save extra info into database.
                .setExtra2(mTitle)
                .setExtra3(String.valueOf(contentID))
                .setExtra4(currentDate())
                .build();
        start();

    }


    private void start() {
        RxPermissions.getInstance(this)
                .request(WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .compose(RxDownload.getInstance(this).<Boolean>transformService(downloadBean))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        String title;
                        String message;
                        String ok;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }, 1500);


                        if (session.getLanguage()) {
                            if (MDetect.INSTANCE.isUnicode()) {
                                title = "စတင်ခြင်း";
                                message = "သင်ရဲ့ဗီဒီယိုစတင်ဒေါင်းလုပ်ဆွဲနေပါပီ";
                                ok = "အိုကေ";
                            } else {
                                title = Rabbit.uni2zg("စတင်ခြင်း");
                                message = Rabbit.uni2zg("သင်ရဲ့ဗီဒီယိုစတင်ဒေါင်းလုပ်ဆွဲနေပါပီ");
                                ok = Rabbit.uni2zg("အိုကေ");
                            }
                        } else {
                            title = "Strating..";
                            message = "Your Movies is Downloading.. You Can Look downloadList";
                            ok = "Ok";
                        }
                        new MaterialDialog.Builder(MovieDetails.this)
                                .title(title)
                                .content(message)
                                .positiveText(ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                                        startActivity(new Intent(MovieDetails.this, MyDownloadManagerActivity.class));
                                    }
                                })
                                .show();
                    }
                });
    }

    public String currentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(date);
        return formatDate;
    }

    private static BaseDownloadTask createDownloadTaskPhoto(String photoUrl, String path) {
        Log.e("vvv", "download photourl " + photoUrl + "path " + path);
        return FileDownloader.getImpl().create(photoUrl)
                .setPath(path, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400);
    }


    public void decryptSetFile(String filePath, String name) {
        final String password = "qI[s$%(OS2_g!!%^&*#@";
        String file = filePath + "/" + name + ".aes";

        Log.e("asdf", " encrypted file " + file);
        final File srcFile = new File(file);
        final File destFile = new File(file + ".tmp");

        try {
            com.comquas.mahar.utils.AESCrypt aesCrypt = new com.comquas.mahar.utils.AESCrypt(password);
            try {
                Log.e("aaa", "start encrypting");
                aesCrypt.decrypt(srcFile.getPath(), destFile.getPath());
                Log.e("aaa", "success");
            } catch (IOException e) {
                Log.e("asdf", "error : " + e.getMessage());
            }
        } catch (GeneralSecurityException e) {
            Log.e("asdf", "error : " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e("asdf", "error : " + e.getMessage());
            e.printStackTrace();
        }

    }


    private void deleteFavApi(final String contentId) {
        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Call<DeleteFav> call = apiInterface.deleteFavoraitesContent(defaultAuthHeader, session.getSessionToekn(), contentId);
        call.enqueue(new Callback<DeleteFav>() {
            @Override
            public void onResponse(Call<DeleteFav> call, Response<DeleteFav> response) {
                Log.e("delete fav", response.body().message + "");
//                Toast.makeText(MovieDetails.this, "delete favoraites ", Toast.LENGTH_SHORT).show();

                if (response.body().message != null) {
                    RealmResults<FavoritesContents> hasFav = realm.where(FavoritesContents.class).equalTo("contentId", contentId).findAll();
                    if (hasFav != null) {

                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                            Log.e("asdf", " translation");
                        }

                        hasFav.deleteAllFromRealm();
                        Log.e("asdf", " successfull delete");
                        realm.commitTransaction();
                        fav.setVisibility(View.VISIBLE);
                        saveFavChange.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<DeleteFav> call, Throwable t) {
                Log.e("asdf", " error " + t.getMessage());
            }
        });

    }

    private void loadSetFavorites(String contentId) {
        APIInterface apiInterface = ApiClient.getClientDev().create(APIInterface.class);
        Call<Favorites> call = apiInterface.setFavoritesContent(defaultAuthHeader, session.getSessionToekn(), contentId);
        call.enqueue(new Callback<Favorites>() {
            @Override
            public void onResponse(Call<Favorites> call, Response<Favorites> response) {
                if (response.body() != null) {
                    Log.e("asdf", " list " + response.body().favoriteId);

                    saveFavIdToRealm(response.body().favoriteId);
                }
            }


            @Override
            public void onFailure(Call<Favorites> call, Throwable t) {

                Log.e("asdf", " error " + t.getMessage());

            }
        });
    }

    private void saveFavIdToRealm(final int favoriteId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                FavoritesContents favoritesContents = bgRealm.createObject(FavoritesContents.class);
                favoritesContents.setFavId(favoriteId);
                favoritesContents.setContentId(contentId);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("asdf", " save into realm object");

                saveFavChange.setVisibility(View.VISIBLE);
                fav.setVisibility(View.GONE);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("asdf", " error save into realm object");
            }
        });
    }

    public static void saveIntoRealm(final String movieName, final String path, final String mPath) {


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                RealmDownloadData downloadedData = bgRealm.createObject(RealmDownloadData.class);
                downloadedData.setName(movieName);
                downloadedData.setPoster(path);
                downloadedData.setFilePath(mPath);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("asdf", " save into realm object");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("asdf", " error save into realm object");
            }
        });
    }

    private String checkRealmDatabase() {
        // Or alternatively do the same all at once (the "Fluent interface"):
        RealmResults<RealmDownloadData> result2 = realm.where(RealmDownloadData.class)
                .equalTo("name", "သတို့သမီးကညာသည်မီးတောငျ")
                .or()
                .equalTo("name", "Peter")
                .findAll();

        if (result2 != null) {
            Log.e("asdf", "result is not null ");
            return "not null";
        } else {
            Log.e("asdf", "realm result is null ");
            return "null";
        }
    }

    private void delectRealmData() {
        // obtain the results of a query
        final RealmResults<RealmDownloadData> results = realm.where(RealmDownloadData.class).findAll();

// All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
//                results.deleteFirstFromRealm();
//                results.deleteLastFromRealm();

//                    // remove a single object
//                    RealmDownloadData realmDownloadData = results.get(5);
//                    dog.deleteFromRealm();

                // Delete all matches
                results.deleteAllFromRealm();
                Log.e("adsf", "delected realm data");
            }
        });
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @Override
    public void clickItemsListener(int positon, ImageView imageView, ArrayList<RelatedMovies> list) {
        Log.e("asdf", list.get(positon).id + list.get(positon).thumbnail_url);
        View sharedView = imageView;
        String transitionName = getString(R.string.movies);
        Intent details = new Intent(MovieDetails.this, MovieDetails.class);
        details.putExtra(MOVIE_PATH, list.get(positon).thumbnail_url);
        details.putExtra(CONTENT_ID, list.get(positon).id);
        ActivityOptions transitionActivityOptions = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Log.e("asdf", "lo");
            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);
            startActivity(details, transitionActivityOptions.toBundle());

        } else {
            startActivity(details);
        }
    }


}
