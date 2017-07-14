package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by student on 6/27/17.
 */
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int postID;

    private String postName;

    private String postAuthor;
    
    private int postUser;
    
    private String filterName;
    
    private String urlOriginal;
    
    private String urlModified;
    
    private Date postDate=new Date();


    public int getPostID() {
        return postID;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName (String postName) {
        this.postName = postName;
    }
    
    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor (String postAuthor) {
        this.postAuthor = postAuthor;
    }
    
    public int getPostUser() {
        return postUser;
    }

    public void setPostUser (int postUser) {
        this.postUser = postUser;
    }

    public Date getPostDate() {
        return postDate;
    }


    public String getFormatDate(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE MMMMM dd, yyyy hh:mm a zzzz", Locale.US);
        return format.format(postDate);
    }

	public String getUrlOriginal() {
		return urlOriginal;
	}

	public void setUrlOriginal(String urlOriginal) {
		this.urlOriginal = urlOriginal;
	}

	public String getUrlModified() {
		return urlModified;
	}

	public void setUrlModified(String urlModified) {
		this.urlModified = urlModified;
	}


	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
}
