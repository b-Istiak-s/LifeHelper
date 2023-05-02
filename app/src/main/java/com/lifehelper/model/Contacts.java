package com.lifehelper.model;

import com.google.gson.annotations.SerializedName;
import com.lifehelper.Constants;

public class Contacts {
    @SerializedName(Constants.USER_NAME)
    private String username;
    @SerializedName(Constants.EMAIL)
    private String email;
    @SerializedName(Constants.FULL_NAME)
    private String fullName;
    @SerializedName(Constants.PHONE)
    private String phone;
    @SerializedName(Constants.LATITUDE)
    private String latitude;
    @SerializedName(Constants.LONGITUDE)
    private String longitude;
    @SerializedName(Constants.CITY)
    private String city;
    @SerializedName(Constants.COUNTRY)
    private String country;
    @SerializedName(Constants.IMG)
    private String img;
    @SerializedName(Constants.USER_TYPE)
    private String userType;
    @SerializedName(Constants.GENDER)
    private String gender;
    @SerializedName(Constants.BIRTH_YEAR)
    private String birthYear;
    @SerializedName(Constants.HOBBY)
    private String hobby;
    @SerializedName(Constants.HEIGHT)
    private String height;
    @SerializedName(Constants.CHAR_TYPE)
    private String charType;
    @SerializedName(Constants.PARTNER)
    private String partner;
    @SerializedName(Constants.LAST_ACTIVE)
    private String lastActive;
    @SerializedName(Constants.VALUE)
    private String value;
    @SerializedName(Constants.MESSAGE)
    private String message;
    @SerializedName(Constants.RELATION_TYPE)
    private String relationType;
    @SerializedName(Constants.SELECTOR)
    private String selector;
    @SerializedName(Constants.TYPE)
    private String type;
    @SerializedName(Constants.CLIENT_SECRET)
    private String clientSecret;
    @SerializedName(Constants.RESTAURANT_NAME)
    private String restaurantName;
    @SerializedName(Constants.OPENING_HOURS)
    private String openingHours;
    @SerializedName(Constants.CLOSING_HOURS)
    private String closingHours;
    @SerializedName(Constants.RESTAURANT_LOGO)
    private String restaurantLogo;
    @SerializedName(Constants.ADDRESS)
    private String address;
    @SerializedName(Constants.TIMESTAMP)
    private String timestamp;
    @SerializedName(Constants.READ)
    private String read;
    @SerializedName(Constants.USER_1)
    private String user1;
    @SerializedName(Constants.USER_2)
    private String user2;
    @SerializedName(Constants.MESSAGE_FROM)
    private String messageFrom;
    @SerializedName(Constants.TO)
    private String to;
    @SerializedName(Constants.FROM)
    private String from;
    @SerializedName(Constants.SENDER)
    private String sender;
    @SerializedName(Constants.NOTIFIED)
    private String notified;
    @SerializedName(Constants.ID)
    private String id;
    @SerializedName(Constants.AMOUNT)
    private String amount;
    @SerializedName(Constants.DESC)
    private String desc;
    @SerializedName(Constants.POST_IMAGE)
    private String postImage;

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }
    public String getPhone() {
        return phone;
    }
    public String getCity(){
        return city;
    }
    public String getCountry(){
        return country;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getImg(){
        return img;
    }
    public String getUserType() {
        return userType;
    }
    public String getGender(){
        return gender;
    }
    public String getBirthYear(){
        return birthYear;
    }
    public String getHobby(){
        return hobby;
    }
    public String getHeight(){
        return height;
    }
    public String getCharType(){
        return charType;
    }
    public String getPartner(){
        return partner;
    }
    public String getLastActive(){
        return lastActive;
    }
    public String getValue(){
        return value;
    }
    public String getMessage(){
        return message;
    }
    public String getRelationType(){
        return relationType;
    }
    public String getSelector(){
        return selector;
    }
    public String getType(){
        return type;
    }
    public String getClientSecret(){
        return clientSecret;
    }
    public String getRestaurantName(){return restaurantName;}
    public String getOpeningHours(){return openingHours;}
    public String getClosingHours(){return closingHours;}
    public String getRestaurantLogo(){return restaurantLogo;}
    public String getAddress(){return address;}
    public String getTimestamp(){return timestamp;}
    public String getRead(){return read;}
    public String getUser1(){return user1;}
    public String getUser2(){return user2;}
    public String getMessageFrom(){return messageFrom;}
    public String getSender(){return sender;}
    public String getTo(){return to;}
    public String getFrom(){return from;}
    public String getNotified(){return notified;}
    public String getId(){return id;}
    public String getAmount(){return amount;}
    public String getDesc(){return desc;}
    public String getPostImage(){return postImage;}
}
