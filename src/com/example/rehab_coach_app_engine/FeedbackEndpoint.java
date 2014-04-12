package com.example.rehab_coach_app_engine;

import com.example.rehab_coach_app_engine.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "feedbackendpoint", namespace = @ApiNamespace(ownerDomain = "example.com", ownerName = "example.com", packagePath = "rehab_coachv1"))
public class FeedbackEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listFeedback")
	public CollectionResponse<Feedback> listFeedback(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Feedback> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Feedback as Feedback");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Feedback>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Feedback obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Feedback> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getFeedback")
	public Feedback getFeedback(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		Feedback feedback = null;
		try {
			feedback = mgr.find(Feedback.class, id);
		} finally {
			mgr.close();
		}
		return feedback;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param feedback the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertFeedback")
	public Feedback insertFeedback(Feedback feedback) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsFeedback(feedback)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(feedback);
		} finally {
			mgr.close();
		}
		return feedback;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param feedback the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateFeedback")
	public Feedback updateFeedback(Feedback feedback) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsFeedback(feedback)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(feedback);
		} finally {
			mgr.close();
		}
		return feedback;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeFeedback")
	public void removeFeedback(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		try {
			Feedback feedback = mgr.find(Feedback.class, id);
			mgr.remove(feedback);
		} finally {
			mgr.close();
		}
	}

	private boolean containsFeedback(Feedback feedback) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Feedback item = mgr.find(Feedback.class, feedback.getId());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
