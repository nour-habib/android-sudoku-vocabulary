package com.bignerdranch.android.sudoku12;

import android.os.Parcel;
import android.os.Parcelable;


public class StatsTracker implements Parcelable{

    public static final int points = 1000;
    int errorCounter;
    int score;

    public StatsTracker()
    {
        errorCounter = 0;
        score = 0;

    }

    protected StatsTracker(Parcel in)
    {
        errorCounter = in.readInt();
        score = in.readInt();
    }

    public static final Parcelable.Creator<StatsTracker>CREATOR = new Parcelable.Creator<StatsTracker>() {
        @Override
        public StatsTracker createFromParcel(Parcel parcel) {
            return new StatsTracker(parcel);
        }

        @Override
        public StatsTracker[] newArray(int i) {
            return new StatsTracker[i];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(errorCounter);
        dest.writeInt(score);
    }


    public int getErrorCounter()
    {
        return errorCounter;
    }

    public void setErrorCounter(int i)
    {
        errorCounter = errorCounter + i;
    }

    public void setScore()
    {
        score = score + points;
    }

    public int getScore()
    {
        return this.score;
    }

}