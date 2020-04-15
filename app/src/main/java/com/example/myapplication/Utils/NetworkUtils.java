package com.example.myapplication.Utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.Model.Review;
import com.example.myapplication.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    private static final String API_Key = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private static final String API_KEY_QUERY_PARAM = "api_key";
    private static final String MOVIE_LANGUAGE_QUERY_PARAM = "language";
    public static final String MOVIE_LANGUAGE = "en-US";
    private static final String MOVIE_PAGE_QUERY_PARAM = "page";
    private static final String TRAILER_VIDEOS_PARAM = "videos";
    private static final String REVIEW_VIDEOS_PARAM = "reviews";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String MOVIE_POPULAR = "/movie/popular";
    private static final String LOG_TAG = "";
    private static int READ_TIMEOUT = 10000;
    private static int CONNECT_TIMEOUT = 15000;

    public static URL createMovieUrl(String stringUrl) {
        Uri.Builder builder = Uri.parse(stringUrl).buildUpon();
        builder.appendQueryParameter(API_KEY_QUERY_PARAM, API_Key);
        builder.appendQueryParameter(MOVIE_LANGUAGE_QUERY_PARAM, MOVIE_LANGUAGE);
        builder.appendQueryParameter(MOVIE_PAGE_QUERY_PARAM, "1");
        Uri uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    public static URL createTrailerUrl(String stringUrl) {

        Uri.Builder builder = Uri.parse(BASE_URL + "movie/" + stringUrl).buildUpon();
        builder.appendEncodedPath(TRAILER_VIDEOS_PARAM);
        builder.appendQueryParameter(API_KEY_QUERY_PARAM, API_Key);

        Uri uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    public static URL createReviewUrl(String stringUrl) {

        Uri.Builder builder = Uri.parse(BASE_URL + "movie/" + stringUrl).buildUpon();
        builder.appendEncodedPath(REVIEW_VIDEOS_PARAM);
        builder.appendQueryParameter(API_KEY_QUERY_PARAM, API_Key);

        Uri uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Extract MOVIE JSON
    private static List<Movie> extractMovieFromJson(String movieJSON) {
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject urlMovie = movieArray.getJSONObject(i);
                String id = urlMovie.getString("id");
                String title = urlMovie.getString("original_title");
                String releaseDate = urlMovie.getString("release_date");
                String image = urlMovie.getString("poster_path");
                String ratingAverage = urlMovie.getString("vote_average");
                String plotSynopsis = urlMovie.getString("overview");
//                String trailer = urlMovie.getString("videos");
//                String trailerKey = urlMovie.getString("trailer_key");

                Movie movie = new Movie(id, title, releaseDate, image, ratingAverage, plotSynopsis);
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "There was a problem parsing the JSON data.", e);
        }
        return movies;
    }


    // Extract TRAILER JSON
    private static List<Trailer> extractTrailerFromJson(String trailerJSON) {
        if (TextUtils.isEmpty(trailerJSON)) {
            return null;
        }
        List<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(trailerJSON);
            JSONArray trailerArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject urlTrailer = trailerArray.getJSONObject(i);
                String name = urlTrailer.getString("name");
                String site = urlTrailer.getString("site");
                String key = urlTrailer.getString("key");

                Trailer trailer = new Trailer(name, site, key);
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "There was a problem parsing the JSON data.", e);
        }
        return trailers;
    }

    // Extract REVIEW JSON
    private static List<Review> extractReviewFromJson(String reviewJSON) {
        if (TextUtils.isEmpty(reviewJSON)) {
            return null;
        }
        List<Review> reviews = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(reviewJSON);
            JSONArray reviewArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject urlReview = reviewArray.getJSONObject(i);
                String id = urlReview.getString("id");
                String author = urlReview.getString("author");
                String content = urlReview.getString("content");
                String url = urlReview.getString("url");
                Review review = new Review(id, author, content, url);
                reviews.add(review);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "There was a problem parsing the JSON data.", e);
        }
        return reviews;
    }

    // Fetch MOVIE data
    public static List<Movie> fetchMovieData(String requestUrl) {
        URL url = createMovieUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Movie> movieArticles = extractMovieFromJson(jsonResponse);
        return movieArticles;
    }

    // Fetch TRAILER data
    public static List<Trailer> fetchTrailerData(String requestUrl) {
        URL url = createTrailerUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Trailer> trailerArticles = extractTrailerFromJson(jsonResponse);
        return trailerArticles;
    }

    // Fetch REVIEW data
    public static List<Review> fetchReviewData(String requestUrl) {
        URL url = createReviewUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Review> reviewArticles = extractReviewFromJson(jsonResponse);
        return reviewArticles;
    }
}
