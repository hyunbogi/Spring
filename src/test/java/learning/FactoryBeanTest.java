package learning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/learning/learning-applicationContext.xml")
public class FactoryBeanTest {
    @Autowired
    private ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message, instanceOf(Message.class));
        assertThat(((Message) message).getText(), is("Factory Bean"));
    }

    @Test
    public void getFactoryBeanItself() {
        Object message = context.getBean("&message");
        assertThat(message, instanceOf(MessageFactoryBean.class));
    }

    public static class Message {
        private String text;

        private Message(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static Message newMessage(String text) {
            return new Message(text);
        }
    }

    public static class MessageFactoryBean implements FactoryBean<Message> {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public Message getObject() throws Exception {
            return Message.newMessage(text);
        }

        @Override
        public Class<?> getObjectType() {
            return Message.class;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

}
