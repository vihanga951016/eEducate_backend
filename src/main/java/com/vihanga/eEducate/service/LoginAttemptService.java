package com.vihanga.eEducate.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    /*
    * This class is use to prevent brute force attack to our system. The mechanism of this prevention is like this: -
    *
    * Create an in-memory cache to keep count of user attempting times. Maximum attempting times are 8 times. If
    * maximum attemptions over 8 times, that user account will lock or disabled.
    */
    private static final int MAXIMUM_NUMBER_OF_ATTEMPT = 8;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;
    /*
     * This is the simple structure of the cache memory that we are gonna create.
     * USER(key)     ATTEMPTS
     * user1             1
     * user2             2
     * user3             1
     *
     * 'LoadingCache<String, Integer>' In here "String" is the key of the cache.
     * And "Integer" is the number of attempting times.
     */

    public LoginAttemptService(){
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100) // Take 100 users from given time.
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
        });
    }

    /*
     * Add a user into the cache.
     */
    public void addUserToLoginAttemptCache(String username) throws ExecutionException{
        int attempts = 0;
        attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        // This "loginAttemptCache.get(username)" part shows the current attempt times.
        loginAttemptCache.put(username, attempts);
    }

    /*
     * Check the attempting times of users.
     */
    public boolean hasExceededMaxAttempts(String username) throws ExecutionException{
        return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPT;
    }

    /*
     * Find the user from the key and remove that user from the cache.
     */
    public void evictUserFromLoginAttemptCache(String username){
        loginAttemptCache.invalidate(username);
    }
}
