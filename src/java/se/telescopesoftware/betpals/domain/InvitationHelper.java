package se.telescopesoftware.betpals.domain;

import java.util.HashSet;
import java.util.Set;
//TODO: move to other package

/**
 * 
 * Helper class to keep all information needed to create invitations to competition.
 * Used only to pass user entered info from web page.
 *
 */
public class InvitationHelper {

	public static final Integer MEDIA_FACEBOOK = 1; 
	public static final Integer MEDIA_TWEETER = 2; 
	
	private Long competitionId;
	private String invitation;
	private boolean allFriends;
	private Set<Long> friendsIdSet = new HashSet<Long>();
	private Set<Long> groupIdSet = new HashSet<Long>();
	private Set<Long> communityIdSet = new HashSet<Long>();
	private Set<Integer> mediaIdSet = new HashSet<Integer>();
	
	
	public String getInvitation() {
		return invitation;
	}
	
	public void setInvitation(String invitation) {
		this.invitation = invitation;
	}
	
	public boolean isAllFriends() {
		return allFriends;
	}
	
	public void setAllFriends(boolean allFriends) {
		this.allFriends = allFriends;
	}
	
	public Set<Long> getFriendsIdSet() {
		return friendsIdSet != null ? friendsIdSet : new HashSet<Long>();
	}
	
	public void setFriendsIdSet(Set<Long> friendsIdSet) {
		this.friendsIdSet = friendsIdSet;
	}
	
	public Set<Long> getGroupIdSet() {
		return groupIdSet != null ? groupIdSet : new HashSet<Long>();
	}
	
	public void setGroupIdSet(Set<Long> groupIdSet) {
		this.groupIdSet = groupIdSet;
	}
	
	public Set<Long> getCommunityIdSet() {
		return communityIdSet != null ? communityIdSet : new HashSet<Long>();
	}
	
	public void setCommunityIdSet(Set<Long> communityIdSet) {
		this.communityIdSet = communityIdSet;
	}
	
	public Set<Integer> getMediaIdSet() {
		return mediaIdSet;
	}
	
	public void setMediaIdSet(Set<Integer> mediaIdSet) {
		this.mediaIdSet = mediaIdSet;
	}

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}
	
	public boolean shouldInviteToFacebook() {
		return mediaIdSet != null ? mediaIdSet.contains(MEDIA_FACEBOOK) : false;
	}

}
