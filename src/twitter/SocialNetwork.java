package twitter;

import java.util.*;
import java.util.stream.Collectors;

public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets a list of tweets providing the evidence, not modified by this method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase(); // Case-insensitive username
            Set<String> mentions = extractMentions(tweet.getText());

            // Initialize the author's following list if not present
            followsGraph.putIfAbsent(author, new HashSet<>());

            // Add all mentioned users to the author's following list, excluding self-mentions
            for (String mention : mentions) {
                if (!mention.equals(author)) {
                    followsGraph.get(author).add(mention);
                }
            }
        }

        return followsGraph;
    }

    /**
     * Helper method to extract @-mentioned usernames from a tweet's text.
     * 
     * @param text the content of the tweet.
     * @return a set of all mentioned usernames (in lowercase).
     */
    private static Set<String> extractMentions(String text) {
        Set<String> mentions = new HashSet<>();
        String[] words = text.split("\\s+");
        
        for (String word : words) {
            if (word.startsWith("@") && word.length() > 1) {
                mentions.add(word.substring(1).toLowerCase()); // Ensure lowercase consistency
            }
        }
        return mentions;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();

        // Calculate follower counts by iterating over all followers in the graph
        for (Set<String> following : followsGraph.values()) {
            for (String user : following) {
                followerCounts.put(user, followerCounts.getOrDefault(user, 0) + 1);
            }
        }

        // Sort users by follower count in descending order
        return followerCounts.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Descending order
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
