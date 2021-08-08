
package util ;

import java.awt.Dimension ;
import java.awt.Rectangle ;
import java.awt.Robot ;
import java.awt.Toolkit ;
import java.awt.image.BufferedImage ;
import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.util.Properties ;
import java.util.StringTokenizer ;
import java.util.Vector ;

import javax.imageio.ImageIO ;

import org.openqa.selenium.Alert ;
import org.openqa.selenium.By ;
import org.openqa.selenium.WebDriver ;
import org.openqa.selenium.support.ui.ExpectedConditions ;
import org.openqa.selenium.support.ui.WebDriverWait ;

public class CommonUtil
{
  
  /** 파일의 내용을 읽어서 문자열로 리턴한다. 개행은 \r\n이 아니라 그냥 \n 으로 바꿔서 리턴 - 아님.수정했음. */
  public String getFileContent ( String fileName ) throws Exception
  {
    
    StringBuffer sb = new StringBuffer ( "" ) ;
    try
    {
      String s = "" ;
      BufferedReader br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( fileName ) , "utf8" ) ) ;
      while ( ( s = br.readLine ( ) ) != null )
      /**
       * s의 맨끝에는 \n이 있어야 한다는 것 잊지 말것 readLine()이 \n은 버퍼에 저장하지 않는다. 빈칸이라도 끝에는 \n이
       * 있다
       */
      {
        if ( "".equals ( s ) )
        {
          continue ;
        }
        else
        {
          sb.append ( s ) ;
        }
        
      }
      /** while문의 끝 */
      
      br.close ( ) ;
      
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      throw e ;
    }
    
    return sb.toString ( ) ;
    
  }
  
  /**
   * id pw 작은 벡터에 넣어서 큰 벡터에 넣어 리턴
   * 
   * @param filePathAndName
   * @return
   * @throws Exception
   */
  public Vector < Vector < String > > getFileContentIntoVector ( String filePathAndName ) throws Exception
  {
    
    Vector < Vector < String > > outerVector = new Vector < Vector < String > > ( ) ;
    
    String file = Thread.currentThread ( ).getContextClassLoader ( ).getResource ( filePathAndName ).getFile ( ) ;
    BufferedReader br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( file ) , "utf8" ) ) ;
    String userID = "" ;
    String userPW = "" ;
    String s = "" ;
    
    while ( ( s = br.readLine ( ) ) != null )
    {
      Vector < String > innerVector = new Vector < String > ( ) ;
      if ( "".equals ( s.trim ( ) ) )
      {
        continue ;
      }
      if ( s.trim ( ).startsWith ( "#" ) )
      {
        continue ;
      }
      else
      {
        StringTokenizer st = new StringTokenizer ( s , "	" ) ;
        userID = String.valueOf ( st.nextToken ( ) ) ;
        userPW = String.valueOf ( st.nextToken ( ) ) ;
        
        innerVector.addElement ( userID ) ;
        innerVector.addElement ( userPW ) ;
        outerVector.addElement ( innerVector ) ;
      }
      
    }
    br.close ( ) ;
    
    return outerVector ;
  }
  
  /**
   * propeties 파일 내용 읽어온다.
   * 
   * @param name
   * @param filePathAndName
   * @return
   * @throws Exception
   */
  public String getFromProperties ( String name , String filePathAndName ) throws Exception
  {
    InputStream is = getClass ( ).getResourceAsStream ( filePathAndName ) ;
    Properties props = new Properties ( ) ;
    String retVal = "" ;
    try
    {
      props.load ( is ) ;
      retVal = props.getProperty ( name ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      retVal = "" ;
    }
    finally
    {
      if ( is != null )
      {
        is.close ( ) ;
      }
    }
    return retVal ;
    
  }
  
  public boolean isAlertPresentAndAccept ( WebDriver driver )
  {
    try
    {      
      WebDriverWait wait = new WebDriverWait ( driver , 5 ) ;
      wait.until ( ExpectedConditions.alertIsPresent ( ) ) ;
      Alert alert = driver.switchTo ( ).alert ( ) ;
      String alertText = alert.getText ( ).trim ( ) ;
      System.out.println ( "Alert data: " + alertText ) ;
      alert.accept ( ) ;
      return true ;
    }
    catch ( Exception ex )
    {
      ex.printStackTrace ( ) ;
      return false ;
    }
  }
  
  
  public boolean existsElement(WebDriver driver , String mode , String cssOrIdOrXpath) {
    try {
        if("id".equals ( mode ))
        driver.findElement(By.id(cssOrIdOrXpath));
        else if("css".equals ( mode ))
          driver.findElement(By.cssSelector(cssOrIdOrXpath));
        else if("xpath".equals ( mode ))
          driver.findElement(By.xpath (cssOrIdOrXpath));
    } catch (Exception e) {
        e.printStackTrace ( );
        return false;
    }
    return true;
 }
  
  
  /**
   * 주어진 이름으로 디렉토리를 만들고 주어진 이름이 파일로 끝날경우 파일도 생성한다. 디렉토리만 생성할 경우 매개변수 path 는 /로
   * 끝이 나야한다.
   */
  public void makeDirectory ( String path )
  {
    File f = null ;
    try
    {
      f = new File ( path ) ;
      f.mkdirs ( ) ;
      f = null;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
  
  
  /**
   * 밀리세컨을 매개변수로 넣으면 20210806/16_43 형식의 폴더를 생성한다.
   * 
   * @param milli
   *          long
   * @return String
   * @throws Exception
   */
  public String makeFolder (String rootFoler , long milli ) throws Exception
  {
    String retVal = "" ;
    try
    {
      

      
      java.util.TimeZone seoul = java.util.TimeZone.getTimeZone ( "Asia/Seoul" ) ;
      String DATE_FORMAT = "yyyyMMdd" ;
      java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat ( DATE_FORMAT ) ;
      sdf1.setTimeZone ( seoul ) ;
      retVal = sdf1.format ( new java.util.Date ( milli ) ) ;
      
      File f = new File(rootFoler);
      if(!f.exists ( ))
      {
        f.mkdirs ( );
      }
      
      if(!rootFoler.endsWith ( File.separator ))
      {
        rootFoler = rootFoler + File.separator;
      }
      
      String  newFolder = rootFoler + retVal;
      makeDirectory ( newFolder );
      
      DATE_FORMAT = "HH_mm_ss" ;
      sdf1 = new java.text.SimpleDateFormat ( DATE_FORMAT ) ;
      sdf1.setTimeZone ( seoul ) ;
      retVal = sdf1.format ( new java.util.Date ( milli ) ) ;
      if(!newFolder.endsWith ( File.separator ))
      {
        newFolder = newFolder + File.separator;
      }
      retVal = newFolder + retVal + File.separator;
      makeDirectory ( retVal );
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      throw e ;
    }
    return retVal ;
  }
  
  
  
  /**
   * 출처 : https://www.tabnine.com/code/java/methods/java.awt.Robot/createScreenCapture
   * Capture screen with the input string as file name
   *
   * @param fileName a given file name
   * @throws Exception if error occurs
   */
  public void captureScreen(String fileName) throws Exception {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle screenRectangle = new Rectangle(screenSize);
    Robot robot = new Robot();
    BufferedImage image = robot.createScreenCapture(screenRectangle);
    File file = new File(fileName);
    ImageIO.write(image, "png", file);
    System.out.println("A screenshot is captured to: " + file.getPath());
  }
  
  
  
  
}
