
package daum ;

import java.io.File ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Vector ;
import java.util.concurrent.TimeUnit ;

import org.openqa.selenium.By ;
import org.openqa.selenium.JavascriptExecutor ;
import org.openqa.selenium.NoSuchElementException ;
import org.openqa.selenium.StaleElementReferenceException ;
import org.openqa.selenium.WebDriver ;
import org.openqa.selenium.WebElement ;
import org.openqa.selenium.chrome.ChromeDriver ;
import org.openqa.selenium.chrome.ChromeOptions ;

public class RunDaum
{
  
  public util.CommonUtil util ;
  
  public WebDriver driver ;
  
  public Vector < Vector < String > > outerIDsPws = new Vector < Vector < String > > ( ) ;
  
  public Vector < String > innnerIDsPws = new Vector < String > ( ) ;
  
  public Vector < String > cssSelectors = new Vector < String > ( ) ;
  
  public String url ;
  
  public String urlForClick ;
  
  public String urlForLogout ;
  
  public String inputId ;
  
  public String inputPw ;
  
  public String loginButton ;
  
  public String userID ;
  
  public String userPW ;
  
  public String tabCss ;
  
  public String recommendBtnCss ;
  
  public String rootFoler ;
  
  public int scrollX ;
  
  public int scrollY ;
  
