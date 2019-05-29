package videosharebe;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.service.LoginServiceImpl;
import org.demartino.videosharingsite.service.UploadService;
import org.demartino.videosharingsite.service.UserService;
import org.demartino.videosharingsite.service.UserServiceImpl;
import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class UserServiceTest {

	@Mock
	private UserDao mockDao;
	@Mock
	private UserService userService;
	@Mock
	private UploadService uploadService;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@InjectMocks
	private LoginServiceImpl loginServiceImpl;
	
	private static final Logger logger = Logger.getLogger(UserServiceTest.class);
	
	 private User user;
	 private User updatedUser;
	 private AppUser appUser;
	 private AppUser updatedAppUser;
	 private Login login;
	 private UserAndVideoListContainer userAndVideoListContainer;
	 private List<User> users;
	 private List<Upload> uploads;
	 private List<AppUser> appUsers;
	
	 
	@BeforeMethod
	public void intializer() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeClass
	public void setUp() throws Exception {

		user = new User();
		
		user.setAddress("45 Oxford Lane");
		user.setAge((short)34);
		user.setPhoneNumber("6667778989");
		user.setUsername("Pantolimon");
		user.setPassword("Pantolimon");
		user.setEmail("pant@oxford.edu");
		user.setId(123L);
		
		appUser = new AppUser(user);
		
		updatedUser = new User();
		
		updatedUser.setAddress("64 Setmount Lane");
		updatedUser.setAge((short)35);
		updatedUser.setPhoneNumber("8347564534");
		updatedUser.setUsername("George");
		updatedUser.setPassword("George");
		updatedUser.setEmail("George@Yale.edu");
		updatedUser.setId(123L);
		
		updatedAppUser = new AppUser();

		updatedAppUser.setAddress("64 Setmount Lane");
		updatedAppUser.setAge((short)35);
		updatedAppUser.setPhoneNumber("8347564534");
		updatedAppUser.setUsername("George");
		updatedAppUser.setPassword("George");
		updatedAppUser.setEmail("George@Yale.edu");
		updatedAppUser.setId(123L);
		
		
		//updatedAppUser = new AppUser(updatedUser);
		
		login = new Login();
		
		login.setUsername("Lyra");
		login.setPassword("password");
		
		userAndVideoListContainer = new UserAndVideoListContainer();
		
		users = new ArrayList<User>();
		users.add(user);
		users.add(updatedUser);
		
		appUsers = new ArrayList<AppUser>();
		appUsers.add(appUser);
		appUsers.add(updatedAppUser);
		
		uploads = new ArrayList<Upload>();
	}
	
	//Test Create User Service Layer Method
	@Test
	public void serviceLayerCreatesNewUserReturnsNewUser() {
	
		 Mockito.when(mockDao.findUserByUsername(appUser.getUsername())).thenReturn(null);
		 Mockito.when(mockDao.createUser(Mockito.any(AppUser.class))).thenReturn(appUser);
		 User userToBeTested = userServiceImpl.createUser(user);
		 userToBeTested.setPassword(user.getPassword());
		 Assert.assertTrue(new ReflectionEquals(user).matches(userToBeTested)); 
	}
	
	@Test
	public void userNotCreatedBecauseItAlreadyExists() {
		Mockito.when(mockDao.findUserByUsername(appUser.getUsername())).thenReturn(appUser);
		User userToBeTested = userServiceImpl.createUser(user);
		Assert.assertEquals(userToBeTested, null);
	}
	
	@Test
	public void serviceLayerFailsToCreateUserBecauseUserIsNull() {
		User userToBeTested = userServiceImpl.createUser(null);
		Assert.assertEquals(userToBeTested, null);
	}
	
	//Test Service Layer Delete User Method
	@Test
	public void serviceLayerDeletesUserReturnsBoolean() {
		Mockito.when(mockDao.deleteUserByUsername("username")).thenReturn(true);
		boolean deleted = userServiceImpl.deleteUserByUsername("username");
		Assert.assertTrue(deleted);
	}
	
	@Test
	public void serviceLayerFailsToDeleteUserBecauseUsernameIsNull() {
		boolean wasDeleted = userServiceImpl.deleteUserByUsername(null);
		Assert.assertEquals(wasDeleted, false);
	}

	//Test Service Layer Update Method
	@Test
	public void serviceLayerUpdatesUser() {
		Mockito.when(mockDao.updateUser(Mockito.any(AppUser.class))).thenReturn(updatedAppUser);
		Mockito.when(mockDao.findUserByUsername(updatedUser.getUsername())).thenReturn(updatedAppUser);
		User userToBeTested = userServiceImpl.updateUser(updatedUser);
		logger.debug("updatedUser is: " + updatedUser.toString());
		Assert.assertTrue(new ReflectionEquals(updatedUser, "password").matches(userToBeTested));
	}

	@Test 
	public void serviceLayerFailsToUpdateUserBecauseUserDoesntExist() {
		Mockito.when(mockDao.updateUser(Mockito.any(AppUser.class))).thenReturn(null); //what does this warning mean?
		User userToBeTested = userServiceImpl.updateUser(updatedUser);
		Assert.assertEquals(userToBeTested, null);
	}
	
	@Test 
	public void serviceLayerFailsToUpdateUserBecauseUserIsNull() {
		User userToBeTested = userServiceImpl.updateUser(null);
		Assert.assertEquals(userToBeTested, null);
	}
	
	//Test Login 
	@Test(priority = 1)
	public void userSuccessfullyLogsIn() {
		Mockito.when(mockDao.isValidLogin(login)).thenReturn(appUser);
		Mockito.when(userService.getAllUsers()).thenReturn(users);
		Mockito.when(uploadService.getAllVideosForUser(login.getUsername())).thenReturn(uploads);
		userAndVideoListContainer = loginServiceImpl.login(login);
		List<User> listOfUsers = userAndVideoListContainer.getUsers();
		Assert.assertTrue(listOfUsers.size() > 0);
	}
	
	@Test 
	public void userFailsToLoginDueToInvalidCredentials() {
		Mockito.when(mockDao.isValidLogin(login)).thenReturn(null);
		userAndVideoListContainer = loginServiceImpl.login(login);
		List<User> listOfUsers = userAndVideoListContainer.getUsers();
		Assert.assertTrue(listOfUsers.size() == 0);
	}
	
	@Test 
	public void userFailsToLoginBecauseLoginObjectIsNull() {
		userAndVideoListContainer = loginServiceImpl.login(null);
		Assert.assertNull(userAndVideoListContainer);
	}
	
	//Test findUserByUsername
	@Test 
	public void userFindsUserByUsername() {
		Mockito.when(mockDao.findUserByUsername("Pantolimon")).thenReturn(appUser);
		User userToBeTested = userServiceImpl.findUserByUsername("Pantolimon");
		Assert.assertTrue(new ReflectionEquals(user, "password").matches(userToBeTested));
	}
	
	@Test
	public void userFailsToFindUserByUsernameBecauseUserDoesntExist() {
		Mockito.when(mockDao.findUserByUsername("username")).thenReturn(null);
		User user = userServiceImpl.findUserByUsername("username");
		Assert.assertNull(user);
	}
	
	@Test
	public void userFailsToFindUserByUsernameBecauseUsernameIsNull() {
		Mockito.when(mockDao.findUserByUsername(null)).thenReturn(null);
		User user = userServiceImpl.findUserByUsername(null);
		Assert.assertNull(user);
	}
	
	//Test doesUserExist
	@Test
	public void doesUserExistFindsAUser() {
		Mockito.when(mockDao.findUserByUsername(user.getUsername())).thenReturn(appUser);
		Assert.assertFalse(userServiceImpl.doesUserExist(user.getUsername()));
	}
	
	@Test
	public void doesUserExistDoesntFindAUser() {
		Mockito.when(mockDao.findUserByUsername(user.getUsername())).thenReturn(null);
		Assert.assertTrue(userServiceImpl.doesUserExist(user.getUsername()));
	}
	
	@Test
	public void doesUserExistIsPassedANullUsernameAndReturnsTrue() {
		Assert.assertTrue(userServiceImpl.doesUserExist(null));
	}
	
	//Test getAllUsers
	@Test
	public void testGetAllUsers() {
		Mockito.when(mockDao.getAllUsers()).thenReturn(appUsers);
		List<User> user = userServiceImpl.getAllUsers();
		Assert.assertTrue(user.size() > 0);
	}
}