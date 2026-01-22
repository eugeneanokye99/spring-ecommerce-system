package com.shopjoy.util;

import com.shopjoy.entity.User;

import java.util.Comparator;

public class UserComparators {
    
    public static final Comparator<User> BY_USERNAME_ASC = 
        Comparator.comparing(User::getUsername, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<User> BY_USERNAME_DESC = 
        Comparator.comparing(User::getUsername, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<User> BY_EMAIL_ASC = 
        Comparator.comparing(User::getEmail, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<User> BY_EMAIL_DESC = 
        Comparator.comparing(User::getEmail, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<User> BY_CREATED_DATE_ASC = 
        Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<User> BY_CREATED_DATE_DESC = 
        Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<User> BY_ID_ASC = 
        Comparator.comparing(User::getUserId, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<User> BY_ID_DESC = 
        Comparator.comparing(User::getUserId, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static Comparator<User> getComparator(String sortBy, String direction) {
        boolean isDesc = "DESC".equalsIgnoreCase(direction);
        
        switch (sortBy.toLowerCase()) {
            case "username":
                return isDesc ? BY_USERNAME_DESC : BY_USERNAME_ASC;
            case "email":
                return isDesc ? BY_EMAIL_DESC : BY_EMAIL_ASC;
            case "createdat":
            case "created_at":
                return isDesc ? BY_CREATED_DATE_DESC : BY_CREATED_DATE_ASC;
            case "id":
            case "userid":
                return isDesc ? BY_ID_DESC : BY_ID_ASC;
            default:
                return isDesc ? BY_ID_DESC : BY_ID_ASC;
        }
    }
}
