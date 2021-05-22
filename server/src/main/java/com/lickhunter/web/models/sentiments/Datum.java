
package com.lickhunter.web.models.sentiments;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "symbol",
    "price",
    "price_btc",
    "market_cap",
    "percent_change_24h",
    "percent_change_7d",
    "percent_change_30d",
    "volume_24h",
    "max_supply",
    "timeSeries",
    "social_dominance_calc_24h_previous",
    "social_contributors_calc_24h_previous",
    "url_shares_calc_24h_previous",
    "tweet_spam_calc_24h_previous",
    "news_calc_24h_previous",
    "average_sentiment_calc_24h_previous",
    "social_score_calc_24h_previous",
    "social_volume_calc_24h_previous",
    "alt_rank_30d_calc_24h_previous",
    "alt_rank_calc_24h_previous",
    "social_dominance_calc_24h",
    "social_dominance_calc_24h_percent",
    "social_contributors_calc_24h",
    "social_contributors_calc_24h_percent",
    "url_shares_calc_24h",
    "url_shares_calc_24h_percent",
    "tweet_spam_calc_24h",
    "tweet_spam_calc_24h_percent",
    "news_calc_24h",
    "news_calc_24h_percent",
    "average_sentiment_calc_24h",
    "average_sentiment_calc_24h_percent",
    "social_score_calc_24h",
    "social_score_calc_24h_percent",
    "social_volume_calc_24h",
    "social_volume_calc_24h_percent",
    "asset_id",
    "time",
    "open",
    "high",
    "low",
    "volume",
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
    "social_contributors",
    "social_volume",
    "social_volume_global",
    "social_dominance",
    "market_cap_global",
    "market_dominance",
    "tags",
    "close"
})
public class Datum {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("price_btc")
    private Integer priceBtc;
    @JsonProperty("market_cap")
    private Long marketCap;
    @JsonProperty("percent_change_24h")
    private Double percentChange24h;
    @JsonProperty("percent_change_7d")
    private Double percentChange7d;
    @JsonProperty("percent_change_30d")
    private Double percentChange30d;
    @JsonProperty("volume_24h")
    private Double volume24h;
    @JsonProperty("max_supply")
    private String maxSupply;
    @JsonProperty("timeSeries")
    private List<TimeSeries> timeSeries = null;
    @JsonProperty("social_dominance_calc_24h_previous")
    private Integer socialDominanceCalc24hPrevious;
    @JsonProperty("social_contributors_calc_24h_previous")
    private Integer socialContributorsCalc24hPrevious;
    @JsonProperty("url_shares_calc_24h_previous")
    private Integer urlSharesCalc24hPrevious;
    @JsonProperty("tweet_spam_calc_24h_previous")
    private Integer tweetSpamCalc24hPrevious;
    @JsonProperty("news_calc_24h_previous")
    private Integer newsCalc24hPrevious;
    @JsonProperty("average_sentiment_calc_24h_previous")
    private Double averageSentimentCalc24hPrevious;
    @JsonProperty("social_score_calc_24h_previous")
    private Long socialScoreCalc24hPrevious;
    @JsonProperty("social_volume_calc_24h_previous")
    private Integer socialVolumeCalc24hPrevious;
    @JsonProperty("alt_rank_30d_calc_24h_previous")
    private Integer altRank30dCalc24hPrevious;
    @JsonProperty("alt_rank_calc_24h_previous")
    private Integer altRankCalc24hPrevious;
    @JsonProperty("social_dominance_calc_24h")
    private Integer socialDominanceCalc24h;
    @JsonProperty("social_dominance_calc_24h_percent")
    private Integer socialDominanceCalc24hPercent;
    @JsonProperty("social_contributors_calc_24h")
    private Integer socialContributorsCalc24h;
    @JsonProperty("social_contributors_calc_24h_percent")
    private Double socialContributorsCalc24hPercent;
    @JsonProperty("url_shares_calc_24h")
    private Integer urlSharesCalc24h;
    @JsonProperty("url_shares_calc_24h_percent")
    private Double urlSharesCalc24hPercent;
    @JsonProperty("tweet_spam_calc_24h")
    private Integer tweetSpamCalc24h;
    @JsonProperty("tweet_spam_calc_24h_percent")
    private Double tweetSpamCalc24hPercent;
    @JsonProperty("news_calc_24h")
    private Integer newsCalc24h;
    @JsonProperty("news_calc_24h_percent")
    private Double newsCalc24hPercent;
    @JsonProperty("average_sentiment_calc_24h")
    private Double averageSentimentCalc24h;
    @JsonProperty("average_sentiment_calc_24h_percent")
    private Integer averageSentimentCalc24hPercent;
    @JsonProperty("social_score_calc_24h")
    private Long socialScoreCalc24h;
    @JsonProperty("social_score_calc_24h_percent")
    private Double socialScoreCalc24hPercent;
    @JsonProperty("social_volume_calc_24h")
    private Integer socialVolumeCalc24h;
    @JsonProperty("social_volume_calc_24h_percent")
    private Double socialVolumeCalc24hPercent;
    @JsonProperty("asset_id")
    private Integer assetId;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("open")
    private Double open;
    @JsonProperty("high")
    private Double high;
    @JsonProperty("low")
    private Double low;
    @JsonProperty("volume")
    private Long volume;
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
    @JsonProperty("social_contributors")
    private Integer socialContributors;
    @JsonProperty("social_volume")
    private Integer socialVolume;
    @JsonProperty("social_volume_global")
    private Integer socialVolumeGlobal;
    @JsonProperty("social_dominance")
    private Double socialDominance;
    @JsonProperty("market_cap_global")
    private Long marketCapGlobal;
    @JsonProperty("market_dominance")
    private Double marketDominance;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("close")
    private Double close;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("price_btc")
    public Integer getPriceBtc() {
        return priceBtc;
    }

