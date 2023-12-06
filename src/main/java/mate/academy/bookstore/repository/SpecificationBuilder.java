package mate.academy.bookstore.repository;

import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<Book> build(BookSearchParameters searchParameters);
}
