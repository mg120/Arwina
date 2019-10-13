package com.tamiuz.arwina.networking;


import com.tamiuz.arwina.Models.AllCompaniesModel;
import com.tamiuz.arwina.Models.ContactUsResponseModel;
import com.tamiuz.arwina.Models.DeliversResponseModel;
import com.tamiuz.arwina.Models.EditProductResponseModel;
import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.Models.NotifationsModel;
import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.Models.RegisterResponseModel;
import com.tamiuz.arwina.Models.SettingsInfoModel;
import com.tamiuz.arwina.Models.UnReadMesagesResponseModel;
import com.tamiuz.arwina.Models.UserLoginModel;
import com.tamiuz.arwina.Models.ForgetPasswordModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiServiceInterface {

    // ---------------- LogIn -------------------------------
    @FormUrlEncoded
    @POST("api/login")
    Call<UserLoginModel> userLogin(@Field("phone") String phone,
                                   @Field("password") String password,
                                   @Field("firebase_token") String firebase_token);

    // ---------------- Regsiter -----------------------------
    @Multipart
    @POST("api/register")
    Call<RegisterResponseModel> register(@Query("role") int role,
                                         @Part("name") RequestBody name,
                                         @Query("phone") String phone,
                                         @Query("password") String password,
                                         @Query("commercialreg") String commercialreg,
                                         @Query("commercialregno") String commercialregno,
                                         @Query("address") String address,
                                         @Query("havedelivery") String havedelivery,
                                         @Query("firebase_token") String firebase_token,
                                         @Query("lat") double lat,
                                         @Query("lng") double lng,
                                         @Part MultipartBody.Part user_image,
                                         @Part MultipartBody.Part car_image,
                                         @Part MultipartBody.Part lisence_image,
                                         @Part MultipartBody.Part identity_image );


    // ----------------- Profile Data ------------------------------
    @FormUrlEncoded
    @POST("api/profile")
    Call<UserLoginModel> profile_Data(@Field("user_id") int user_id);


    // ---------------- Update Profile -----------------------------
    @Multipart
    @POST("api/updateprofile")
    Call<RegisterResponseModel> update_profile(@Query("user_id") int user_id,
                                               @Part("name") RequestBody name,
                                               @Query("phone") String phone,
                                               @Query("commercialreg") String commercialreg,
                                               @Query("commercialregno") String commercialregno,
                                               @Query("address") String address,
                                               @Query("havedelivery") String havedelivery,
                                               @Query("lat") double lat,
                                               @Query("lng") double lng,
                                               @Part MultipartBody.Part user_image,
                                               @Part MultipartBody.Part carimage,
                                               @Part MultipartBody.Part licenseimage,
                                               @Part MultipartBody.Part identityimage
    );


    // ------------------ Reset Password ------------------------------------
    // 1 ----------------   Forget Password ----------------------
    @FormUrlEncoded
    @POST("api/forgetpassword")
    Call<ForgetPasswordModel> forget_password(@Field("phone") String phone);

    // 2 ----------------   Forget Password ----------------------
    @FormUrlEncoded
    @POST("api/activcode")
    Call<ForgetPasswordModel> send_code(@Field("phone") String phone,
                                        @Field("forgetcode") String forgetcode);

    // 3 ----------------   New Password ----------------------
    @FormUrlEncoded
    @POST("api/rechangepass")
    Call<ForgetPasswordModel> new_password(@Field("phone") String phone,
                                           @Field("new_password") String new_password);


    // ---------------- All Companies ---------------------------
    @POST("api/allcompanies")
    Call<AllCompaniesModel> getAllCompanies();


    // ---------------- Un Read Notifications ---------------------------
    @POST("api/unreadnotificationcount")
    Call<UnReadMesagesResponseModel> unReadNotifications(@Query("user_id") int user_id);


    // ---------------- Company Products ---------------------------
    @POST("api/allitems")
    Call<ProductsModel> company_Products(@Query("trader_id") int trader_id);


    // ---------------- Company Products ---------------------------
    @POST("api/alldelivers")
    Call<DeliversResponseModel> get_Delivers();


    // ---------------- My Products -----------------------------
    @FormUrlEncoded
    @POST("api/myitems")
    Call<ProductsModel> getUser_Products(@Field("user_id") int user_id);


    // ---------------- My Orders -----------------------------
    @FormUrlEncoded
    @POST("api/myorders")
    Call<OrdersModel> getUser_Orders(@Field("user_id") int user_id);


    // ---------------- Add Product ------------------------------
    @Multipart
    @POST("api/saveitem")
    Call<MakeOrderResponseModel> add_product(@Query("trader_id") int trader_id,
                                             @Part("title") RequestBody title,
                                             @Query("qty") String qty,
                                             @Query("maxqty") String maxqty,
                                             @Query("address") String address,
                                             @Query("desc") String desc,
                                             @Query("price") String price,
                                             @Part MultipartBody.Part user_image);


    // ---------------- Update Product ------------------------------
    @Multipart
    @POST("api/updateitem")
    Call<EditProductResponseModel> update_product(@Query("item_id") int item_id,
                                                  @Query("trader_id") int trader_id,
                                                  @Part("title") RequestBody title,
                                                  @Query("qty") String qty,
                                                  @Query("maxqty") String maxqty,
                                                  @Query("address") String address,
                                                  @Query("desc") String desc,
                                                  @Query("price") String price,
                                                  @Part MultipartBody.Part user_image);


    // ---------------- Delete Product -----------------------------
    @FormUrlEncoded
    @POST("api/delitem")
    Call<ForgetPasswordModel> delete_product(@Field("item_id") int item_id,
                                             @Field("trader_id") int trader_id);


    // ---------------- Make Order ------------------------------
    @FormUrlEncoded
    @POST("api/makeorder")
    Call<MakeOrderResponseModel> order_product(@Field("user_id") int user_id,
                                               @Field("company_id") int company_id,
                                               @Field("item_id") int item_id,
                                               @Field("placelat") double placelat,
                                               @Field("placelng") double placelng,
                                               @Field("placeaddress") String placeaddress,
                                               @Field("qty") String qty,
                                               @Field("price") int price,
                                               @Field("delivery_id") int delivery_id,
                                               @Field("deliverdate") String deliverdate,
                                               @Field("timetype") String timetype,
                                               @Field("desc") String desc);

    // ---------------- Update Order -------------------------------
    @FormUrlEncoded
    @POST("api/orderprocess")
    Call<MakeOrderResponseModel> update_order(@Field("order_id") int order_id,
                                              @Field("user_id") int user_id);


    // ---------------- Setting Info -------------------------------
    @FormUrlEncoded
    @POST("api/settinginfo")
    Call<SettingsInfoModel> getSettingsInfo(@Field("lang") String lang);


    // ---------------- Contact Us ---------------------------------
    @FormUrlEncoded
    @POST("api/contactus")
    Call<ContactUsResponseModel> contact_Message(@Field("name") String name,
                                                 @Field("phone") String phone,
                                                 @Field("message") String message);


    // ---------------- Notifications ------------------------------
    @FormUrlEncoded
    @POST("api/mynotification")
    Call<NotifationsModel> getNotifications(@Field("user_id") int user_id);

}