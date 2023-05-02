package com.lifehelper.remote;

import com.lifehelper.Constants;
import com.lifehelper.model.Contacts;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("life_helper/users/create.php")
    Call<Contacts> signUpUser(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.EMAIL) String email,
            @Field(Constants.PASSWORD) String password,
            @Field(Constants.FULL_NAME) String fullName,
            @Field(Constants.PHONE) String phone,
            @Field(Constants.LATITUDE) String latitude,
            @Field(Constants.LONGITUDE) String longitude,
            @Field(Constants.USER_TYPE) String contact);


    @FormUrlEncoded
    @POST("life_helper/users/resend.php")
    Call<Contacts> resendMail(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.EMAIL) String email,
            @Field(Constants.TYPE) String type);

    @FormUrlEncoded
    @POST("life_helper/users/login.php")
    Call<Contacts> loginUser(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.PASSWORD) String email);

    @Multipart
    @POST("life_helper/users/update.php")
    Call<Contacts> updateProfile(
            @Part MultipartBody.Part file,
            @Part(Constants.USER_NAME) RequestBody username,
            @Part(Constants.FULL_NAME) RequestBody fullName,
            @Part(Constants.PHONE) RequestBody phone,
            @Part(Constants.GENDER) RequestBody gender,
            @Part(Constants.BIRTH_YEAR) RequestBody birthYear,
            @Part(Constants.HOBBY) RequestBody hobby,
            @Part(Constants.HEIGHT) RequestBody height,
            @Part(Constants.CHAR_TYPE) RequestBody charType);


    @FormUrlEncoded
    @POST("life_helper/users/specific_user.php")
    Call<Contacts> userData(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.PASSWORD) String email);

    @FormUrlEncoded
    @POST("life_helper/users/forgot_pass.php")
    Call<Contacts> forgotPass(
      @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/users/change_pass.php")
    Call<Contacts> changePass(
            @Field(Constants.PASSWORD) String password,
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/dating/choose.php")
    Call<Contacts> findPartner(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/dating/remove.php")
    Call<Contacts> removePartner(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/dating/get_partner_data.php")
    Call<Contacts> getPartnerData(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/users/user_data.php")
    Call<List<Contacts>> getPeopleData(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.LIMIT) int limit,
            @Field(Constants.OFF_SET) int offset,
            @Field(Constants.TYPE) String type
    );

    @FormUrlEncoded
    @POST("life_helper/dating/interaction.php")
    Call<Contacts> interacted(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.PEOPLE_USER) String peopleUsername,
            @Field(Constants.INTERACTED) String interacted
    );
    @FormUrlEncoded
    @POST("life_helper/dating/selected_partner.php")
    Call<Contacts> selectPartner(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/users/wallet/add.php")
    Call<Contacts> addMoneyToWallet(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.CART_CONTENT) String cartContent,
            @Field(Constants.PASSWORD) String password,
            @Field(Constants.AMOUNT) String amount
    );

    @FormUrlEncoded
    @POST("life_helper/users/wallet/add_to_db.php")
    Call<Contacts> addMoneyToDB(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.PASSWORD) String password,
            @Field(Constants.AMOUNT) String amount,
            @Field(Constants.DATE_TIME) String datetime,
            @Field(Constants.TIMEZONE) String timezone
    );


    @Multipart
    @POST("life_helper/restaurants/create.php")
    Call<Contacts> createRestaurant(
            @Part MultipartBody.Part file,
            @Part(Constants.USER_NAME) RequestBody username,
            @Part(Constants.RESTAURANT_NAME) RequestBody restaurantName,
            @Part(Constants.ADDRESS) RequestBody address,
            @Part(Constants.CITY) RequestBody city,
            @Part(Constants.COUNTRY) RequestBody country,
            @Part(Constants.PHONE) RequestBody phone,
            @Part(Constants.OPENING_HOURS) RequestBody openingHours,
            @Part(Constants.CLOSING_HOURS) RequestBody closingHours,
            @Part(Constants.MAP_LINK_ADDRESS) RequestBody mapLinkAddress
    );

    @FormUrlEncoded
    @POST("life_helper/restaurants/get_restaurants_data.php")
    Call<List<Contacts>> getRestaurantsData(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.LIMIT) int limit,
            @Field(Constants.OFF_SET) int offset,
            @Field(Constants.CITY) String city
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/create.php")
    Call<Contacts> createMessageList(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/message_list.php")
    Call<List<Contacts>> getMessageList(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.LIMIT) int limit,
            @Field(Constants.OFF_SET) int offset
    );
    @FormUrlEncoded
    @POST("life_helper/dating/message/chat.php")
    Call<List<Contacts>> getChat(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.SENDER) String sender,
            @Field(Constants.LIMIT) int limit,
            @Field(Constants.OFF_SET) int offset
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/send_message.php")
    Call<Contacts> sendMessage(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.TO) String to,
            @Field(Constants.MESSAGE) String message
    );
    @FormUrlEncoded
    @POST("life_helper/dating/message/load_newest_messages.php")
    Call<List<Contacts>> getNewMessages(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.SENDER) String sender,
            @Field(Constants.TIMESTAMP) String timestamp
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/check_for_newest_messages.php")
    Call<Contacts> checkForNewMessages(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.SENDER) String sender,
            @Field(Constants.TIMESTAMP) String timestamp
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/message_seen.php")
    Call<Contacts> messageSeen(
            @Field(Constants.USER_NAME) String username,
            @Field(Constants.TO) String to
    );

    @FormUrlEncoded
    @POST("life_helper/dating/message/read_receipt_only.php")
    Call<Contacts> changeReadReceipt(
            @Field(Constants.ID) String id
    );

    @FormUrlEncoded
    @POST("life_helper/users/wallet/load_transaction.php")
    Call<List<Contacts>> getTransactionDetails(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/users/wallet/available_money_in_wallet.php")
    Call<Contacts> getAvailableMoneyData(
            @Field(Constants.USER_NAME) String username
    );

    @FormUrlEncoded
    @POST("life_helper/restaurants/feed/get_feed_data.php")
    Call<List<Contacts>> getRestaurantFeed(
            @Field(Constants.ID) String restaurantId,
            @Field(Constants.LIMIT) int limit,
            @Field(Constants.OFF_SET) int offset
    );

    @FormUrlEncoded
    @POST("life_helper/restaurants/feed/create/get_feed_data.php")
    Call<Contacts> post_feedWithoutImg(
            @Field(Constants.ID) String restaurantId,
            @Field(Constants.DESC) String desc
    );
}
