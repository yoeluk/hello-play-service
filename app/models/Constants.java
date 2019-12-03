package models;

import play.libs.typedmap.TypedKey;

import javax.inject.Singleton;

@Singleton
public class Constants {
    public static final String SESSION_USERNAME_KEY = "username";
    public static final String SESSION_USERID_KEY = "userId";
    public static final String SESSION_USEREMAIL_KEY = "userEmail";
    public static final TypedKey<String> authUsername = TypedKey.create("AUTH_USER_NAME");
}
