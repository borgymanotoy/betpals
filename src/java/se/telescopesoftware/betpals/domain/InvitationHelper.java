package se.telescopesoftware.betpals.domain;

import java.util.Set;
//TODO: move to other package

/**
 * 
 * Helper class to keep all information needed to create invitations to competition.
 * Used only to pass user entered info from web page.
 *
 */
public class InvitationHelper {

	private Long competitionId;
	private String invitation;
	private boolean allFriends;
	private Set<Long> friendsIdSet;
	private Set<Long> groupIdSet;
	private Set<Long> communityIdSet;
	private Set<Long> mediaIdSet;
	
	
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
		return friendsIdSet;
	}
	
	public void setFriendsIdSet(Set<Long> friendsIdSet) {
		this.friendsIdSet = friendsIdSet;
	}
	
	public Set<Long> getGroupIdSet() {
		return groupIdSet;
	}
	
	public void setGroupIdSet(Set<Long> groupIdSet) {
		this.groupIdSet = groupIdSet;
	}
	
	public Set<Long> getCommunityIdSet() {
		return communityIdSet;
	}
	
	public void setCommunityIdSet(Set<Long> communityIdSet) {
		this.communityIdSet = communityIdSet;
	}
	
	public Set<Long> getMediaIdSet() {
		return mediaIdSet;
	}
	
	public void setMediaIdSet(Set<Long> mediaIdSet) {
		this.mediaIdSet = mediaIdSet;
	}

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}

}
