package com.xiroid.imovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 预览片信息
 *
 * @author xiaojunzhou
 * @date 16/6/4
 */
public class VideoInfo implements Parcelable {

    private String id;
    private String name;
    private String key;
    private String type;

    public VideoInfo() {
        
    }

    protected VideoInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
        type = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(type);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }
}
