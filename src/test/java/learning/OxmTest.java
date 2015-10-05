package learning;

import com.hyunbogi.spring.dao.sql.jaxb.SqlType;
import com.hyunbogi.spring.dao.sql.jaxb.Sqlmap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/learning/OxmTest-context.xml")
public class OxmTest {
    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    public void unmarshallSqlmap() throws IOException {
        Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sql/sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();
        assertThat(sqlList.size(), is(6));
        assertThat(sqlList.get(0).getKey(), is("userAdd"));
        assertThat(sqlList.get(1).getKey(), is("userGet"));
    }
}
