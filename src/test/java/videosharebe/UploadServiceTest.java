package videosharebe;

import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.service.UploadServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.log4testng.Logger;

public class UploadServiceTest {
	
	@Mock
	private VideoDao mockDao;
	
	@InjectMocks
	private UploadServiceImpl uploadServiceImpl;
	
	private static final Logger logger = Logger.getLogger(UploadServiceTest.class);
	
	@BeforeMethod 
	public void intializer ()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeClass 
	public void setUp() 
	{
		
	}
	
	
}
