package byaj.controllers;

import byaj.configs.CloudinaryConfig;
import byaj.models.*;
import byaj.repositories.*;
import byaj.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;


@Controller
public class HomeController {

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private byaj.services.UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	CloudinaryConfig cloudc;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	LikeRepository likeRepository;

	// Home page That shows all tweets in database

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("post", new Post());
		model.addAttribute("follow", new Follow());
		model.addAttribute("like", new Like());
		model.addAttribute("posts", postRepository.findAllByOrderByPostDateDesc());
		return "postresults2";
	}

	// Request Mapping for Login Page

	@RequestMapping("/login")
	public String login(Model model) {
		return "login2";
	}

	// Post to Bullhorn Get and Post Mapping

	@GetMapping("/post")
	public String newPost(Model model, Principal principal) {
		model.addAttribute("post", new Post());
		model.addAttribute("posts", postRepository
				.findAllByPostUserOrderByPostDateDesc(userRepository.findByUsername(principal.getName()).getId()));
		model.addAttribute("follow", new Follow());
		return "post2";
	}

	@PostMapping("/post")
	public String processPost(@RequestParam("file") MultipartFile file, @Valid Post post, Principal principal) {
		if (file.isEmpty()) {
			post.setUrlOriginal(null);
			post.setUrlModified(null);
			post.setPostUser(userRepository.findByUsername(principal.getName()).getId());
			post.setPostAuthor(userRepository.findByUsername(principal.getName()).getUsername());
			postRepository.save(post);
			return "redirect:/";
		}
		try {
			// Uses an automatic naming scheme for the resource
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resource", "auto"));

			// Gets the name of the file
			String name = uploadResult.get("public_id").toString();
			String originalImage = cloudc.createUrl(name, 100, 150, "fit", "");
			String ModdedImage = cloudc.createUrl(name, 100, 150, "fit", post.getFilterName());
			post.setUrlOriginal(originalImage);
			post.setUrlModified(ModdedImage);
			post.setPostUser(userRepository.findByUsername(principal.getName()).getId());
			post.setPostAuthor(userRepository.findByUsername(principal.getName()).getUsername());
			postRepository.save(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	// Account Get and Post Mapping

	@GetMapping("/account")
	public String GetProfilePic(Model model, Principal principal) {
		model.addAttribute("profile", userRepository.findByUsername(principal.getName()));
		return "account";
	}

	@PostMapping("/account")
	public String processProfilePic(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			Principal principal) {
		if (file.isEmpty()) {
			return "redirect:/account";
		}

		try {
			// Uses an automatic naming scheme for the resource
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resource", "auto"));

			// Gets the name of the file
			String name = uploadResult.get("public_id").toString();

			// Adds effect to images
			String profileImage = cloudc.createUrl(name, 100, 150, "fit", "");

			// Adds image Urls to database
			userRepository.findByUsername(principal.getName()).setProfilePic(profileImage);
			userRepository.save(userRepository.findByUsername(principal.getName()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/account";
	}

	// Following Get Mapping

	@GetMapping("/following")
	public String viewFollowing(Model model, Principal principal) {
		model.addAttribute("profileBuilder", new ProfileBuilder());
		model.addAttribute("users", userRepository.findByUsername(principal.getName()).getFollowing());
		model.addAttribute("follow", new Follow());
		return "following";
	}

	@PostMapping("/following")
	public String changeFollowingStatus(@Valid Follow follow, BindingResult bindingResult, Principal principal,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (follow.getFollowType().toLowerCase().equals("follow")) {
			userService.followUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		if (follow.getFollowType().toLowerCase().equals("unfollow")) {
			userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		return "redirect:/following";
	}
	// Followers Get Mapping

	@GetMapping("/followers")
	public String viewFollowers(Model model, Principal principal) {
		model.addAttribute("profileBuilder", new ProfileBuilder());
		model.addAttribute("users", userRepository.findByUsername(principal.getName()).getFollowed());
		model.addAttribute("follow", new Follow());
		return "followers";
	}

	@PostMapping("/followers")
	public String changeFollowerStatus(@Valid Follow follow, BindingResult bindingResult, Principal principal,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (follow.getFollowType().toLowerCase().equals("follow")) {
			userService.followUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		if (follow.getFollowType().toLowerCase().equals("unfollow")) {
			userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		return "redirect:/followers";
	}
	// Users Get Mapping

	@GetMapping("/users")
	public String viewUsers(Model model, Principal principal) {
		model.addAttribute("profileBuilder", new ProfileBuilder());
		model.addAttribute("users", userRepository.findAllByOrderByUserDateDesc());
		model.addAttribute("follow", new Follow());
		return "userresults2";
	}

	// Follow User Post Mapping (for userresults2)

	@PostMapping("/followUser")
	public String changeFollowUserStatus(@Valid Follow follow, BindingResult bindingResult, Principal principal,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (follow.getFollowType().toLowerCase().equals("follow")) {
			userService.followUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		if (follow.getFollowType().toLowerCase().equals("unfollow")) {
			userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		return "redirect:/users";
	}

	// Follow Post Mapping (for home page "/")

	@PostMapping("/follow")
	public String changeFollowStatus(@Valid Follow follow, BindingResult bindingResult, Principal principal,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (follow.getFollowType().toLowerCase().equals("follow")) {
			userService.followUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		if (follow.getFollowType().toLowerCase().equals("unfollow")) {
			userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		return "redirect:/";
	}
	
	
    @PostMapping("/like")
    public String changeLikeStatus(@Valid Like like, BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        if(like.getLikeType().toLowerCase().equals("like")){
            userService.likePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        if(like.getLikeType().toLowerCase().equals("unlike")){
            userService.unlikePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        like.setLikeValue(null);
        like.setLikeUser(userRepository.findByUsername(principal.getName()).getId());
        like.setLikeAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        likeRepository.save(like);
        
        return "redirect:/";
    }
    
    
    @PostMapping("/likeFeed")
    public String changeLikeFeedStatus(@Valid Like like, BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/NewsFeed";
        }
        if(like.getLikeType().toLowerCase().equals("like")){
            userService.likePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        if(like.getLikeType().toLowerCase().equals("unlike")){
            userService.unlikePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        like.setLikeUser(userRepository.findByUsername(principal.getName()).getId());
        like.setLikeAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        likeRepository.save(like);
        
        return "redirect:/NewsFeed";
    }

	// View Postings of Users

	@PostMapping("/generate/posts")
	public String generatePosts(@Valid ProfileBuilder profileBuilder, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		model.addAttribute("posts",
				postRepository.findAllByPostAuthorOrderByPostDateDesc(profileBuilder.getProfileBuilderValue()));
		model.addAttribute("follow", new Follow());
		return "postresults2";
	}

	// News Feed Get & Post Mapping (NOT DONE)
	
	@GetMapping("/news")
	public String newsFeed(Model model, Principal principal) {
        model.addAttribute("post", new Post());

        ArrayList<User> Tweets= new ArrayList();
        ArrayList<Post> posts = new ArrayList();
        ArrayList<User> users = new ArrayList();
        for(int i = 0; i<postRepository.findAllByOrderByPostDateDesc().size(); i++){
        	Tweets.add(userRepository.findByUsername(postRepository.findAllByOrderByPostDateDesc().get(i).getPostAuthor()));
        }
        for(int i = 0; i<postRepository.findAllByOrderByPostDateDesc().size(); i++){
            if(userRepository.findByUsername(principal.getName()).followingContains(Tweets.get(i))){
                posts.add(postRepository.findAllByOrderByPostDateDesc().get(i));
                users.add(Tweets.get(i));
            }
        }
        
        
        model.addAttribute("posts", posts);
        model.addAttribute("users", users);
        model.addAttribute("userPrincipal", userRepository.findByUsername(principal.getName()));
		return "NewsFeed";
	}

	@PostMapping("/news")
	public String newsFeedPageResults(@Valid Follow follow, BindingResult bindingResult, Principal principal, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (follow.getFollowType().toLowerCase().equals("follow")) {
			userService.followUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		if (follow.getFollowType().toLowerCase().equals("unfollow")) {
			userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()),
					userRepository.findByUsername(principal.getName()));
		}
		return "redirect:/NewsFeed";
	}

	// Registration Page

	@GetMapping("/register")
	public String showRegistrationPage(Model model) {
		model.addAttribute("user", new User());
		return "register2";
	}

	@PostMapping("/register")
	public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		model.addAttribute("user", user);
		userValidator.validate(user, result);

		if (result.hasErrors()) {
			return "register2";
		} else {
			userService.saveUser(user);
		}
		return "redirect:/login";
	}

}