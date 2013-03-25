package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.code.morphia.Key;
import org.bson.types.ObjectId;

import utils.DBHolder;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;

@Entity
public class Quote {

	public static final String AUTHOR_EMPTY_FILTER = "";

	@Id
	public ObjectId id;

	/**
	 * Source url of given quote
	 */
	public String url;

	public String quoteText;

	public String author;

	public String userIp;

	public Date creationDate;

	public QuoteState quoteState;

	public Date approvalDate;

	public Date publishedDate;

    public Date lastUpdateDate;

    /**
     * When approved, processed by demagog.cz team and published on demagog.cz site
     * we save link to that demagog.cz's article here.
     * (like http://demagog.cz/diskusie/113/rekordni-nezamestnanost-ekonomicka-situace-cr)
     */
    public String demagogBacklinkUrl;

	public int voteCount;

	public boolean deleted;

    /**
     * State transitions:
     *    NEW -> APPROVED_FOR_VOTING -> ANALYSIS_IN_PROGRESS -> CHECKED
     */
	public enum QuoteState {

        /**
         * Waiting to be approved or cancelled (spam / low quality / out of topic ... etc.) by admin.
         */
        NEW,

        /**
         * In this state users can vote for the Quote and share it.
         */
        APPROVED_FOR_VOTING,

        /**
         * Means that the Quote is accepted by demagog.cz team to analysis. Sometime in future
         * it is going to be published on demagog.cz
         */
        ANALYSIS_IN_PROGRESS,

        /**
         * Analysis of quote is done and published on demagog.cz
         */
        CHECKED_AND_PUBLISHED

	}

	public Quote() {
	}

	public Quote(String quoteText, String url, String author, QuoteState state) {
		this.url = url;
		this.quoteText = quoteText;
		this.author = author;
		this.creationDate = new Date();
		this.quoteState = state;
	}

	public Key save() {
		return DBHolder.ds.save(this);
	}

	public static void setChecked(ObjectId id) {
		DBHolder.ds.update(DBHolder.ds.createQuery(Quote.class).field("_id").equal(id), DBHolder.ds.createUpdateOperations(Quote.class).set("quoteState", QuoteState.ANALYSIS_IN_PROGRESS));
	}

	public static void approve(ObjectId id, String text, String author) {
		DBHolder.ds.update(
                DBHolder.ds.createQuery(Quote.class).field("_id").equal(id),
                DBHolder.ds.createUpdateOperations(Quote.class)
                        .set("quoteState", QuoteState.APPROVED_FOR_VOTING)
                        .set("approvalDate", new Date()));
	}

	public static void upVote(ObjectId id) {
		DBHolder.ds.update(DBHolder.ds.createQuery(Quote.class).field("_id").equal(id), DBHolder.ds.createUpdateOperations(Quote.class).inc("voteCount"));
	}

	public static Quote findById(ObjectId id) {
		return DBHolder.ds.find(Quote.class, "_id", id).get();
	}

	public static List<Quote> findAll() {
		return DBHolder.ds.find(Quote.class).field("deleted").equal(false).asList();
	}

	public static List<Quote> findAllWithStateOrderedByCreationDate(QuoteState state) {
		return DBHolder.ds.find(Quote.class)
                            .field("deleted").equal(false)
                            .field("quoteState").equal(state)
                            .order("-creationDate, _id")
                            .asList();
	}

    public static List<Quote> findAllWithStateOrderedByVoteCount(QuoteState state) {
        return DBHolder.ds.find(Quote.class)
                .field("deleted").equal(false)
                .field("quoteState").equal(state)
                .order("-voteCount, _id")
                .asList();
    }

	public static void delete(ObjectId id, boolean pernament) {
		if (pernament) {
			DBHolder.ds.delete(DBHolder.ds.createQuery(Quote.class).field("_id").equal(id));
		} else {
			DBHolder.ds.update(DBHolder.ds.createQuery(Quote.class).field("_id").equal(id), DBHolder.ds.createUpdateOperations(Quote.class).set("deleted", true));
		}
	}

	public static void deleteAll() {
		DBHolder.ds.delete(DBHolder.ds.createQuery(Quote.class));
	}

	public int vote() {
		return ++voteCount;
	}

	public static List<Quote> findAllSortedByVote(QuoteState state) {
		Query<Quote> query = DBHolder.ds.find(Quote.class).field("deleted").equal(false);
		query = query.field("quoteState").equal(state);
		return query.order("-voteCount").asList();
	}

	public static List<Quote> findAllSortedByCreationDate(boolean onlyApproved) {
		Query<Quote> query = DBHolder.ds.find(Quote.class).field("deleted").equal(false);
		if (onlyApproved) {
			query = query.field("quoteState").equal(QuoteState.APPROVED_FOR_VOTING);
		}
		return query.order("-creationDate").asList();
	}

	public static List<Quote> findAllSortedByVoteFilteredByAuthor(String author, QuoteState state) {
		Query<Quote> query = DBHolder.ds.find(Quote.class).field("deleted").equal(false);
		query = query.field("quoteState").equal(state);
		return query.filter("author", author).order("-voteCount").asList();
	}

	public static List<String> getAllAuthorNames(QuoteState state) {
		Set<String> names = new TreeSet<String>();
		names.add(AUTHOR_EMPTY_FILTER);

		for (Quote quote : findAllWithStateOrderedByCreationDate(state)) {
			if (quote.author != null) {
				names.add(quote.author);
			}
		}
		return new ArrayList<String>(names);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}

        Quote quote = (Quote) o;

        if (deleted != quote.deleted) {
			return false;
		}
        if (voteCount != quote.voteCount) {
			return false;
		}
        if (approvalDate != null ? !approvalDate.equals(quote.approvalDate) : quote.approvalDate != null) {
			return false;
		}
        if (publishedDate != null ? !publishedDate.equals(quote.publishedDate) : quote.publishedDate != null) {
			return false;
		}
        if (author != null ? !author.equals(quote.author) : quote.author != null) {
			return false;
		}
        if (creationDate != null ? !creationDate.equals(quote.creationDate) : quote.creationDate != null) {
			return false;
		}
        if (demagogBacklinkUrl != null ? !demagogBacklinkUrl.equals(quote.demagogBacklinkUrl) : quote.demagogBacklinkUrl != null) {
			return false;
		}
        if (id != null ? !id.equals(quote.id) : quote.id != null) {
			return false;
		}
        if (lastUpdateDate != null ? !lastUpdateDate.equals(quote.lastUpdateDate) : quote.lastUpdateDate != null) {
			return false;
		}
        if (quoteState != quote.quoteState) {
			return false;
		}
        if (quoteText != null ? !quoteText.equals(quote.quoteText) : quote.quoteText != null) {
			return false;
		}
        if (url != null ? !url.equals(quote.url) : quote.url != null) {
			return false;
		}
        if (userIp != null ? !userIp.equals(quote.userIp) : quote.userIp != null) {
			return false;
		}

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (quoteText != null ? quoteText.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (userIp != null ? userIp.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (quoteState != null ? quoteState.hashCode() : 0);
        result = 31 * result + (approvalDate != null ? approvalDate.hashCode() : 0);
        result = 31 * result + (publishedDate != null ? publishedDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (demagogBacklinkUrl != null ? demagogBacklinkUrl.hashCode() : 0);
        result = 31 * result + voteCount;
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
