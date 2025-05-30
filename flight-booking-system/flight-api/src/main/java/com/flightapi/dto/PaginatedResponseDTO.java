package com.flightapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * A generic Data Transfer Object (DTO) for representing paginated API responses.
 * It includes the list of content for the current page, along with pagination metadata.
 *
 * @param <T> The type of the content in the list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO<T> {

    /**
     * The list of items for the current page.
     */
    private List<T> content;
    /**
     * The current page number (0-indexed).
     */
    private int pageNo;
    /**
     * The number of items per page.
     */
    private int pageSize;
    /**
     * The total number of items across all pages.
     */
    private long totalElements;
    /**
     * The total number of pages.
     */
    private int totalPages;
    /**
     * A boolean flag indicating if this is the last page.
     */
    private boolean last;

    /**
     * Constructs a {@link PaginatedResponseDTO} from a Spring Data {@link Page} object.
     *
     * @param page The {@link Page} object containing the data and pagination information.
     */
    public PaginatedResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.pageNo = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
