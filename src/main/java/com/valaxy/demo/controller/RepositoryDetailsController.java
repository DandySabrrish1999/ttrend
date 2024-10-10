package com.stalin.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class RepositoryDetailsController {


	// Create a logger instance
	private static final Logger logger = LoggerFactory.getLogger(RepositoryDetailsController.class);





    @Autowired
    private Environment env;

	@RequestMapping("/")
	public String getRepos() throws IOException {
		GitHub github = new GitHubBuilder().withPassword("valaxytech@gmail.com", "XXXXXXXX").build();
		GHRepositorySearchBuilder builder = github.searchRepositories();
		return "This is dandy sabarish, aspiring devops enginner to get into FAANG";
	}

	@GetMapping("/trends")
	public Map<String, String> getTwitterTrends(@RequestParam("placeid") String trendPlace, @RequestParam("count") String trendCount) {
		String consumerKey = env.getProperty("CONSUMER_KEY");
		String consumerSecret = env.getProperty("CONSUMER_SECRET");
		String accessToken = env.getProperty("ACCESS_TOKEN");
		String accessTokenSecret = env.getProperty("ACCESS_TOKEN_SECRET");
		logger.info("consumerKey: {} consumerSecret: {} accessToken: {} accessTokenSecret: {}", 
                    consumerKey, consumerSecret, accessToken, accessTokenSecret);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		        .setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		logger.info("Twitter Factory: {}", tf);
		Twitter twitter = tf.getInstance();
		logger.info("Twitter object: {}", twitter);
		Map<String, String> trendDetails = new HashMap<>();
		try {
			Trends trends = twitter.getPlaceTrends(Integer.parseInt(trendPlace));
			logger.info("After API call");
			int count = 0;
			for (Trend trend : trends.getTrends()) {
				if (count < Integer.parseInt(trendCount)) {
					trendDetails.put(trend.getName(), trend.getURL());
					count++;
				}
			}
		} catch (TwitterException e) {
			logger.error("Twitter exception occurred", e);
			trendDetails.put("test", "MyTweet");
            
			

		}catch (Exception e) {
			logger.error("Exception occurred", e);
			trendDetails.put("test", "MyTweet");
            
		}
		return trendDetails;
	}

}
