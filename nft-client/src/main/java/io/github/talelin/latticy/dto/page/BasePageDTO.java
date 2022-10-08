package io.github.talelin.latticy.dto.page;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BasePageDTO {

    @Min(value = 1, message = "{page.count.min}")
    @Max(value = 30, message = "{page.count.max}")
    private Long count;

    @Min(value = 0, message = "{page.number.min}")
    private Long page;

    public Long getCount() {
        if (null == count) {
            return 10L;
        }
        return count;
    }

    public Long getPage() {
        if (null == page) {
            return 0L;
        }
        return page;
    }

}
