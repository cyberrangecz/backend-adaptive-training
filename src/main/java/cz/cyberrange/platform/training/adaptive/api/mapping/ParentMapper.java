package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.api.dto.responses.PageResultResource;
import org.springframework.data.domain.Page;

/**
 * ParentMapper is parent class for mappers which contains only one method for creating pagination.
 */

public interface ParentMapper {

    default PageResultResource.Pagination createPagination(Page<?> objects) {
        PageResultResource.Pagination pageMetadata = new PageResultResource.Pagination();
        pageMetadata.setNumber(objects.getNumber());
        pageMetadata.setNumberOfElements(objects.getNumberOfElements());
        pageMetadata.setSize(objects.getSize());
        pageMetadata.setTotalElements(objects.getTotalElements());
        pageMetadata.setTotalPages(objects.getTotalPages());
        return pageMetadata;
    }
}
