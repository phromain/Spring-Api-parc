package fr.rp.springapiparc.dto.out;

import com.github.slugify.Slugify;
import fr.rp.springapiparc.entity.RegionEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionOutDto {
        private Integer id;
        private String nomRegion;
        private String slugRegion;

    //final Slugify slg = Slugify.builder().build();

        public RegionOutDto(RegionEntity region) {
            this.id = region.getId();
            this.nomRegion = region.getNomRegion();

            final Slugify slg = Slugify.builder().build();

            this.slugRegion = slg.slugify(region.getNomRegion());

        }




}