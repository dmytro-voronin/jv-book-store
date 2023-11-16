package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

	public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setAuthor("J.K. Rowling");
                book.setTitle("Harry Potter and the Chamber of Secrets");
                book.setIsbn("9781526637888");
                book.setPrice(BigDecimal.valueOf(300));
                book.setDescription("fantasy");
                book.setCoverImage("firm");
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}
