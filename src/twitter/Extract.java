
package twitter;

import java.util.List; 
import java.time.Instant; 
import java.util.HashSet; 
import java.util.Set;


public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) { 
            return new Timespan(null, null); 
        } 
 
        Instant startTime = tweets.get(0).getTimestamp(); 
        Instant endTime = tweets.get(0).getTimestamp(); 
 
        for (Tweet tweet : tweets) { 
            Instant timestamp = tweet.getTimestamp(); 
            if (timestamp.isBefore(startTime)) {  
                startTime = timestamp; 
            } 
            if (timestamp.isAfter(endTime)) { 
                endTime = timestamp; 
            } 
        } 
 
        return new Timespan(startTime, endTime);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>(); 
        for (Tweet tweet : tweets) { 
            String text = tweet.getText(); 
            String[] words = text.split(" ");
            
            for (String word : words) { 
                if (word.startsWith("@") && word.length() > 1) {  
                    String mentionedUser = word.substring(1).replaceAll("[^a-zA-Z0-9_]", "").toLowerCase(); 
                    mentionedUsers.add(mentionedUser); 
                } 
            } 
        }
        
        return mentionedUsers;
    }
}