    @JsonProperty("price_btc")
    public void setPriceBtc(Integer priceBtc) {
        this.priceBtc = priceBtc;
    }

    @JsonProperty("market_cap")
    public Long getMarketCap() {
        return marketCap;
    }

    @JsonProperty("market_cap")
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    @JsonProperty("percent_change_24h")
    public Double getPercentChange24h() {
        return percentChange24h;
    }

    @JsonProperty("percent_change_24h")
    public void setPercentChange24h(Double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    @JsonProperty("percent_change_7d")
    public Double getPercentChange7d() {
        return percentChange7d;
    }

    @JsonProperty("percent_change_7d")
    public void setPercentChange7d(Double percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    @JsonProperty("percent_change_30d")
    public Double getPercentChange30d() {
        return percentChange30d;
    }

    @JsonProperty("percent_change_30d")
    public void setPercentChange30d(Double percentChange30d) {
        this.percentChange30d = percentChange30d;
    }

    @JsonProperty("volume_24h")
    public Double getVolume24h() {
        return volume24h;
    }

    @JsonProperty("volume_24h")
    public void setVolume24h(Double volume24h) {
        this.volume24h = volume24h;
    }

    @JsonProperty("max_supply")
    public String getMaxSupply() {
        return maxSupply;
    }

    @JsonProperty("max_supply")
    public void setMaxSupply(String maxSupply) {
        this.maxSupply = maxSupply;
    }

    @JsonProperty("timeSeries")
    public List<TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    @JsonProperty("timeSeries")
    public void setTimeSeries(List<TimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }

    @JsonProperty("social_dominance_calc_24h_previous")
    public Integer getSocialDominanceCalc24hPrevious() {
        return socialDominanceCalc24hPrevious;
    }

    @JsonProperty("social_dominance_calc_24h_previous")
    public void setSocialDominanceCalc24hPrevious(Integer socialDominanceCalc24hPrevious) {
        this.socialDominanceCalc24hPrevious = socialDominanceCalc24hPrevious;
    }

    @JsonProperty("social_contributors_calc_24h_previous")
    public Integer getSocialContributorsCalc24hPrevious() {
        return socialContributorsCalc24hPrevious;
    }

    @JsonProperty("social_contributors_calc_24h_previous")
    public void setSocialContributorsCalc24hPrevious(Integer socialContributorsCalc24hPrevious) {
        this.socialContributorsCalc24hPrevious = socialContributorsCalc24hPrevious;
    }

    @JsonProperty("url_shares_calc_24h_previous")
    public Integer getUrlSharesCalc24hPrevious() {
        return urlSharesCalc24hPrevious;
    }

    @JsonProperty("url_shares_calc_24h_previous")
    public void setUrlSharesCalc24hPrevious(Integer urlSharesCalc24hPrevious) {
        this.urlSharesCalc24hPrevious = urlSharesCalc24hPrevious;
    }

    @JsonProperty("tweet_spam_calc_24h_previous")
    public Integer getTweetSpamCalc24hPrevious() {
        return tweetSpamCalc24hPrevious;
    }

    @JsonProperty("tweet_spam_calc_24h_previous")
    public void setTweetSpamCalc24hPrevious(Integer tweetSpamCalc24hPrevious) {
        this.tweetSpamCalc24hPrevious = tweetSpamCalc24hPrevious;
    }

    @JsonProperty("news_calc_24h_previous")
    public Integer getNewsCalc24hPrevious() {
        return newsCalc24hPrevious;
    }

    @JsonProperty("news_calc_24h_previous")
    public void setNewsCalc24hPrevious(Integer newsCalc24hPrevious) {
        this.newsCalc24hPrevious = newsCalc24hPrevious;
    }

    @JsonProperty("average_sentiment_calc_24h_previous")
    public Double getAverageSentimentCalc24hPrevious() {
        return averageSentimentCalc24hPrevious;
    }

    @JsonProperty("average_sentiment_calc_24h_previous")
    public void setAverageSentimentCalc24hPrevious(Double averageSentimentCalc24hPrevious) {
        this.averageSentimentCalc24hPrevious = averageSentimentCalc24hPrevious;
    }

    @JsonProperty("social_score_calc_24h_previous")
    public Long getSocialScoreCalc24hPrevious() {
        return socialScoreCalc24hPrevious;
    }

    @JsonProperty("social_score_calc_24h_previous")
    public void setSocialScoreCalc24hPrevious(Long socialScoreCalc24hPrevious) {
        this.socialScoreCalc24hPrevious = socialScoreCalc24hPrevious;
    }

    @JsonProperty("social_volume_calc_24h_previous")
    public Integer getSocialVolumeCalc24hPrevious() {
        return socialVolumeCalc24hPrevious;
    }

    @JsonProperty("social_volume_calc_24h_previous")
    public void setSocialVolumeCalc24hPrevious(Integer socialVolumeCalc24hPrevious) {
        this.socialVolumeCalc24hPrevious = socialVolumeCalc24hPrevious;
    }

    @JsonProperty("alt_rank_30d_calc_24h_previous")
    public Integer getAltRank30dCalc24hPrevious() {
        return altRank30dCalc24hPrevious;
    }

    @JsonProperty("alt_rank_30d_calc_24h_previous")
    public void setAltRank30dCalc24hPrevious(Integer altRank30dCalc24hPrevious) {
        this.altRank30dCalc24hPrevious = altRank30dCalc24hPrevious;
    }

    @JsonProperty("alt_rank_calc_24h_previous")
    public Integer getAltRankCalc24hPrevious() {
        return altRankCalc24hPrevious;
    }

    @JsonProperty("alt_rank_calc_24h_previous")
    public void setAltRankCalc24hPrevious(Integer altRankCalc24hPrevious) {
        this.altRankCalc24hPrevious = altRankCalc24hPrevious;
    }

    @JsonProperty("social_dominance_calc_24h")
    public Integer getSocialDominanceCalc24h() {
        return socialDominanceCalc24h;
    }

    @JsonProperty("social_dominance_calc_24h")
    public void setSocialDominanceCalc24h(Integer socialDominanceCalc24h) {
        this.socialDominanceCalc24h = socialDominanceCalc24h;
    }

    @JsonProperty("social_dominance_calc_24h_percent")
    public Integer getSocialDominanceCalc24hPercent() {
        return socialDominanceCalc24hPercent;
    }

    @JsonProperty("social_dominance_calc_24h_percent")
    public void setSocialDominanceCalc24hPercent(Integer socialDominanceCalc24hPercent) {
        this.socialDominanceCalc24hPercent = socialDominanceCalc24hPercent;
    }

    @JsonProperty("social_contributors_calc_24h")
    public Integer getSocialContributorsCalc24h() {
        return socialContributorsCalc24h;
    }

    @JsonProperty("social_contributors_calc_24h")
    public void setSocialContributorsCalc24h(Integer socialContributorsCalc24h) {
        this.socialContributorsCalc24h = socialContributorsCalc24h;
    }

    @JsonProperty("social_contributors_calc_24h_percent")
    public Double getSocialContributorsCalc24hPercent() {
        return socialContributorsCalc24hPercent;
    }

    @JsonProperty("social_contributors_calc_24h_percent")
    public void setSocialContributorsCalc24hPercent(Double socialContributorsCalc24hPercent) {
        this.socialContributorsCalc24hPercent = socialContributorsCalc24hPercent;
    }

    @JsonProperty("url_shares_calc_24h")
    public Integer getUrlSharesCalc24h() {
        return urlSharesCalc24h;
    }

    @JsonProperty("url_shares_calc_24h")
    public void setUrlSharesCalc24h(Integer urlSharesCalc24h) {
        this.urlSharesCalc24h = urlSharesCalc24h;
    }

    @JsonProperty("url_shares_calc_24h_percent")
    public Double getUrlSharesCalc24hPercent() {
        return urlSharesCalc24hPercent;
    }

    @JsonProperty("url_shares_calc_24h_percent")
    public void setUrlSharesCalc24hPercent(Double urlSharesCalc24hPercent) {
        this.urlSharesCalc24hPercent = urlSharesCalc24hPercent;
    }

    @JsonProperty("tweet_spam_calc_24h")
    public Integer getTweetSpamCalc24h() {
        return tweetSpamCalc24h;
    }

    @JsonProperty("tweet_spam_calc_24h")
    public void setTweetSpamCalc24h(Integer tweetSpamCalc24h) {
        this.tweetSpamCalc24h = tweetSpamCalc24h;
    }

    @JsonProperty("tweet_spam_calc_24h_percent")
    public Double getTweetSpamCalc24hPercent() {
        return tweetSpamCalc24hPercent;
    }

    @JsonProperty("tweet_spam_calc_24h_percent")
    public void setTweetSpamCalc24hPercent(Double tweetSpamCalc24hPercent) {
        this.tweetSpamCalc24hPercent = tweetSpamCalc24hPercent;
    }

    @JsonProperty("news_calc_24h")
    public Integer getNewsCalc24h() {
        return newsCalc24h;
    }

    @JsonProperty("news_calc_24h")
    public void setNewsCalc24h(Integer newsCalc24h) {
        this.newsCalc24h = newsCalc24h;
    }

    @JsonProperty("news_calc_24h_percent")
    public Double getNewsCalc24hPercent() {
        return newsCalc24hPercent;
    }

    @JsonProperty("news_calc_24h_percent")
    public void setNewsCalc24hPercent(Double newsCalc24hPercent) {
        this.newsCalc24hPercent = newsCalc24hPercent;
    }

    @JsonProperty("average_sentiment_calc_24h")
    public Double getAverageSentimentCalc24h() {
        return averageSentimentCalc24h;
    }

    @JsonProperty("average_sentiment_calc_24h")
    public void setAverageSentimentCalc24h(Double averageSentimentCalc24h) {
        this.averageSentimentCalc24h = averageSentimentCalc24h;
    }

    @JsonProperty("average_sentiment_calc_24h_percent")
    public Integer getAverageSentimentCalc24hPercent() {
        return averageSentimentCalc24hPercent;
    }

    @JsonProperty("average_sentiment_calc_24h_percent")
    public void setAverageSentimentCalc24hPercent(Integer averageSentimentCalc24hPercent) {
        this.averageSentimentCalc24hPercent = averageSentimentCalc24hPercent;
    }

    @JsonProperty("social_score_calc_24h")
    public Long getSocialScoreCalc24h() {
        return socialScoreCalc24h;
    }

    @JsonProperty("social_score_calc_24h")
    public void setSocialScoreCalc24h(Long socialScoreCalc24h) {
        this.socialScoreCalc24h = socialScoreCalc24h;
    }

    @JsonProperty("social_score_calc_24h_percent")
    public Double getSocialScoreCalc24hPercent() {
        return socialScoreCalc24hPercent;
    }

    @JsonProperty("social_score_calc_24h_percent")
    public void setSocialScoreCalc24hPercent(Double socialScoreCalc24hPercent) {
        this.socialScoreCalc24hPercent = socialScoreCalc24hPercent;
    }

    @JsonProperty("social_volume_calc_24h")
    public Integer getSocialVolumeCalc24h() {
        return socialVolumeCalc24h;
    }

    @JsonProperty("social_volume_calc_24h")
    public void setSocialVolumeCalc24h(Integer socialVolumeCalc24h) {
        this.socialVolumeCalc24h = socialVolumeCalc24h;
    }

    @JsonProperty("social_volume_calc_24h_percent")
    public Double getSocialVolumeCalc24hPercent() {
        return socialVolumeCalc24hPercent;
    }

    @JsonProperty("social_volume_calc_24h_percent")
    public void setSocialVolumeCalc24hPercent(Double socialVolumeCalc24hPercent) {
        this.socialVolumeCalc24hPercent = socialVolumeCalc24hPercent;
    }

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

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonProperty("close")
    public Double getClose() {
        return close;
    }

    @JsonProperty("close")
    public void setClose(Double close) {
        this.close = close;
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
