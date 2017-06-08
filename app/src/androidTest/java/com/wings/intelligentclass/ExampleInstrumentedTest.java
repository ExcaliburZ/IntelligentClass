package com.wings.intelligentclass;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;
import com.wings.intelligentclass.utils.RetrofitManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wings.intelligentclass", appContext.getPackageName());
    }

    @Test
    public void testRegisterUSer() {
        User user = new User("zjq", "name", "1234@qq", "1350", "13471126","desc");
        Result result = RetrofitManager.getInstance().RegisterUser(user);
        System.out.println(result);
    }

    @Test
    public void testLogin() {
        User user = new User("zjq", "123");
        LoginInfo info = RetrofitManager.getInstance().Login(user);
        System.out.println(info);
    }
}
