/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		this.addType(Mentions.class);
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
