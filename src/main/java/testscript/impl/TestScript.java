package testscript.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import testscript.AbstractScript;

public class TestScript implements AbstractScript{
    //需要测试的网站
    String url="https://educoder.trustie.net/myshixuns/7695iq2cgr/stages/2awt74zofrpu";

    public void execute(){

        //配置并创建PhantomJSDriver
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs.exe");
        WebDriver driver = new PhantomJSDriver(caps);

        //进入需要自动化测试的网站
        driver.get(url);

        //如果需要先登录，则先登录操作
        String currentUrl= driver.getCurrentUrl();
        if("https://educoder.trustie.net/login".equals(currentUrl)){
            System.out.println("login.....");
            String username="hnhtwrm@163.com";
            String password="tr123123";
            login(driver,username,password);
        }

        //按正常执行情况来说，执行到这已经跳转到了需要自动化测试的网站
        //所以如果没有跳转到目标网站，那么说明出问题了
        if(!url.equals( driver.getCurrentUrl()) ){
            throw new RuntimeException("没有跳转到目标网站，可能是登录没有成功");
        }





        System.out.println("finish");
    }

    //用于用户的登录
    //还没有做异常处理，可能会有异常抛出
    public void login(WebDriver driver,String username,String password){

        //获取用户名输入框，输入用户名
        WebElement loginInput= driver.findElement(By.id("name_loggin_input"));
        loginInput.clear();
        loginInput.sendKeys(username);

        //获取密码输入框，输入密码
        WebElement passwordInput= driver.findElement(By.id("password_loggin_input"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        //获取登录按钮，并点击登录
        WebElement submitButtion= driver.findElement(By.xpath("//*[@id=\"main_login_form\"]/ul/li[4]/button"));
        submitButtion.click();

        //登录成功后之后会跳转到"https://educoder.trustie.net/users/m92407385"
        //如果登录成功，就调转到需要自动化测试的网站
        if("https://educoder.trustie.net/users/m92407385".equals(driver.getCurrentUrl())){
            driver.get(url);
        }
    }
}
