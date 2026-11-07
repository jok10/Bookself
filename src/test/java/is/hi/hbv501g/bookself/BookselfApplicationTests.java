package is.hi.hbv501g.bookself;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "JWT_SECRET=VGhpc0lzQVRlc3RTZWNyZXRGb3JUZXN0aW5nT25seTEyMzQ="
})
class BookselfApplicationTests {

    @Test
    void contextLoads() {
    }

}
