package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.MitreTechnique;
import cz.muni.ics.kypo.training.adaptive.dto.training.technique.MitreTechniqueDTO;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MitreTechniqueMapper {

    MitreTechnique mapToEntity(MitreTechniqueDTO dto);

    MitreTechniqueDTO mapToDTO(MitreTechnique dto);

    @Named("ignoreId")
    @Mapping(target = "id", ignore = true)
    MitreTechniqueDTO mapToDTOIgnoreId(MitreTechnique dto);

    List<MitreTechnique> mapDTOsToList(Collection<MitreTechniqueDTO> dtos);


    List<MitreTechniqueDTO> mapToListDTO(Collection<MitreTechnique> entities);

    @Named("ignoreIds")
    @IterableMapping(qualifiedByName = "ignoreId")
    Set<MitreTechniqueDTO> mapToListDTOIgnoreIds(Collection<MitreTechnique> entities);




}
