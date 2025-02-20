package dev.mehdizebhi.web3.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination {
    private int page;
    private int size;
    private long total;
    private int totalPages;

    public Pagination(Page<?> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public static Pagination of(Page<?> page) {
        return new Pagination(page);
    }
}
