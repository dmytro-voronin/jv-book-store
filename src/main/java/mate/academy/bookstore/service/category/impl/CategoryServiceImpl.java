package mate.academy.bookstore.service.category.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import mate.academy.bookstore.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(("Can`t find category by id" + id)));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        if (categoryRepository.existsById(id)) {
            Category category = categoryMapper.toEntity(categoryDto);
            category.setId(id);
            Category updatedCategory = categoryRepository.save(category);
            return categoryMapper.toDto(updatedCategory);
        } else {
            throw new mate.academy.bookstore.exception.EntityNotFoundException(
                    "Can't update, category with id " + id + " doesn't exist."
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
