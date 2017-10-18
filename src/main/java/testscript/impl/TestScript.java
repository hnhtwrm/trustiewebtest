package testscript.impl;

import bean.User;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import service.TestService;
import testscript.AbstractScript;

import java.util.regex.Pattern;

public class TestScript implements AbstractScript{
    //用户名和密码,用于登录
    private String username;
    private String password;
    //需要测试的网站的url，以及对应的答案
    private String url;
    private String answer;

    public TestScript() {
        User user = TestService.getInstance().getUser();
        this.username = user.getUsername();
        this.password = user.getPassword();

        //默认获取第0个url，可以通过setUrlAndAnswer修改
        this.url = TestService.getInstance().getUrl(0);
        this.answer = TestService.getInstance().getAnswer(url);
    }

    public void setUrlAndAnswer(int index){
        this.url = TestService.getInstance().getUrl(index);
        this.answer = TestService.getInstance().getAnswer(url);
    }

    public void execute(){

        //配置并创建PhantomJSDriver
        //得先用google浏览器测试一下才行，不然不知道具体的流程是不是按照预期
        DesiredCapabilities caps = new DesiredCapabilities();
//        String driverPath = this.getClass().getResource("/phantomjs.exe").getPath();
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs.exe");
        WebDriver driver = new PhantomJSDriver(caps);

        System.out.println(url);
        //进入需要自动化测试的网站
        driver.get(url);

        //如果需要先登录，则先登录操作
        String currentUrl= driver.getCurrentUrl();

        if("https://www.educoder.net/login".equals(currentUrl)){
            System.out.println("login.....");
            login(driver);
        }

        //按正常执行情况来说，执行到这已经跳转到了需要自动化测试的网站
        //所以如果没有跳转到目标网站，那么说明出问题了
        if(!url.equals( driver.getCurrentUrl()) ){
            throw new RuntimeException("没有跳转到目标网站，可能是登录没有成功");
        }

        evalute(driver);
    }

    //用于用户的登录
    //还没有做异常处理，可能会有异常抛出
    public void login(WebDriver driver){
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

        //登录成功后之后会跳转到"https://educoder.trustie.net/users/xxxx"
        //如果登录成功，就调转到需要自动化测试的网站
        System.out.println(driver.getCurrentUrl());

        if (driver.getCurrentUrl().contains("https://www.educoder.net/users/")){
            driver.get(url);
        }
    }

    //评测
    public void evalute(WebDriver driver){
        System.out.println("已经成功登录或者跳过登录验证！开始评测......");

        //填入答案
        JavascriptExecutor  js = (JavascriptExecutor)driver;
        String answer = "docker run --name my_container busybox:latest echo \\\"Hello Docke\\\"";
        String script = "editor_CodeMirror.setValue(\""+answer+"\")";
        js.executeScript(script);
        //点击评测
        driver.findElement(By.id("code_test")).click();

        while (true) {
            //循环等待获取评测结果
            try {
                //如果有弹窗，则说明评测成功了（至少目前是这样的）
                Thread.sleep(500);
                driver.switchTo().defaultContent().findElement(By.id("popupWrap"));
                System.out.println("通过成功");
                break;
            } catch (Exception e) {
                if (driver.findElement(By.xpath("//*[@id=\"evaluating_contents\"]/p")).getText().contains("不匹配")) {
                    System.out.println("通关失败");
                    break;
                }
                //没有找到不匹配的提示信息，说明仍然在评测....
                System.out.println("评测中...");
            }
        }
    }


    @Override
    public String toString() {
        return "TestScript{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

}
