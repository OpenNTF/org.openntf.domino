package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.impl.DElementStore;

public class SocialStore extends DElementStore {

	public SocialStore() {
		this.addType(Socializer.class);
		this.addType(Comment.class);
		this.addType(CommentsOn.class);
		this.addType(CommentsAbout.class);
		this.addType(Commentable.class);
		this.addType(Likes.class);
		this.addType(Likeable.class);
		this.addType(Rates.class);
		this.addType(Rateable.class);
		this.addType(Share.class);
		this.addType(ShareAbout.class);
		this.addType(SharedBy.class);
		this.addType(SharedWith.class);
		this.addType(Shareable.class);
	}

	public void setSocializerDelegateStore(final DElementStore store) {
		//TODO NTF framing this out makes it clear that a possible proxy store should be another element store
		store.setProxyStoreKey(this.getStoreKey());
	}

	public void setSocialableDelegateStore(final DElementStore store) {
		//TODO NTF framing this out makes it clear that a possible proxy store should be another element store
		// that way we can single-cache manage the elements here.
		store.setProxyStoreKey(this.getStoreKey());
	}

}
