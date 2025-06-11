package com.example.NatakaLK.dto.paginated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedDTO {
    private List<?> content;
    private int totalPages;
    private long totalItems;

}
