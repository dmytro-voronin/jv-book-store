package mate.academy.bookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.impl.BookMapperImpl;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapperImpl bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find valid books by category id")
    void findAllByCategoryId_WithAvailableCategoryId_ShouldReturnValidBooks() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("History"));
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Biology"));

        Category science = new Category();
        science.setId(1L);
        science.setName("Science");
        Set<Category> scienceCategories = new HashSet<>();
        scienceCategories.add(science);

        Book history = new Book();
        history.setId(1L);
        history.setTitle("History");
        history.setCategories(scienceCategories);

        Book biology = new Book();
        biology.setId(2L);
        biology.setTitle("Biology");
        biology.setCategories(scienceCategories);

        List<Book> books = new ArrayList<>();
        books.add(history);
        books.add(biology);

        when(bookRepository.findAllByCategoriesId(ArgumentMatchers.anyLong())).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(history)).thenReturn(expected.get(0));
        when(bookMapper.toDtoWithoutCategories(biology)).thenReturn(expected.get(1));

        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(1L, PageRequest.of(0, 10));

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    @DisplayName("Update by not valid id")
    void update_WithNotValidId_NotSuccess() {
        BookDto bookDto = new BookDto()
                .setTitle("Math");
        Long id = 10L;

        when(bookRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(bookDto, id);
        });
    }

    @Test
    @DisplayName("Find by not valid id")
    void findById_WithNotValidId_NotSuccess() {
        Long id = 10L;

        when(bookRepository.findById(id))
                .thenThrow(new EntityNotFoundException("Can't find book by id " + id));

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(id);
        });
    }
}
