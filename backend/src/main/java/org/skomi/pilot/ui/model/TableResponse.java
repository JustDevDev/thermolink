package org.skomi.pilot.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Basic response
 *
 * @param <T> Any table content
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TableResponse<T> {
    List<T> content;
    int totalPages;
    int totalElements;
    int size;
    int currentPage;
}
