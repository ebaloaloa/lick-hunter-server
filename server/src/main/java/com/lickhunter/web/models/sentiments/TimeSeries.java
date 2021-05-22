
package com.lickhunter.web.models.sentiments;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "asset_id",
    "time",
    "open",
    "close",
    "high",
    "low",
    "volume",
    "market_cap",
    "url_shares",
    "unique_url_shares",
    "reddit_posts",
    "reddit_posts_score",
    "reddit_comments",
    "reddit_comments_score",
    "tweets",
    "tweet_spam",
    "tweet_followers",
    "tweet_quotes",
    "tweet_retweets",
    "tweet_replies",
    "tweet_favorites",
    "tweet_sentiment1",
    "tweet_sentiment2",
    "tweet_sentiment3",
    "tweet_sentiment4",
    "tweet_sentiment5",
    "tweet_sentiment_impact1",
    "tweet_sentiment_impact2",
    "tweet_sentiment_impact3",
    "tweet_sentiment_impact4",
    "tweet_sentiment_impact5",
    "social_score",
    "average_sentiment",
    "sentiment_absolute",
    "sentiment_relative",
    "search_average",
    "news",
    "price_score",
    "social_impact_score",
    "correlation_rank",
    "galaxy_score",
    "volatility",
    "alt_rank",
    "alt_rank_30d",
    "market_cap_rank",
    "percent_change_24h_rank",
    "volume_24h_rank",
    "social_volume_24h_rank",
    "social_score_24h_rank",
    "medium",
    "youtube",
    "social_contributors",
    "social_volume",
    "price_btc",
    "social_volume_global",
    "social_dominance",
    "percent_change_24h",
    "market_cap_global",
    "market_dominance"
})
public class TimeSeries {

    @JsonProperty("asset_id")
    private Integer assetId;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("open")
    private Double open;
    @JsonProperty("close")
    private Double close;
    @JsonProperty("high")
    private Double high;
    @JsonProperty("low")
    private Double low;
    @JsonProperty("volume")
    private Long volume;
    @JsonProperty("market_cap")
    private Long marketCap;
    @JsonProperty("url_shares")
    private Integer urlShares;
    @JsonProperty("unique_url_shares")
    private Integer uniqueUrlShares;
    @JsonProperty("reddit_posts")
    private Integer redditPosts;
    @JsonProperty("reddit_posts_score")
    private Integer redditPostsScore;
    @JsonProperty("reddit_comments")
    private Integer redditComments;
    @JsonProperty("reddit_comments_score")
    private Integer redditCommentsScore;
    @JsonProperty("tweets")
    private Integer tweets;
    @JsonProperty("tweet_spam")
    private Integer tweetSpam;
    @JsonProperty("tweet_followers")
    private Integer tweetFollowers;
    @JsonProperty("tweet_quotes")
    private Integer tweetQuotes;
    @JsonProperty("tweet_retweets")
    private Integer tweetRetweets;
    @JsonProperty("tweet_replies")
    private Integer tweetReplies;
    @JsonProperty("tweet_favorites")
    private Integer tweetFavorites;
    @JsonProperty("tweet_sentiment1")
    private Integer tweetSentiment1;
    @JsonProperty("tweet_sentiment2")
    private Integer tweetSentiment2;
    @JsonProperty("tweet_sentiment3")
    private Integer tweetSentiment3;
    @JsonProperty("tweet_sentiment4")
    private Integer tweetSentiment4;
    @JsonProperty("tweet_sentiment5")
    private Integer tweetSentiment5;
    @JsonProperty("tweet_sentiment_impact1")
    private Integer tweetSentimentImpact1;
    @JsonProperty("tweet_sentiment_impact2")
    private Integer tweetSentimentImpact2;
    @JsonProperty("tweet_sentiment_impact3")
    private Integer tweetSentimentImpact3;
    @JsonProperty("tweet_sentiment_impact4")
    private Integer tweetSentimentImpact4;
    @JsonProperty("tweet_sentiment_impact5")
    private Integer tweetSentimentImpact5;
    @JsonProperty("social_score")
    private Integer socialScore;
    @JsonProperty("average_sentiment")
    private Double averageSentiment;
    @JsonProperty("sentiment_absolute")
    private Integer sentimentAbsolute;
    @JsonProperty("sentiment_relative")
    private Integer sentimentRelative;
    @JsonProperty("search_average")
    private Object searchAverage;
    @JsonProperty("news")
    private Integer news;
    @JsonProperty("price_score")
    private Double priceScore;
    @JsonProperty("social_impact_score")
    private Double socialImpactScore;
    @JsonProperty("correlation_rank")
    private Integer correlationRank;
    @JsonProperty("galaxy_score")
    private Double galaxyScore;
    @JsonProperty("volatility")
    private Double volatility;
    @JsonProperty("alt_rank")
    private Integer altRank;
    @JsonProperty("alt_rank_30d")
    private Integer altRank30d;
    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;
    @JsonProperty("percent_change_24h_rank")
    private Integer percentChange24hRank;
    @JsonProperty("volume_24h_rank")
    private Integer volume24hRank;
    @JsonProperty("social_volume_24h_rank")
    private Integer socialVolume24hRank;
    @JsonProperty("social_score_24h_rank")
    private Integer socialScore24hRank;
    @JsonProperty("medium")
    private Object medium;
    @JsonProperty("youtube")
    private Object youtube;
    @JsonProperty("social_contributors")
    private Integer socialContributors;
    @JsonProperty("social_volume")
    private Integer socialVolume;
    @JsonProperty("price_btc")
    private Integer priceBtc;
    @JsonProperty("social_volume_global")
    private Integer socialVolumeGlobal;
    @JsonProperty("social_dominance")
    private Double socialDominance;
    @JsonProperty("percent_change_24h")
    private Double percentChange24h;
    @JsonProperty("market_cap_global")
    private Long marketCapGlobal;
    @JsonProperty("market_dominance")
    private Double marketDominance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("asset_id")
    public Integer getAssetId() {
        return assetId;
    }

