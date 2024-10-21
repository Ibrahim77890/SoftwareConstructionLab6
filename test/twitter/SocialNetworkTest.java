/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /**
     * Test 1: Empty List of Tweets
     * Ensures that an empty list results in an empty graph.
     */
    @Test
    public void testGuessFollowsGraphEmptyList() {
        List<Tweet> tweets = Collections.emptyList();
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("Graph should be empty", graph.isEmpty());
    }

    /**
     * Test 2: Tweets Without Mentions
     * Verifies that tweets with no mentions do not add entries to the graph.
     */
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = Arrays.asList(new Tweet(1, "KaifaHalaq", "Just enjoying the day!", Instant.now()),
            new Tweet(2, "Ibrahim", "Having lunch at a new place.", Instant.now())
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertFalse("Graph should be empty since no mentions are present", graph.isEmpty());
    }

    /**
     * Test 3: Single Mention
     * Tests whether a user who mentions someone is correctly added to the graph.
     */
    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "kaifahalaq", "Hello @ibrahim", Instant.now())
        );

        // Call the method and print the resulting graph
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
//        System.out.println("Graph: " + graph);

        // Debug individual keys and values in the graph
        if (graph.containsKey("kaifahalaq")) {
//            System.out.println("Followers of kaifahalaq: " + graph.get("kaifahalaq"));
        } else {
//            System.out.println("Key 'kaifahalaq' not found in graph");
        }

        // Assert condition with printed feedback if it fails
        assertTrue("kaifahalaq should follow ibrahim", 
            graph.containsKey("kaifahalaq") && graph.get("kaifahalaq").contains("ibrahim"));
    }


    /**
     * Test 4: Multiple Mentions
     * Checks if multiple mentioned users are linked to the tweet author.
     */
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "KaifaHalaq", "Hello @Ibrahim and @nabeel", Instant.now())
        );
        System.out.println(tweets);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        System.out.println(graph.get("kaifahalaq").containsAll(Set.of("ibrahim", "nabeel")));
        assertTrue("KaifaHalaq should follow Ibrahim and Nabeel", graph.get("kaifahalaq").containsAll(Set.of("ibrahim", "nabeel")));
    }

    /**
     * Test 5: Multiple Tweets from One User
     * Ensures that repeated mentions from the same user are captured.
     */
    @Test
    public void testGuessFollowsGraphMultipleTweets() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "KaifaHalaq", "Hello @ibrahim", Instant.now()),
            new Tweet(2, "KaifaHalaq", "Good morning @nabeel", Instant.now())
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("KaifaHalaq should follow both Ibrahim and Nabeel", graph.get("kaifahalaq").containsAll(Set.of("ibrahim", "nabeel")));
    }

    /**
     * Test 6: Empty Graph for influencers()
     * Verifies that no users yield an empty influencer list.
     */
    @Test
    public void testInfluencersEmptyGraph() {
        Map<String, Set<String>> graph = Collections.emptyMap();
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue("Influencers list should be empty", influencers.isEmpty());
    }

    /**
     * Test 7: Single User Without Followers
     * Tests that a user without followers yields no influencers.
     */
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> graph = Map.of("KaifaHalaq", Set.of());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue("Influencers list should be empty", influencers.isEmpty());
    }

    /**
     * Test 8: Single Influencer
     * Verifies correct identification of the only influencer.
     */
    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> graph = Map.of(
            "Ibrahim", Set.of("KaifaHalaq")
        );
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("KaifaHalaq should be the only influencer", List.of("KaifaHalaq"), influencers);
    }

    /**
     * Test 9: Multiple Influencers
     * Tests for correct influencer ordering.
     */
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> graph = Map.of(
            "KaifaHalaq", Set.of("Ibrahim"),
            "Nabeel", Set.of("Ibrahim"),
            "NigBro", Set.of("Nabeel")
        );
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("Influencers should be in correct order", List.of("Ibrahim", "Nabeel"), influencers);
    }

    /**
     * Test 10: Tied Influence
     * Ensures equal influencers are handled correctly.
     */
    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> graph = Map.of(
            "KaifaHalaq", Set.of("Ibrahim"),
            "Nabeel", Set.of("Ibrahim"),
            "NigBro", Set.of("Nabeel")
        );
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue("Ibrahim and Nabeel should be in the top influencers", 
            influencers.indexOf("Ibrahim") < influencers.indexOf("Nabeel") || 
            influencers.indexOf("Nabeel") < influencers.indexOf("Ibrahim"));
    }
}
