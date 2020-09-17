package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

public class Invite {

    private Activity inviteActivity;
    private String inviterId;
    private String inviterName;
    private String inviteeId;
    private String inviteId;
    private Timestamp inviteTime;
    private String inviterPicUrl;

    public Invite() {
    }

    public Invite(Activity inviteActivity, String inviterId, String inviterName, String inviteeId,
                  String inviteId, Timestamp inviteTime, String inviterPicUrl) {
        this.inviteActivity = inviteActivity;
        this.inviterId = inviterId;
        this.inviterName = inviterName;
        this.inviteeId = inviteeId;
        this.inviteId = inviteId;
        this.inviteTime = inviteTime;
        this.inviterPicUrl = inviterPicUrl;
    }

    public Activity getInviteActivity() {
        return inviteActivity;
    }

    public void setInviteActivity(Activity inviteActivity) {
        this.inviteActivity = inviteActivity;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public String getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(String inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public Timestamp getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Timestamp inviteTime) {
        this.inviteTime = inviteTime;
    }

    public String getInviterPicUrl() {
        return inviterPicUrl;
    }

    public void setInviterPicUrl(String inviterPicUrl) {
        this.inviterPicUrl = inviterPicUrl;
    }
}