    @JsonProperty("asset_id")
    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    @JsonProperty("time")
    public Integer getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Integer time) {
        this.time = time;
    }

    @JsonProperty("open")
    public Double getOpen() {
        return open;
    }

    @JsonProperty("open")
    public void setOpen(Double open) {
        this.open = open;
    }

    @JsonProperty("close")
    public Double getClose() {
        return close;
    }

    @JsonProperty("close")
    public void setClose(Double close) {
        this.close = close;
    }

    @JsonProperty("high")
    public Double getHigh() {
        return high;
    }

    @JsonProperty("high")
    public void setHigh(Double high) {
        this.high = high;
    }

    @JsonProperty("low")
    public Double getLow() {
        return low;
    }

    @JsonProperty("low")
    public void setLow(Double low) {
        this.low = low;
    }

    @JsonProperty("volume")
    public Long getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @JsonProperty("market_cap")
    public Long getMarketCap() {
        return marketCap;
    }

    @JsonProperty("market_cap")
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    @JsonProperty("url_shares")
    public Integer getUrlShares() {
        return urlShares;
    }

    @JsonProperty("url_shares")
    public void setUrlShares(Integer urlShares) {
        this.urlShares = urlShares;
    }

    @JsonProperty("unique_url_shares")
    public Integer getUniqueUrlShares() {
        return uniqueUrlShares;
    }

    @JsonProperty("unique_url_shares")
    public void setUniqueUrlShares(Integer uniqueUrlShares) {
        this.uniqueUrlShares = uniqueUrlShares;
    }

    @JsonProperty("reddit_posts")
    public Integer getRedditPosts() {
        return redditPosts;
    }

    @JsonProperty("reddit_posts")
    public void setRedditPosts(Integer redditPosts) {
        this.redditPosts = redditPosts;
    }

    @JsonProperty("reddit_posts_score")
    public Integer getRedditPostsScore() {
        return redditPostsScore;
    }

    @JsonProperty("reddit_posts_score")
    public void setRedditPostsScore(Integer redditPostsScore) {
        this.redditPostsScore = redditPostsScore;
    }

    @JsonProperty("reddit_comments")
    public Integer getRedditComments() {
        return redditComments;
    }

    @JsonProperty("reddit_comments")
    public void setRedditComments(Integer redditComments) {
        this.redditComments = redditComments;
    }

    @JsonProperty("reddit_comments_score")
    public Integer getRedditCommentsScore() {
        return redditCommentsScore;
    }

    @JsonProperty("reddit_comments_score")
    public void setRedditCommentsScore(Integer redditCommentsScore) {
        this.redditCommentsScore = redditCommentsScore;
    }

    @JsonProperty("tweets")
    public Integer getTweets() {
        return tweets;
    }

    @JsonProperty("tweets")
    public void setTweets(Integer tweets) {
        this.tweets = tweets;
    }

    @JsonProperty("tweet_spam")
    public Integer getTweetSpam() {
        return tweetSpam;
    }

    @JsonProperty("tweet_spam")
    public void setTweetSpam(Integer tweetSpam) {
        this.tweetSpam = tweetSpam;
    }

    @JsonProperty("tweet_followers")
    public Integer getTweetFollowers() {
        return tweetFollowers;
    }

    @JsonProperty("tweet_followers")
    public void setTweetFollowers(Integer tweetFollowers) {
        this.tweetFollowers = tweetFollowers;
    }

    @JsonProperty("tweet_quotes")
    public Integer getTweetQuotes() {
        return tweetQuotes;
    }

    @JsonProperty("tweet_quotes")
    public void setTweetQuotes(Integer tweetQuotes) {
        this.tweetQuotes = tweetQuotes;
    }

    @JsonProperty("tweet_retweets")
    public Integer getTweetRetweets() {
        return tweetRetweets;
    }

    @JsonProperty("tweet_retweets")
    public void setTweetRetweets(Integer tweetRetweets) {
        this.tweetRetweets = tweetRetweets;
    }

    @JsonProperty("tweet_replies")
    public Integer getTweetReplies() {
        return tweetReplies;
    }

    @JsonProperty("tweet_replies")
    public void setTweetReplies(Integer tweetReplies) {
        this.tweetReplies = tweetReplies;
    }

    @JsonProperty("tweet_favorites")
    public Integer getTweetFavorites() {
        return tweetFavorites;
    }

    @JsonProperty("tweet_favorites")
    public void setTweetFavorites(Integer tweetFavorites) {
        this.tweetFavorites = tweetFavorites;
    }

    @JsonProperty("tweet_sentiment1")
    public Integer getTweetSentiment1() {
        return tweetSentiment1;
    }

    @JsonProperty("tweet_sentiment1")
    public void setTweetSentiment1(Integer tweetSentiment1) {
        this.tweetSentiment1 = tweetSentiment1;
    }

    @JsonProperty("tweet_sentiment2")
    public Integer getTweetSentiment2() {
        return tweetSentiment2;
    }

    @JsonProperty("tweet_sentiment2")
    public void setTweetSentiment2(Integer tweetSentiment2) {
        this.tweetSentiment2 = tweetSentiment2;
    }

    @JsonProperty("tweet_sentiment3")
    public Integer getTweetSentiment3() {
        return tweetSentiment3;
    }

    @JsonProperty("tweet_sentiment3")
    public void setTweetSentiment3(Integer tweetSentiment3) {
        this.tweetSentiment3 = tweetSentiment3;
    }

    @JsonProperty("tweet_sentiment4")
    public Integer getTweetSentiment4() {
        return tweetSentiment4;
    }

    @JsonProperty("tweet_sentiment4")
    public void setTweetSentiment4(Integer tweetSentiment4) {
        this.tweetSentiment4 = tweetSentiment4;
    }

    @JsonProperty("tweet_sentiment5")
    public Integer getTweetSentiment5() {
        return tweetSentiment5;
    }

    @JsonProperty("tweet_sentiment5")
    public void setTweetSentiment5(Integer tweetSentiment5) {
        this.tweetSentiment5 = tweetSentiment5;
    }

    @JsonProperty("tweet_sentiment_impact1")
    public Integer getTweetSentimentImpact1() {
        return tweetSentimentImpact1;
    }

    @JsonProperty("tweet_sentiment_impact1")
    public void setTweetSentimentImpact1(Integer tweetSentimentImpact1) {
        this.tweetSentimentImpact1 = tweetSentimentImpact1;
    }

    @JsonProperty("tweet_sentiment_impact2")
    public Integer getTweetSentimentImpact2() {
        return tweetSentimentImpact2;
    }

    @JsonProperty("tweet_sentiment_impact2")
    public void setTweetSentimentImpact2(Integer tweetSentimentImpact2) {
        this.tweetSentimentImpact2 = tweetSentimentImpact2;
    }

    @JsonProperty("tweet_sentiment_impact3")
    public Integer getTweetSentimentImpact3() {
        return tweetSentimentImpact3;
    }

    @JsonProperty("tweet_sentiment_impact3")
    public void setTweetSentimentImpact3(Integer tweetSentimentImpact3) {
        this.tweetSentimentImpact3 = tweetSentimentImpact3;
    }

    @JsonProperty("tweet_sentiment_impact4")
    public Integer getTweetSentimentImpact4() {
        return tweetSentimentImpact4;
    }

    @JsonProperty("tweet_sentiment_impact4")
    public void setTweetSentimentImpact4(Integer tweetSentimentImpact4) {
        this.tweetSentimentImpact4 = tweetSentimentImpact4;
    }

    @JsonProperty("tweet_sentiment_impact5")
    public Integer getTweetSentimentImpact5() {
        return tweetSentimentImpact5;
    }

    @JsonProperty("tweet_sentiment_impact5")
    public void setTweetSentimentImpact5(Integer tweetSentimentImpact5) {
        this.tweetSentimentImpact5 = tweetSentimentImpact5;
    }

    @JsonProperty("social_score")
    public Integer getSocialScore() {
        return socialScore;
    }

    @JsonProperty("social_score")
    public void setSocialScore(Integer socialScore) {
        this.socialScore = socialScore;
    }

    @JsonProperty("average_sentiment")
    public Double getAverageSentiment() {
        return averageSentiment;
    }

    @JsonProperty("average_sentiment")
    public void setAverageSentiment(Double averageSentiment) {
        this.averageSentiment = averageSentiment;
    }

    @JsonProperty("sentiment_absolute")
    public Integer getSentimentAbsolute() {
        return sentimentAbsolute;
    }

    @JsonProperty("sentiment_absolute")
    public void setSentimentAbsolute(Integer sentimentAbsolute) {
        this.sentimentAbsolute = sentimentAbsolute;
    }

    @JsonProperty("sentiment_relative")
    public Integer getSentimentRelative() {
        return sentimentRelative;
    }

    @JsonProperty("sentiment_relative")
    public void setSentimentRelative(Integer sentimentRelative) {
        this.sentimentRelative = sentimentRelative;
    }

    @JsonProperty("search_average")
    public Object getSearchAverage() {
        return searchAverage;
    }

    @JsonProperty("search_average")
    public void setSearchAverage(Object searchAverage) {
        this.searchAverage = searchAverage;
    }

    @JsonProperty("news")
    public Integer getNews() {
        return news;
    }

    @JsonProperty("news")
    public void setNews(Integer news) {
        this.news = news;
    }

    @JsonProperty("price_score")
    public Double getPriceScore() {
        return priceScore;
    }

    @JsonProperty("price_score")
    public void setPriceScore(Double priceScore) {
        this.priceScore = priceScore;
    }

    @JsonProperty("social_impact_score")
    public Double getSocialImpactScore() {
        return socialImpactScore;
    }

    @JsonProperty("social_impact_score")
    public void setSocialImpactScore(Double socialImpactScore) {
        this.socialImpactScore = socialImpactScore;
    }

    @JsonProperty("correlation_rank")
    public Integer getCorrelationRank() {
        return correlationRank;
    }

    @JsonProperty("correlation_rank")
    public void setCorrelationRank(Integer correlationRank) {
        this.correlationRank = correlationRank;
    }

    @JsonProperty("galaxy_score")
    public Double getGalaxyScore() {
        return galaxyScore;
    }

    @JsonProperty("galaxy_score")
    public void setGalaxyScore(Double galaxyScore) {
        this.galaxyScore = galaxyScore;
    }

    @JsonProperty("volatility")
    public Double getVolatility() {
        return volatility;
    }

    @JsonProperty("volatility")
    public void setVolatility(Double volatility) {
        this.volatility = volatility;
    }

    @JsonProperty("alt_rank")
    public Integer getAltRank() {
        return altRank;
    }

    @JsonProperty("alt_rank")
    public void setAltRank(Integer altRank) {
        this.altRank = altRank;
    }

    @JsonProperty("alt_rank_30d")
    public Integer getAltRank30d() {
        return altRank30d;
    }

    @JsonProperty("alt_rank_30d")
    public void setAltRank30d(Integer altRank30d) {
        this.altRank30d = altRank30d;
    }

    @JsonProperty("market_cap_rank")
    public Integer getMarketCapRank() {
        return marketCapRank;
    }

    @JsonProperty("market_cap_rank")
    public void setMarketCapRank(Integer marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    @JsonProperty("percent_change_24h_rank")
    public Integer getPercentChange24hRank() {
        return percentChange24hRank;
    }

    @JsonProperty("percent_change_24h_rank")
    public void setPercentChange24hRank(Integer percentChange24hRank) {
        this.percentChange24hRank = percentChange24hRank;
    }

    @JsonProperty("volume_24h_rank")
    public Integer getVolume24hRank() {
        return volume24hRank;
    }

    @JsonProperty("volume_24h_rank")
    public void setVolume24hRank(Integer volume24hRank) {
        this.volume24hRank = volume24hRank;
    }

    @JsonProperty("social_volume_24h_rank")
    public Integer getSocialVolume24hRank() {
        return socialVolume24hRank;
    }

    @JsonProperty("social_volume_24h_rank")
    public void setSocialVolume24hRank(Integer socialVolume24hRank) {
        this.socialVolume24hRank = socialVolume24hRank;
    }

    @JsonProperty("social_score_24h_rank")
    public Integer getSocialScore24hRank() {
        return socialScore24hRank;
    }

    @JsonProperty("social_score_24h_rank")
    public void setSocialScore24hRank(Integer socialScore24hRank) {
        this.socialScore24hRank = socialScore24hRank;
    }

    @JsonProperty("medium")
    public Object getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(Object medium) {
        this.medium = medium;
    }

    @JsonProperty("youtube")
    public Object getYoutube() {
        return youtube;
    }

    @JsonProperty("youtube")
    public void setYoutube(Object youtube) {
        this.youtube = youtube;
    }

    @JsonProperty("social_contributors")
    public Integer getSocialContributors() {
        return socialContributors;
    }

    @JsonProperty("social_contributors")
    public void setSocialContributors(Integer socialContributors) {
        this.socialContributors = socialContributors;
    }

    @JsonProperty("social_volume")
    public Integer getSocialVolume() {
        return socialVolume;
    }

    @JsonProperty("social_volume")
    public void setSocialVolume(Integer socialVolume) {
        this.socialVolume = socialVolume;
    }

    @JsonProperty("price_btc")
    public Integer getPriceBtc() {
        return priceBtc;
    }

    @JsonProperty("price_btc")
    public void setPriceBtc(Integer priceBtc) {
        this.priceBtc = priceBtc;
    }

    @JsonProperty("social_volume_global")
    public Integer getSocialVolumeGlobal() {
        return socialVolumeGlobal;
    }

    @JsonProperty("social_volume_global")
    public void setSocialVolumeGlobal(Integer socialVolumeGlobal) {
        this.socialVolumeGlobal = socialVolumeGlobal;
    }

    @JsonProperty("social_dominance")
    public Double getSocialDominance() {
        return socialDominance;
    }

    @JsonProperty("social_dominance")
    public void setSocialDominance(Double socialDominance) {
        this.socialDominance = socialDominance;
    }

    @JsonProperty("percent_change_24h")
    public Double getPercentChange24h() {
        return percentChange24h;
    }

    @JsonProperty("percent_change_24h")
    public void setPercentChange24h(Double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    @JsonProperty("market_cap_global")
    public Long getMarketCapGlobal() {
        return marketCapGlobal;
    }

    @JsonProperty("market_cap_global")
    public void setMarketCapGlobal(Long marketCapGlobal) {
        this.marketCapGlobal = marketCapGlobal;
    }

    @JsonProperty("market_dominance")
    public Double getMarketDominance() {
        return marketDominance;
    }

    @JsonProperty("market_dominance")
    public void setMarketDominance(Double marketDominance) {
        this.marketDominance = marketDominance;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
