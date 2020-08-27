package com.diablo.jayson.kicksv1.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LatestFinishedActivity {

    @PrimaryKey
    public String latestFinishedId;
    public Activity latestActivity;


}