  public void doProcess ( ) throws Exception
  {
    
    try
    {
      
      System.setProperty ( "webdriver.chrome.driver" , "./lib/chromedriver.exe" ) ;
      
      // 속성파일 , 아이디 파일들을 읽어오서 세팅한다.
      init ( ) ;
      System.out.println ( "1. 속성파일을 읽었음" ) ;
      
      // 아이디 가져와서 로그인 후 특정 버튼 클릭하고 driver 종료한다.
      for ( int i = 0 ; i < outerIDsPws.size ( ) ; i ++ )
      {
        try
        {
          Vector < String > innerV = outerIDsPws.elementAt ( i ) ;
          
          userID = innerV.elementAt ( 0 ) ;
          userPW = innerV.elementAt ( 1 ) ;
          
          // 크롬 알림창 제거 옵션으로 크롬 실행
          Map < String, Object > prefs = new HashMap < String, Object > ( ) ;
          prefs.put ( "profile.default_content_setting_values.notifications" , 2 ) ;
          ChromeOptions options = new ChromeOptions ( ) ;
          options.setExperimentalOption ( "prefs" , prefs ) ;
          options.addArguments ( "--user-agent=Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko" ) ;
          driver = new ChromeDriver ( options ) ;
          
          // 로그인 한다
          driver.get ( url ) ;
          driver.manage ( ).window ( ).maximize ( ) ;
          Thread.sleep ( 100 ) ;
          login ( url , inputId , inputPw , loginButton , userID , userPW , driver ) ;
          System.out.println ( "2. " + userID + " 로 로그인 했음" ) ;
          
          if ( ! rootFoler.endsWith ( File.separator ) )
          {
            rootFoler = rootFoler + File.separator + "daum_" + userID + File.separator ;
          }
          else
          {
            rootFoler = rootFoler + "daum_" + userID + File.separator ;
          }
          
          File f = new File ( rootFoler ) ;
          if ( ! f.exists ( ) )
          {
            f.mkdirs ( ) ;
          }
          f = null ;
          
          String folder = util.makeFolder ( rootFoler , System.currentTimeMillis ( ) ) ;
          connectAndClick ( urlForClick , tabCss , recommendBtnCss , folder ) ;
          System.out.println ( "3. " + urlForClick + " 로 방문해서 버튼 클릭했음" ) ;
          
          System.out.println ( "4. " + userID + " process 종료" ) ;
          
        }
        catch ( Exception e )
        {
          e.printStackTrace ( ) ;
          continue ;
        }
        
      }
      
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    finally
    {
      // driver.close();
    }
  }
  
  /**
   * 속성파일 , 아이디 파일들을 읽어오서 세팅한다.
   * 
   * @return
   * @throws Exception
   */
  public void init ( ) throws Exception
  {
    
    // 방문할 url form 의 id 정보를 가져온다.
    util = new util.CommonUtil ( ) ;
    
    url = util.getFromProperties ( "loginUrl" , "/DaumInfo.properties" ) ;
    urlForClick = util.getFromProperties ( "urlForClick" , "/DaumInfo.properties" ) ;
    inputId = util.getFromProperties ( "inputIdOfForm" , "/DaumInfo.properties" ) ;
    inputPw = util.getFromProperties ( "inputPwOfForm" , "/DaumInfo.properties" ) ;
    loginButton = util.getFromProperties ( "loginButtonOfForm" , "/DaumInfo.properties" ) ;
    tabCss = util.getFromProperties ( "tabCss" , "/DaumInfo.properties" ) ;
    recommendBtnCss = util.getFromProperties ( "recommendBtnCss" , "/DaumInfo.properties" ) ;
    rootFoler = util.getFromProperties ( "rootFolder" , "/DaumInfo.properties" ) ;
    
    scrollX = Integer.parseInt ( util.getFromProperties ( "scrollX" , "/DaumInfo.properties" ) ) ;
    scrollY = Integer.parseInt ( util.getFromProperties ( "scrollY" , "/DaumInfo.properties" ) ) ;
    // id , pw 읽어온다.
    outerIDsPws = util.getFileContentIntoVector ( "DaumIdPwList.txt" ) ;
    
  }
  
  /**
   * 로그인 한다.
   * 
   * @param url
   * @param inputId
   * @param inputPw
   * @param loginButton
   * @param userID
   * @param userPW
   * @param driver
   * @throws Exception
   */
  public void login ( String url , String inputId , String inputPw , String loginButton , String userID , String userPW , WebDriver driver ) throws Exception
  {
    
    // id 엘리먼트 찾음
    WebElement id = driver.findElement ( By.id ( inputId ) ) ;
    
    // pw 엘리먼트 찾음
    WebElement password = driver.findElement ( By.id ( inputPw ) ) ;
    
    // 로그인버튼 엘리먼트 찾음
    WebElement loginBtn = driver.findElement ( By.id ( loginButton ) ) ;
    
    id.sendKeys ( userID ) ;
    Thread.sleep ( 500 ) ;
    password.sendKeys ( userPW ) ;
    Thread.sleep ( 500 ) ;
    loginBtn.click ( ) ;
    Thread.sleep ( 1000 ) ;
    System.out.println ( "Had logged in using mouse click" ) ;
    
  }
  
  /**
   * 로그아웃 한다.
   * 
   * @param url
   * @param inputId
   * @param inputPw
   * @param loginButton
   * @param userID
   * @param userPW
   * @param driver
   * @throws Exception
   */
  public void logout ( String logoutButton ) throws Exception
  {
    
    // 로그아웃버튼 엘리먼트 찾음
    WebElement loginBtn = driver.findElement ( By.cssSelector ( logoutButton ) ) ;
    
    Thread.sleep ( 500 ) ;
    loginBtn.click ( ) ;
    Thread.sleep ( 1000 ) ;
    System.out.println ( "Had logged out using mouse click" ) ;
    
  }
  
  /**
   * 
   * @param connectUrl
   * @param driver
   * @throws Exception
   */
  public void connectAndClick ( String connectUrl , String tabCss , String recommendBtnCss , String folder ) throws Exception
  {
    try
    {
      
      JavascriptExecutor js = ( JavascriptExecutor ) driver ;
      driver.navigate ( ).to ( connectUrl ) ;
      
      // 하나 이상의 웹 요소가 존재/가시/농축/클릭 가능한지 등을 확인한다.
      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
      
      // 클릭하기전 찬반순인지 최신순인지 먼저 선택해야 함 - 최신탭 클릭
      String cssSelector = tabCss ;
      WebElement element = driver.findElement ( By.cssSelector ( cssSelector ) ) ;
      
      while ( true )
      {
        if ( element.isDisplayed ( ) )
        {
          break ;
        }
        else
        {
          System.out.println ( "tabElement element 생성 대기중...." ) ;
        }
      }
      
      // 댓글 찬성 반대 버튼 클릭
      cssSelector = recommendBtnCss ;
      WebElement recommendBtn = driver.findElement ( By.cssSelector ( cssSelector ) ) ;
      System.out.println ( "location : " + recommendBtn.getLocation ( ) ) ;
      
      js.executeScript ( "window.scrollBy(" + ( recommendBtn.getLocation ( ).getX ( ) - scrollX ) + "," + ( recommendBtn.getLocation ( ).getY ( ) - scrollY ) + ")" ) ;
      
      util.captureScreen ( folder + "pre.png" ) ;
      
      /**
       * 아래 코드로도 클릭 이벤트 효과 가져올 수 있음. 클릭
       * js.executeScript("arguments[0].clcick();", recommendBtn);
       */
      safeJavaScriptClick ( driver , recommendBtn ) ;
      
      util.captureScreen ( folder + "post.png" ) ;
      
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    finally
    {
      driver.close();
    }
    
  }
  
  // 출처 :
  // https://www.seleniumeasy.com/selenium-tutorials/click-element-using-javascriptexecutor
  public void safeJavaScriptClick ( WebDriver driver , WebElement element ) throws Exception
  {
    try
    {
      if ( element.isEnabled ( ) && element.isDisplayed ( ) )
      {
        System.out.println ( "Clicking on element with using java script click" ) ;
        
        ( ( JavascriptExecutor ) driver ).executeScript ( "arguments[0].click();" , element ) ;
      }
      else
      {
        System.out.println ( "Unable to click on element" ) ;
      }
    }
    catch ( StaleElementReferenceException e )
    {
      System.out.println ( "Element is not attached to the page document " + e.getStackTrace ( ) ) ;
    }
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Element was not found in DOM " + e.getStackTrace ( ) ) ;
    }
    catch ( Exception e )
    {
      System.out.println ( "Unable to click on element " + e.getStackTrace ( ) ) ;
    }
  }
  
  public static void main ( String [ ] args ) throws Exception
  {
    RunDaum rc = new RunDaum ( ) ;
    rc.doProcess ( ) ;
  }
  
}
