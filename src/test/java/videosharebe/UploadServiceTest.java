package videosharebe;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.service.UploadServiceImpl;
import org.demartino.videosharingsite.view.Upload;
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

public class UploadServiceTest {
	
	@Mock
	private VideoDao mockDao;
	
	@InjectMocks
	private UploadServiceImpl uploadServiceImpl;
	
	private static final Logger logger = Logger.getLogger(UploadServiceTest.class);
	
	UploadedVideo uploadedVideo;
	UploadRemote uploadRemote;
	Long id;
	List<UploadedVideo> uploadedVideos;
	
	@BeforeMethod 
	public void intializer ()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeClass 
	public void setUp() 
	{
		uploadedVideo = new UploadedVideo();
		
		uploadedVideo.setId(10L);
		uploadedVideo.setPath("C:/mytemp/file");
		uploadedVideo.setTitle("myvideo"); 
		
		uploadRemote = new UploadRemote(uploadedVideo);
		
		uploadedVideos = new ArrayList<UploadedVideo>();
		uploadedVideos.add(uploadedVideo);
		
		
		id = 88L;
	}
	
	
	//Test create Video method
	@Test
	public void userCreatesNewVideo() {
		Mockito.when(mockDao.findVideoByTitle(uploadedVideo.getTitle())).thenReturn(null);
		Mockito.when(mockDao.createVideo(Mockito.any(UploadedVideo.class))).thenReturn(uploadedVideo);
		UploadRemote returnedUploadRemote = uploadServiceImpl.createVideo(uploadRemote, "username");
		Assert.assertTrue(new ReflectionEquals(uploadRemote).matches(returnedUploadRemote));
	}
	
	@Test
	public void userCantCreateNewVideoBecuaseOneAlreadyExistsWithSameTitle () {
		Mockito.when(mockDao.findVideoByTitle(uploadedVideo.getTitle())).thenReturn(uploadedVideo);
		UploadRemote returnedUploadRemote = uploadServiceImpl.createVideo(uploadRemote, "username");
		Assert.assertNull(returnedUploadRemote);		
	}
	
	@Test 
	public void userCantCreateNewVideoBecuaseUploadIsNull() {
		UploadRemote returnedUploadRemote = uploadServiceImpl.createVideo(null, "username");
		Assert.assertNull(returnedUploadRemote);	
	}
	
	//Test delete By Id
	@Test
	public void userDeletesVideoById() {
		Mockito.when(mockDao.deleteVideoById(id)).thenReturn(true);
		boolean isDeleted = uploadServiceImpl.deleteVideoById(id);
		Assert.assertTrue(isDeleted);
	}
	
	@Test
	public void userFailsToDeleteVideoById() {
		Mockito.when(mockDao.deleteVideoById(id)).thenReturn(false);
		boolean isDeleted = uploadServiceImpl.deleteVideoById(id);
		Assert.assertFalse(isDeleted);
	}
	
	@Test
	public void nullIsPassedToDeleteFunction() {
		boolean isDeleted = uploadServiceImpl.deleteVideoById(null);
		Assert.assertFalse(isDeleted);
	}
	
	//Test FindVideoByTitle 
	@Test
	public void userFindsVideoByTitle() {
		Mockito.when(mockDao.findVideoByTitle(uploadRemote.getTitle())).thenReturn(uploadedVideo);
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.findVideoByTitle(uploadRemote.getTitle());
		Assert.assertTrue(new ReflectionEquals(uploadRemote).matches(uploadRemoteToBeTested));
	}
	
	@Test
	public void userCantFindVideoByTitle() {
		Mockito.when(mockDao.findVideoByTitle(uploadRemote.getTitle())).thenReturn(null);
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.findVideoByTitle(uploadRemote.getTitle());
		Assert.assertNull(uploadRemoteToBeTested);
	}
	
	@Test
	public void nullPassedToFindVideoById() {
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.findVideoByTitle(null);
		Assert.assertNull(uploadRemoteToBeTested);
	}
	
	//Test updateVideo 
	@Test
	public void userUpdatesVideoSuccessfully() {
		Mockito.when(mockDao.updateVideo(Mockito.any(UploadedVideo.class))).thenReturn(uploadedVideo);
		Mockito.when(mockDao.findVideoByTitle(uploadRemote.getTitle())).thenReturn(uploadedVideo);
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.updateVideo(uploadRemote);
		Assert.assertTrue(new ReflectionEquals(uploadRemote).matches(uploadRemoteToBeTested));
	}
	
	@Test
	public void userCantUpdateVideoBecauseItDoesntExist() {
		Mockito.when(mockDao.findVideoByTitle(uploadRemote.getTitle())).thenReturn(null);
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.updateVideo(uploadRemote);
		Assert.assertNull(uploadRemoteToBeTested);
	}
	
	@Test 
	public void nullIsPassedToUpdate() {
		UploadRemote uploadRemoteToBeTested = uploadServiceImpl.updateVideo(null);
		Assert.assertNull(uploadRemoteToBeTested);
	}
	
	//Test all getAllVideosForUser
	@Test
	public void userGetsAllVideosForGivenUsername() {
		Mockito.when(mockDao.getAllVideosForUser("username")).thenReturn(uploadedVideos);
		List<Upload> resultToBeTested = uploadServiceImpl.getAllVideosForUser("username");
		Assert.assertTrue(resultToBeTested.size() > 0);
	}
	
	
}
