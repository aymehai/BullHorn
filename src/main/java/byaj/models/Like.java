package byaj.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by student on 6/27/17.
 */
@Entity
@Table(name = "likeData")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int likeID;

    private String likeValue;

    private String likeType;
    
    private String likeAuthor;
    
    private int likeUser;

	public int getLikeID() {
		return likeID;
	}

	public void setLikeID(int likeID) {
		this.likeID = likeID;
	}

	public String getLikeValue() {
		return likeValue;
	}

	public void setLikeValue(String likeValue) {
		this.likeValue = likeValue;
	}

	public String getLikeType() {
		return likeType;
	}

	public void setLikeType(String likeType) {
		this.likeType = likeType;
	}

	public String getLikeAuthor() {
		return likeAuthor;
	}

	public void setLikeAuthor(String likeAuthor) {
		this.likeAuthor = likeAuthor;
	}

	public int getLikeUser() {
		return likeUser;
	}

	public void setLikeUser(int likeUser) {
		this.likeUser = likeUser;
	}
}
   