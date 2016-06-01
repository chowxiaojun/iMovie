package com.xiroid.imovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojunzhou on 16/3/19.
 * @function 存储影片信息
 */
public class MovieInfo implements Parcelable {
    private static final String BASE_IMG_URL ="http://image.tmdb.org/t/p/w185/";

    private int id;
    private String posterPath;  // 封面图片路径
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private double voteAverage;

    public MovieInfo() {

    }


    protected MovieInfo(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPoster() {
        return BASE_IMG_URL + posterPath;
    }

    @Override
    public String toString() {
        return "title: " + getOriginalTitle() + " overview: "+ getOverview() + " rating: "
                + getVoteAverage() + " release date: " + getReleaseDate() + " poster: " +
                getPoster();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeDouble(voteAverage);
    }
}
