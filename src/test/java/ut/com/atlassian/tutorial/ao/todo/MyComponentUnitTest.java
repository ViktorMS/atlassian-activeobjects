package ut.com.atlassian.tutorial.ao.todo;

import org.junit.Test;
import com.atlassian.tutorial.ao.todo.api.MyPluginComponent;
import com.atlassian.tutorial.ao.todo.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}