package com.example.daniel.assistme;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfig {
    private ConfigurationBuilder configurationBuilder;
    private Twitter twitter;
    private TwitterFactory twitterFactory;


    private final static String CONSUMER_KEY = "9wBQDd7dSlWyyFxLK2mtDBCUv";
    private final static String CONSUMER_SECRET_KEY = "UlhZOTiraUnxiaQeTMHJYVDRNQg3yZLNwVknzTAztDytT9L6RO";
    private final static String ACCESS_TOKEN = "1155080438-IdE9AiefXP6AJcUpWxK8YGzHyw0xLnnoG3jZIlB";
    private final static String ACCESS_TOKEN_SECRET = "44Qn95kaID40ZezsE5XZrMrG237iT9OaG3A2LAa3q1624";

    public void configTwitter() {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET_KEY);
        configurationBuilder.setOAuthAccessToken(ACCESS_TOKEN);
        configurationBuilder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance();
    }
    public Twitter getTwitterInstance(){
        return twitter;
    }
}
