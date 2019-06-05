package videosharebe;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.service.LoginServiceImpl;
import org.demartino.videosharingsite.service.UploadServiceImpl;
import org.demartino.videosharingsite.service.UserServiceImpl;
import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class LoginServiceTest {
	

	@Mock
	private UserDao mockDao;
	
	/**
	 * Necessary because without this and the next mock a null pointer
	 * exception is thrown. 
	 */
	@Mock 
	private VideoDao mockVideoDao;
	
	@Mock
	private UserServiceImpl userServiceImplMock;
	
	@Mock
	private UploadServiceImpl uploadServiceImplMock;

	@InjectMocks
	private LoginServiceImpl loginServiceImpl;

	@InjectMocks
	private UploadServiceImpl uploadServiceImpl;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	
	private Login login;
	private UserAndVideoListContainer userAndVideoListContainer;
	private AppUser appUser;
	List<Upload> uploads;
	List<AppUser> appUsers;
	List<User> users;
	
	@BeforeClass
	private void initializer() {
		
		MockitoAnnotations.initMocks(this);
		
		userAndVideoListContainer = new UserAndVideoListContainer();
		
		login = new Login();
		login.setUsername("Lyra");
		login.setPassword("password");
		
		appUser = new AppUser();
		appUser.setAddress("45 Oxford Lane");
		appUser.setAge((short)34);
		appUser.setPhoneNumber("6667778989");
		appUser.setUsername("Pantolimon");
		appUser.setPassword("Pantolimon");
		appUser.setEmail("pant@oxford.edu");
		appUser.setId(123L); 		
		
		appUsers = new ArrayList<AppUser>();
		appUsers.add(appUser);
		
		User user = new User(appUser);
		users = new ArrayList<User>();
		users.add(user);
		
		uploads = new ArrayList<Upload>();
	}
	
	@Test
	public void userSuccessfullyLogsIn() {
		Mockito.when(mockDao.isValidLogin(login)).thenReturn(appUser);
		//Mockito.when(mockDao.getAllUsers()).thenReturn(appUsers);
		Mockito.when(userServiceImplMock.getAllUsers()).thenReturn(users);
		Mockito.when(uploadServiceImplMock.getAllVideosForUser(login.getUsername())).thenReturn(uploads);
		Mockito.when(mockVideoDao.getAllVideosForUser(login.getUsername())).thenReturn(null);
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
}
