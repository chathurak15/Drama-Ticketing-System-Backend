package com.example.NatakaLK.dto.requestDTO;

import com.example.NatakaLK.model.enums.AreaType;
import com.example.NatakaLK.model.enums.TheatreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreUpdateDTO {
        private Long id;
        private String name;
        private AreaType areaType;
        private TheatreStatus status;
        private Integer totalCapacity;
        private BigDecimal openAreaPrice;
        private List<SeatTypeDTO> seatTypes;
}
