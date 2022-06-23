package com.example.swaggerdemo.json;

public class info_array_Util {
    private String messageNo, memberNo, mobileNo, nickname, headPath, content;
    private int messageType, readFlag;
    private long sendTime, updateTime;
 
    public String getMessageNo() {
        return messageNo;
    }
 
    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }
 
    public String getMemberNo() {
        return memberNo;
    }
 
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }
 
    public String getMobileNo() {
        return mobileNo;
    }
 
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
 
    public String getNickname() {
        return nickname;
    }
 
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
 
    public String getHeadPath() {
        return headPath;
    }
 
    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }
 
    public String getContent() {
        return content;
    }
 
    public void setContent(String content) {
        this.content = content;
    }
 
    public int getMessageType() {
        return messageType;
    }
 
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
 
    public int getReadFlag() {
        return readFlag;
    }
 
    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }
 
    public long getSendTime() {
        return sendTime;
    }
 
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
 
    public long getUpdateTime() {
        return updateTime;
    }
 
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
 
    @Override
    public String toString() {
        return "info_array_Util [messageNo=" + messageNo + ", memberNo="
                + memberNo + ", mobileNo=" + mobileNo + ", nickname="
                + nickname + ", headPath=" + headPath + ", content=" + content
                + ", messageType=" + messageType + ", readFlag=" + readFlag
                + ", sendTime=" + sendTime + ", updateTime=" + updateTime + "]";
    }
 
}